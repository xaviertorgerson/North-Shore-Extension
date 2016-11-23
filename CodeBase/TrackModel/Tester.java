import java.util.Scanner;

public class Tester {

	public static void main(String[] args) {
		
		TrackModel track = new TrackModel();
		track.loadBlocks("trackData.csv");
		
		Scanner user_input = new Scanner(System.in);
		
		for(;;){
			for (int k = 0; k < 50; k++) {
				System.out.println("\n");
			}
			System.out.print("What block would you like to inspect? ");
			int inspectBlock = user_input.nextInt();
			user_input.nextLine();	
			System.out.print("What line? ");
			String inspectLine = user_input.nextLine();
			Block selectedBlock = track.getBlockWithNumberOnLine(inspectBlock,inspectLine);
			if(selectedBlock != null){	
				selectedBlock.inspect();
			}
			else {
				break;
			}
		}
	}

}
