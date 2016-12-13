
import java.util.ArrayList;


class CTCTrainManager{
	
	private ArrayList<Integer> currentBlocks = new ArrayList<Integer>();
	private ArrayList<String> currentLine = new ArrayList<String>();
	private ArrayList<Integer> currentDestination = new ArrayList<Integer>();
	private ArrayList<Float> currentDistance = new ArrayList<Float>();
	private ArrayList<Float> currentSpeed = new ArrayList<Float>();
	
	public CTCTrainManager(){
		for(int i = 0 ; i<20 ; i++)
		{
			currentBlocks.add(-1);
			currentDestination.add(-1);
			currentDistance.add((float)0);
			currentSpeed.add((float)30);
		}
		currentLine.add("x");
		currentLine.add("x");
		currentLine.add("x");
		currentLine.add("x");
		currentLine.add("x");
		currentLine.add("x");
	}
	
	public void deleteTrain(int trainID)
	{
		currentBlocks.set(trainID, -1);
		currentDestination.set(trainID, -1);
	}
	
	public void setLocation(int trainID, int currentBlock)
	{
		currentBlocks.set(trainID, currentBlock);
	}
	
	public int getLocation(int trainID)
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
	
	public void setDistance(int trainID, float distance)
	{
		currentDistance.set(trainID, distance);
		
	}
	
	public float getDistance(int trainID)
	{
		return currentDistance.get(trainID);
	}
	
	public void setSpeed(int trainID, float speed)
	{
		currentSpeed.set(trainID, speed);
	}
	
	public float getSpeed(int trainID)
	{
		return currentSpeed.get(trainID);
	}
	
	
	
	

}