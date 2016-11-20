/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackcont_subsys;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Jeff
 */
/*IMPORTANT:
 * PLC code written in the following format
 *      (logic type):
 *      if (relative block #) (relative block state) then (new block state) (explicit block # NOTE: optional)
 * 
 *      Logic Types:
 *          crossing: logic for railroad crossing blocks goes to crossingLogic arraylist
 *          switch: logic for switching blocks goes to switchLogic arraylist
 *          failure: logic for failed blocks goes to failureLogic arraylist
 *          occupied: logic for occupied blocks goes to occupiedLogic arraylist
 *          general: logic for general track blocks, track blocks with no special properties goes to generalLogic arraylist
 *          explicit: logic for explicit track blocks, track blocks that need extra logic goes to explicitLogic arraylist
 *      
 *      relative block #:
 *          -x means that block is x blocks behind the track block currently being tested
 *          +x means that block is x blocks infront of the track block currently being tested
 *          0 means that you will test the current block being tested (used to set heater/failure)
 *  
 *      relative block state:
 *          occupied: relative block is occupied by a train =1
 *          failure: relative block has failed =2
 *          endOfTrack: relative block cannot recieve this train, either because the switch isn't set correctly or the track doesn't exist =3
 *          trainInList: the train located in the relative block is within the suggestion list =4
 *          temp: the train temperture is below a set threshold =5
 *      
 *      new block state:
 */

public class TrackCont_PLC {
    public static final int MINTEMP=32;
    
    String plcFileName;
    ArrayList<PLCLogic> crossingLogic;
    ArrayList<PLCLogic> switchLogic;
    ArrayList<PLCLogic> failureLogic;
    ArrayList<PLCLogic> occupiedLogic;
    ArrayList<PLCLogic> generalLogic;
    boolean explicitLogic;
    BetterList explicitBlocks;
    
    public TrackCont_PLC(String FileName){
        plcFileName="";
        crossingLogic=new ArrayList<PLCLogic>();
        switchLogic=new ArrayList<PLCLogic>();
        explicitBlocks=new BetterList(-1,null);
        updatePLCCode(FileName);
    }
    
    public boolean updatePLCCode(String fileName){
        if(!plcFileName.equals(fileName)){
            plcFileName=fileName;
            BufferedReader reader=null;
            File plcFile=new File(plcFileName);
            try{
                reader=new BufferedReader(new FileReader(plcFile));
                //PUT PLC READ CODE HERE
                String line;
                ArrayList<PLCLogic> activeList=null;
                while((line=reader.readLine())!=null && line.length()!=0){
                    switch(line){
                        case("crossing:"):
                            explicitLogic=false;
                            activeList=crossingLogic;
                            break;
                        case("switch:"):
                            explicitLogic=false;
                            activeList=switchLogic;
                            break;
                        case("failure:"):
                            explicitLogic=false;
                            activeList=failureLogic;
                            break;
                        case("occupied:"):
                            explicitLogic=false;
                            activeList=occupiedLogic;
                            break;
                        case("general:"):
                            explicitLogic=false;
                            activeList=generalLogic;
                            break;
                        case("explicit:"):
                            explicitLogic=true;
                            break;
                        default: //read in if statement
                            if(activeList==null){
                                return false;
                            }
                            if(!addToList(activeList,line)){
                                return false;
                            }
                            break;
                    }
                }
            }catch(FileNotFoundException e){
                e.printStackTrace();
                plcFileName="";
                return false;
            }catch(IOException e){
                e.printStackTrace();
                return false;
            }finally{
                try{
                    for(int i=0;i<crossingLogic.size();++i){
                        System.out.println(crossingLogic.get(i).nbs+"   "+crossingLogic.get(i).rbs);
                    }
                    System.out.println(switchLogic.get(0).nbs+"   "+switchLogic.get(0).rbs);
                    System.out.println(explicitBlocks.find(129).nbs);
                    if(reader!=null){
                        reader.close();
                    }
                }catch(IOException e){
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean addToList(ArrayList<PLCLogic> activeList,String line){
        String [] seperatedCode=line.split(" ");
        if(seperatedCode.length>=5){
            if(seperatedCode[0].equals("if")){
                PLCLogic newLogic=new PLCLogic(seperatedCode[2],seperatedCode[4],Integer.parseInt(seperatedCode[1]));
                if(explicitLogic){
                    if(seperatedCode[5]==null){
                        return false;
                    }
                    System.out.println("adding");
                    explicitBlocks.add(Integer.parseInt(seperatedCode[5]),newLogic);
                    return true;
                }
                activeList.add(newLogic);
                return true;
            }
        }
        return false;
    }
    
    public TrackBlock checkBlock(TrackBlock prevb, TrackModel model,SwitchStateSuggestion s, int blockNum){
        //test the track block at block num to see what type of block it is, act on it accordingly
        ArrayList<PLCLogic> checkLogic=generalLogic;
        for(int logicCount=0;logicCount<5;++logicCount){
            switch(logicCount){
                case 0:
                    checkLogic=generalLogic;
                    break;
                case 1:
                    if(model.getBlock(blockNum).infastructure.equals("crossing"))
                        checkLogic=crossingLogic;
                    else
                        checkLogic=null;
                    break;
                case 2:
                    if(model.getBlock(blockNum).failure)
                        checkLogic=failureLogic;
                    else
                        checkLogic=null;
                    break;
                case 3:
                    if(model.getBlock(blockNum).infastructure.equals("switch"))
                        checkLogic=switchLogic;
                    else
                        checkLogic=null;
                    break;
                case 4:
                    if(model.getBlock(blockNum).occupied)
                        checkLogic=occupiedLogic;
                    else
                        checkLogic=null;
                    break;  
            }
            if(checkLogic!=null){
                for(int i=0;i<checkLogic.size();++i){
                    switch(checkLogic.get(i).rbs){
                        case endOfTrack:
                            int relative=checkLogic.get(i).relativeBlockNum;
                            if(testLogicOnSwitchingBlock(model.(blockNum-relative-1),model.(blockNum-relative),checkLogic.get(i),s,prevb)){
                                setLogicOnSwitchingBlock(model.getBlock(blockNum),checkLogic.get(i));
                            }
                            break;
                        default:
                            if(testLogicOnBlock(model.getBlock(blockNum),model.getBlock(blockNum-checkLogic.get(i).relativeBlockNum),checkLogic.get(i),s,prevb)){
                                setLogicOnBlock(model.getBlock(blockNum),checkLogic.get(i));
                            }
                            break;
                    }
                }
            }
        }
        PLCLogic eLogic=explicitBlocks.find(blockNum);
        //it is an explicit block so explicit block stuff applies
        if(eLogic!=null){
            if(testLogicOnBlock(model.getBlock(blockNum),model.getBlock(blockNum-eLogic.relativeBlockNum),eLogic,s,prevb)){
                setLogicOnBlock(model.getBlock(blockNum),eLogic);
            }
        }
        return model.getBlock(blockNum);
    }
    
    private boolean testLogicOnBlock(TrackBlock currentTrackBlock, TrackBlock relativTrackBlock,PLCLogic plcl, SwitchStateSuggestion s, TrackBlock previousTBState){
        switch(plcl.rbs){
            case occupied:
                //return relativTrackBlock.getTrainPresent()!=0;
            case failure:
                //return relativTrackBlock.getFailureStatus();
            case endOfTrack:
                /*if(relativeTrackBlock.infastructure.equals("switch")){
                 *      return ((relativeTrackBlock.getSwitch().getState0()==currentTrackBlock)&&!relativeTrackBlock.getSwitch().state
                                || (relativeTrackBlock.getSwitch().getState1()==currentTrackBlock)&&!relativeTrackBlock.getSwitch().state);
                 * }
                 */
                return false;
            case trainInSugList:
                /* if(tb.switch){
                    * for(int i=0;i<s.trainNum.length;++s){
                       *      if(relativeTrackBlock.trainNumber==s.trainNum[i])
                       *            return true;
                       *
                    * }
                 * }
                 * return false;
                 */
            case temp:
                //return relativeTrackBlock.temp<=MINTEMP
        }
        return true;
    }
    
    //private boolean testLogicOnSwitchingBlock(){
        
    //}
    
    private TrackBlock setLogicOnBlock(TrackBlock currentTrackBlock,PLCLogic plcl){
        switch(plcl.nbs){
            case start:
                //currentTrackBlock.setGo(true);
                return currentTrackBlock;
            case stop:
                //currentTrackBlock.setGo(false);
                return currentTrackBlock;
            case cross:
                //currentTrackBlock.getCrossing().setState(true);
                return currentTrackBlock;
            case moveSwitch:
                //currentTrackBlock.getSwitch().setState(true);
                return currentTrackBlock;
            case fail:
                //currentTrackBlock.setFailureStatus(true);
                return currentTrackBlock;
            case heat:
                //currentTrackBlock.setHeater(true);
                return currentTrackBlock;
        }
        return null;
    }
}
