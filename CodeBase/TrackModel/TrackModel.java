import java.io.*;
import java.util.ArrayList;

class TrackModel {
	
	public ArrayList<Line> lineList;
	
	public TrackModel() {
		lineList = new ArrayList<Line>();
	}

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

	public Block getBlock(String line, int num) {
		Line newLine = getLine(line);
		if(newLine == null) 
			return null;
		return getLine(line).getBlock(num);
	}

	public Line getLine(String line) {
		for(int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getName().equals(line)) {
				return lineList.get(i);
			}
		}
		return null;
	}
		
}
