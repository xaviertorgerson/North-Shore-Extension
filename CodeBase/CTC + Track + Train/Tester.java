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
		TrackCont_Master trackCont = new TrackCont_Master(track, ctc);
		ctc.getWayside(trackCont);
		long lastUpdate = 0;
		while(true)
		{
				
			long current = System.currentTimeMillis();

			if (lastUpdate == 0)
			{
				deltaT = 0;
			}
			
			else
			{
				deltaT = (current - lastUpdate) / 1000.0;
			}
			
			
			track.update(deltaT);
			
		}
		//ctc.trainOccupancyUpdate(block, 1);
		

	}

}
