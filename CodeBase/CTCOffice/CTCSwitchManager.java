public class CTCSwitchManager{

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
		System.out.println("Calling get switch with " + line + " and " + switchID);
		Switch just = trackModel.getSwitch(line, switchID);
		Block ayo = just.getState0();
		return ayo.getNumber();
	}
	
	public int getBlockat1(String line, int switchID)
	{
		System.out.println("Calling get switch with " + line + " and " + switchID);
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
}