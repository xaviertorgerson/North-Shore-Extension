public class Train{
	
	TrainController tc;
	
	public Train(int ID){
		tc = new TrainController(ID);
	}
	
	//Sent in simulation time in miliseconds
	public void update(int timePassed){
		tc.update(timePassed);
	}
	
	public void updateGrade(float newElevation, float newGrade){
		if(newElevation > tc.tv.tm.elevation)
			tc.tv.tm.grade = newGrade;
		else
			tc.tv.tm.grade = -1 * newGrade;
		tc.tv.tm.elevation = newElevation;
	}
	
	public void updateRequest(float authority, float speed){
		tc.tv.tm.authority = authority;
		tc.tv.tm.setpnt = speed;
	}
	
	public float getDistance(){
		return tc.tv.tm.currentDistance;
	}
	
	public int curBlock = 0;
	public int prevBlock = -1;
	
	public void setBlock(int block){	
		if(block != curBlock)
			prevBlock = curBlock;
		curBlock = block;
	}
	
	public void setDistance(int dist){
		tc.tv.tm.currentDistance = currentDistance;
	}
	
	public int psngrCount(){
		return tc.tv.tm.curPassengers;
	}
	
	public void enterStation(int embarkers, int side){
		//tc.enterStation(side);
		tc.tv.enterStation(embarkers);
	}
}