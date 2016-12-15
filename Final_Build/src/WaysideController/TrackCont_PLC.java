/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author Jeff
 */
/*Terms/Concepts:
 *      current block: the block that is being fed into the PLC, the block being worked on
 *      	This block will recieve the new block state if the PLC logic statement is true (the then of the if then)
 *      relative block: a block within the jurisdiction of the controller that will be tested
 *      	This block is tested acording to the if part of the PLC logic statement
 *      relative block #: A veriable that says how many blocks relative to the the current block will be tested
 *      	i.e. if the relative block # is equal to 2 and the current block number is equal to 4 
 *      		the relative block state will be tested block 4, then on the block next to block 4, finally next next
 *      	     Similarly if rb#=-1 and current block Number=23 then the relative block state will be tested on
 *      		block 23 and the block previous to 23
 *      relative block state: the state of the relative block being tested
 *      new block state: if the relative block state is true then the current block will be given this new state
 *      general: applies to every block
 *      explicit: only applies to the block specified
 *      logic type: the logic statement will only apply to blocks that are this type
 *      Priority: priority within a logic type is implicit given the oder of the logic statements (top to bottom)
 *      	the top logic statements will have lower priority compared to the lower statements
 *      	Priority of the logic types are as follows (1)general,(2)crossing,(3)switch,(4)explicit where 4 is highest priority

IMPORTANT: PLC code is case sensative, make sure that the PLC logic you write matches the way it is written in the PLCLogic and TrackCont_PLC java codes

PLC logic statements are written in the following format
 *      (logic type):
 *      if (relative block #) (relative block state) then (new block state) (explicit block # NOTE: optional)
 * 
 *      Logic Types:
 *          crossing: logic for railroad crossing blocks goes to crossingLogic arraylist
 *          switch: logic for switching blocks goes to switchLogic arraylist
 *          general: logic for general track blocks, track blocks with no special properties goes to generalLogic arraylist, will have lowest priority
 *          explicit: logic for explicit track blocks, track blocks that need extra logic goes to explicitLogic arraylist, will have highest priority
 *      
 *      relative block
 *          The number of blocks that will be tested, the PLC will check for the 'relative block state' on every designated block
 *          -x means that block is x blocks behind the track block currently being tested (iterate using getPreviousBlock)
 *          +x means that the relative block state will be tested on (iterate using getNextBlock)
 *  
 *      relative block state: the 'N' prefix denotes NOT, i.e. Noccupied means not occupied
 *          occupied: relative block is occupied by a train
 *          failure: relative block has failed
 *          switchSet(1 or 0): relative block has switch set to 1 or 0
 *          trainInSugList: relative block is occupied by a train in this switches switch state suggestion list
 *          no(Next or Prev)Connection: relative block returns a null getNextBlock or getPreviousBlock
 *          trainMovingRight or trainMovingLeft: find the direction of the train and if it is moving in the direction specified return true
 *      
 *      new block state:
 *          stop or start: set the blocks go to 0 or 1
 *          stopLeft or stopRight: if the train is moving in the indicated direction, set the blocks go to 0
 *          cross(1 or 0): set crossing's state to 0 or 1
 *          switch(1 or 0): set switch's state to 0 or 1
 *          switchSug: set switch's state to whatever is stored in the switch suggestion list
 *      
 *      explicit block #:
 *          the logic will only apply to this specified block 
 *          This should only be used in the explicit logic function
 */

public class TrackCont_PLC {
    public static final int MINTEMP=32;
    
    String plcFileName;
    ArrayList<PLCLogic> crossingLogic;
    ArrayList<PLCLogic> switchLogic;
    ArrayList<PLCLogic> manualLogic; //Need to stop the track controller from doing something stupid
    ArrayList<PLCLogic> generalLogic;
    ArrayList<TrainDirection> trainsOnTrack;
    boolean explicitLogic;
    BetterList explicitBlocks;
    int [] ranges;
    int suggestedTrain;
    String line;
    int direction;
    boolean manual;
    
    //flags
    boolean error;
    boolean switchChange;
    
    //struct to hold the direction a train with given trainID is moving 
    class TrainDirection{
        boolean direction;
        int trainID;
        TrainDirection(int num, boolean dir){
            direction=dir;
            trainID=num;
        }
    }
    
    //constructor
    public TrackCont_PLC(String FileName,int [] range){
        plcFileName=FileName;
        manual=false;
        crossingLogic=new ArrayList<PLCLogic>();
        switchLogic=new ArrayList<PLCLogic>();
        generalLogic=new ArrayList<PLCLogic>();
        explicitBlocks=new BetterList(-1,null);
        
        trainsOnTrack=new ArrayList<TrainDirection>();
        ranges=range;
        updatePLCCode(FileName);
        switchChange=false;
        error=false;
    }
    
    //iterate trhough the PLC code text file, update boolean logic
    public boolean updatePLCCode(String fileName){
        //plcFileName=fileName;
        BufferedReader reader=null;
        File plcFile=new File(fileName);
        try{
            PrintWriter overwrite=null;
            if(!plcFileName.equals(fileName)){
                overwrite=new PrintWriter(plcFileName,"UTF-8");
            }
            reader=new BufferedReader(new FileReader(plcFile));
            //PUT PLC READ CODE HERE
            String line;
            ArrayList<PLCLogic> activeList=null;
            while((line=reader.readLine())!=null && line.length()!=0){
                if(!plcFileName.equals(fileName)){
                    overwrite.println(line);
                }
                switch(line){
                    case("crossing:"):
                        explicitLogic=false;
                        activeList=crossingLogic;
                        break;
                    case("switch:"):
                        explicitLogic=false;
                        activeList=switchLogic;
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
            if(overwrite!=null){
                overwrite.close();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            //plcFileName="";
            return false;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }finally{
            try{
                if(reader!=null){
                    reader.close();
                }
            }catch(IOException e){
                return false;
            }
        }
        return true;
    }
    
    //parse a line from the PLC code into usable boolean logic
    private boolean addToList(ArrayList<PLCLogic> activeList,String line){
        String [] seperatedCode=line.split(" ");
        if(seperatedCode.length>=5){
            if(seperatedCode[0].equals("if")){
                PLCLogic newLogic=new PLCLogic(seperatedCode[3],seperatedCode[5],Integer.parseInt(seperatedCode[2]),seperatedCode[1].equals("ALT"));
                if(explicitLogic){
                    if(seperatedCode[6]==null){
                        return false;
                    }
                    explicitBlocks.add(Integer.parseInt(seperatedCode[6]),newLogic);
                    return true;
                }
                activeList.add(newLogic);
                return true;
            }
        }
        return false;
    }
    
    //test the track block at block num to see what type of block it is, act on it accordingly
    public Block checkBlock(TrainOccupationFinder lastTrainOcup, SwitchStateSuggestion s, Block currentBlock){
        ArrayList<PLCLogic> checkLogic=generalLogic;
        switchChange=false;
        
        if(lastTrainOcup!=null){
            checkForNewTrains(lastTrainOcup,currentBlock);
        }
        
        String logicType="";
        for(int logicCount=0;logicCount<3;++logicCount){
            switch(logicCount){
                case 0:
                    checkLogic=generalLogic;
                    logicType="general";
                    break;
                case 1:
                    if(currentBlock.getInfrastructure().equals("CROSSING")){
                        checkLogic=crossingLogic;
                        logicType="crossing";
                    }else
                        checkLogic=null;
                    break;
                case 2:
                    if(currentBlock.getInfrastructure().equals("SWITCH")){
                        checkLogic=switchLogic;
                        logicType="switch";
                    }else
                        checkLogic=null;
                    break;  
            }
            if(checkLogic!=null){
                for(int i=0;i<checkLogic.size();++i){
                    testLogicOnBlocks(checkLogic.get(i).relativeBlockNum,currentBlock,checkLogic.get(i),s,logicType);
                }
            }else{
                //break;
            }
        }
        PLCLogic eLogic=explicitBlocks.find(currentBlock.getNumber());
        //it is an explicit block so explicit block stuff applies
        while(eLogic!=null){
            testLogicOnBlocks(eLogic.relativeBlockNum,currentBlock,eLogic,s,"explicit");
            eLogic=explicitBlocks.find(currentBlock.getNumber());
        }
        explicitBlocks.resetFind();
        return currentBlock;
    }
    
    //find the direction a train is traveling
    public void checkForNewTrains(TrainOccupationFinder prevBlockState, Block currentBlockState){
        if(currentBlockState.getTrainPresent()!=0){
            if(currentBlockState.getTrainPresent()!=prevBlockState.trainNum){
                int trainIndex=findTrainOnTrack(currentBlockState.getTrainPresent());
                if(prevBlockState.prevTrainNum==currentBlockState.getTrainPresent()){
                    //train is comming from the left, is moving to the right, ->
                    System.out.println("train mobing right");
                    if(trainIndex==-1){
                        trainsOnTrack.add(new TrainDirection(currentBlockState.getTrainPresent(),true));
                    }else{
                        trainsOnTrack.get(trainIndex).direction=true;
                    }
                }
                else if(prevBlockState.nextTrainNum==currentBlockState.getTrainPresent()){
                    //train is comming from the right, is moving to the left, <-
                    if(trainIndex==-1){
                        trainsOnTrack.add(new TrainDirection(currentBlockState.getTrainPresent(),false));
                    }else{
                        trainsOnTrack.get(trainIndex).direction=false;
                    }
                }

            }
        }
    }
    
    //find if a given train is in the jurisdiction of the wayside controller
    private int findTrainOnTrack(int trainID){
        for(int i=0;i<trainsOnTrack.size();++i){
            if(trainsOnTrack.get(i).trainID==trainID)
                return i;
        }
        return -1;
    }
    
    //Test each block in range of the current block, range decided by the second value in the PLC code
    private void testLogicOnBlocks(int relativeBlockNum,Block currentBlock,PLCLogic plcl, 
            SwitchStateSuggestion s,String logicType){
        direction=1;
        
        if(plcl.relativeBlockNum<0)
            direction=-1;
        relativeBlockNum=Math.abs(relativeBlockNum);
        int [] checkedBlocks=new int[relativeBlockNum];
        checkedBlocks[0]=currentBlock.getNumber();
        
        //Test logic on the current block
        Block relativeBlock=currentBlock;
        if(testLogicOnBlock(currentBlock,relativeBlock,plcl,s)&&((plcl.rbs.state!=0 && plcl.rbs.state!=7 && plcl.rbs.state!=8) || plcl.nbs.state==1)){
            setLogicOnBlock(currentBlock,plcl,s);
        }
        if(currentBlock.getInfrastructure().equals("SWITCH") && logicType.equals("switch")){
            int switch0Num=currentBlock.getSwitch().getState0().getNumber();
            int switch1Num=currentBlock.getSwitch().getState1().getNumber();
            relativeBlock=moveAlongTrack(checkedBlocks,relativeBlock);
            if(plcl.alternatePath && relativeBlock.getNumber()==switch0Num){//can either use the regular or alternate path for after the switch
                relativeBlock=currentBlock.getSwitch().getState1();
            }else if(relativeBlock.getNumber()==switch1Num){
                relativeBlock=currentBlock.getSwitch().getState0();
            }
        }else{
            relativeBlock=moveAlongTrack(checkedBlocks,relativeBlock);
        }
        
        //test logic on every block within the relativeBlockNum range
        for(int i=Math.abs(relativeBlockNum);i>0 && relativeBlock!=null;i-=1){
            //check the block, if the block has the correct logic then change the block and exit
            if(!blockInRange(relativeBlock.getNumber())){
                //System.out.println("ERROR: Block Not In Range"); //not really stopping anything, will just print to console something whent wrong
                break;
            }
            
            //preform boolean operations on the block
            if(testLogicOnBlock(currentBlock,relativeBlock,plcl,s)){
                setLogicOnBlock(currentBlock,plcl,s);
            }
            
            //move along the block in the specified direction
            relativeBlock=moveAlongTrack(checkedBlocks,relativeBlock);
        }
    }
    
    //move to the proceding block on the line
    private Block moveAlongTrack(int [] checkedBlocks,Block relativeBlock){
        Block prevBlock=relativeBlock.getPreviousBlock();
        Block nextBlock=relativeBlock.getNextBlock();
        boolean alreadyCheckedPrev=false;
        boolean alreadyCheckedNext=false;
        for(int i=0;i<checkedBlocks.length;++i){
            if(prevBlock!=null){
                if(prevBlock.getNumber()==checkedBlocks[i]){
                    alreadyCheckedPrev=true;
                }
            }
            if(nextBlock!=null){
                if(nextBlock.getNumber()==checkedBlocks[i]){
                    alreadyCheckedNext=true;
                }
            }
        }
        if(direction>0){
            if(alreadyCheckedNext){
                direction=-1;
                return relativeBlock.getPreviousBlock();
            }else{
                return relativeBlock.getNextBlock();
            }
        }else{
            if(alreadyCheckedPrev){
                direction=1;
                return relativeBlock.getNextBlock();
            }else{
                return relativeBlock.getPreviousBlock();
            }
        }
    }
    
    //test each individual block according to user set PLC code
    private boolean testLogicOnBlock(Block currentBlock, Block relativeBlock,PLCLogic plcl, SwitchStateSuggestion s){
            switch(plcl.rbs.state){
                case 0: //occupied,Noccupied
                    return (relativeBlock.getTrainPresent()!=0)==plcl.rbs.logic;
                case 1: //failure
                    return relativeBlock.getFailureStatus()==plcl.rbs.logic;
                case 2: //direction of switch set up or down (1 or 0), looks at switch set high (switchSet0 switchSet1)
                    if(relativeBlock.getInfrastructure().equals("SWITCH")){
                        return (relativeBlock.getSwitch().getState()==plcl.rbs.logic);
                    }
                    return false;
                case 3: //train in suggestion list (switchSug)
                    if(currentBlock.getInfrastructure().equals("SWITCH")){
                        if(s.trainNum!=null && s.state!=null){
                            for(int i=0;i<s.trainNum.length;i++){
                               if(relativeBlock.getTrainPresent()==s.trainNum[i] && relativeBlock.getTrainPresent()!=0){
                                    suggestedTrain=i;
                                    return true;
                               }
                            }
                        }
                     }
                     return false;
                case 4: //temp
                    //return relativeBlock.getTempurature()<=MINTEMP;
                     return false;
                case 5: //noNextConnection
                    if(relativeBlock.getNextBlock()==null){
                        return true;
                    }
                    return false;
                case 6: //noPrevConnection
                    if(relativeBlock.getPreviousBlock()==null){
                        return true;
                    }
                    return false;
                case 7: //trian moving right, means block is occupied and train moves right (i.e. to next block)
                    if(relativeBlock.getTrainPresent()!=0 && trainsOnTrack!=null){
                        int trainIndex=findTrainOnTrack(relativeBlock.getTrainPresent());
                        if(trainIndex!=-1){
                            return trainsOnTrack.get(trainIndex).direction;
                        }
                    }
                    return false;
                case 8: //train is moving left
                    if(relativeBlock.getTrainPresent()!=0 && trainsOnTrack!=null){
                        int trainIndex=findTrainOnTrack(relativeBlock.getTrainPresent());
                        if(trainIndex!=-1){
                            return !trainsOnTrack.get(trainIndex).direction;
                            
                        }
                    }
                    return false;
            }
        return true;
    }
    
    //find if the block is detectable from this track controller, within the track controllers range
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
    
    //change block values
    private Block setLogicOnBlock(Block currentBlock,PLCLogic plcl,SwitchStateSuggestion s){
        switch(plcl.nbs){
            case start:
                currentBlock.setGo(true);
                return currentBlock;
            case stop:
                currentBlock.setGo(false);
                return currentBlock;
            case stopRight:
                int trainIndex=findTrainOnTrack(currentBlock.getTrainPresent());
                if(trainIndex!=-1){
                    if(trainsOnTrack.get(trainIndex).direction){
                        currentBlock.setGo(false);
                    }
                }
                return currentBlock;
            case stopLeft:
                trainIndex=findTrainOnTrack(currentBlock.getTrainPresent());
                if(trainIndex!=-1){
                    if(!trainsOnTrack.get(trainIndex).direction){
                        currentBlock.setGo(false);
                    }
                }
                return currentBlock;
            case cross1:
                currentBlock.getCrossing().setState(true);
                return currentBlock;
            case cross0:
                currentBlock.getCrossing().setState(false);
                return currentBlock;
            case switch1:
                //make sure that the state is new and that the switch block doesn't have a train on it
                if(!currentBlock.getSwitch().getState() && currentBlock.getTrainPresent()==0){
                    currentBlock.getSwitch().setState(true);
                    switchChange=true;
                }
                return currentBlock;
            case switch0:
                if(currentBlock.getSwitch().getState() && currentBlock.getTrainPresent()==0){
                    currentBlock.getSwitch().setState(false);
                    switchChange=true;
                }
                return currentBlock;
            case switchSug:
                if(s.state!=null && s.trainNum!=null && !manual){
                    if(currentBlock.getSwitch().getState()!=s.state[suggestedTrain] && currentBlock.getTrainPresent()==0){
                        currentBlock.getSwitch().setState(s.state[suggestedTrain]);
                        switchChange=true;
                    }
                }
                return currentBlock;
        }
        return null;
    }
}
