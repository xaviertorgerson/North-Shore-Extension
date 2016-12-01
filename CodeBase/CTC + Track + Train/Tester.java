import java.util.Scanner;

public class Tester {

	public static void main(String[] args) {
		
		TrackModel track = new TrackModel();
		track.loadBlocks("trackData.csv");
		
		CTCGUI ctc = new CTCGUI();
		ctc.getTrackModel(track);
		ctc.setVisible(true);
		Block block = new Block();
		block = track.getBlock("Green", 152);
		
		long lastUpdate = 0;
		
		Block testBlock = track.getBlock("Green", 152);
		track.addTrain(6, testBlock);

		Switch greenHead = track.getSwitch("Green", 0);
		greenHead.setState(true);
		
		testBlock.setSetPointSpeed(25);
		testBlock.setAuthority(3);
		
		int updateCount = 0;
		while(updateCount < 50)
		{
			long deltaT = 0;
			long current = System.currentTimeMillis();

			if (lastUpdate == 0)
			{
				deltaT = 0;
			}
			
			else
			{
				deltaT = (current - lastUpdate);
			}
			lastUpdate = current;
			track.update(deltaT);
			updateCount++;
		}
		//ctc.trainOccupancyUpdate(block, 1);
		

	}

}
