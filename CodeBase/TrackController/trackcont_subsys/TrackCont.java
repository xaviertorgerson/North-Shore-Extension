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
    //might just want to have the model be in here so I can reference/change it more easily
    //weird dependencies mightbe a problem
    
    public TrackCont(int i,int [] ranges,TrackCont_GUI g, TrackModel m,CTCOffice o,boolean setGui){
        trackRange=ranges;
        gui=g;
        controlsGui=false;
        id=i;
        plc=new TrackCont_PLC(("Wayside"+i+"_PLCCode.txt"),ranges);
        switchState=new SwitchStateSuggestion[4];
        occupiedBlockNumbers=new int[20];
        controlsGui=setGui;
        if(plc.error){
            stopEverything();
            System.out.println("PCL ERROR");
        }
    }
    public void updateUI(Block tb,boolean top){
        if(controlsGui)
            gui.updateUI(tb, top);
    }
    //add train
    public void addTrain(){
        if(model.getBlockWithNumber(trackRange[2]).getTrainPresent()==0){
            model.addTrain(trackRange[2]);
        }
    }
    public Block getBlock(int blockNum){
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
                model.getBlockWithNumber(j).setAuthority(0);
            }
        }
    }
    //should return a series of block requests, maybe have it so that only blocks that need changed
    //will be in the request along with the changes
    public void updateModel(boolean top, int startNum, boolean goToNext){
        //process each block through the plc
        Block currentBlock=getBlock(startNum);
        while(blockInRange(currentBlock.getNumber())){
            SwitchStateSuggestion s=new SwitchStateSuggestion(-10,null,null);
            if(currentBlock.getInfrastructure().equals("switch")){
                if(trackRange.length>2){ //if the track does split to another range (i.e. has a switch but isn't a loop)
                    if(!top){ //only go to the other range if you are on the first one
                        //checks all blocks not already found/blocks in another range
                        if(currentBlock.getSwitch().getState0().getNumber()==currentBlock.getNumber()+1){//check which way the switch splits the track (>- or -<)
                            Block nb=currentBlock.getSwitch().getState1();
                            updateModel(false,nb.getNumber(),true);
                        }
                        else if(currentBlock.getSwitch().getNumber().getState0()==currentBlock.getNumber()-1){
                            Block nb=currentBlock.getSwitch().getState1();
                            updateModel(false,nb.getNumber(),false);
                        }else if(currentBlock.getSwitch().getNumber().getState1()==currentBlock.getNumber()+1){//check which way the switch splits the track (>- or -<)
                            Block nb=currentBlock.getSwitch().getState0();
                            updateModel(false,nb.getNumber(),true);
                        }
                        else if(currentBlock.getSwitch().getNumber().getState1()==currentBlock.getNumber()-1){
                            Block nb=currentBlock.getSwitch().getState0();
                            updateModel(false,nb.getNumber(),false);
                        }
                    }else{
                        return;
                    }
                }
                //find the switch state suggestion for this block
                for(int i=0;i<switchState.length && switchState[i]!=null;++i){
                    if(currentBlock.getNumber()==switchState[i].blockNum)
                        s=switchState[i];
                }
                if(s.blockNum<0){
                    System.out.println("major error: switch suggestion not set");
                }
            }
            Block checkedBlock=plc.checkBlock(prevTrackModel.getBlockWithNumber(currentBlock.getNumber()), getAllBlocksInRange(), s, currentBlock); //check block using the PLC
            
            //send block back into the model
            model.updateBlock(checkedBlock);
            
            //update the UI
            updateUI(checkedBlock,top);
            
            //if any switch has chainged, you must reevaluate all occupied blocks, make sure that everything will work
            if(plc.switchChange){
                plc.switchChange=false;
                int i=0;
                while(occupiedBlockNumbers[i]!=0){
                    plc.checkBlock(prevTrackModel.getBlockWithNumber(occupiedBlockNumbers[i]), getAllBlocksInRange(), s, prevTrackModel.getBlockWithNumber(occupiedBlockNumbers[i]));
                }
            }
            if(currentBlock.getInfrastructure().equals("switch")){
                    currentBlock=currentBlock.getSwitch().getState0();
            }else{
                if(goToNext){ //moving allong the track going from previous to next block
                    currentBlock=currentBlock.getNextBlock();
                }else{ //moving allong the track going from next to previous block
                    currentBlock=currentBlock.getPrevBlock();
                }
            }
            if(currentBlock==null){ //the next block is the yard (null) or doesn't exits (also null)
                return;
            }
        }
        return;
    }
    private Block[] getAllBlocksInRange(){
        Block [] allBlocks=new Block [trackRange.length/2];
        int allBlockLocation=0;
        for(int i=0;i<trackRange.length;i+=2){
            Block [] temp=model.getRange(trackRange);
            for(int j=allBlockLocation;j<temp.length;++j){
                allBlocks[j]=temp[j-allBlockLocation];
                allBlockLocation++;
            }
        }
        return allBlocks;
    }
    public void setSpeedAuth(int bNum,int newAuth, int newSpeed){
        Block tb=model.getBlockWithNumber(bNum);
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
        if(!plc.updatePLCCode(newCode)){
            System.out.println("ERROR: bad PLC code");
            stopEverything();
        }
        return true;
    }
}