/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jeff
 */
public class SwitchStateSuggestion {
    int blockNum;
    boolean[] state;
    int[] trainNum;
	int numberofTrains = 1;
    public SwitchStateSuggestion(int bn, boolean[] s, int[] tn){
        blockNum=bn;
        state=s;
        trainNum=tn;
    }
	
	
	public void deleteTrain(int trainID)
	{
		for(int i = 0; i< numberofTrains; i++)
		{
			if(trainNum[i] == trainID)
			{
				trainNum[i] = -1;
			}
		}
	}
	public void addTrain(int trainID, boolean oneState)
	{
		trainNum[numberofTrains-1] = trainID;
		state[numberofTrains-1] = oneState;
		numberofTrains++;
		//This weird numbering is to allow deleteTrain to go through all of the values
	}
	
	public void setBlockNumber(int newBlock)
	{
		blockNum = newBlock;
	}
}
