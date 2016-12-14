import java.lang.StringBuilder;

public class TrainModel{
	
	PID pid = new PID();
	
	//1st, 2nd, or 3rd raido button
	//On/Open, Off/Closed, or Fail
	int lights = 2;
	int ac = 2;
	int heater = 2;
	int leftDoors = 2;
	int rightDoors = 2;
	
	int ID;
	boolean auto = true;
	boolean ad = false;
	//boolean vehicleParam = false;
	
	float spdReq = 0;
	float powReq = 0;
	
	boolean srvBrk = false;
	boolean psngrBrk = false;
	boolean eBrk = false;
	
	float setpnt = 0;
	float curSpd = 0;
	float authority = 0;
	
	
	static final int numCars = 2;
	static final int numDoors = 16;
	static final int length = 211; //ft
	static final float width = (float)8.7; //ft
	static final float height = (float)11.2; //ft
	static final int trainWeight = 180334; //lb
	static final int passWeight = 157; //lb
	static final int maxPassenger = 444;
	static final float maxAcceleration = (float)1.64; //ft/s^2
	static final float maxDeceleration = (float)8.95; //ft/s^2
	static final float maxSpd = (float)43.5;  //mph
	static final int crewCount = 1;
	
	float elevation = 0;
	float grade = 0;	
	int curPassengers = 0;
	int psngrEnter = 0;
	int passengerWeight = 0;	
	float currentDistance = 0;
	
	boolean engineFailure = false;
	boolean signalPickupFailure = false;
	boolean brakeFailure = false;	

	public int getID(){
		return ID;
	}
	
	public TrainModel(int id){
		ID = id;
	}
	
	public int setLights(int state){
		lights = state;
		return lights;		
	}
	
	public int setHeat(int state){
		heater = state;
		return heater;		
	}
	
	public int setLDoor(int state){
		leftDoors = state;
		return leftDoors;		
	}
	
	public int setRDoor(int state){
		rightDoors = state;
		return rightDoors;		
	}
	
	public boolean setAuto(boolean state){
		auto = state;
		return auto;		
	}
	
	public boolean setAd(boolean state){
		ad = state;
		return ad;		
	}
	
	public float setSpdReq(float state){
		spdReq = state;
		return spdReq;		
	}
	
	public float getCurSpd(){
		return curSpd;		
	}
	
	public float setPwrReq(float timeChange){
		float stopDist = stoppingDistance(timeChange/1000)/5280; 
		if(auto)
			powReq = pid.update(curSpd, setpnt, timeChange, authority, stopDist);
		else
			powReq = pid.update(curSpd, spdReq, timeChange, authority, stopDist);
		if(authority == 0)
			powReq = 0;
		return powReq;
	}
	
	public int setAC(int state){
		ac = state;
		return ac;		
	}
	
	public boolean setPsngrBrk(boolean state){
		psngrBrk = state;
		return psngrBrk;
	}
	
	public boolean setEBrk(boolean state){
		eBrk = state;
		return eBrk;		
	}
	
	public boolean setSrvBrk(boolean state){
		srvBrk = state;
		return srvBrk;		
	}
		
	public String getVehicleParam(){
		StringBuilder b = new StringBuilder();
		b.append("Train ID: " + ID + "\n");
		b.append("Number of cars: " + numCars + " cars\n");
		b.append("Number of doors: " + numDoors + " doors\n");
		b.append("Train length: " + length + " ft\n");
		b.append("Train width: " + width + " ft\n");
		b.append("Train height: " + height + " ft\n");
		b.append("Train mass: " + trainWeight + " lb\n");
		b.append("Maximum passengers: " + maxPassenger + " people\n");
		b.append("Maximum acceleration: " + maxAcceleration + " ft/s^2\n");
		b.append("Maximum deceleration: " + maxDeceleration+ " ft/s^2\n");
		b.append("Maximum speed: " + maxSpd + " mph\n");
		
		return b.toString();
	}

	public String stateToString(int state){
		if(state == 1){
			return "on";
		}
		else if(state == 2){
			return "off";
		}
		else{
			return "fail";
		}
	}

	public String doorStateToString(int state){
		if(state == 1){
			return "open";
		}
		else if(state == 2){
			return "closed";
		}
		else{
			return "fail";
		}
	}

	public String boolToString(boolean b){
		if(b){
			return "on";
		}
		else{
			return "off";
		}
	}
	
	
	public int currentWeight(){
		return trainWeight + passWeight * curPassengers;
	}
	
	public float stoppingDistance(float deltaT){
		float curWeight = currentWeight();
		
		float Ffriction = (float).7 * curWeight * 32 * (float)Math.cos(grade/100);

		float decel = Ffriction / curWeight;
		
		float stopTime = (curSpd) / decel;
		
		float stopDist = curSpd * stopTime + decel * stopTime * stopTime / 2;	

		return stopDist;
	}
	
}