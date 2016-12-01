import java.util.Scanner;
import javax.swing.Timer;

public class Tester {

	public static void main(String[] args) {
		
		final TrackModel track = new TrackModel();
		track.loadBlocks("trackData.csv");
		
	//	CTCGUI ctc = new CTCGUI();
		//TrackCont_Master trackCont = new TrackCont_Master(track, ctc);
	//	ctc.getTrackModel(track);
	//	ctc.getWayside(trackCont);
	//	ctc.setVisible(true);
		
		Block testBlock = track.getBlock("Green", 152);
		track.addTrain(6, testBlock);
		testBlock.setSetPointSpeed(25);
		testBlock.setAuthority(4);
		
		Switch greenHead = track.getSwitch("Green", 0);
		greenHead.setState(true);
		
		System.out.println(testBlock);
		//final Train newTrain = new Train(10);
		//newTrain.updateRequest(3,25);
		
		Timer timer = new Timer(100, new java.awt.event.ActionListener(){
			//newTrain.updateGrade(0,0);
			public void actionPerformed(java.awt.event.ActionEvent e){
				track.update(100);
			}
		});
		timer.start();

		/*
		   while(true)
		{
			long deltaT = 0;
			long current = System.currentTimeMillis();
			System.out.println("Current" + current);	
			if(lastUpdate != 0) 
				deltaT = (current - lastUpdate);
			else 
				deltaT = 0;

			lastUpdate = current;
			
			System.out.println(deltaT);
			newTrain.update(deltaT);
			updateCount++;
		}
		//ctc.trainOccupancyUpdate(block, 1);
		*/	

	}

}
