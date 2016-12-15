import java.util.*;
import java.io.*;

//Simple tester code for the track controller, requires the track model
public class TrackCont_Tester {

	public static void main(String[] args) {
		TrackModel track = new TrackModel();
		track.loadBlocks("bin/trackData.csv");
		
		System.out.println("LOOK HERE " + track.getSwitch("Green",1));
		
		TrackCont [] controllers=new TrackCont[16];
		TrackCont_GUI gui;
        File plcFile=new File("TrackContList.txt");
        BufferedReader reader=null;
        try{
            reader=new BufferedReader(new FileReader(plcFile));
            //PUT PLC READ CODE HERE
            String trackLine="Red";
            String line;
            while((line=reader.readLine())!=null && line.length()!=0){
                if(line.equals("Green Line")){
                    trackLine="Green";
                }
                if(line.equals("Red Line")){
                    trackLine="Red";
                }
                if(line.charAt(0)=='C'){
                    String [] seperatedCode=line.split(",");
                    int [] ranges=new int [seperatedCode.length-2];
                    int id=Integer.parseInt(seperatedCode[1]);
                    for(int i=0;i<ranges.length;i++){
                        ranges[i]=Integer.parseInt(seperatedCode[i+2]);
                    }
                    controllers[id-1]=new TrackCont(id,ranges,track,null,trackLine);
                }
            }
            //setup gui
            gui=new TrackCont_GUI(null,controllers);
            for(int i=0;i<controllers.length;++i){
                controllers[i].setGui(gui,false);
            }
            controllers[0].controlsGui=true;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        for(int i=0;i<controllers.length;++i){
            controllers[i].updateModel();
        }
        Block testBlock = track.getBlock("Green", 152);

        Switch greenHead = track.getSwitch("Green", 0);
        greenHead.setState(true);

        System.out.println(testBlock);

        int trainNum=1;
        Scanner sc=new Scanner(System.in);
        System.out.println("\n\nNOTE: A Red block in the gui indicates that trains in this region are given 0 authority by the track model"
                + "\nMore specifically the wayside controller sets the block's go variable to false\n");
        System.out.println("NOTE: manual switch control will not automatically update the gui in test mode, "
                + "\nto manually switch the switch in this fashion you'll need to"
                + "\n1) select manual switch control"
                + "\n2) click on the switch you wish to toggle"
                + "\n3) using the next or previous wayside selectors go to the next or previous wayside"
                + "\n4) go back to the wayside with the switch");
        System.out.println("\nAlso if the track doesn't show up on the gui, try the same trick");
        while(true)
        {
            System.out.println("Change Track State:");
            System.out.println("Enter in track state to change:\nO=occupancy, F=failure");
            String type=sc.nextLine();
            System.out.println("Enter in track line: Green or Red");
            String line=sc.nextLine();
            System.out.println("Enter in track block number");
            int bnum=sc.nextInt();
            Block block;
            switch(type){
                case "O":
                    block=track.getBlock(line,bnum);
                    if(block!=null){
                            if(block.getTrainPresent()==0){
                                    block.setTrainPresent(trainNum);
                                    trainNum++;
                            }else{
                                    block.setTrainPresent(0);
                                    trainNum--;
                            }
                    }
                    break;
                case "F":
                    block=track.getBlock(line,bnum);
                    if(block!=null){
                            if(!block.getFailureStatus()){
                                    block.setFailureStatus();
                            }else{
                                    block.resetFailureStatus();
                            }
                    }
                    break;
                default:
                    System.out.println("Improper input");
                    break;
            }
            for(int i=0;i<controllers.length;++i){
                controllers[i].updateModel();
            }
            for(int i=0;i<controllers.length;++i){
                controllers[i].updateModel();
            }
            for(int i=0;i<controllers.length;++i){
                controllers[i].updateModel();
            }
            System.out.println("set the track state:"+type+" on "+line+" line block Number"+bnum+"\n\n");
            sc.nextLine();
        }

}

}
