import java.util.Arrays;
import java.util.Scanner;

public class Block {

	private String[] parameterList;
	private String line;
	private String section;
	private int number;
	private float size;
	private float grade;
	private float speedLimit;
	private float elevation;
	private float cumElevation;	
	private int direction;
	
	private String name;
	private String infrastructure;	//Look at the infrastructure and if the infrastructure has a switch indicator on it, look at the switch 
									//member of the class
	public boolean heaters;
	private Switch junction;
	private Crossing crossing;
	private Station station;

	private int trainPresent;

	private boolean go;
	private float setPointSpeed;
	private float authority;

	private boolean failureStatus;
	private boolean brokenRailStatus, powerStatus, trackCircuitStatus;
	
	public Block() {
	}

	public Block(String csvLine) {
		parameterList = csvLine.split(",");
		System.out.println("Parameter List");	
		//Line
		setLine(new String(parameterList[0]));
		//Section
		setSection(new String(parameterList[1]));
		//Block Number
		setNumber(Integer.parseInt(parameterList[2]));
		//Block Length
		setSize(Float.parseFloat(parameterList[3]));
		//Block Grade
		setGrade(Float.parseFloat(parameterList[4]));
		//Speed Limit
		setSpeedLimit(Float.parseFloat(parameterList[5]));
		//Name
		setName(parameterList[6]);
		//Infrastructure
		setInfrastructure(parameterList[6]);
		//Elevation
		setElevation(Float.parseFloat(parameterList[7]));
		//Cumaltive Elevation
		setCumElevation(Float.parseFloat(parameterList[8]));
		if(parameterList.length > 9 && getInfrastructure().equals("SWITCH")) {
		    int switchNum = Integer.parseInt(parameterList[9].split(" ")[1]);
			System.out.println(switchNum);
			junction = new Switch(switchNum);
		}	
		if(parameterList.length > 10) {
			//Arrow Direction
			//setDirection(parameterList[10]);
		}
	}

	public String getLine() {
		return line;
	}
	
	public String getSection() {
		return section;
	}
	
	public int getNumber() {
		return number;
	}
	
	public float getSize() {
		return size;
	}
	
	public float getGrade() {
		return grade;
	}
	
	public float getSpeedLimit() {
		return speedLimit;
	}
	
	public float getElevation() {
		return elevation;
	}
	
	public float getCumElevation() {
		return cumElevation;
	}

	public int getDirection() {
		return direction;
	}

	public String getName() {
		return name;
	}

	public String getInfrastructure() {
		return infrastructure;	
	}

	public boolean getHeaters() {
		return heaters;
	}

	public Switch getSwitch() {
		return junction;
	}
	
	public Crossing getCrossing() {
		return crossing;
	}

	public Station getStation() {
		return station;
	}

	public float getSetPointSpeed() {
		return setPointSpeed;	
	}
	
	public float getAuthority() {
		return authority;	
	}

	public int getTrainPresent() {
		return trainPresent;
	}

	public boolean getFail() {
		return (brokenRailStatus && powerStatus && trackCircuitStatus);
	}

	public boolean getBrokenRailStatus() {
		return brokenRailStatus;
	}
	
	public boolean getPowerStatus() {
		return powerStatus;
	}
	
	public boolean getTrackCircuitStatus() {
		return trackCircuitStatus;
	}

	public void setLine(String newLine) {
		line = new String(newLine);
	}

	public void setSection(String newSection) {
		section = new String(newSection);
	}
	
	public void setNumber(int newNumber) {
		number = newNumber;
	}

	public void setSize(float newSize) {
		size = newSize;
	}

	public void setGrade(float newGrade) {
		grade = newGrade;
	}

	public void setSpeedLimit(float newSpeedLimit) {
		speedLimit = newSpeedLimit;
	}

	public void setElevation(float newElevation) {
		elevation = newElevation;
	}

	public void setCumElevation(float newCumElevation) {
		cumElevation = newCumElevation;
	}

	public void setDirection(int newDirection) {
		direction = newDirection;
	}

	public void setName(String newName) {
		name = new String(newName);
	}

	public void setInfrastructure(String newInfrastructure) {
		//Parse to first space
		
		infrastructure = new String(newInfrastructure);
	}

	public void setHeaters(boolean state) {
		heaters = state;
	}

	public void setSwitch(Switch newSwitch) {
		junction = newSwitch;
	}

	public void setCrossing(Crossing newCrossing) {
		crossing = newCrossing;
	}
	
	public void setStation(Station newStation) {
		station = newStation;
	}

	public void setTrainPresent(int trainID) {
		trainPresent = trainID; 
	}

	public void setSetPointSpeed(int newSetPointSpeed) {
		setPointSpeed = newSetPointSpeed;
	}

	public void setAuthority(int newAuthority) {
		authority = newAuthority;
	}

	public void setBrokenRailStatus(boolean state) {
		brokenRailStatus = state;
	}

	public void setPowerStatus(boolean state) {
		powerStatus = state;
	}
	
	public void setTrackCircuitStatus(boolean state) {
		trackCircuitStatus = state;
	}
	
	

	public String toString() {
		return "\tID\n\t\tLine: " + this.line + 
			"\n\t\tSection: " + this.section +
			"\n\t\tNumber: " + this.number +
			"\n\tParameters\n\t\tLength: " + this.size +
			"\n\t\tGrade: " + this.grade +
			"\n\t\tSpeed Limit: " + this.speedLimit +
			"\n\t\tElevation: " + this.elevation +
			"\n\t\tCum Elevation: " + this.cumElevation +
			"\n\tInfrastructure\n\t\tTrain Present: " + this.trainPresent +
			"\n\t\tHeaters: " + this.heaters +
			"\n\t\tInfrastructure: " + this.infrastructure +
			"\n\tFailures\n\t\tBroken Rail: " + this.brokenRailStatus +
			"\n\t\tTrack Circuit Failure: " + this.powerStatus +
			"\n\t\tPower Failure: " + this.trackCircuitStatus;
	}
	
	public void inspect() {
	
		Scanner user_input = new Scanner(System.in);
		int choice;

		do{
			for (int k = 0; k < 50; k++) {
					System.out.println("\n");
			}
			System.out.println(toString());
			System.out.print("\n1. Edit Size\n2. Toggle Train Present\n3. Toggle Heaters\n4. Toggle Broken Rail\n5. Toggle Track Circuit Failure\n6. Toggle Power Failure\n7. Return to browser\n? ");
			choice = user_input.nextInt();
			if(choice == 1) {
				System.out.print("What is the new size? ");
				size = user_input.nextInt();	
			}
			else if(choice == 2) {
				System.out.print("What train is present? ");
				trainPresent = user_input.nextInt();
			}
			else if(choice == 3) {
				heaters = !heaters;
			}
			else if(choice == 4) {
				brokenRailStatus = !brokenRailStatus;
			}
			else if(choice == 5) {
				trackCircuitStatus = !trackCircuitStatus;
			}
			else if(choice == 6) {
				powerStatus = !powerStatus;
			}
			else {
				break;
			}
		}while(choice >=1 && choice <=6);	
	}
}
