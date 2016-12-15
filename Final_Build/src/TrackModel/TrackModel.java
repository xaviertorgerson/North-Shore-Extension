import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * The TrackModel stores the data representing the physical system
 *
 * @author Xavier Torgerson
 * @since 2016-12-15
 */
class TrackModel extends JFrame { // extends JFrame implements ActionListener {

	private javax.swing.JTextField blockNumberTextField;
	private javax.swing.JButton goButton;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JTextField lineTextField;
	private javax.swing.JButton loadButton;
	private javax.swing.JLabel outLabel;
	public ArrayList<Line> lineList;

	public TrackModel() {

		initComponents();

		lineList = new ArrayList<Line>();
	}

	@SuppressWarnings("unchecked")
		// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
		private void initComponents() {

			jLabel2 = new javax.swing.JLabel();
			jLabel1 = new javax.swing.JLabel();
			goButton = new javax.swing.JButton();
			lineTextField = new javax.swing.JTextField();
			blockNumberTextField = new javax.swing.JTextField();
			loadButton = new javax.swing.JButton();
			outLabel = new javax.swing.JLabel();

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

			jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
			jLabel2.setText("Line");

			jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
			jLabel1.setText("Block #");

			goButton.setText("Inspect");
			goButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					goButtonActionPerformed(evt);
				}
			});

			lineTextField.setText("Green");
			lineTextField.setPreferredSize(new java.awt.Dimension(75, 28));

			blockNumberTextField.setMinimumSize(new java.awt.Dimension(85, 28));

			loadButton.setText("Load Blocks");
			loadButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					loadButtonActionPerformed(evt);
				}
			});

			outLabel.setMaximumSize(new java.awt.Dimension(1000, 1000));
			outLabel.setMinimumSize(new java.awt.Dimension(250, 1000));
			outLabel.setPreferredSize(new java.awt.Dimension(500, 500));

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(layout.createSequentialGroup()
								.addGap(20, 20, 20)
								.addComponent(jLabel1)
								.addGap(62, 62, 62)
								.addComponent(jLabel2))
							.addGroup(layout.createSequentialGroup()
								.addComponent(blockNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(10, 10, 10)
								.addComponent(lineTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, 0)
								.addComponent(goButton))
							.addComponent(loadButton))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(outLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(23, 23, 23))
					);
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addGap(12, 12, 12)
						.addComponent(loadButton)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(jLabel1)
							.addComponent(jLabel2))
						.addGap(4, 4, 4)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(blockNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(lineTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(goButton))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(outLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(16, Short.MAX_VALUE))
					);

			pack();
		this.setVisible(true);	
	}// </editor-fold>                        


	private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showOpenDialog(this);
		
		String path=chooser.getSelectedFile().getAbsolutePath();
		
		System.out.println(path.substring(path.length()-3).equals("csv"));
		loadBlocks(path);
	}           

	private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Block newBlock = getBlock(lineTextField.getText(), Integer.parseInt(blockNumberTextField.getText()));
		System.out.println("GO");
		if(newBlock != null) {
			outLabel.setText(newBlock.toDisplay());
		}
	}

	/*
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
	*/

	/**
	 * Update function is called in the main control loop 
	 * Track Circuit detects trains and Track Signals are passed to trains
	 * Trains locations are updated in the model
	 *
	 * @param dt change in time since last update so trains can determine displacement
	 */
	public void update(int dt) {

		this.repaint();
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
					float newAuthority = (float)(checkBlock.getAuthority()-(updateTrain.getDistance()*0.000189394));
					updateTrain.updateRequest(newAuthority, checkBlock.getSetPointSpeed());
				}
				else{ 
					updateTrain.updateRequest(0,0);
				}

				updateTrain.update(dt);

				if(updateTrain.getDistance() > (3.28084*checkBlock.getSize())) {
					if(checkBlock.getNextBlock().getNumber() != updateTrain.prevBlock) {
						if(checkBlock.getPreviousBlock() != null) {
							if(checkBlock.getPreviousBlock().getNumber() != updateTrain.prevBlock) 
								System.out.println("Error Case 1");
						}
						checkBlock.setTrainPresent(0);
						checkBlock.getNextBlock().setTrainPresent(updateTrain.getID());
						updateTrain.setDistance((int)(updateTrain.getDistance()-(3.28084*checkBlock.getSize())));
						updateTrain.setBlock(checkBlock.getNextBlock().getNumber());
					}
					else if(checkBlock.getPreviousBlock().getNumber() != updateTrain.prevBlock) {
						if(checkBlock.getNextBlock() != null) {
							if(checkBlock.getNextBlock().getNumber() != updateTrain.prevBlock) 
								System.out.println("Error Case 2");
						}
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
			//lineCombo.addItem(new String(newBlock.getLine()));
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
		
		track.setVisible(true);	
		//Train newTrain = new Train(10);
		//track.addTrain(6,track.getBlock("Green",152));

		//inspect(track);
	}

}
