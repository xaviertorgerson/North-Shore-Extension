import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * The TrackModel stores the data representing the physical system
 *
 * @author Xavier Torgerson
 * @since 2016-12-15
 */
class TrackModel extends JFrame implements ActionListener {

	JPanel panel;

	JComboBox<String> lineCombo;
	JTextField numberText;
	JButton viewBlock;

	JLabel description;

	public ArrayList<Line> lineList;

	public TrackModel() {

		super("TrackModel");
		setSize(1000, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout( new FlowLayout() );       // set the layout manager

		lineCombo = new JComboBox<>();
		add(lineCombo);

		numberText = new JTextField(5);
		add(numberText);

		viewBlock = new JButton("GO");
		viewBlock.setActionCommand("GO");
		viewBlock.addActionListener(this);
		add(viewBlock);

		description = new JLabel("                                                                           ");  // construct a JLabel
		add(description);                        // add the label to the JFrame

		lineList = new ArrayList<Line>();
	}

	public void actionPerformed(ActionEvent e) {
		if("GO".equals(e.getActionCommand())) {
			String activeLine = lineCombo.getSelectedItem().toString();
			int activeBlock = Integer.parseInt(numberText.getText());
			Block testBlock = getBlock(activeLine, activeBlock);
			description.setText(testBlock.toDisplay());	
		}
		else if("LINE".equals(e.getActionCommand())) {
		}
	}

	/**
	 * Update function is called in the main control loop 
	 * Track Circuit detects trains and Track Signals are passed to trains
	 * Trains locations are updated in the model
	 *
	 * @param dt change in time since last update so trains can determine displacement
	 */
	public void update(int dt) {

		//1. Set train speed and authority
		//2. Set train grade
		//3. Update trains
		//4. Update Train Presence
		//If move blocks
		//a. Change Previous and Current block for Train
		//b. Update Displacement

		for(int i = 0; i < lineList.size(); i++) {
			for(int k = 0; k < lineList.get(i).trainCount(); k++) {
				Train updateTrain = lineList.get(i).trainList.get(k);
				Block checkBlock = lineList.get(i).getBlock(updateTrain.curBlock);
				updateTrain.updateGrade(checkBlock.getCumElevation(), checkBlock.getGrade());

				if(checkBlock.getGo()){
					float newAuthority = (float)checkBlock.getAuthority()-(updateTrain.getDistance()*0.000189394);
					System.out.println("CTC says " + checkBlock.getAuthority() + " Train is " + (updateTrain.getDistance*.000189394) + " authority is " + newAuthority);
					updateTrain.updateRequest(newAuthority, checkBlock.getSetPointSpeed());
				}
				else{ 
					updateTrain.updateRequest(0,0);
				}

				updateTrain.update(dt);

				if(updateTrain.getDistance() > (3.28084*checkBlock.getSize())) {
					if(checkBlock.getNextBlock().getNumber() != updateTrain.prevBlock) {
						checkBlock.setTrainPresent(0);
						checkBlock.getNextBlock().setTrainPresent(updateTrain.getID());
						updateTrain.setDistance((int)(updateTrain.getDistance()-(3.28084*checkBlock.getSize())));
						updateTrain.setBlock(checkBlock.getNextBlock().getNumber());
					}
					else if(checkBlock.getPreviousBlock().getNumber() != updateTrain.prevBlock) {
						checkBlock.setTrainPresent(0);
						checkBlock.getPreviousBlock().setTrainPresent(updateTrain.getID());
						updateTrain.setDistance((int)(updateTrain.getDistance()-(3.28084*checkBlock.getSize())));
						updateTrain.setBlock(checkBlock.getPreviousBlock().getNumber());
					}
				}
			}
		} 			
	}

	/**
	 * Loads Blocks from a .csv file in the format provided
	 * Load switches to connect track sections
	 * Link loaded blocks neighbors with a linked list for easy iteration
	 *
	 * @param file Filename of (.csv) file with block data
	 */
	public void loadBlocks(String file) {
		System.out.println("Loading Blocks");	
		//Read file
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			String fileLine;
			//Iterate over lines
			while ((fileLine = br.readLine()) != null) {
				Block newBlock = new Block(fileLine); //Create new block w/ line data
				addBlock(newBlock);	//Add new block to model	
			}
		}
		catch(Exception e) {
			System.out.println(e.getClass());
		}

		for(int i = 0; i < lineList.size(); i++) {
			lineList.get(i).loadDirection(); //Parse arrow data for allowable directions of travel 
			lineList.get(i).linkBlocks(); //Link blocks from block data
			lineList.get(i).loadSwitches(); //Load switches from block data
		}

	}

	/**
	 * Add new Block to its specific line
	 * Look at line element in new block to find what line to add block on
	 *
	 * @param newBlock Block to add to line
	 */
	private void addBlock(Block newBlock) {
		int index = 0;
		while(index < lineList.size()) {
			if (lineList.get(index).getName().equals(newBlock.getLine()))
				break;
			index++;
		}
		if (index == lineList.size()) {
			lineList.add(new Line(newBlock.getLine()));
			System.out.println(newBlock.getLine());
			lineCombo.addItem(new String(newBlock.getLine()));
		}
		lineList.get(index).addBlock(newBlock);
	}

	/**
	 * Get block on a line with a specific number
	 *
	 * @param line What line the desired block is on
	 * @param num What the block number of the desired block is
	 * @return desired block
	 */
	public Block getBlock(String line, int num) {
		Line newLine = getLine(line);
		if(newLine == null) 
			return null;
		return newLine.getBlock(num);
	}

	/**
	 * Add a train on a Block
	 *
	 * @param trainID ID of the new train
	 * @param newBlock block where the train should be added (usually next to yard)
	 */
	public void addTrain(int trainID, Block newBlock) {
		for(int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getName().equals(newBlock.getLine()))
				lineList.get(i).addTrain(trainID,newBlock);
		}
	}

	/**
	 * Get switch on line with switchID
	 *
	 * @param line line the switch is on
	 * @param switchID ID of the desired switch
	 * @return desired swtich
	 */
	public Switch getSwitch(String line, int switchID) {
		Line newLine = getLine(line);
		if(newLine == null) 
			return null;
		return newLine.getSwitch(switchID);
	}

	/**
	 * Get Line by name
	 * 
	 * @param line name of line to return
	 * @return desired line
	 */
	public Line getLine(String line) {
		for(int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getName().equals(line)) {
				return lineList.get(i);
			}
		}
		return null;
	}

	/**
	 * Get array list of blocks on line between start and end
	 *
	 * @param line name of line where section is found
	 * @param start block number of the start of list
	 * @param end block number of the end of list
	 * @return Array list of blocks
	 */
	public ArrayList<Block> getLine(String line, int start, int end) {
		for(int i = 0; i < lineList.size(); i++) {
			if (lineList.get(i).getName().equals(line)) {
				return lineList.get(i).getLine(start, end);
			}
		}
		return null;
	}

	public static void inspect(TrackModel track) {
		Scanner user_input = new Scanner(System.in);

		for(;;){

			//Clear Screen
			for (int k = 0; k < 50; k++) {
				System.out.println("\n");
			}


			System.out.print("What block would you like to inspect? ");
			int inspectBlock = user_input.nextInt();
			user_input.nextLine();	

			System.out.print("What line? ");
			String inspectLine = user_input.nextLine();
			Block selectedBlock = track.getBlock(inspectLine, inspectBlock);

			if(selectedBlock != null){	
				selectedBlock.inspect();
			}
			else {
				break;
			}
		}

	}

	public static void main(String[] args) {

		TrackModel track = new TrackModel();
		track.loadBlocks("trackData.csv");
		track.setVisible(true);	
		//Train newTrain = new Train(10);
		//track.addTrain(6,track.getBlock("Green",152));

		//inspect(track);
	}

}
