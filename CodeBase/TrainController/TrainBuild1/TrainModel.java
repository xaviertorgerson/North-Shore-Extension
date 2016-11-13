public class TrainModel{
	
	int lights = 2;
	int ac = 2;
	int heater = 2;
	int leftDoors = 2;
	int RightDoors = 2;
	
	int ID;
	boolean auto = false;
	boolean ad = false;
	boolean vehicleParam = false;
	
	int spdReq = 0;
	int powReq = 0;
	
	boolean srbBrk = false;
	boolean psngrBrk = false;
	boolean eStop = false;
	
	int setpnt = 0;
	int curSpd = 0;
	int authority = 0;
	
	public TrainModel(int id){
		ID = id;
	}
}