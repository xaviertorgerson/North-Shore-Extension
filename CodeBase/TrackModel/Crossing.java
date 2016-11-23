public class Crossing {

	private boolean state;

	public Crossing() {
		state = false;	
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean newState) {
		state = newState;	
	}

	public String toString() {
		if(state) 
			return "DOWN";
		else 
			return "UP";
	}
}
