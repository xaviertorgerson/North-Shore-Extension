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
    //might just want to have the model be in here so I can reference/change it more easily
    //weird dependencies mightbe a problem
    
    public TrackCont(int i,int [] ranges,TrackCont_GUI g, TrackModel m,CTCOffice o){
        trackRange=ranges;
        gui=g;
        controlsGui=false;
        id=i;
        plc=new TrackCont_PLC(("Wayside"+i+"_PLCCode.txt"),ranges);
        switchState=new SwitchStateSuggestion[4];
    }
    
    public void updateUI(TrackBlock tb,boolean top){
        
    }
    
    //add train
    public void addTrain(){
        if(model.getBlock(trackRange[2]).getOccupation==0){
            model.addTrain(trackRange[2]);
        }
    }
    
    public TrackBlock getBlock(int blockNum){
        return null;
    }
    
    //check if you can change the track block, can only look at track blocks when they are
    //the first two or the last two
    private boolean blockInRange(int blockNum){
        boolean check=false;
        for(int i=0;i<trackRange.length;i+=2){
            if(blockNum>=(trackRange[i]) && blockNum<=(trackRange[i+1]))
                check=true;
        }
        return check;
    }
    
    private void stopEverything(){
        //set all blocks authorities to zero
        for(int i=0;i<trackRange.length && trackRange[i]!=-1;++i){
            for(int j=trackRange[i];j<trackRange[i+1];++j){
                model.getBlock(j).setAuthority(0);
            }
        }
    }
    
    //should return a series of block requests, maybe have it so that only blocks that need changed
    //will be in the request along with the changes
    public void updateModel(boolean top, int startNum, boolean goToNext){
        //process each block through the plc
        TrackBlock tb=getBlock(startNum);
        while(blockInRange(tb.getBlockNum())){
            SwitchStateSuggestion s=new SwitchStateSuggestion(-10,null,null);
            if(tb.infastructure.equals("switch")){
                if(trackRange.length>2){ //if the track does split to another range (i.e. has a switch but isn't a loop)
                    if(!top){ //only go to the other range if you are on the first one
                        //checks all blocks not already found/blocks in another range
                        if(tb.currentTrackBlock.getSwitch0()==currentTrackBlock.getBlockNum()+1){//check which way the switch splits the track (>- or -<)
                            TrackBlock nb=tb.getSwitch1();
                            updateModel(false,nb.blockNum,true);
                        }
                        if(currentTrackBlock.getSwitch0()==currentTrackBlock.getBlockNum()-1){
                            TrackBlock nb=tb.getSwitch1();
                            updateModel(false,nb.blockNum,false);
                        }
                    }else{
                        return;
                    }
                }
                //find the switch state suggestion for this block
                for(int i=0;i<switchState.length && switchState[i]!=null;++i){
                    if(tb.getBlockNum()==switchState[i].blockNum)
                        s=switchState[i];
                }
                if(s.blockNum<0){
                    System.out.println("major error: switch suggestion not set");
                }
            }
            TrackBlock checkedBlock=plc.checkBlock(prevTrackModel.getBlock(tb.getBlockNum()), getAllBlocksInRange(), s, tb.getBlockNum()); //check block using the PLC
            model.updateBlock(checkedBlock);
            gui.updateUI(checkedBlock,top);
            if(goToNext){ //moving allong the track going from previous to next block
                tb=tb.getNextBlock();
            }else{ //moving allong the track going from next to previous block
                tb=tb.getPrevBlock();
            }
            if(tb==null){ //the next block is the yard (null)
                return;
            }
        }
        return;
    }
    
    private TrackBlock[] getAllBlocksInRange(){
        TrackBlock [] allBlocks=new TrackBlock [trackRange.length/2];
        int allBlockLocation=0;
        for(int i=0;i<trackRange.length;i+=2){
            TrackBlock [] temp=model.getRange();
            for(int j=allBlockLocation;j<temp.length;++j){
                allBlocks[j]=temp[j-allBlockLocation];
                allBlockLocation++;
            }
        }
        return allBlocks;
    }
    
    public void setSpeedAuth(int bNum,float newAuth, float newSpeed){
        TrackBlock tb=model.getBlock(bNum);
        if(newSpeed<tb.getSpeedLimit){
            tb.setSetpointSpeed(newSpeed);
        }
        tb.setAuthority(newAuth);
        if(bNum>trackRange[1])
            gui.updateUI(tb,false);
        else
            gui.updateUI(tb,true);
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
        plc.updatePLCCode(newCode);
        return true;
    }
}