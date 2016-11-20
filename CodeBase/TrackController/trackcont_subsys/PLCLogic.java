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
        occupied,failure,endOfTrack,trainInSugList,temp;
    }
    public enum newBlockState{
        stop,cross,moveSwitch,fail,heat,start
    }
    relativeBlockState rbs;
    newBlockState nbs;
    int relativeBlockNum;
    
    public PLCLogic(String rbsString, String nbsString, int rbn){
        rbs=rbs.valueOf(rbsString);
        nbs=nbs.valueOf(nbsString);
        relativeBlockNum=rbn;
    }
}