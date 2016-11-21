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
    int [] ranges;
    int trainNum;
    
    public TrackCont_PLC(String FileName,int [] range){
        plcFileName="";
        crossingLogic=new ArrayList<PLCLogic>();
        switchLogic=new ArrayList<PLCLogic>();
        explicitBlocks=new BetterList(-1,null);
        updatePLCCode(FileName);
        ranges=range;
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
                PLCLogic newLogic=new PLCLogic(seperatedCode[3],seperatedCode[5],Integer.parseInt(seperatedCode[2]),seperatedCode[1].equals("ALT"));
                if(explicitLogic){
                    if(seperatedCode[6]==null){
                        return false;
                    }
                    System.out.println("adding");
                    explicitBlocks.add(Integer.parseInt(seperatedCode[6]),newLogic);
                    return true;
                }
                activeList.add(newLogic);
                return true;
            }
        }
        return false;
    }
    
    public TrackBlock checkBlock(TrackBlock prevBlock, TrackBlock [] blocksInRange,SwitchStateSuggestion s, TrackBlock currentBlock){
        //test the track block at block num to see what type of block it is, act on it accordingly
        ArrayList<PLCLogic> checkLogic=generalLogic;
        for(int logicCount=0;logicCount<5;++logicCount){
            switch(logicCount){
                case 0:
                    checkLogic=generalLogic;
                    break;
                case 1:
                    if(currentBlock.infastructure.equals("crossing"))
                        checkLogic=crossingLogic;
                    else
                        checkLogic=null;
                    break;
                case 2:
                    if(currentBlock.failure)
                        checkLogic=failureLogic;
                    else
                        checkLogic=null;
                    break;
                case 3:
                    if(currentBlock.infastructure.equals("switch"))
                        checkLogic=switchLogic;
                    else
                        checkLogic=null;
                    break;
                case 4:
                    if(currentBlock.occupied)
                        checkLogic=occupiedLogic;
                    else
                        checkLogic=null;
                    break;  
            }
            if(checkLogic!=null){
                for(int i=0;i<checkLogic.size();++i){
                    testLogicOnBlocks(checkLogic.get(i).relativeBlockNum,currentBlock,checkLogic.get(i),s,prevBlock);
                    break;
                }
            }
        }
        PLCLogic eLogic=explicitBlocks.find(currentBlock.getBlockNum());
        //it is an explicit block so explicit block stuff applies
        while(eLogic!=null){
            testLogicOnBlocks(eLogic.relativeBlockNum,currentBlock,eLogic,s,prevBlock);
            eLogic=explicitBlocks.find(currentBlock.getBlockNum());
        }
        return currentBlock;
    }
    
    //Test each block in range of the current block, range decided by the second value in the PLC code
    private void testLogicOnBlocks(int relativeBlockNum,TrackBlock currentTrackBlock,PLCLogic plcl, SwitchStateSuggestion s, TrackBlock previousTBState){
        int direction=1;
        if(plcl.relativeBlockNum<0)
            direction=-1;
        TrackBlock relativeTrackBlock=currentTrackBlock;
        for(int i=relativeBlockNum;i>0;i+=direction){
            //check the block, if the block has the correct logic then change the block and exit
            if(testLogicOnBlock(currentTrackBlock,relativeTrackBlock,plcl,s,previousTBState)){
                setLogicOnBlock(currentTrackBlock,plcl,s);
                return;
            }
            if(direction>0)
                relativeTrackBlock=relativeTrackBlock.getNext();
            else
                relativeTrackBlock=relativeTrackBlock.getPrev();
            if(!blockInRange(relativeTrackBlock.getBlockNum())){
                System.out.println("ERROR: Block Not In Range");
            }
            if(currentTrackBlock.infastructure.equals("switch")){
                if(plcl.alternatePath){//can either use the regular or alternate path for after the switch
                    relativeTrackBlock=relativeTrackBlock.getSwitch1();
                }else{
                    relativeTrackBlock=relativeTrackBlock.getSwitch0();
                }
            }
        }
    }
    
    private boolean testLogicOnBlock(TrackBlock currentTrackBlock, TrackBlock relativeTrackBlock,PLCLogic plcl, SwitchStateSuggestion s, TrackBlock previousTBState){
        
            switch(plcl.rbs.state){
                case 0: //occupied,Noccupied
                    //return (relativTrackBlock.getTrainPresent()!=0)==plcl.rbs.logic;
                case 1: //failure
                    //return relativTrackBlock.getFailureStatus()==plcl.rbs.logic;
                case 2: //end of track
                    /*if(relativeTrackBlock.infastructure.equals("switch")){
                     *      return ((relativeTrackBlock.getSwitch().getState0()==currentTrackBlock)&&!relativeTrackBlock.getSwitch().state
                                    || (relativeTrackBlock.getSwitch().getState1()==currentTrackBlock)&&!relativeTrackBlock.getSwitch().state);
                     * }
                     */
                    return false;
                case 3: //train in suggestion list
                    /* if(currentTrackBlock.infastructure.equals("switch")){
                        * for(int i=0;i<s.trainNum.length;++s){
                           *      if(relativeTrackBlock.trainNumber==s.trainNum[i]){
                                        trainNum=i;
                           *            return true;
                                  }
                           *
                        * }
                     * }
                     * return false;
                     */
                case 4: //temp
                    //return relativeTrackBlock.temp<=MINTEMP
            }
        return true;
    }
    
    private boolean blockInRange(int blockNum){
        int detectionPossible=2;
        for(int i=0;i<ranges.length;i+=2){
            if(blockNum>ranges[i+1]+detectionPossible){
                return false;
            }
            detectionPossible=0;
        }
        return true;
    }
    //private boolean testLogicOnSwitchingBlock(){
        
    //}
    
    private TrackBlock setLogicOnBlock(TrackBlock currentTrackBlock,PLCLogic plcl,SwitchStateSuggestion s){
        switch(plcl.nbs){
            case start:
                //currentTrackBlock.setGo(true);
                return currentTrackBlock;
            case stop:
                //currentTrackBlock.setGo(false);
                return currentTrackBlock;
            case cross1:
                //currentTrackBlock.getCrossing().setState(true);
                return currentTrackBlock;
            case cross0:
                //currentTrackBlock.getCrossing().setState(false);
                return currentTrackBlock;
            case switch1:
                //currentTrackBlock.getSwitch().setState(true);
                return currentTrackBlock;
            case switch0:
                //currentTrackBlock.getSwitch().setState(false);
                return currentTrackBlock;
            case switchSug:
                currentTrackBlock.getSwitch().setState(s.state[trainNum]);
                return currentTrackBlock;
            case fail1:
                //currentTrackBlock.setFailureStatus(true);
                return currentTrackBlock;
            case heat1:
                //currentTrackBlock.setHeater(true);
                return currentTrackBlock;
            case fail0:
                //currentTrackBlock.setFailureStatus(false);
                return currentTrackBlock;
            case heat0:
                //currentTrackBlock.setHeater(false);
                return currentTrackBlock;
        }
        return null;
    }
}
