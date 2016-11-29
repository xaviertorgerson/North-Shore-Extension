/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackcont_subsys;
import java.util.*;
/**
 *
 * @author Jeff
 */
public class TrackCont {
    int[] trackRange; //contains all ranges in the section, more than one range because section will have
                      //alternate paths to go on
    TrackCont_GUI gui;
    TrackCont_PLC plc;
    TrackModel model;
    TrackModel prevTrackModel;
    CTCOffice office;
    boolean controlsGui;
    int id;
    SwitchStateSuggestion [] switchState;
    int [] occupiedBlockNumbers;
    int numberOfTrains;
    String line;
    //might just want to have the model be in here so I can reference/change it more easily
    //weird dependencies mightbe a problem
    
    public TrackCont(int i,int [] ranges, TrackModel m,CTCOffice o,String l){
        trackRange=ranges;
        controlsGui=false;
        id=i;
        plc=new TrackCont_PLC(("Wayside"+i+"_PLCCode.txt"),ranges);
        switchState=new SwitchStateSuggestion[4];
        occupiedBlockNumbers=new int[20];
        line=l;
        model=m;
        prevTrackModel=m;
        if(plc.error){
            stopEverything();
            System.out.println("PCL ERROR");
        }
    }
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
    public void addTrain(){
        System.out.println("in add Train");
        if(model.getBlock(line,trackRange[2]).getTrainPresent()==0){
            //model.addTrain(trackRange[2]);
        }
    }
    public Block getBlock(int blockNum){
        return null;
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
    private void stopEverything(){
        //set all blocks authorities to zero
        for(int i=0;i<trackRange.length && trackRange[i]!=-1;i+=2){
            for(int j=trackRange[i];j<trackRange[i+1];++j){
                model.getBlock(line,j).setGo(false);
            }
        }
    }
    
    //should return a series of block requests, maybe have it so that only blocks that need changed
    //will be in the request along with the changes
    public void updateModel(){
        gui.firstSwitch=true;
        boolean top=true;
        numberOfTrains=0;
        occupiedBlockNumbers=new int[20];
        for(int i=0;i<trackRange.length;i+=2){
            for(int blockNum=trackRange[i];blockNum<=trackRange[i+1];++blockNum){
                Block blockToBeChecked=model.getBlock(line,blockNum);
                SwitchStateSuggestion s=new SwitchStateSuggestion(-10,null,null);
                
                //if the block is a switch get the switchSuggestion for it
                if(blockToBeChecked.getInfrastructure().equals("SWITCH")){
                    s=getSwitchSuggestion(blockToBeChecked.getNumber());
                }
                
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
        //create a copy of the model for future use
        prevTrackModel=model;
    }
    
    public void updateBlock(int blockNum, SwitchStateSuggestion s, Block blockToBeChecked, boolean top){
        
        //use PLC to check block state and update block
        Block checkedBlock=plc.checkBlock(prevTrackModel.getBlock(line,blockNum), s, blockToBeChecked);

        //send block back into the model
        //model.updateBlock(checkedBlock);

        //update the UI
        updateUI(checkedBlock,top);
    }
    
    //get a swicth blocks switchStateSuggestion
    private SwitchStateSuggestion getSwitchSuggestion(int blockNum){
        SwitchStateSuggestion s=new SwitchStateSuggestion(-10,null,null);
        for(int k=0;k<switchState.length && switchState[k]!=null;++k){
            if(blockNum==switchState[k].blockNum){
                    s=switchState[k];
            }
        }
        if(s.blockNum<0){
            System.out.println("major error: switch suggestion not set");
        }
        return s;
    }
    public void setSpeedAuth(int bNum,int newAuth, int newSpeed){
        Block tb=model.getBlock(line,bNum);
        if(newSpeed<tb.getSpeedLimit()){
            tb.setSetPointSpeed(newSpeed);
        }
        tb.setAuthority(newAuth);
        if(bNum>trackRange[1])
            updateUI(tb,false);
        else
            updateUI(tb,true);
    }
    public void updateSwtiches(SwitchStateSuggestion newRoute){
        int i;
        for(i=0;i<switchState.length && switchState[i]!=null;++i){
            if(newRoute.blockNum==switchState[i].blockNum){
                switchState[i]=newRoute;
                return;
            }
        }
        if(i<switchState.length)
            switchState[i]=newRoute;
        else
            System.out.print("ERROR: switch doesn't exist");
    }
    public boolean updatePLCCode(String newCode){
        System.out.println("update PLC with new code at "+newCode);
        if(!plc.updatePLCCode(newCode)){
            System.out.println("ERROR: bad PLC code");
            stopEverything();
            return false;
        }
        return true;
    }
}