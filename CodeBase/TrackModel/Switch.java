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
	}

	public String toString() {
			return "center " + center.getNumber() + " state0: " + state0.getNumber() + " state1: " + state1.getNumber();
	}
}
