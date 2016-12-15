import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Line stores the set of Blocks, Switches and Trains that make up a line
 *
 * @author Xavier Torgerson
 * @since 2016-12-15
 */
class Line {

	String name;
	public ArrayList<Block> blockList;
	public ArrayList<Switch> switchList;
	public ArrayList<Train> trainList;

	/**
	 * Initialize line with name
	 *
	 * @param line name of line
	 */
	public Line(String line) {
		blockList = new ArrayList<Block>();
		switchList = new ArrayList<Switch>();
		trainList = new ArrayList<Train>();
		name = new String(line);
	}

	public String getName() {
		return new String(name);
	}
	
	public void setName(String newName) {
		name = new String(newName);
	}

	/**
	 * Get number of blocks in the line
	 *
	 * @return number of blocks on line
	 */
	public int getLength() {
		return blockList.size();
	}

	/**
	 * Get number of trains in the line
	 *
	 * @return number of trains on line
	 */
	public int trainCount() {
		return trainList.size();
	}

	/**
	 * Add new Block to line
	 *
	 * @param newBlock Block to add to line
	 */
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

	/**
	 * Add a train on a Block
	 *
	 * @param trainID ID of the new Train
	 * @param newBlock block where the train is to be added
	 */
	public void addTrain(int trainID, Block newBlock) {
		Train newTrain = new Train(trainID);
		newTrain.setBlock(newBlock.getNumber());
		trainList.add(newTrain);
		newBlock.setTrainPresent(trainID);
	}

	/**
	 * Get block on line with specific number
	 *
	 * @param line What line the desired block is on
	 * @param num What the block number of the desired block is
	 * @return desired block
	 */
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

	/**
	 * Get switch on line with switchID
	 *
	 * @param line lien the switch is on
	 * @param swithID ID of the desired switch
	 * @return desired Switch
	 */
	public Switch getSwitch(int switchID) {
		for(int i = 0; i < switchList.size(); i++) {
			if (switchList.get(i).getID() == switchID)
				return switchList.get(i);
		}
		return null;
	}

	/**
	 * Get array list of blocks on line between start and end
	 *
	 * @param line name of line where section is found
	 * @param start block number of the start of list
	 * @param end block number of the end of list
	 * @return Array list of blocks
	 */
	public ArrayList<Block> getLine(int start, int end) {
		ArrayList<Block> lineSection = new ArrayList<Block>();
		for(int i = start; i < end; i++) {
			lineSection.add(getBlock(i));
		}

		return lineSection;
	}

	/**
	 * Add the proper direction parameter to the Blocks
	 * Parse the section arrows (head/tail) and generate an int (+, 0, or -) 
	 * 		+ if the nextBlock parameter should be used
	 * 		- if the previousBlock parameter should be used
	 * 		0 if either can be used
	 */
	public void loadDirection() {
		
		String currentSection = "";
		String firstArrow = "";
		for(int i = 0; i < blockList.size(); i++) {
			Block currentBlock = blockList.get(i);
			if(currentBlock != null) {
				if(currentBlock.getSection().equals(currentSection)) { //On the same section
					if(currentBlock.getParameterList().length > 11) { //End of section
						if(firstArrow.equals("Head") && currentBlock.getParameterList()[11].equals("Tail")){
							setSectionWithDirection(currentSection, -1);
						}
						else if(firstArrow.equals("Tail") && currentBlock.getParameterList()[11].equals("Head")){
							setSectionWithDirection(currentSection, 1);
						}
						else if(firstArrow.equals("Head") && currentBlock.getParameterList()[11].equals("Head")){
							setSectionWithDirection(currentSection, 0);
						}
					}
				}
				else { //On a different section
					currentSection = currentBlock.getSection();
					if(currentBlock.getParameterList().length > 11) {
						firstArrow = currentBlock.getParameterList()[11];
						if(firstArrow.equals("Tail/Head") || firstArrow.equals("Head/Head")) {
							setSectionWithDirection(currentSection, 0);
						}
					}
				}
			}
		}
	}


	public void setSectionWithDirection(String newSection, int newDirection) {
		for(int i = 0; i < blockList.size(); i++) {
			Block currentBlock = blockList.get(i);
			if(currentBlock != null) {
				if(currentBlock.getSection().equals(newSection)) {
					currentBlock.setDirection(newDirection);
				}
			}
		}

	}


	/**
	 * Load appropriate switches using Block parameters 
	 * Link switches to appropriate blocks
	 */
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
					switchList.add(newSwitch);
					currentBlock.setSwitch(switchList.get(switchList.size()-1));
				}
			}
		}
	}

	/**
	 * Link switches to neighbors
	 * Every Block has a nextblock and previousblock parameter
	 */
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
