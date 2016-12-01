import javax.swing.Timer;

public class TrainTB{
	public static void main(String[] args){
		Train testTrain = new Train(1);
		testTrain.updateRequest(100, 30);
		Timer timer = new Timer(100, new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e){
				testTrain.update(100);
			}
		});
		timer.start();
		
	}
}