import java.util.Scanner;
import javax.swing.Timer;

public class TrackCont_Tester {

	public static void main(String[] args) {
		TrackModel track = new TrackModel();
		track.loadBlocks("trackData.csv");
		
		System.out.println("LOOK HERE " + track.getSwitch("Green",1));
		
		TrackCont controllers=new TrackCont[16];
		TrackCont_GUI gui;
        File plcFile=new File("TrackContList.txt");
        try{
            reader=new BufferedReader(new FileReader(plcFile));
            //PUT PLC READ CODE HERE
            String trackLine="Red";
            String line;
            boolean guiControl=true;
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
                    guiControl=false;
                }
            }
            //setup gui
            gui=new TrackCont_GUI(this,controllers);
            for(int i=0;i<controllers.length;++i){
                controllers[i].setGui(gui,false);
            }
            controllers[0].controlsGui=true;
            updateModel();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
		
		TrackCont_Master tcMas=new TrackCont_Master(track,ctc);
		Block testBlock = track.getBlock("Green", 152);
		
		Switch greenHead = track.getSwitch("Green", 0);
		greenHead.setState(true);
		
		System.out.println(testBlock);
		
		int trainNum=1;
		Scanner sc=new Scanner(System.in);
		   while(true)
		{
			System.out.println("Change Track State:");
			System.out.println("Enter in track state to change:\nO=occupancy, F=failure");
			String type=Scanner.nextLine();
			System.out.println("Enter in track line: Green or Red");
			String line=Scanner.nextLine();
			System.out.println("Enter in track block number");
			int bnum=Scanner.nextInt();
			Switch(type){
				case "O":
					Block block=track.getBlock(line,bnum);
					if(block!=null){
						if(block.getTrainPresent()==0){
							block.setTrainPresent(trainNum);
							trainNum++;
						}else{
							block.setTrainPresent(trainNum);
							trainNum--;
						}
					}
					break;
				case "F":
					Block block=track.getBlock(line,bnum);
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
		}

	}

}
