import java.util.Random;

public class Station {
	
	private String name;
	private int occupancy;

	public Station(String newName) {
		name = newName;	
		generateOccupancy();	
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public int getOccupancy() {
		return occupancy;
	}

	public void generateOccupancy() {
		Random rand = new Random();
		occupancy = rand.nextInt((1110 - 1) + 1) + 1;	
	}

	public void setOccupancy(int newOccupancy) {
		occupancy = newOccupancy;
		if (occupancy == 0)
			generateOccupancy();
	}

}
