import java.lang.StringBuilder;

public class TrainModel{
	
	//1st, 2nd, or 3rd raido button
	//On/Open, Off/Closed, or Fail
	int lights = 2;
	int ac = 2;
	int heater = 2;
	int leftDoors = 2;
	int rightDoors = 2;
	
	int ID;
	boolean auto = false;
	boolean ad = false;
	//boolean vehicleParam = false;
	
	int spdReq = 0;
	int powReq = 0;
	
	boolean srbBrk = false;
	boolean psngrBrk = false;
	boolean eStop = false;
	
	int setpnt = 0;
	int curSpd = 0;
	int authority = 0;
	
	//Everything is going to be in english units (ft, lb, sec)
	static final int numCars = 5;
	static final int numDoors = 40;
	static final int length = 528;
	static final float width = 8.7;
	static final float height = 11.2;
	static final int trainWeight = 450837;
	static final int maxPassenger = 1110;
	static final float maxAcceleration = 1.64;
	static final float maxDeceleration = 8.95;
	static final float maxSpd = 43.5;
	
	//Need to be filled in with right value
	static final int crewCount = 10;
	static final float kValue = trainWeight;
	
	int elevation = 0;
	int grade = 0;	
	int curPassengers = 0;
	int passengerWeight = 0;	
	int currentDistance = 0;
	
	boolean engineFailure = false;
	boolean signalPickupFailure = false;
	boolean brakeFailure= false;
	
	
	
	public TrainModel(int id){
		ID = id;
	}
	
	
	
	
	
	
	
	//When brake is active, override power request to 0 and activate brake	
	

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
}