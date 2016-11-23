import java.io.*;
import java.util.ArrayList;

class TrackModel {
	
	public ArrayList<Line> lineList;
	
	public TrackModel() {
		lineList = new ArrayList<Line>();
	}

	public void loadBlocks(String file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			String line;
			while ((line = br.readLine()) != null) {
				Block newBlock = new Block(line);
				addBlock(newBlock);	
			}
		}
		catch(Exception e) {
			System.out.println(e.getClass());
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

	public Block getBlockWithNumberOnLine(String line, int num) {
		for(int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getName().equals(line)) {
				return lineList.get(i).getBlockWithNumber(num);
			}
		}
		return null;
	}
		
}
