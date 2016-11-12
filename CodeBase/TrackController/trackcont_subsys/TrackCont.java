/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackcont_subsys;

/**
 *
 * @author Jeff
 */
public class TrackCont {
    int[] trackRange;
    TrackCont_GUI gui;
    TrackCont_PLC plc;
    TrackModel model;
    CTCOffice office;
    boolean controlsGui;
    int id;
    //might just want to have the model be in here so I can reference/change it more easily
    //weird dependencies mightbe a problem
    
    public TrackCont(int i,int start, int end,TrackCont_GUI g,String fileName, TrackModel m,CTCOffice o){
        trackRange=new int[2];
        trackRange[0]=start;
        trackRange[1]=end;
        gui=g;
        controlsGui=false;
        id=i;
        plc=new TrackCont_PLC(fileName);
    }
    
    public void updateUI(){
        
    }
    
    public block getBlock(int blockNum){
        
    }
    
    //should return a series of block requests, maybe have it so that only blocks that need changed
    //will be in the request along with the changes
    public TrackCont_TrackBlocks[] updateModel(~~){
        //process each block through the plc
    }
    
    public TrackCont_TrackBlock updateBlock(int bNum,int[] newParam){
        
    }
}