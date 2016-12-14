public class CTCSwitchManager{
	
/**
 *  The CTCSwitchManager class acts as an interface for me to easily get the blocks a switch leads to.
 *  It also stores the blocks displayed on the checkmarks so that I can set them later if the user wants to 
 *  change the state of one. 
 */

	private TrackModel trackModel;

	private int currentBlock0;
	private int currentBlock1;
	public CTCSwitchManager(TrackModel tm){
		
		this.trackModel = tm;
	}
	
	//trackModel.getSwitch(String line, int switchID)
	//Switch.getState1(), Switch.getState0()
	public int getBlockat0(String line, int switchID)
	{	
		//small check to make sure I don't get hung up with a null pointer
		if(trackModel == null){
			System.out.println("STOP!!!!!");
			return 0;
		}
		Switch just = trackModel.getSwitch(line, switchID);
		Block ayo = just.getState0();
		return ayo.getNumber();
	}
	
	public int getBlockat1(String line, int switchID)
	{
		//System.out.println("Calling get switch with " + line + " and " + switchID);
		if(trackModel == null){
			System.out.println("STOP!!!!!");
			return 0;
		}
		Switch just = trackModel.getSwitch(line, switchID);
		Block ayo = just.getState1();
		return ayo.getNumber();
	}
	
	public void setCurrent0(int b)
	{
		this.currentBlock0 = b;
	}
	
	public void setCurrent1(int b)
	{
		this.currentBlock1 = b;
	}
	
	public int getCurrent0()
	{
		return currentBlock0;
	}
	
	public int getCurrent1()
	{
		return currentBlock1;
	}
	
	public int getCurrentState(Switch test)
	{
		if(test.getState()){
			return 1;
		}
		else{
			return 0;
		}
		return 0;
		
	}
	
	//Just takes the inputs from the CTCGUI and uses them to determine if it is necessary to toggle the switch
	//If necessary, it then toggles the switch.
	public int toggleSwitch(String line, int switchID, int newState){
		
		Switch just = trackModel.getSwitch(line, switchID);
		
		if(just.getState()){
			
			if(newState == 1){
				return;
			}
			else{
				just.toggleSwitch();
			}
		}
		else{
			if(newState == 0){
				return;
			}
			else{
				just.toggleSwitch();
			}
		}
		
	}
	

	
	
}