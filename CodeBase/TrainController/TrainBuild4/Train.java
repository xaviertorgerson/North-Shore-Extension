public class Train{
	
	TrainController tc;
	
	public Train(int ID){
		tc = new TrainController(ID);
	}
	
	public void update(int timePassed){
		tc.update(timePassed);
	}
	
	public void updateGrade(float grade){
		tc.tv.tm.elevation = tc.tv.tm.elevation + grade;
		tc.tv.tm.grade = grade;
	}
	
	public void updateRequest(float authority, float speed){
		tc.tv.tm.authority = authority;
		tc.tv.tm.spdReq = speed;
	}
	
	public float getDistance(){
		return tc.tv.tm.currentDistance;
	}
	
	private int curBlock = 0;
	private int prevBlock = -1;
	
	public void setBlock(int block){		
		prevBlock = curBlock;
		curBlock = block;
	}
	
	public int psngrCount(){
		return tc.tv.tm.curPassengers;
	}
	
	public void enterStation(int embarkers, int side){
		//tc.enterStation(side);
		tc.tv.enterStation(embarkers);
	}
}