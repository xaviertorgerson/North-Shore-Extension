import java.util.ArrayList;


class CTCTrainManager{
	
	private ArrayList<Integer> currentBlocks = new ArrayList<Integer>();
	private ArrayList<String> currentLine = new ArrayList<String>();
	private ArrayList<Integer> currentDestination = new ArrayList<Integer>();
	private ArrayList<Integer> currentAuthority = new ArrayList<>(Integer);
	
	
	public CTCTrainManager(){
		
	}
	
	public deleteTrain(int trainID)
	{
		currentBlocks.set(trainID, -1);
		currentDestination.set(trainID, -1);
		currentAuthority.set(trainID, -1);
	}
	
	public void changeLocation(int trainID, int currentBlock)
	{
		currentBlocks.set(trainID, currentBlock);
	}
	
	public int getBlockofTrain(int trainID)
	{
		return currentBlocks.get(trainID);
	}
	
	public String getLineofTrain(int trainID)
	{
		return currentLine.get(trainID);
	}

	public void setLine(int trainID, String Line)
	{
		currentLine.set(trainID, Line);
	}
	
	public void setDestination(int trainID, int destinationBlock)
	{
		currentDestination.set(trainID, destinationBlock);
	}
	
	public int getDestination(int trainID)
	{
		return currentDestination.get(trainID);
	}
	
}