/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeff
 */

//Saves the train occupancy for three blocks, the current block, the block to the right of the current block (usually next)
//and the block to the left of the current block
public class TrainOccupationFinder {
    public int trainNum;
    public int prevTrainNum;
    public int nextTrainNum;
    
    public TrainOccupationFinder(int c,int p,int n){
        trainNum=c;
        prevTrainNum=p;
        nextTrainNum=n;
    }
}
