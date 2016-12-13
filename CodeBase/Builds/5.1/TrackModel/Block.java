import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Block {

	//BLob Data
	private String[] parameterList;

	//ID
	private String line;
	private String section;
	private int number;
	
	//Parameters
	private float size;
	private float grade;
	private float speedLimit;
	private float elevation;
	private float cumElevation;	
	private int direction;

	//Infrastructure	
	private String infrastructure;	
	private boolean heaters;
	private int switchID; 
	private Switch junction;
	private Crossing crossing;
	private Station station;
	
	//Failures	
	private boolean failureStatus;
	private boolean brokenRailStatus, powerStatus, trackCircuitStatus;
	
	//Track Circuit
	private int trainPresent;
	
	//Track Signal
	private boolean go;
	private float setPointSpeed;
	private float authority;
	
	//Neighbors
	private Block nextBlock;
	private Block previousBlock;

	public Block() {
	}

	public Block(String csvLine) {
		loadBlock(csvLine);	
	}

	/**
	 * Parse String and set block data 
	 *
	 * @param csvLine string of Block data
	 */
	public void loadBlock(String csvLine) {
		
		parameterList = csvLine.split(",");

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
		//Infrastructure
		setInfrastructure(parameterList[6]);
		//Elevation
		setElevation(Float.parseFloat(parameterList[8]));
		//Cumaltive Elevation
		setCumElevation(Float.parseFloat(parameterList[9]));

		//Set No switch value
		setSwitchID(-1);
	
		//SwitchID
		if(parameterList.length > 10 && parameterList[10].length() > 1) {
			int switchNum = Integer.parseInt(parameterList[10].split(" ")[1]);
			setSwitchID(switchNum);
		}

		//Station
		if(getInfrastructure().equals("STATION")) {
			setStation(new Station(parameterList[6]));
		}
		//Crossing
		else if(getInfrastructure().equals("CROSSING")) {
			setCrossing(new Crossing());
		}
		//Direction
		if(parameterList.length > 11) {
		}

	}

	public String[] getParameterList() {
		return parameterList;
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

	public Block getNextBlock() {
		return nextBlock;
	}

	public Block getPreviousBlock() {
		return previousBlock;
	}

	public String getInfrastructure() {
		return infrastructure;	
	}

	public boolean getHeaters() {
		return heaters;
	}
	
	public int getSwitchID() {
		return switchID;
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
	
	public int getTrainPresent() {
		return trainPresent;
	}

	public boolean getGo() {
		return go;		
	}
	
	public float getSetPointSpeed() {
		return setPointSpeed;	
	}
	
	public float getAuthority() {
		return authority;	
	}

	public boolean getFailureStatus() {
		return (brokenRailStatus || powerStatus || trackCircuitStatus);
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

	public void setParameterList(String[] newParameterList) {
		parameterList = newParameterList;
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

	public void setNextBlock(Block newNextBlock) {
		nextBlock = newNextBlock;
	}
	
	public void setPreviousBlock(Block newPreviousBlock) {
		previousBlock = newPreviousBlock;
	}

	public void setInfrastructure(String newInfrastructure) {
		String beginning1 = newInfrastructure.split(" ")[0];
		String beginning2 = beginning1.split(";")[0];
		String beginning3 = beginning2.split(":")[0];
		
		if(beginning3.equals("RAILWAY")) 
			infrastructure = "CROSSING";	
		else 
			infrastructure = beginning3; 
	}

	public void setHeaters(boolean state) {
		heaters = state;
	}

	public void setSwitchID(int newSwitchID) {
		switchID = newSwitchID;
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

	public void setGo(boolean newGo) {
		go = newGo;
	}

	public void setSetPointSpeed(float newSetPointSpeed) {
		setPointSpeed = newSetPointSpeed;
	}

	public void setAuthority(float newAuthority) {
		authority = newAuthority;
		System.out.println("Set NEW Authority of " + authority + " on block num " + number);
	}

	public void setFailureStatus(){
		Random rand = new Random();
		int failure = rand.nextInt(3);
		if(failure == 0) {
			setBrokenRailStatus(true);
		}
		else if(failure == 1) {
			setPowerStatus(true);
		}
		else if(failure == 2) {
			setTrackCircuitStatus(true);
		}
	}

	public void resetFailureStatus() {
		setBrokenRailStatus(false);
		setPowerStatus(false);
		setTrackCircuitStatus(false);
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
		String output;

		output = "\tID\n\t\tLine: " + this.line + 
			"\n\t\tSection: " + this.section +
			"\n\t\tNumber: " + this.number +
			"\n\tParameters\n\t\tLength: " + this.size +
			"\n\t\tGrade: " + this.grade +
			"\n\t\tSpeed Limit: " + this.speedLimit +
			"\n\t\tElevation: " + this.elevation +
			"\n\t\tCum Elevation: " + this.cumElevation +
			"\n\tInfrastructure\n\t\tTrain Present: " + this.trainPresent +
			"\n\t\tHeaters: " + this.heaters;
		
		if(this.getInfrastructure().equals("SWITCH")) {
			output += "\n\t\tSwitch: " + this.switchID; 
		}
		else if(this.getInfrastructure().equals("STATION")) {
			output += "\n\t\tStation: " + this.station;
		}
		else if(this.getInfrastructure().equals("CROSSING")) {
			output += "\n\t\tCrossing: " + this.crossing;
		}
		
		output += "\n\tFailures\n\t\tBroken Rail: " + this.brokenRailStatus +
			"\n\t\tTrack Circuit Failure: " + this.powerStatus +
			"\n\t\tPower Failure: " + this.trackCircuitStatus;

		return output;
	}
	
	public void inspect() {
	
		Scanner user_input = new Scanner(System.in);
		int choice;

		boolean nextExists, previousExists;

		do{
			for (int k = 0; k < 50; k++) {
					System.out.println("\n");
			}
			System.out.println(toString());
			//System.out.print("\n0. Return to Browser\n1. Edit Size\n2. Toggle Train Present\n3. Toggle Heaters\n4. Fail\n5. Fix\n");
			System.out.print("\n0. Return to Browser\n");

			nextExists = false;
			previousExists = false;

			if(nextBlock != null) {
				System.out.println("1. Inspect Next: " + nextBlock.getNumber());
				nextExists = true;
			}
			if(previousBlock != null) {
				System.out.println("2. Inspect Previous: " + previousBlock.getNumber());
				previousExists = true;
			}
			if(infrastructure.equals("SWITCH")) {
				System.out.println("3. Toggle Switch: " + this.getSwitchID());
			}

			choice = user_input.nextInt();
			
			if(choice == 1 && nextExists) {
				nextBlock.inspect();
				break;
			}
			else if(choice == 2 && previousExists) {
				previousBlock.inspect();
				break;
			}
			else if(choice == 3) {
				getSwitch().toggleSwitch();	
			}
			/*	
			else if(choice == 4) {
				setFailureStatus();	
			}
			else if(choice == 5) {
				resetFailureStatus();	
			}
			else if(choice == 6) {
				System.out.print("What is the new size? ");
				size = user_input.nextInt();	
			}
			else if(choice == 7) {
				System.out.print("What train is present? ");
				trainPresent = user_input.nextInt();
			}
			*/
			else {
				break;
			}
		}while(choice >=1 && choice <=6);	
	}
}
