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
    int suggestedTrain;
    
    //flags
    boolean error;
    boolean switchChange;
    
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
                        /*case("failure:"):
                            explicitLogic=false;
                            activeList=failureLogic;
                            break;
                        case("occupied:"):
                            explicitLogic=false;
                            activeList=occupiedLogic;
                            break;*/
                        case("general:"):
                            explicitLogic=false;
                            activeList=generalLogic;
                            break;
                        case("explicit:"):
                            explicitLogic=true;
                            break;
                        default: //read in if statement
                            if(activeList==null){
                                error=true;
                                return false;
                            }
                            if(!addToList(activeList,line)){
                                error=true;
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
    public Block checkBlock(Block prevBlock, Block [] blocksInRange,SwitchStateSuggestion s, Block currentBlock){
        //test the track block at block num to see what type of block it is, act on it accordingly
        ArrayList<PLCLogic> checkLogic=generalLogic;
        for(int logicCount=0;logicCount<3;++logicCount){
            switch(logicCount){
                case 0:
                    checkLogic=generalLogic;
                    break;
                case 1:
                    if(currentBlock.getInfrastructure().equals("crossing"))
                        checkLogic=crossingLogic;
                    else
                        checkLogic=null;
                    break;
                case 2:
                    if(currentBlock.getInfrastructure().equals("switch"))
                        checkLogic=switchLogic;
                    else
                        checkLogic=null;
                    break;
                /*case 3:
                    if(currentBlock.getFailureStatus())
                        checkLogic=failureLogic;
                    else
                        checkLogic=null;
                    break;*/
                
                /*case 4:
                    if(currentBlock.getTrainPresent()!=0)
                        checkLogic=occupiedLogic;
                    else
                        checkLogic=null;
                    break;*/  
            }
            if(checkLogic!=null){
                for(int i=0;i<checkLogic.size();++i){
                    testLogicOnBlocks(checkLogic.get(i).relativeBlockNum,currentBlock,checkLogic.get(i),s,prevBlock);
                    break;
                }
            }
        }
        PLCLogic eLogic=explicitBlocks.find(currentBlock.getNumber());
        //it is an explicit block so explicit block stuff applies
        while(eLogic!=null){
            testLogicOnBlocks(eLogic.relativeBlockNum,currentBlock,eLogic,s,prevBlock);
            eLogic=explicitBlocks.find(currentBlock.getNumber());
        }
        return currentBlock;
    } 
    //Test each block in range of the current block, range decided by the second value in the PLC code
    private void testLogicOnBlocks(int relativeBlockNum,Block currentBlock,PLCLogic plcl, SwitchStateSuggestion s, Block previousTBState){
        int direction=1;
        if(plcl.relativeBlockNum<0)
            direction=-1;
        Block relativeBlock=currentBlock;
        for(int i=relativeBlockNum;i>0 && relativeBlock!=null;i-=direction){
            //check the block, if the block has the correct logic then change the block and exit
            if(!blockInRange(relativeBlock.getNumber())){
                System.out.println("ERROR: Block Not In Range"); //not really stopping anything, will just print to console something whent wrong
            }
            if(testLogicOnBlock(currentBlock,relativeBlock,plcl,s,previousTBState)){
                setLogicOnBlock(currentBlock,plcl,s);
                return;
            }
            if(currentBlock.getInfrastructure().equals("switch")){
                if(plcl.alternatePath){//can either use the regular or alternate path for after the switch
                    relativeBlock=relativeBlock.getSwitch().getState1();
                }else{
                    relativeBlock=relativeBlock.getSwitch().getState0();
                }
            }else{
                if(direction>0)
                    relativeBlock=relativeBlock.getNext();
                else
                    relativeBlock=relativeBlock.getPrev();
            }
        }
    }
    private boolean testLogicOnBlock(Block currentBlock, Block relativeBlock,PLCLogic plcl, SwitchStateSuggestion s, Block previousTBState){
        
            switch(plcl.rbs.state){
                case 0: //occupied,Noccupied
                    return (relativeBlock.getTrainPresent()!=0)==plcl.rbs.logic;
                case 1: //failure
                    return relativeBlock.getFailureStatus()==plcl.rbs.logic;
                case 2: //direction of switch set up or down (1 or 0), looks at switch set high (switchSet0 switchSet1)
                    if(relativeBlock.getInfrastructure().equals("switch")){
                        return (relativeBlock.getSwitch().getState()==plcl.rbs.logic);
                    }
                    return false;
                case 3: //train in suggestion list (switchSug)
                    if(currentBlock.getInfrastructure().equals("switch")){
                        for(int i=0;i<s.trainNum.length;i++){
                           if(relativeBlock.getTrainPresent()==s.trainNum[i]){
                                suggestedTrain=i;
                                return true;
                           }
                        }
                     }
                     return false;
                case 4: //temp
                    //return relativeBlock.getTempurature()<=MINTEMP;
            }
        return true;
    }
    private boolean blockInRange(int blockNum){
        int detectionRange=2;
        boolean detectionPossible=false;
        for(int i=0;i<ranges.length;i+=2){
            if(blockNum>=ranges[i]-detectionRange && blockNum<=ranges[i+1]+detectionRange){
                detectionPossible=true;
            }
        }
        return detectionPossible;
    }
    private Block setLogicOnBlock(Block currentBlock,PLCLogic plcl,SwitchStateSuggestion s){
        switch(plcl.nbs){
            case start:
                currentBlock.setGo(true);
                return currentBlock;
            case stop:
                currentBlock.setGo(false);
                return currentBlock;
            case cross1:
                currentBlock.getCrossing().setState(true);
                return currentBlock;
            case cross0:
                currentBlock.getCrossing().setState(false);
                return currentBlock;
            case switch1:
                if(!currentBlock.getSwitch().getState()){
                    currentBlock.getSwitch().setState(true);
                    switchChange=true;
                }
                return currentBlock;
            case switch0:
                if(currentBlock.getSwitch().getState()){
                    currentBlock.getSwitch().setState(false);
                    switchChange=true;
                }
                return currentBlock;
            case switchSug:
                if(currentBlock.getSwitch().getState()!=s.state[suggestedTrain]){
                    currentBlock.getSwitch().setState(s.state[suggestedTrain]);
                    switchChange=true;
                }
                return currentBlock;
            case fail1:
                currentBlock.setFailureStatus(true);
                return currentBlock;
            case heat1:
                currentBlock.setHeater(true);
                return currentBlock;
            case fail0:
                currentBlock.setFailureStatus(false);
                return currentBlock;
            case heat0:
                currentBlock.setHeater(false);
                return currentBlock;
        }
        return null;
    }
}
