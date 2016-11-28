package trackcont_subsys;

import java.util.ArrayList;

class Line {

	String name;
	public ArrayList<Block> blockList;
	public ArrayList<Switch> switchList;

	public Line(String line) {
		blockList = new ArrayList<Block>();
		switchList = new ArrayList<Switch>();
		name = new String(line);
	}

	public String getName() {
		return new String(name);
	}

	public void setName(String newName) {
		name = new String(newName);
	}

	public Block getBlock(int num) {
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

	public int getLength() {
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

	public void loadSwitches() {
		for(int i = 0; i < blockList.size(); i++) {
			Block currentBlock = blockList.get(i);
			if(currentBlock != null) {
				if(currentBlock.getInfrastructure().equals("SWITCH")) {
					Switch newSwitch = new Switch(currentBlock.getSwitch(), currentBlock);
					for(int k = 0; k < blockList.size(); k++) {
						Block testBlock = blockList.get(k);
						if(testBlock != null) {
							if(testBlock.getSwitch() == currentBlock.getSwitch()) {
								if(Math.abs(testBlock.getNumber() - currentBlock.getNumber()) <= 1 && !testBlock.getInfrastructure().equals("SWITCH")) {
									newSwitch.setState0(testBlock);	
								}
								else if(!testBlock.getInfrastructure().equals("SWITCH")) {
									newSwitch.setState1(testBlock);
								}
							}
						}
					}
					switchList.add(newSwitch);
				}
			}
		}
	}

	public void linkBlocks() {
		for(int i = 0; i < blockList.size(); i++) {
			Block currentBlock = blockList.get(i);
			if(currentBlock != null) {
				if(currentBlock.getSwitch() == -1) {
					currentBlock.setNextBlock(blockList.get(i+1));
					currentBlock.setPreviousBlock(blockList.get(i-1));
				}
				else if(currentBlock.getSwitch() != -1 && currentBlock.getInfrastructure().equals("SWITCH")) {
					currentBlock.setNextBlock(blockList.get(i+1));
					currentBlock.setPreviousBlock(blockList.get(i-1));

					Block tempBlock = currentBlock.getPreviousBlock();
					if(tempBlock.getNextBlock() == null && tempBlock.getPreviousBlock() == null) {
						tempBlock.setNextBlock(blockList.get(i));
						tempBlock.setPreviousBlock(blockList.get(i-2));
					}
					else {
						i++;
						currentBlock = blockList.get(i);
						currentBlock.setNextBlock(blockList.get(i+1));
						currentBlock.setPreviousBlock(blockList.get(i-1));
					}	
				}
			}
		}

		for(int i = 0; i < blockList.size(); i++) {
			Block currentBlock = blockList.get(i);
			if(currentBlock != null) {
				if(currentBlock.getNextBlock() == null && currentBlock.getPreviousBlock() == null) {
					Block lesserBlock = null;
					if(i-1 >= 0)
						lesserBlock = blockList.get(i-1);
					if(lesserBlock != null) {
						if(lesserBlock.getNextBlock() == currentBlock) {
							currentBlock.setPreviousBlock(lesserBlock);
							continue;
						}
					}

					Block greaterBlock = null; 
					if(i+1 < blockList.size())
						greaterBlock = blockList.get(i+1);
					if(greaterBlock != null) {
						if(greaterBlock.getPreviousBlock() == currentBlock) {
							currentBlock.setNextBlock(greaterBlock);
						}
					}
				}
			}
		}
	}
		
	public void checkLinks() {	
		for(int i = 0; i < blockList.size(); i++) {
			Block currentBlock = blockList.get(i);
			if(currentBlock != null) {	
				if(currentBlock.getNextBlock() == null && currentBlock.getPreviousBlock() == null) {
					System.out.println(currentBlock.getNumber() + " is unlinked");
				}
				else if(currentBlock.getPreviousBlock() == null) {
					System.out.println(currentBlock.getNumber() + " has no previous") ;
				}
				else if(currentBlock.getNextBlock() == null) {
					System.out.println(currentBlock.getNumber() + " has no next");
				}
			}
		}
	}
}
