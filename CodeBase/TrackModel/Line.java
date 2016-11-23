import java.util.ArrayList;

class Line {

	public ArrayList<Block> blockList;
	String name;

	public Line(String line) {
		blockList = new ArrayList<Block>();
		name = new String(line);
	}

	public String getName() {
		return new String(name);
	}

	public void setName(String newName) {
		name = new String(newName);
	}

	public Block getBlockWithNumber(int num) {
		if ( num >= 0 && num < blockList.size()) {
			Block tempBlock = blockList.get(num);
			if(tempBlock != null){
				if(tempBlock.getNumber() == num) {
					return tempBlock;
				}
			}
		}

		for (int i = 0; i < blockList.size(); i++) {
			Block tempBlock = blockList.get(i);
			if(tempBlock != null) {
				if(tempBlock.getNumber() == num) {
					return tempBlock;
						
				}	
			}
		}
		return null;
	}

	public int getLineLength() {
		return blockList.size();
	}

	public void addBlock(Block newBlock) {
		try {
			blockList.add(newBlock.getNumber(),newBlock);
		}
		catch(Exception e) {
			int space = newBlock.getNumber()-blockList.size();
			for(int n = 0; n < space; n++) {	
				blockList.add(null);	
			}
			blockList.add(newBlock.getNumber(),newBlock);
		}

	}
}
