import java.util.ArrayList;
import java.util.Iterator;

class Line {

	String name;
	public ArrayList<Block> blockList;
	public ArrayList<Switch> switchList;
	public ArrayList<Train> trainList;

	public Line(String line) {
		blockList = new ArrayList<Block>();
		switchList = new ArrayList<Switch>();
		name = new String(line);
	}

	public int getLength() {
		return blockList.size();
	}

	public String getName() {
		return new String(name);
	}

	public void setName(String newName) {
		name = new String(newName);
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

	public void addTrain(int trainID, Block newBlock) {
		Train newTrain = new Train(trainID);
		newTrain.setBlock(newBlock.getNumber());
		trainList.add(newTrain);
		newBlock.setTrainPresent(trainID);
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

	public Switch getSwitch(int switchID) {
		for(int i = 0; i < switchList.size(); i++) {
			if (switchList.get(i).getID() == switchID)
				return switchList.get(i);
		}
		return null;
	}

	public ArrayList<Block> getLine(int start, int end) {
		ArrayList<Block> lineSection = new ArrayList<Block>();
		for(int i = start; i < end; i++) {
			lineSection.add(getBlock(i));
		}

		return lineSection;
	}

	public void loadSwitches() {
		
		//Iterate through block list	
		for(int i = 0; i < blockList.size(); i++) {
			
			Block currentBlock = blockList.get(i);
			if(currentBlock != null) {
				if(currentBlock.getInfrastructure().equals("SWITCH")) {
					
					Switch newSwitch = new Switch(currentBlock.getSwitchID(), currentBlock);	
					for(int k = 0; k < blockList.size(); k++) {
						Block testBlock = blockList.get(k);
						if(testBlock != null) {
							if(testBlock.getSwitchID() == currentBlock.getSwitchID()) {
								if(Math.abs(testBlock.getNumber() - currentBlock.getNumber()) <= 1 && !testBlock.getInfrastructure().equals("SWITCH")) {
									newSwitch.setState0(testBlock);	
								}
								else if(!testBlock.getInfrastructure().equals("SWITCH")) {
									newSwitch.setState1(testBlock);
								}
							}
						}
					}
					System.out.println(newSwitch); switchList.add(newSwitch);
					currentBlock.setSwitch(switchList.get(switchList.size()-1));
				}
			}
		}
	}

	public void linkBlocks() {
		for(int i = 0; i < blockList.size(); i++) {
			Block currentBlock = blockList.get(i);
			if(currentBlock != null) {
				if(currentBlock.getSwitchID() == -1) {
					currentBlock.setNextBlock(blockList.get(i+1));
					currentBlock.setPreviousBlock(blockList.get(i-1));
				}
				else if(currentBlock.getSwitchID() != -1 && currentBlock.getInfrastructure().equals("SWITCH")) {
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

		checkLinks();
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
