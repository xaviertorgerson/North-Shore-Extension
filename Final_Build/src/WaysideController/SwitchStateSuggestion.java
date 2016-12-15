/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jeff
 */

//Class to store the switch state suggestions for 1 given switch given by the CTC office to control the route of a train
public class SwitchStateSuggestion {
    int blockNum; //block number of the switch
    //the state and trainNum arrays are linked by index meaning that the train with trainNum (trainID) at index 1 will have
    //  the state at index 1 (meaning that if the switch detects this train (index 1) it will go to state at index 1)
    boolean[] state; //state of the switch (F for state0, T for state 1)
    int[] trainNum; //train numbers
    int numberofTrains = 1; //current number of trains on the track
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
