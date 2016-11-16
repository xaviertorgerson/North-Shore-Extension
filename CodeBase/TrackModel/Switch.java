public class Switch {
	
	private int number;
	private int state0;
	private int state1;
	private boolean state;

	public Switch(int newNum) {
		setNumber(newNum);	
	}
	
	public int getNumber() {
		return number;
	}

	public int getState0() {
		return state0;
	}

	public int getState1() {
		return state1;
	}

	public boolean getState() {
		return state;
	}
	
	public void setNumber(int newNum) {
		number = newNum;
	}

	public void setState0(int blockNum) {
		state0 = blockNum;
	}
	
	public void setState1(int blockNum) {
		state1 = blockNum;
	}
	
	public void setState(boolean newState) {
		state = newState;
	}
}
