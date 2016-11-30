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
		
		
		//ctc.trainOccupancyUpdate(block, 1);
		

	}

}
