public class Train{
	
	TrainController tc;
	
	public Train(int ID){
		tc = new TrainController(ID);
	}
	
	public void update(){
		tc.update();
	}
}