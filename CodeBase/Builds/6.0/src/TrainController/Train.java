public class Train{
	
	TrainController tc;
	boolean safteyOverride = false;

	
	public Train(int ID){
		tc = new TrainController(ID);
	}
	
	//Sent in simulation time in miliseconds
	public void update(long timePassed){
		tc.update(timePassed);
	}
	
	public int getID(){
		return tc.tv.tm.getID();
	}
	
	public void updateGrade(float newElevation, float newGrade){
		if(newElevation > tc.tv.tm.elevation)
			tc.tv.tm.grade = newGrade;
		else
			tc.tv.tm.grade = -1 * newGrade;
		tc.tv.tm.elevation = newElevation;
	}
	
	public void updateRequest(float authority, float speed){
		if(!tc.tv.tm.signalPickupFailure)
		{
			tc.tv.tm.authority = authority;
			if(speed > 45.5){
				tc.tv.tm.setpnt = (float) 45.5;
				if(safteyOverride == false){
					tc.safteyOverride();
					safteyOverride = true;
				}
			}	
			else{
				tc.tv.tm.setpnt = speed;
				safteyOverride = false;
			}
		}
	}
	
	public float getDistance(){
		return tc.tv.tm.currentDistance;
	}
	
	public int curBlock = 0;
	public int prevBlock = -1;
	public int nextBlock = 0;
	
	public void setNextBlock(int block){
		nextBlock = block;
	}
	
	public void setBlock(int block){	
		if(block != curBlock)
			prevBlock = curBlock;
		curBlock = block;
	}
	
	public void setDistance(float dist){
		tc.tv.tm.currentDistance = dist;
	}
	
	public int psngrCount(){
		return tc.tv.tm.curPassengers;
	}
	
	public void enterStation(int embarkers, int side){
		tc.tv.enterStation(embarkers);
		tc.announceStation();
	}
}
