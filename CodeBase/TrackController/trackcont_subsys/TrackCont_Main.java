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
public class TrackCont_Main {

    /**
     * @param args the command line arguments
     */
    TrackCont controllers[];
    TrackCont_GUI gui;
    TrackModel model;
    CTCOffice office;
    
    //read relevant track controller info from a file, the file will determine each controllers range of blocks and the
    //number of controllers
    //File will also have the file name for each plc controller (1 plc per tc)
    public TrackCont_Main(TrackModel,CTCOffice){
        controllers=new TrackCont[];//will set controllers array size equal to the number of track controllers
        //for i=0 to all track controlers (probably will end up reading the track controller's range from a file or something)
        controllers[i]=new TrackCont(i,int start, int end,gui,fileName,model,office);//get track range from file
        
        gui=new TrackCont_GUI(this,controllers);
    }
    
    public TrackCont_TrackBlock[] processTrack(){
        //for each track controller, send it it's section of the track for processing, the controller will then
        //return its section, by the end all track blocks will be updated and the track model will have a completely
        //new track array
        //This will return the entire track to the track model
        //if anything needs to go to the office it will be sent after everything is processed
        controllers[i].updateTrack(its range of track blocks);
        //model.update(all new signals);
        //office.update(any signal that ctc needs to know about);
    }
    
    public TrackCont_TrackBlock updateBlock(int blockNum, int[] newParameters){
        //find the block and update it with the new parameters, also in block
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        new TrackCont_Main();
    }
}
