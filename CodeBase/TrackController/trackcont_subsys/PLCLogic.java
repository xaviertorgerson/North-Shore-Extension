/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackcont_subsys;

/**
 *
 * @author jeff
 */
public class PLCLogic {
    public enum relativeBlockState{
        occupied(0,true),failure(1,true),switchSet1(2,true),switchSet0(2,false),trainInSugList(3,true),temp(4,true),
        Noccupied(0,false),Nfailure(1,false),noNextConnection(5,true),noPrevConnection(6,true);
        
        int state;
        boolean logic;
        relativeBlockState(int rbs,boolean l){
            state=rbs;
            logic=l;
        }
    }
    public enum newBlockState{
        stop,cross1,cross0,switch1,switch0,switchSug,fail1,fail0,heat1,heat0,start,addTrain;
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