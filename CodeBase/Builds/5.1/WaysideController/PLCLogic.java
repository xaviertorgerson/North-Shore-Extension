/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeff
 */

//This stores a single PLC logic instruction after it has been parsed from the file in TrackCont_PLC
// An explanation about how PLC logic files work and what each isntruction does can be found at PLC_Coding_ReadMe.txt
public class PLCLogic {
    public enum relativeBlockState{
        occupied(0,true),failure(1,true),switchSet1(2,true),switchSet0(2,false),trainInSugList(3,true),temp(4,true),
        Noccupied(0,false),Nfailure(1,false),noNextConnection(5,true),noPrevConnection(6,true),trainMovingRight(7,true),trainMovingLeft(8,true);
        
        int state;
        boolean logic;
        relativeBlockState(int rbs,boolean l){
            state=rbs;
            logic=l;
        }
    }
    public enum newBlockState{
        stop(0),start(0),cross1(1),cross0(1),switch1(2),switch0(2),switchSug(3),fail1(4),fail0(4),heat1(5)
        ,heat0(5),addTrain(6),stopLeft(7),stopRight(7);
        
        int state;
        newBlockState(int nbs){
            state=nbs;
        }
    }
    relativeBlockState rbs;
    newBlockState nbs;
    int relativeBlockNum;
    boolean alternatePath;
    public PLCLogic(String rbsString, String nbsString, int rbn, boolean alt){
        rbs=rbs.valueOf(rbsString);
        nbs=nbs.valueOf(nbsString);
        relativeBlockNum=rbn;
        alternatePath=alt;
    }
}