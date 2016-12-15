/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author Jeff
 */

//Main class for the track controller
public class TrackCont {
    int[] trackRange; //contains all ranges in the section, more than one range because section will have
                      //alternate paths to go on
                      //NOTE: each controller technically has two ranges, its controllable range (range where the controller can set block states, given by track range)
                            //and its detectable range (range where the controller can detect block state, is 2 past the ends of its controllable block range)
    TrackCont_GUI gui; //gui, shared by all track controllers
    TrackCont_PLC plc; //the plc for only this controller
    TrackModel model; //track model, shared by all controllers
    CTCGUI office; //CTC office shared by all controllers
    boolean controlsGui; //if this is true then this controller will be displayed by the gui
    int id; //the id for this track controller, 1-16
    SwitchStateSuggestion [] switchState; //an array of switch state suggesstions given by the CTC office to the switches in this controllers jurisdiction
    int [] occupiedBlockNumbers; //all block numbers currently occupied by a train in this controllers range
    int numberOfTrains; //number of trains in this controllers range
    String line; //the line of this controller
    TrainOccupationFinder [] prevTrackOcup; //the location of trains in the controllers range during after the last track controller update, used to track train direction
    TrainOccupationFinder [] onlyDetectablePrevOcupation; //the loccation of trains 2 blocks out of its range (still in detectable range, not in controllable range)

    //Constructor for the track controller
    public TrackCont(int i,int [] ranges, TrackModel m,CTCGUI o,String l){
        trackRange=ranges;
        controlsGui=false;
        id=i;
        plc=new TrackCont_PLC(("Wayside"+i+"_PLCCode.txt"),ranges);
        switchState=new SwitchStateSuggestion[4];
        occupiedBlockNumbers=new int[20];
        line=l;
        model=m;
        office=o;
        
        int trackSize=0;
        for(int j=0;j<trackRange.length;j+=2){
            trackSize+=(trackRange[j+1]-trackRange[j])+1;
        }
        prevTrackOcup=new TrainOccupationFinder[trackSize];
        onlyDetectablePrevOcupation=new TrainOccupationFinder[8];
        for(int j=0;j<8;++j){
            onlyDetectablePrevOcupation[j]=new TrainOccupationFinder(0,0,0);
        }
        if(plc.error){
            stopEverything();
            System.out.println("PCL ERROR");
        }
    }
    
    //set the gui for this controller, decide if it should be displayed
    public void setGui(TrackCont_GUI g,boolean guiCont){
        gui=g;
        controlsGui=guiCont;
    }
    
    //update GUI one block at a time
    public void updateUI(Block tb,boolean top){
        if(controlsGui){
            gui.updateUI(tb, top);
        }
    }
    //add train
    public void addTrain(int trainID){
        if(model.getBlock(line,trackRange[2]).getTrainPresent()==0){
            model.addTrain(trainID,model.getBlock(line,trackRange[2]));
        }
    }

    //check if you can change the track block, can only look at track blocks when they are
    //the first two or the last two
    //returns 0 for block not found, returns 1 for top 2 for !top (bottom track or in track range 2)
    private int blockInRange(int blockNum){
        int check=0;
        for(int i=0;i<trackRange.length;i+=2){
            if(blockNum>=(trackRange[i]) && blockNum<=(trackRange[i+1]))
                check=i/2+1;
        }
        return check;
    }
    
    //Stop all trains (set authority to 0 on all blocks)
    private void stopEverything(){
        //set all blocks authorities to zero
        for(int i=0;i<trackRange.length && trackRange[i]!=-1;i+=2){
            for(int j=trackRange[i];j<trackRange[i+1];++j){
                model.getBlock(line,j).setGo(false);
            }
        }
    }
    
    //update all blocks within this controllers range
    public void updateModel(){
        gui.firstSwitch=true;
        gui.bottomStart=0;
        boolean top=true;
        numberOfTrains=0;
        occupiedBlockNumbers=new int[20];
        updatePLCOutOfBounds(); //update the direction for trains out of controllable range
        //iterate through all blocks within the track controllers range
        for(int i=0;i<trackRange.length;i+=2){
            for(int blockNum=trackRange[i];blockNum<=trackRange[i+1];++blockNum){
                Block blockToBeChecked=model.getBlock(line,blockNum);
                SwitchStateSuggestion s=new SwitchStateSuggestion(-10,null,null);
                
                //if the block is a switch get the switchSuggestion for it
                if(blockToBeChecked.getInfrastructure().equals("SWITCH")){
                    s=getSwitchSuggestion(blockToBeChecked.getNumber());
                }
                
                //update an individual block using PLC logic in the PLC
                updateBlock(blockNum,s,blockToBeChecked,top);
                
                //if a switch has changed state, all occupied blocks need to recheck their states
                if(plc.switchChange){
                    int ocupiedIter=0;
                    while(occupiedBlockNumbers[ocupiedIter]!=0 && ocupiedIter<occupiedBlockNumbers.length){
                        s=new SwitchStateSuggestion(-10,null,null);
                        boolean occupiedTop=occupiedBlockNumbers[ocupiedIter]==1;
                        Block occupiedReCheck=model.getBlock(line,occupiedBlockNumbers[ocupiedIter]);
                        updateBlock(occupiedBlockNumbers[ocupiedIter],s,blockToBeChecked,occupiedTop);
                        ocupiedIter++;
                    }
                }
                
                //if there is a train in this section add its block number to the array of occupied block numbers
                if(blockToBeChecked.getTrainPresent()!=0){
                    occupiedBlockNumbers[numberOfTrains]=blockToBeChecked.getNumber();
                    numberOfTrains++;
                }
            }
            top=false;
        }
        findOccupationOutOfBounds();
    }
    
    //update a single block within this controllers controllable range
    public void updateBlock(int blockNum, SwitchStateSuggestion s, Block blockToBeChecked, boolean top){
        //use PLC to check block state and update block
        int prevNum=blockNum-trackRange[0];
        if(blockNum>trackRange[1] || blockNum<trackRange[0]){
            prevNum=blockNum-trackRange[2]+(trackRange[1]-trackRange[0]+1);
        }
        Block checkedBlock;
        checkedBlock=plc.checkBlock(prevTrackOcup[prevNum], s, blockToBeChecked);
        //send block back into the model
        if(checkedBlock.getTrainPresent()!=0){
            office.trainOccupancyUpdate(checkedBlock,checkedBlock.getTrainPresent());
        }
        //update the UI
        updateUI(checkedBlock,top);
        
        prevTrackOcup[prevNum]=findOccupation(checkedBlock);
    }
    
    //find the occupation status (is it occupied, is its left occupied, is its right occupied) for a given block
    private TrainOccupationFinder findOccupation(Block checkedBlock){
        TrainOccupationFinder occupate;
        int ptn=0;
        int ntn=0;
        int tn=checkedBlock.getTrainPresent();
        if(checkedBlock.getNextBlock()!=null){
            ntn=checkedBlock.getNextBlock().getTrainPresent();
        }
        if(checkedBlock.getPreviousBlock()!=null){
            ptn=checkedBlock.getPreviousBlock().getTrainPresent();
        }
        occupate=new TrainOccupationFinder(tn,ptn,ntn);
        return occupate;
    }
    
    //detect if there are any trains outside of this blocks controllable range
    private void findOccupationOutOfBounds(){
        boolean goNext=false;
        for(int i=0;i<trackRange.length;i+=2){
            Block firstBlock=model.getBlock(line,trackRange[i]);
            for(int j=0;j<2;++j){
                if(firstBlock.getPreviousBlock()!=null && !goNext){
                    firstBlock=firstBlock.getPreviousBlock();
                    onlyDetectablePrevOcupation[i*2+j]=findOccupation(firstBlock);
                }
                if(firstBlock.getNextBlock()!=null && goNext){
                    firstBlock=firstBlock.getNextBlock();
                    onlyDetectablePrevOcupation[i*2+j]=findOccupation(firstBlock);
                }
            }
            goNext=!goNext;
        }
    }
    
    //find the direction of trains outside of this controllers controllable range
    private void updatePLCOutOfBounds(){
        for(int i=0;i<trackRange.length;i+=2){
            Block out=model.getBlock(line,trackRange[i]);
            if(out.getPreviousBlock()!=null){
                plc.checkForNewTrains(onlyDetectablePrevOcupation[i],out.getPreviousBlock());
                if(out.getPreviousBlock().getPreviousBlock()!=null){
                    plc.checkForNewTrains(onlyDetectablePrevOcupation[i+1],out.getPreviousBlock().getPreviousBlock());
                }
            }
            out=model.getBlock(line,trackRange[i+1]);
            if(out.getNextBlock()!=null){
                plc.checkForNewTrains(onlyDetectablePrevOcupation[i+2],out.getNextBlock());
                if(out.getNextBlock().getNextBlock()!=null){
                    plc.checkForNewTrains(onlyDetectablePrevOcupation[i+3],out.getNextBlock().getNextBlock());
                }
            }
        }
    }
    
    //get a swicth blocks switchStateSuggestion
    private SwitchStateSuggestion getSwitchSuggestion(int blockNum){
        SwitchStateSuggestion s=new SwitchStateSuggestion(-10,null,null);
        for(int k=0;k<switchState.length && switchState[k]!=null;++k){
            if(blockNum==switchState[k].blockNum){
                    s=switchState[k];
            }
        }
        return s;
    }
    
    //CTC sets speed and Authority
    public void setSpeedAuth(int bNum,float newAuth, float newSpeed){
        Block tb=model.getBlock(line,bNum);
        if(newSpeed<tb.getSpeedLimit()){
            tb.setSetPointSpeed(newSpeed);
        }
		tb.setSetPointSpeed(newSpeed);
		System.out.println("New authority for block " + bNum + " determined by Wayside is " + newAuth);
        tb.setAuthority(newAuth);
        if(bNum>trackRange[1]){
            updateUI(tb,false);
        }else{
            updateUI(tb,true);
        }
    }
    
    //Send new switchState exception from CTC
    public void updateSwtiches(SwitchStateSuggestion newRoute){
        int i;
        for(i=0;i<switchState.length && switchState[i]!=null;++i){
            if(newRoute.blockNum==switchState[i].blockNum){
                switchState[i]=newRoute;
                System.out.println("add new route to cont"+id+" to switch "+switchState[i].blockNum);
                return;
            }
        }
        if(i<switchState.length){
            switchState[i]=newRoute;
            System.out.println("add new route to cont"+id+" to switch "+switchState[i].blockNum);
        }else{
            System.out.print("ERROR: switch doesn't exist");
        }
    }
    
    //Update PLC Code
    public boolean updatePLCCode(String newCode){
        System.out.println("update PLC with new code at "+newCode);
        if(!plc.updatePLCCode(newCode)){
            System.out.println("ERROR: bad PLC code");
            stopEverything();
            return false;
        }
        return true;
    }
    
    //toggle a switch when it is clicked on by the user
    public void toggleSwitches(int blockNum){
        Block block=model.getBlock(line,blockNum);
        if(block.getTrainPresent()==0){
            block.getSwitch().setState(!block.getSwitch().getState());
        }
    }
    
    //set the PLC between manual and automatic mode
    //in automatic, switch states are decided by PLC code
    //in manual the user can set switch states
    public void setPLCManual(boolean m){
        plc.manual=m;
    }
}