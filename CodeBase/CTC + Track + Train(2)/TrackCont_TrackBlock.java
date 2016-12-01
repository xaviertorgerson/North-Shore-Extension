/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jeff
 */
public class TrackCont_TrackBlock {
    int blockNum;
    int type;
    int nextBlock;
    int prevBlock;
    int altPrevBlock;
    int altNextBlock;
    double speedLimit;
    int authority;
    boolean occupied;
    boolean state;
    boolean failure;
    boolean heater;
    
    public TrackCont_TrackBlock(int num,int t,int next,int prev, int aNext, int aPrev){
        blockNum=num;
        type=t;
        occupied=false;
        state=false;
        failure=false;
        heater=false;
        nextBlock=next;
        prevBlock=prev;
        altPrevBlock=aPrev;
        altNextBlock=aNext;
        speedLimit=12.0;
        authority=2;
    }
}
