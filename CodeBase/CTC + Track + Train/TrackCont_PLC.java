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
    ArrayList<PLCLogic> stationLogic;
    ArrayList<PLCLogic> manualLogic; //Need to stop the track controller from doing something stupid
    ArrayList<PLCLogic> failureLogic;
    ArrayList<PLCLogic> occupiedLogic;
    ArrayList<PLCLogic> generalLogic;
    ArrayList<TrainDirection> trainsOnTrack;
    boolean explicitLogic;
    BetterList explicitBlocks;
    int [] ranges;
    int suggestedTrain;
    String line;
    int direction;
    
    //flags
    boolean error;
    boolean switchChange;
    
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
        plcFileName="";
        crossingLogic=new ArrayList<PLCLogic>();
        switchLogic=new ArrayList<PLCLogic>();
        generalLogic=new ArrayList<PLCLogic>();
        explicitBlocks=new BetterList(-1,null);
        
        trainsOnTrack=new ArrayList<TrainDirection>();
        updatePLCCode(FileName);
        ranges=range;
        switchChange=false;
        error=false;
    }
    
    //iterate trhough the PLC code text file, update boolean logic
    public boolean updatePLCCode(String fileName){
        if(!plcFileName.equals(fileName)){
            plcFileName=fileName;
            BufferedReader reader=null;
            File plcFile=new File(plcFileName);
            System.out.println(plcFileName);
            try{
                reader=new BufferedReader(new FileReader(plcFile));
                //PUT PLC READ CODE HERE
                String line;
                ArrayList<PLCLogic> activeList=null;
                while((line=reader.readLine())!=null && line.length()!=0){
                    switch(line){
                        case("crossing:"):
                            System.out.println("set crossing");
                            explicitLogic=false;
                            activeList=crossingLogic;
                            break;
                        case("switch:"):
                            System.out.println("set switch");
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
                            System.out.println("set general");
                            explicitLogic=false;
                            activeList=generalLogic;
                            break;
                        case("explicit:"):
                            System.out.println("set explicit");
                            explicitLogic=true;
                            break;
                        default: //read in if statement
                            System.out.println(line);
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
                        System.out.println(generalLogic.get(i).nbs+"   "+generalLogic.get(i).relativeBlockNum+"   "+generalLogic.get(i).rbs);
                    }
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
                    System.out.println("adding to explicit logic");
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
    public Block checkBlock(Block prevBlock, SwitchStateSuggestion s, Block currentBlock){
        ArrayList<PLCLogic> checkLogic=generalLogic;
        switchChange=false;
        
        findIfTrainLeftTrack(prevBlock, currentBlock);
        checkForNewTrains(prevBlock,currentBlock);
        
        for(int trainDirIter=0;trainDirIter<trainsOnTrack.size();++trainDirIter){
            System.out.println("Train ID "+trainsOnTrack.get(trainDirIter).trainID+" direction="+trainsOnTrack.get(trainDirIter).direction);
        }
        
        for(int logicCount=0;logicCount<3;++logicCount){
            switch(logicCount){
                case 0:
                    checkLogic=generalLogic;
                    break;
                case 1:
                    if(currentBlock.getInfrastructure().equals("CROSSING")){
                        checkLogic=crossingLogic;
                    }else
                        checkLogic=null;
                    break;
                case 2:
                    if(currentBlock.getInfrastructure().equals("SWITCH")){
                        checkLogic=switchLogic;
                    }else
                        checkLogic=null;
                    break;
                /*case 3:
                    if(currentBlock.getFail())
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
                }
            }else{
                //break;
            }
        }
        PLCLogic eLogic=explicitBlocks.find(currentBlock.getNumber());
        if(eLogic==null&& currentBlock.getNumber()==9)
            System.out.println("9 returns null elogic");
        //it is an explicit block so explicit block stuff applies
        while(eLogic!=null){
            System.out.println("eLogic= "+eLogic.nbs);
            testLogicOnBlocks(eLogic.relativeBlockNum,currentBlock,eLogic,s,prevBlock);
            eLogic=explicitBlocks.find(currentBlock.getNumber());
        }
        explicitBlocks.resetFind();
        return currentBlock;
    }
    
    //find the direction a train is traveling
    private void checkForNewTrains(Block prevBlockState, Block currentBlockState){
        if(currentBlockState.getTrainPresent()!=0){
            System.out.println(currentBlockState.getTrainPresent()+" ****** "+prevBlockState.getTrainPresent());
            if(currentBlockState.getTrainPresent()!=prevBlockState.getTrainPresent()){
                if(findTrainOnTrack(currentBlockState.getTrainPresent())){
                    if(prevBlockState.getPreviousBlock().getTrainPresent()==currentBlockState.getTrainPresent()){
                        //train is comming from the left, is moving to the right, ->
                        trainsOnTrack.add(new TrainDirection(currentBlockState.getTrainPresent(),true));
                    }
                    else if(prevBlockState.getNextBlock().getTrainPresent()==currentBlockState.getTrainPresent()){
                        //train is comming from the right, is moving to the left, <-
                        trainsOnTrack.add(new TrainDirection(currentBlockState.getTrainPresent(),false));
                    }else{
                        System.out.println("TRACK FAILURE");
                    }
                }
            }
        }
    }
    
    private boolean findTrainOnTrack(int trainID){
        for(int i=0;i<trainsOnTrack.size();++i){
            if(trainsOnTrack.get(i).trainID==trainID)
                return true;
        }
        return false;
    }
    
    private void findIfTrainLeftTrack(Block prevBlockState, Block currentBlockState){
        if(prevBlockState.getTrainPresent()!=0 && currentBlockState.getTrainPresent()==0){
            for(int i=0;i<ranges.length;i+=2){
                if(currentBlockState.getNumber()==ranges[i] || currentBlockState.getNumber()==ranges[i+1]){
                    for(int j=0;j<trainsOnTrack.size();++j){
                        if(trainsOnTrack.get(j).trainID==prevBlockState.getTrainPresent())
                            trainsOnTrack.remove(j);
                    }
                }
            }
        }
    }
    
    //Test each block in range of the current block, range decided by the second value in the PLC code
    private void testLogicOnBlocks(int relativeBlockNum,Block currentBlock,PLCLogic plcl, SwitchStateSuggestion s, Block previousTBState){
        direction=1;
        
        if(plcl.relativeBlockNum<0)
            direction=-1;
        relativeBlockNum=Math.abs(relativeBlockNum);
        int [] checkedBlocks=new int[relativeBlockNum];
        checkedBlocks[0]=currentBlock.getNumber();
        int checkedBlocksIter=1;
        
        //Test logic on the current block
        Block relativeBlock=currentBlock;
        if(testLogicOnBlock(currentBlock,relativeBlock,plcl,s,previousTBState)&&plcl.rbs.state!=0){
            setLogicOnBlock(currentBlock,plcl,s);
        }
        if(currentBlock.getInfrastructure().equals("SWITCH")){
            if(plcl.alternatePath){//can either use the regular or alternate path for after the switch
                relativeBlock=currentBlock.getSwitch().getState1();
            }else{
                relativeBlock=currentBlock.getSwitch().getState0();
            }
        }else{
            relativeBlock=moveAlongTrack(checkedBlocks,relativeBlock);
        }
        
        //test logic on every block within the relativeBlockNum range
        for(int i=Math.abs(relativeBlockNum);i>0 && relativeBlock!=null;i-=1){
            //check the block, if the block has the correct logic then change the block and exit
            if(!blockInRange(relativeBlock.getNumber())){
                System.out.println("ERROR: Block Not In Range"); //not really stopping anything, will just print to console something whent wrong
                break;
            }
            
            //preform boolean operations on the block
            if(testLogicOnBlock(currentBlock,relativeBlock,plcl,s,previousTBState)){
                setLogicOnBlock(currentBlock,plcl,s);
            }
            
            //move along the block in the specified direction
            relativeBlock=moveAlongTrack(checkedBlocks,relativeBlock);
            if(currentBlock.getNumber()==12){
                if(relativeBlock!=null){
                    //System.out.println("move "+direction+" to block#"+relativeBlock.getNumber()+" occupation= "+relativeBlock.getTrainPresent());
                }
            }
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
    private boolean testLogicOnBlock(Block currentBlock, Block relativeBlock,PLCLogic plcl, SwitchStateSuggestion s, Block previousTBState){
            switch(plcl.rbs.state){
                case 0: //occupied,Noccupied
                    boolean test=(relativeBlock.getTrainPresent()!=0)==plcl.rbs.logic;
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
                        if(s.trainNum!=null){
                            for(int i=0;i<s.trainNum.length;i++){
                               if(relativeBlock.getTrainPresent()==s.trainNum[i]){
                                    suggestedTrain=i;
                                    return true;
                               }
                            }
                        }
                     }
                     return false;
                case 4: //temp
                    //return relativeBlock.getTempurature()<=MINTEMP;
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
            case cross1:
                currentBlock.getCrossing().setState(true);
                System.out.println("\nset to cross1");
                return currentBlock;
            case cross0:
                currentBlock.getCrossing().setState(false);
                return currentBlock;
            case switch1:
                if(!currentBlock.getSwitch().getState()){
                    System.out.println("\nset to switch1");
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
                //currentBlock.setFail(true);
                return currentBlock;
            case heat1:
                currentBlock.setHeaters(true);
                return currentBlock;
            case fail0:
                //currentBlock.setFailureStatus(false);
                return currentBlock;
            case heat0:
                currentBlock.setHeaters(false);
                return currentBlock;
        }
        return null;
    }
}
