import java.util.Arrays;
import java.util.Scanner;

public class Block {

	public String line;
	public String section;
	public int number;
	public float grade;
	public float speedLimit;
	public float elevation;
	public float cumElevation;	
	public float size;
	public String infrastructure;	
	public String switchBlock;
	public String direction;
	public boolean brokenRailStatus, powerStatus, trackCircuitStatus;

	public boolean trainPresent;
	public boolean heaters;
	
	public Block() {
	}

	public Block(String csvLine) {
		String[] parameterList = csvLine.split(",");
		
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
		setElevation(Float.parseFloat(parameterList[7]));
		//Cumaltive Elevation
		setCumElevation(Float.parseFloat(parameterList[8]));
		if(parameterList.length > 9) {
			//Switch Block
			setSwitchBlock(parameterList[9]);
		}	
		if(parameterList.length > 10) {
			//Arrow Direction
			setDirection(parameterList[10]);
		}
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

	public void setSize(float newSize) {
		size = newSize;
	}
	
	public void setInfrastructure(String newInfrastructure) {
		infrastructure = new String(newInfrastructure);
	}
	
	public void setSwitchBlock(String newSwitchBlock) {
		switchBlock = new String(newSwitchBlock);
	}

	public void setDirection(String newDirection) {
		direction = new String(newDirection);
	}

	public void setBrokenRailFail(boolean state) {
		brokenRailStatus = state;
	}

	public void setPowerFail(boolean state) {
		powerStatus = state;
	}
	
	public void setTrackCircuitFail(boolean state) {
		trackCircuitStatus = state;
	}
	
	public void setTrainPresent(boolean state) {
		trainPresent = state;
	}
	
	public void setHeaters(boolean state) {
		heaters = state;
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
				trainPresent = !trainPresent;
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
