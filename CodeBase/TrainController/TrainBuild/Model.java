public class Model extends java.util.Observable {	
	
	private int counter;													//All model data here

	public Model(){
		System.out.println("Model()");
	}

	public void setValue(int value) {										//All methods to manipulate data also here
		this.counter = value;
		System.out.println("Model init: counter = " + counter);
		setChanged();
		notifyObservers(counter);											//it is the model's responsibilty to notify the view of a change
	} 
	
	public void incrementValue() {
		++counter;
		System.out.println("Model     : counter = " + counter);
		setChanged();
		notifyObservers(counter);
	} 
	
	public void decrementValue() {
		--counter;
		System.out.println();
	}
}