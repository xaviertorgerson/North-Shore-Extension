package trackcont_subsys;

public class Switch {
	
	private int ID;
	private Block center;
	private boolean state;
	private Block state0; 
	private Block state1;

	public Switch(int newID, Block newCenter) {
		setID(newID);	
		setCenter(newCenter);
	}
	
	public int getID() {
		return ID;
	}

	public Block getCenter() {
		return center;
	}

	public Block getState0() {
		return state0;
	}

	public Block getState1() {
		return state1;
	}

	public boolean getState() {
		return state;
	}
	
	public void setID(int newID) {
		ID = newID;
	}
	
	public void setCenter(Block newBlock) {
		center = newBlock;
	}

	public void setState0(Block newBlock) {
		state0 = newBlock;
	}
	
	public void setState1(Block newBlock) {
		state1 = newBlock;
	}
	
	public void setState(boolean newState) {
		state = newState;

		if(!state){
			center.setNextBlock(state0);
			if(state0.getNextBlock() == null) {
				state0.setNextBlock(center);
			}
			else if(state0.getPreviousBlock() == null) {
				state0.setPreviousBlock(center);
			}

			if(state1.getNextBlock() == center) {
				state1.setNextBlock(null);
			}
			else if(state1.getPreviousBlock() == center) {
				state1.setPreviousBlock(null);
			}
		}
		else if(state) {
			center.setNextBlock(state1);
			if(state0.getNextBlock() == center) {
				state0.setNextBlock(null);
			}
			else if(state0.getPreviousBlock() == center) {
				state0.setPreviousBlock(null);
			}

			if(state1.getNextBlock() == null) {
				state1.setNextBlock(center);
			}
			else if(state1.getPreviousBlock() == null) {
				state1.setPreviousBlock(center);
			}
		}
	}

	public void toggleSwitch() {
		setState(!state);
	}

	public String toString() {
			return "center " + center.getNumber() + " state0: " + state0.getNumber() + " state1: " + state1.getNumber();
	}
}
