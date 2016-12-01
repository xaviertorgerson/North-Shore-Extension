import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class TrackModel {
	
	public ArrayList<Line> lineList;
	
	public TrackModel() {
		lineList = new ArrayList<Line>();
	}
	
	public void update(long dt) {
		//System.out.println("Update in trackmod");
		for(int i = 0; i < lineList.size(); i++) {
			for(int k = 0; k < lineList.get(i).trainCount(); k++) {

				Train updateTrain = lineList.get(i).trainList.get(k);
				Block checkBlock = lineList.get(i).getBlock(updateTrain.curBlock);

					updateTrain.updateGrade(checkBlock.getCumElevation(), checkBlock.getGrade());
					updateTrain.updateRequest(checkBlock.getAuthority(), checkBlock.getSetPointSpeed());
					updateTrain.update(dt);
					if(updateTrain.getDistance() > checkBlock.getSize()) {
						if(checkBlock.getNextBlock().getNumber() != updateTrain.prevBlock) {
							checkBlock.setTrainPresent(0);
							checkBlock.getNextBlock().setTrainPresent(updateTrain.getID());
							float newDistance = (updateTrain.getDistance()-checkBlock.getSize());
							System.out.println(newDistance);
							updateTrain.setDistance(newDistance);
							updateTrain.setBlock(checkBlock.getNextBlock().getNumber());
						}
						else if(checkBlock.getPreviousBlock().getNumber() != updateTrain.prevBlock) {
							checkBlock.setTrainPresent(0);
							checkBlock.getPreviousBlock().setTrainPresent(updateTrain.getID());
							float newDistance = (updateTrain.getDistance()/5280-checkBlock.getSize());
							System.out.println(newDistance);
							updateTrain.setDistance(newDistance);
							updateTrain.setBlock(checkBlock.getPreviousBlock().getNumber());
						}
					}
					
				System.out.println("Train " + updateTrain.getID() + " at " + updateTrain.getDistance() + "ft on Block " + checkBlock.getNumber());
				}
			} 			
		}
			
		//1. Set train speed and authority
		//2. Set train grade
		//3. Update trains
		//4. Update Train Presence
			//If move blocks
			//a. Change Previous and Current block for Train
			//b. Update Displacement

	public void loadBlocks(String file) {
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			String fileLine;
			while ((fileLine = br.readLine()) != null) {
				Block newBlock = new Block(fileLine);
				addBlock(newBlock);	
			}
		}
		catch(Exception e) {
			System.out.println(e.getClass());
		}
	
		for(int i = 0; i < lineList.size(); i++) {
			lineList.get(i).linkBlocks();
			lineList.get(i).loadSwitches();
		}
		
	}
	
	private void addBlock(Block newBlock) {
		int index = 0;
		while(index < lineList.size()) {
			if (lineList.get(index).getName().equals(newBlock.getLine()))
				break;
			index++;
		}
		if (index == lineList.size()) {
			lineList.add(new Line(newBlock.getLine()));		
		}
		lineList.get(index).addBlock(newBlock);
	}

	public void addTrain(int trainID, Block newBlock) {
		for(int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getName().equals(newBlock.getLine()))
				lineList.get(i).addTrain(trainID,newBlock);
		}
	}
	
	public Block getBlock(String line, int num) {
		Line newLine = getLine(line);
		if(newLine == null) 
			return null;
		return newLine.getBlock(num);
	}

	public Switch getSwitch(String line, int switchID) {
		Line newLine = getLine(line);
		if(newLine == null) 
			return null;
		return newLine.getSwitch(switchID);
	}

	public Line getLine(String line) {
		for(int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getName().equals(line)) {
				return lineList.get(i);
			}
		}
		return null;
	}
	
	public ArrayList<Block> getLine(String line, int start, int end) {
		for(int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getName().equals(line)) {
				return lineList.get(i).getLine(start, end);
			}
		}
		return null;
	}

	public static void inspect(TrackModel track) {
		Scanner user_input = new Scanner(System.in);

		for(;;){

			//Clear Screen
			for (int k = 0; k < 50; k++) {
				System.out.println("\n");
			}


			System.out.print("What block would you like to inspect? ");
			int inspectBlock = user_input.nextInt();
			user_input.nextLine();	
			
			System.out.print("What line? ");
			String inspectLine = user_input.nextLine();
			Block selectedBlock = track.getBlock(inspectLine, inspectBlock);
			
			if(selectedBlock != null){	
				selectedBlock.inspect();
			}
			else {
				break;
			}
		}

	}

	public static void main(String[] args) {

		TrackModel track = new TrackModel();
		track.loadBlocks("trackData.csv");
	
		//Train newTrain = new Train(10);
		track.addTrain(6,track.getBlock("Green",152));
		inspect(track);
	}

}
