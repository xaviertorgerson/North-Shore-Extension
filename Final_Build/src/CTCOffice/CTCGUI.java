import javax.swing.table.*;
import javax.swing.Timer;
import java.lang.Math;

/**
 * The CTCGUI class includes all of the code for initiating the GUI elements and code for responding to 
 * user interaction with the GUI elements. It contains classes CTCTrainManager and  CTCSwitchManager,  
 * an instance of the main trackModel passed in from NSE.java, and an instance of Track_ContMaster 
 * passed in from NSE.java. The first two classes help CTCGUI.java track trains and set switches, and 
 * the last two allow the GUI to interact with and get information from the rest of the system. 
 */

//Suggestion includes speed, speed limit and authority should be calculated by other things (like CTC or maybe wayside)
//CRC diagrams and class diagrams are very different things
//Package diagram
/*
 * Throughput statistics (just as a chart) , track failures as a list , button to add/ remove tracks, and button to display train schedule
 * @author admin
 */
public class CTCGUI extends javax.swing.JFrame {

	//Both SwitchStateSuggestion arrays are ways for the CTCOffice to recommend switch states to the wayside controller 
	//They are passed along in the update route function
	private SwitchStateSuggestion[] switchSuggGreen = new SwitchStateSuggestion[6]; 
	private SwitchStateSuggestion[] switchSuggRed = new SwitchStateSuggestion[7]; 
	public int simSpeedFactor = 1;
	private int maxTrainID=0;
	private int displayBlocks[] = {0,0,0,0}; 
	private int displayBlockLine[] = {0,0,0,0};
	private int greenSwitchBlocks[] = {62, 12,29,58,77,86};

	public CTCGUI(){
		
		initComponents();
		int[] blank = new int[20];
		boolean[] blank2 = new boolean[20];
		//Initializing switch suggestions to prevent a nullpointer later
		for(int i = 0; i<6 ; i++)
		{
			switchSuggGreen[i] = new SwitchStateSuggestion(greenSwitchBlocks[i], blank2, blank);
		}
	}
	private CTCTrainManager CTCtrains = new CTCTrainManager();
	private CTCSwitchManager CTCswitches = new CTCSwitchManager(trackModel);
	
    static TrackModel trackModel;
    TrackCont_Master trackCont = new TrackCont_Master();
    static Block NullBlock = new Block();
	
    public int minuteDepart;
    public static int getHourDepart;
    public static boolean Event = false;
    static int trcounter = 0;

	
    public void getTrackModel(TrackModel tm)
    {
         this.trackModel = tm;
		 CTCswitches = new CTCSwitchManager(trackModel);
    }
	
    public void getWayside(TrackCont_Master trackController)
    {
        this.trackCont = trackController;	
    }
	//This method is public and is called by the wayside controller to alert me that the train has moved. 
    public void trainOccupancyUpdate(Block currBlock, int trainID)
    {	
		if(trainID == maxTrainID + 1){
			maxTrainID++;
		}

		DefaultTableModel model = (DefaultTableModel)MonitorTrains.getModel();
		model.setValueAt(currBlock.getNumber(), trainID-1, 0);
		model.setValueAt(CTCtrains.getDestination(trainID), trainID-1, 2);
		model.setValueAt(trainID, trainID-1, 1);
		if(currBlock.getNumber() == CTCtrains.getLocation(trainID)){
			return;
		}
		else{
			CTCtrains.setLocation(trainID, currBlock.getNumber());
		}
		
		Block destinationBlock = trackModel.getBlock(CTCtrains.getLineofTrain(trainID), CTCtrains.getDestination(trainID));
		//If the train's current location matches its destination, we can now stop the train. 
		if(currBlock.getNumber() == CTCtrains.getDestination(trainID)){
			System.out.println("First speed auth set" +"on line" + CTCtrains.getLineofTrain(trainID) + "and block num" +  currBlock.getNumber());
			trackCont.updateSpeedAuth(CTCtrains.getLineofTrain(trainID), currBlock.getNumber(), (float)0, (float)(0));
			
			return;
		}
		//If the train is a certain distance from its destination and the destination block is a bit small, then we need to slow 
		//down the train a little to make sure it can stop when it gets to that block.
		//According to my track model guy, this shouldn't be necessary since the train model should be slowing itself down as it 
		//gets a decreasing authority (which I do pass it) but I suppose I can't do anything about that. 
		else if((abs(currBlock.getNumber()-CTCtrains.getDestination(trainID)) < 2) && destinationBlock.getSize() < 101){
			System.out.println("Second speed auth set"+"on line" + CTCtrains.getLineofTrain(trainID) + "and block num" +  currBlock.getNumber() );
			trackCont.updateSpeedAuth(CTCtrains.getLineofTrain(trainID), currBlock.getNumber(), (float)20, (float)(75*0.00062));
			//TO-DO checks for reverse ideally should go here so they don't affect the suggestions
			//Though I guess using abs accounts for both previous blocks and next blocks, we'll see if I get to test it
			return;
		}

		else{
			//Give authority in miles
			//Get them to stop in the middle of the block (pretend that they are at the beginning of the block and then Xavier takes care of slight displacements)
		    float distance = (CTCtrains.getDistance(trainID) - currBlock.getSize());
			//Used to have a problem with negative distances, probably will not now that I added the conditional at the beginning but I will leave this here just 
			//in case
			if(distance<0){
				distance = 0; 
			}
			System.out.println("Third speed auth set" +"on line" + CTCtrains.getLineofTrain(trainID) + "and block num" +  currBlock.getNumber() + " with authority " + distance);
			trackCont.updateSpeedAuth(CTCtrains.getLineofTrain(trainID), currBlock.getNumber(), (float)CTCtrains.getSpeed(trainID), (float)distance*(float)0.00062);
			CTCtrains.setDistance(trainID, distance);
		}
		
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    //If you are reading this code, I highly recommend you collapse this method within whatever text editor you are using. 
	//All it has is a bunch of non-implementation related GUI setup stuff. 
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        buttonGroup8 = new javax.swing.ButtonGroup();
        buttonGroup9 = new javax.swing.ButtonGroup();
        buttonGroup10 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        SetSwitchonLine = new javax.swing.JComboBox();
        SetSwitchatNum = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        EnableSwitch = new javax.swing.JCheckBox();
        DisableSwitch = new javax.swing.JCheckBox();
        ManualMode = new javax.swing.JButton();
        SimSpeedSel = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        MonitorTrains = new javax.swing.JTable();
        DestinationChange = new javax.swing.JButton();
        TrainID = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        MonitorBlockNumber = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        MonitorLine = new javax.swing.JComboBox();
        EnableBlock = new javax.swing.JButton();
        DisableBlock = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        MonitorBlockTable = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jComboBox10 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox();
        SendSuggestion2 = new javax.swing.JButton();
		redSuggSpeed = new javax.swing.JTextField();
		greenSuggSpeed = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        SelectSpeed = new javax.swing.JComboBox();
        SendSuggestion = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
		jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CTC Office - Blue Team");

        jScrollPane2.setMaximumSize(new java.awt.Dimension(32600, 31000));

        jLabel16.setIcon(new javax.swing.ImageIcon("bin/1186traindiagram.png")); // NOI18N
        jScrollPane2.setViewportView(jLabel16);

        jLabel17.setText("Simulation Speed");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Set Switches"));

        SetSwitchonLine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Red", "Green" }));
        SetSwitchonLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetSwitchonLineActionPerformed(evt);
            }
        });

        SetSwitchatNum.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        SetSwitchatNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetSwitchatNumActionPerformed(evt);
            }
        });

        jLabel7.setText("Switch #");

        buttonGroup10.add(EnableSwitch);
        EnableSwitch.setText("Enable");
        EnableSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnableSwitchActionPerformed(evt);
            }
        });

        buttonGroup10.add(DisableSwitch);
        DisableSwitch.setText("Disable");
        DisableSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisableSwitchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DisableSwitch)
                    .addComponent(EnableSwitch)
                    .addComponent(SetSwitchonLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(SetSwitchatNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(SetSwitchonLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SetSwitchatNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addComponent(EnableSwitch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DisableSwitch, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        ManualMode.setText("Activate manual mode");
        ManualMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManualModeActionPerformed(evt);
            }
        });

        SimSpeedSel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SimSpeedSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimSpeedSelActionPerformed(evt);
            }
        });

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"1", "Pioneer", "3", "4", "5", "6", "7", "8", "Edgebrook", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "Whited", "23", "24", "25", "26", "27", "28", "29", "30", "Station Bank", "32", "33", "34", "35", "36", "37", "38", "Central (1)", "40", "41", "42", "43", "44", "45", "46", "47", "Inglewood", "49", "50", "51", "52", "53", "54", "55", "56", "Overbrook", "58", "59", "60", "61", "62", "63", "64", "Glenbury", "66", "67", "68", "69", "70", "71", "72", "Dormont (1)", "74", "75", "76", "77", "Mt Lebanon", "79", "80", "81", "82", "83", "84", "85", "86", "87", "Poplar", "89", "90", "91", "92", "93", "94", "95", "Castle Shannon", "97", "98", "99", "100", "101", "102", "103", "104", "Dormont (2)", "106", "107", "108", "109", "110", "111", "112", "113", "Glenbury", "115", "116", "117", "118", "119", "120", "121", "122", "Overbrook", "124", "125", "126", "127", "128", "129", "130", "131", "Inglewood (2)", "133", "134", "135", "136", "137", "138", "139", "140", "Central (2)", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151" ,"152" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });


        jLabel10.setText("New destination");

        jLabel8.setText("New speed");

        MonitorTrains.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Train Location", "Train ID", "Destination"
            }
        ));
        MonitorTrains.setRowHeight(32);
        jScrollPane1.setViewportView(MonitorTrains);

        DestinationChange.setText("Set new destination");
        DestinationChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DestinationChangeActionPerformed(evt);
            }
        });

        TrainID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}));
        TrainID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrainIDActionPerformed(evt);
            }
        });

        jLabel9.setText("TrainID");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(DestinationChange)
                        .addGap(63, 63, 63))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(TrainID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                             .addGap(82, 82, 82)
							.addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
	                         .addGap(76, 76 ,76 ))))		                            
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(TrainID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(DestinationChange)
                .addGap(24, 24, 24))
        );

        jTabbedPane1.addTab("Trains", jPanel2);

             MonitorBlockNumber.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", " ", " " }));
        MonitorBlockNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MonitorBlockNumberActionPerformed(evt);
            }
        });

        jLabel5.setText("Block #");

        MonitorLine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Red", "Green" }));
        MonitorLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MonitorLineActionPerformed(evt);
            }
        });

        EnableBlock.setText("Enable");
        EnableBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnableBlockActionPerformed(evt);
            }
        });

        DisableBlock.setText("Disable");
        DisableBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisableBlockActionPerformed(evt);
            }
        });

        MonitorBlockTable.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        MonitorBlockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Block #", "Status", "Occupied", "Speed", "Authority"
            }
        ));
        MonitorBlockTable.setRowHeight(32);
        jScrollPane3.setViewportView(MonitorBlockTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(MonitorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(MonitorBlockNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)
                        .addComponent(EnableBlock)
                        .addGap(32, 32, 32)
                        .addComponent(DisableBlock)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DisableBlock)
                    .addComponent(EnableBlock)
                    .addComponent(MonitorBlockNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(MonitorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Blocks", jPanel4);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Set Suggestion"));
        jPanel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "Shadyside", "8", "9", "10", "11", "12", "13", "14", "15", "Herron Ave", "17", "18", "19", "20", "Swissville", "22", "23", "24", "Penn Station\t", "26", "27", "28", "29", "30", "31", "32", "33", "34", "Steel Plaza", "36", "37", "38", "39", "40", "41", "42", "43", "44", "First Ave", "46", "47", "Station Square", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "South Hills Junction", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", " " }));
        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });

        jLabel12.setText("Destination");

        jLabel13.setText("Speed");

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", " ", " " }));
        jComboBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox11ActionPerformed(evt);
            }
        });

        SendSuggestion2.setText("Send");
        SendSuggestion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendSuggestion2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addContainerGap(35, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(SendSuggestion2))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(redSuggSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(redSuggSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(SendSuggestion2))
        );

        jTabbedPane2.addTab("Red", jPanel7);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Set Suggestion"));
        jPanel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "Pioneer", "3", "4", "5", "6", "7", "8", "Edgebrook", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "Whited", "23", "24", "25", "26", "27", "28", "29", "30", "South Bank", "32", "33", "34", "35", "36", "37", "38", "Central (1)", "40", "41", "42", "43", "44", "45", "46", "47", "Inglewood (1)", "49", "50", "51", "52", "53", "54", "55", "56", "Overbrook", "58", "59", "60", "61", "62", "63", "64", "Glenbury", "66", "67", "68", "69", "70", "71", "72", "Dormont (1)", "74", "75", "76", "77", "Mt Lebanon", "79", "80", "81", "82", "83", "84", "85", "86", "87", "Poplar", "89", "90", "91", "92", "93", "94", "95", "Castle Shannon", "97", "98", "99", "100", "101", "102", "103", "104", "Dormont (2)", "106", "107", "108", "109", "110", "111", "112", "113", "Glenbury", "115", "116", "117", "118", "119", "120", "121", "122", "Overbrook", "124", "125", "126", "127", "128", "129", "130", "131", "Inglewood", "133", "134", "135", "136", "137", "138", "139", "140", "Central (2)", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel15.setText("Destination");

        jLabel18.setText("Speed");
		MonitorTrains.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
	

        SelectSpeed.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", " ", " " }));
        SelectSpeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectSpeedActionPerformed(evt);
            }
        });

        SendSuggestion.setText("Send");
        SendSuggestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendSuggestionActionPerformed(evt);
            }
        });

        jButton5.setText("Load Schedule");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SendSuggestion))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(greenSuggSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(greenSuggSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SendSuggestion)
                    .addComponent(jButton5)))
        );

        jTabbedPane2.addTab("Green", jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jLabel17)
                        .addGap(31, 31, 31)
                        .addComponent(SimSpeedSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(ManualMode))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17)
                                .addComponent(SimSpeedSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ManualMode))
                        .addGap(101, 101, 101)
                        .addComponent(jTabbedPane1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(51, 51, 51))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Tr");

        pack();
    }// </editor-fold>     

    private void MonitorBlockNumberActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        // TODO add your handling code here:
		int blockNum = atoi((String)MonitorBlockNumber.getSelectedItem());
		
		if(blockNum < 78 && ((String)MonitorLine.getSelectedItem()).equals("Red")){
			
			for(int i = 3; i > 0 ; i--){
				displayBlocks[i] = displayBlocks[i-1];
				displayBlockLine[i] = displayBlockLine[i-1];
				//If displayBlockLine has a 0, that means look at the green line. If it has a 1, look at the red. 
			} //row, collumn
			
			Block dispBlock = trackModel.getBlock("Red", blockNum);

			displayBlocks[0] = dispBlock.getNumber();
			displayBlockLine[0] = 1;
			
			for(int i=0; i<4 ; i++){ //For loop to fill all the block information
				if(displayBlocks[i] == 0){
					continue;
				}
				if(displayBlockLine[i] == 1 && displayBlocks[i] != 0){
				dispBlock = trackModel.getBlock("Red", displayBlocks[i]);
				}
				else if(displayBlockLine[i] == 0 && displayBlocks[i] != 0){
				dispBlock = trackModel.getBlock("Green", displayBlocks[i]);
				}
				
				DefaultTableModel model = (DefaultTableModel)MonitorBlockTable.getModel();
				if(displayBlockLine[i] == 1){
				model.setValueAt((((Integer)dispBlock.getNumber()).toString()) + " (R)", i, 0);
				}
				if(displayBlockLine[i] == 0){
				model.setValueAt((((Integer)dispBlock.getNumber()).toString()) + " (G)", i, 0);
				}
				model.setValueAt(dispBlock.getTrainPresent(), i, 2);
				model.setValueAt(dispBlock.getSetPointSpeed(), i, 3);
				model.setValueAt(dispBlock.getAuthority(), i, 4);
				

				//Displays different messages based on the status of the rail being inspected
				if(dispBlock.getFailureStatus()){
					if(dispBlock.getBrokenRailStatus()){
						model.setValueAt("Broken rail", i, 1);
					}	
					else if(dispBlock.getTrackCircuitStatus()){
						model.setValueAt("Broken TC", i, 1);
					}
							
					else if(dispBlock.getPowerStatus()){
						model.setValueAt("Power failure", i, 1);
					}
					else if(dispBlock.getClosedForMaintenence()){
						model.setValueAt("CFM", i, 1);
					}
				}
				else{
					model.setValueAt("Working", i, 1);
				}
			}	
		}
		else if(((String)MonitorLine.getSelectedItem()).equals("Green"))
		{
			for(int i = 3; i > 0 ; i--){
				displayBlocks[i] = displayBlocks[i-1];
				displayBlockLine[i] = displayBlockLine[i-1];
				//If displayBlockLine has a 0, that means look at the green line. If it has a 1, look at the red. 
			} //row, collumn
			
			Block dispBlock = trackModel.getBlock("Green", blockNum);
			
			displayBlocks[0] = dispBlock.getNumber();
			displayBlockLine[0] = 0;
			
			for(int i=0; i<4 ; i++){ //For loop to fill all the block information
				if(displayBlocks[i] == 0){
					continue;
				}
				if(displayBlockLine[i] == 1 && displayBlocks[i] != 0){
				dispBlock = trackModel.getBlock("Red", displayBlocks[i]);
				}
				else if(displayBlockLine[i] == 0 && displayBlocks[i] != 0){
				dispBlock = trackModel.getBlock("Green", displayBlocks[i]);
				}
				
				DefaultTableModel model = (DefaultTableModel)MonitorBlockTable.getModel();
				if(displayBlockLine[i] == 1){
				model.setValueAt((((Integer)dispBlock.getNumber()).toString()) + " (R)", i, 0);
				}
				if(displayBlockLine[i] == 0){
				model.setValueAt((((Integer)dispBlock.getNumber()).toString()) + " (G)", i, 0);
				}
				model.setValueAt(dispBlock.getTrainPresent(), i, 2);
				model.setValueAt(dispBlock.getSetPointSpeed(), i, 3);
				model.setValueAt(dispBlock.getAuthority(), i, 4);
				
				if(dispBlock.getFailureStatus()){
					if(dispBlock.getBrokenRailStatus()){
						model.setValueAt("Broken rail", i, 1);
					}	
					else if(dispBlock.getTrackCircuitStatus()){
						model.setValueAt("Broken TC", i, 1);
					}
							
					else if(dispBlock.getPowerStatus()){
						model.setValueAt("Power failure", i, 1);
					}
					else if(dispBlock.getClosedForMaintenence()){
						model.setValueAt("CFM", i, 1);
					}
				}
				else{
					model.setValueAt("Working", i, 1);
				}
			}
			
		}
		
		
		
    }                                                  
	//Might delete this function if I feel like it
    private void MonitorLineActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void EnableBlockActionPerformed(java.awt.event.ActionEvent evt) {                                            
		int blockNum = atoi((String)MonitorBlockNumber.getSelectedItem());
		if(blockNum < 78 && ((String)MonitorLine.getSelectedItem()).equals("Red")){
			Block dispBlock = trackModel.getBlock("Red", blockNum);
			dispBlock.setClosedForMaintenence(false);
		}
		else
		{
			Block dispBlock = trackModel.getBlock("Green", blockNum);
			dispBlock.setClosedForMaintenence(false);
		}
		
    }                                           

    private void DisableBlockActionPerformed(java.awt.event.ActionEvent evt) {                                             
		int blockNum = atoi((String)MonitorBlockNumber.getSelectedItem());
		if(blockNum < 78 && ((String)MonitorLine.getSelectedItem()).equals("Red")){
			Block dispBlock = trackModel.getBlock("Red", blockNum);
			dispBlock.setClosedForMaintenence(true);
			
		}
		else
		{
			Block dispBlock = trackModel.getBlock("Green", blockNum);
			dispBlock.setClosedForMaintenence(true);
		}
    }                                            
	
    private void SetSwitchonLineActionPerformed(java.awt.event.ActionEvent evt) {                                                
		// This is the switch number selector
		String switchLine = (String)SetSwitchonLine.getSelectedItem();
		int switchNum = atoi((String)SetSwitchatNum.getSelectedItem());
		if(switchLine.equals("Green") && switchNum < 6){
			//Process and output choices for switches
			int firstChoice = CTCswitches.getBlockat0(switchLine, switchNum);
			EnableSwitch.setText(((Integer)firstChoice).toString());
			int secondChoice = CTCswitches.getBlockat1(switchLine, switchNum);
			DisableSwitch.setText(((Integer)secondChoice).toString());
		}
		if(switchLine.equals("Red") && switchNum >= 6){
			//Process and output choices for switches
			int firstChoice = CTCswitches.getBlockat0(switchLine, switchNum);
			EnableSwitch.setText(((Integer)firstChoice).toString());
			int secondChoice = CTCswitches.getBlockat1(switchLine, switchNum);
			DisableSwitch.setText(((Integer)secondChoice).toString());
		}
			
    }                                               

    private void SetSwitchatNumActionPerformed(java.awt.event.ActionEvent evt) {                                               
  
		//This is the switch number selector
		String switchLine = (String)SetSwitchonLine.getSelectedItem();
		int switchNum = atoi((String)SetSwitchatNum.getSelectedItem());
		if(switchLine.equals("Green") && switchNum < 6){
			//Process and output choices for switches that the user can set
			int firstChoice = CTCswitches.getBlockat0(switchLine, switchNum);
			EnableSwitch.setText(((Integer)firstChoice).toString());
			int secondChoice = CTCswitches.getBlockat1(switchLine, switchNum);
			DisableSwitch.setText(((Integer)secondChoice).toString());
		}
		if(switchLine.equals("Red") && switchNum >= 6){
			//Process and output choices
			int firstChoice = CTCswitches.getBlockat0(switchLine, switchNum);
			EnableSwitch.setText(((Integer)firstChoice).toString());
			int secondChoice = CTCswitches.getBlockat1(switchLine, switchNum);
			DisableSwitch.setText(((Integer)secondChoice).toString());
		}
    }                                              

    private void DisableSwitchActionPerformed(java.awt.event.ActionEvent evt) {                                              
		//System.out.println("Choosing state 1");
		String switchLine = (String)SetSwitchonLine.getSelectedItem();
		int switchNum = atoi((String)SetSwitchatNum.getSelectedItem());
		//Making sure that the switch they are toggling is a valid switch
		if((switchLine.equals("Green") && switchNum < 6) || (switchLine.equals("Red") && switchNum >= 6)){
			 CTCswitches.toggleSwitch(switchLine, switchNum, 1);
		}
		
    }                                             

    private void EnableSwitchActionPerformed(java.awt.event.ActionEvent evt) {    //Enable is actually setting it to be at state 0       
		//System.out.println("Choosing state 0");
		String switchLine = (String)SetSwitchonLine.getSelectedItem();
		int switchNum = atoi((String)SetSwitchatNum.getSelectedItem());
		//Making sure that the switch they are toggling is a valid switch
		if((switchLine.equals("Green") && switchNum < 6) || (switchLine.equals("Red") && switchNum >= 6)){
			 CTCswitches.toggleSwitch(switchLine, switchNum, 0);
		}
    }                                            

    private void TrainIDActionPerformed(java.awt.event.ActionEvent evt) {   //Note that I am now taking in the trainID, not the block number at all                                            

    }                                              

    private void ManualModeActionPerformed(java.awt.event.ActionEvent evt) {                                           

    }                                          

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {                                           

    }                                          

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {                                           

    }                                          

                                         
	// This runs when "Set new destination" is pressed
    private void DestinationChangeActionPerformed(java.awt.event.ActionEvent evt) {                                                  
		
		if(jTextField1.getText().equals("")){
			return;
		}
		
		String newDestination = (String)jComboBox4.getSelectedItem();
		newDestination = greenLookup(newDestination);
		int destinationBlock = atoi(newDestination);
		int greenTraverse = 1;
		float distance = 0;
		float speed = 0;
		boolean destinationFound = false;
		int trainID = atoi((String)TrainID.getSelectedItem());
		if(trainID > maxTrainID){
			return;
		}
		
		String text = jTextField1.getText();
		if (text != null && !text.isEmpty()) {
			speed = Float.parseFloat(text);
		}
		//Make sure they can't choose an invalid train and therefore mess the entire system up

		
		CTCtrains.setDestination(trainID, destinationBlock);
		
		
		int nextBlockNum = CTCtrains.getLocation(trainID);
		Block nextBlock = trackModel.getBlock(CTCtrains.getLineofTrain(trainID), nextBlockNum);
		Block tempBlock = nextBlock;
		
		while(!destinationFound){
			//This loop traverses the green block and sets the train's authority once it finds the train's destination.
			//I treat it a little bit like a state machine - i.e. when I have reached the end of one section, I move to a
			//different "state" in the track. 
			System.out.println("Moved on to block number " + nextBlock.getNumber());
			//To do- if the next block is null, try switching the switch and see what you get
			switch(greenTraverse){
				
				case 1:
					//Covers blocks 63 to 76
					nextBlock = nextBlock.getNextBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					if(nextBlock.getNumber() == 76){
						greenTraverse = 2;
						nextBlock = trackModel.getBlock("Green", 77);
						distance = nextBlock.getSize() + distance;
						break;
					}
					
					break;
				case 2:
					//Covers from 77 to 100, going forward
					nextBlock = nextBlock.getNextBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					if(nextBlock.getNumber() == 100){
						greenTraverse = 3;
						nextBlock = trackModel.getBlock("Green", 85);
						distance = nextBlock.getSize() + distance;
						break;
					}
					
					break;
				case 3:
					//Goes from 100 to 77, going backward
					nextBlock = nextBlock.getPreviousBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					if(nextBlock.getNumber() == 77 ){ 
						greenTraverse = 4;
						nextBlock = trackModel.getBlock("Green", 101);
						distance = nextBlock.getSize() + distance;
						break;
						
					}
					
					break;
				case 4:
					//Goes up section R
					nextBlock = nextBlock.getNextBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					if(nextBlock.getNumber() == 150){
						greenTraverse = 5;
						nextBlock = trackModel.getBlock("Green", 28);
						distance = nextBlock.getSize() + distance;
						break;
						
					}		
					break;
					
				case 5:
					//Now we go to 28 onward, but I don't know what I would do from here so I am stopping
					nextBlock = nextBlock.getNextBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					destinationFound = true;
					break;
				
			}
		
		}
		trackCont.updateSpeedAuth(CTCtrains.getLineofTrain(trainID), tempBlock.getNumber(), (float)speed, (float)(1500*0.00062));
		CTCtrains.setDistance(trainID, 1500);
		
		
    }                                                 
                  

    private void SimSpeedSelActionPerformed(java.awt.event.ActionEvent evt) {                                            
		String newSimSpeed = (String)SimSpeedSel.getSelectedItem();
		int newSpd = atoi(newSimSpeed);
		simSpeedFactor = newSpd;	
    }                                           

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {                                            

    }                                           

    private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) {                                            

    }                                           

    private void SendSuggestion2ActionPerformed(java.awt.event.ActionEvent evt) {                                                

	System.out.println("Red suggestion");
		if(redSuggSpeed.getText().equals("")){
			return;
		}
		//Combo box 10 has all of the red destinations.
		String destination =(String)jComboBox10.getSelectedItem();
		float speed = 35; //Initializing it 
		destination = redLookup(destination);
		String text = greenSuggSpeed.getText();
		if (text != null && !text.isEmpty()) {
			speed = Float.parseFloat(text);
		}
		//Initialize proper tracking vvariables for the train on the red line
		CTCtrains.setSpeed(maxTrainID+1, speed);
		CTCtrains.setLine(maxTrainID+1, "Red");
		CTCtrains.setDestination(maxTrainID+1, atoi(destination));
		CTCtrains.setDistance(maxTrainID+1, 2000);
		trackCont.addTrain("Red", maxTrainID+1);
		trackCont.updateSpeedAuth("Red", 77, (float)35, (float)(1000 * 0.00062));
    }                                               

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           

    }                                          

    private void SelectSpeedActionPerformed(java.awt.event.ActionEvent evt) {                                            

    }                                           

    private void SendSuggestionActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // This is the send button
		if(greenSuggSpeed.getText().equals("")){
			return;
		}
		String destination =(String)jComboBox1.getSelectedItem();
		float speed = 35; //Initializing it 
		String text = greenSuggSpeed.getText();
		if (text != null && !text.isEmpty()) {
			speed = Float.parseFloat(text);
		}
		CTCtrains.setSpeed(maxTrainID+1, speed);
		
		destination = greenLookup(destination);

		//trainID CANNOT be 0
		
		int destinationBlock = atoi(destination);
						
		CTCtrains.setDestination(maxTrainID+1, destinationBlock);
		
		Block greenHead = new Block();
		greenHead = trackModel.getBlock("Green", 152);
		boolean destinationFound = false;
		//System.out.println("Error here");
		float distance = greenHead.getSize();
		Block nextBlock = new Block();
		if(((Block)greenHead).getSwitchID() != -1)
		{
			int switchID = greenHead.getSwitchID();
			Switch test = trackModel.getSwitch("Green", switchID);
			nextBlock = test.getCenter();
			distance = distance + nextBlock.getSize();	
		}
		if(nextBlock.getNumber() == destinationBlock)
		{
			destinationFound = true;
		}
		
		int greenTraverse = 1;
		while(!destinationFound)
		{
			//This loop traverses the green block and sets the train's authority once it finds the train's destination.
			System.out.println("Moved on to block number " + nextBlock.getNumber());
			//To do- if the next block is null, try switching the switch and see what you get
			switch(greenTraverse)
			{
				
				case 1:
					nextBlock = nextBlock.getNextBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					if(nextBlock.getNumber() == 76){
						greenTraverse = 2;
						nextBlock = trackModel.getBlock("Green", 77);
						distance = nextBlock.getSize() + distance;
						break;
					}
					
					break;
				case 2:
					nextBlock = nextBlock.getNextBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					if(nextBlock.getNumber() == 100){
						greenTraverse = 3;
						nextBlock = trackModel.getBlock("Green", 85);
						distance = nextBlock.getSize() + distance;
						break;
					}
					
					break;
				case 3:
					nextBlock = nextBlock.getPreviousBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					if(nextBlock.getNumber() == 77 ){ 
						greenTraverse = 4;
						nextBlock = trackModel.getBlock("Green", 101);
						distance = nextBlock.getSize() + distance;
						break;
						
					}
					
					break;
				case 4:
					nextBlock = nextBlock.getNextBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					if(nextBlock.getNumber() == 150){
						greenTraverse = 5;
						nextBlock = trackModel.getBlock("Green", 28);
						distance = nextBlock.getSize() + distance;
						break;
						
					}		
					break;
					
				case 5:
					nextBlock = nextBlock.getNextBlock();
					distance = distance + nextBlock.getSize();
					if(nextBlock.getNumber() == destinationBlock){
						destinationFound = true;
						break;
					}
					destinationFound = true;
					break;
				
			}
		
		}
		
		
		
		
		trackCont.addTrain("Green", maxTrainID+1);
		//Set the switch state suggestions
		if(maxTrainID%2 == 0)
		{
			for(int i = 0; i<6; i++)
			{
				switchSuggGreen[i].setBlockNumber(greenSwitchBlocks[i]);
				switchSuggGreen[i].addTrain(maxTrainID+1, false);
			}
			
		}
		else
		{
			for(int i = 0; i<6; i++)
			{
				System.out.println(i);
				int switchBlockNumber = greenSwitchBlocks[i];
				switchSuggGreen[i].setBlockNumber(switchBlockNumber);
				switchSuggGreen[i].addTrain(maxTrainID+1, true);	
			}
			
		}
								
		CTCtrains.setDistance(maxTrainID+1, distance);
		CTCtrains.setLine(maxTrainID+1, "Green");
		CTCtrains.setLocation(maxTrainID+1, 152);
		System.out.println("The total distance was found to be " + distance); //From 62 to 96, calculating a distance of 5361.6. Apparently should be 5236? 
		// Line, block number, speed, authority
		trackCont.updateSpeedAuth("Green", 152, (float)35, (float)(distance * 0.00062));
		trackCont.updateRoute(switchSuggGreen, "Green");
		//TrackModel.inspect(trackModel);
		/*DefaultTableModel model = (DefaultTableModel)MonitorTrains.getModel();
		model.setValueAt(destinationBlock, maxTrainID-1, 2);
		model.setValueAt(maxTrainID, maxTrainID-1, 1);*/
		

    }                                              

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                         

    }                                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CTCGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JComboBox AMorPMDep1;
    private javax.swing.JComboBox TrainID;
    private javax.swing.JButton DestinationChange;
    private javax.swing.JButton DisableBlock;
    private javax.swing.JCheckBox DisableSwitch;
    private javax.swing.JButton EnableBlock;
    private javax.swing.JCheckBox EnableSwitch;
    private javax.swing.JButton ManualMode;
    private javax.swing.JComboBox MonitorBlockNumber;
    private javax.swing.JTable MonitorBlockTable;
    private javax.swing.JComboBox MonitorLine;
    private javax.swing.JTable MonitorTrains;
    private javax.swing.JComboBox SelectSpeed;
    private javax.swing.JButton SendSuggestion;
    private javax.swing.JButton SendSuggestion2;
    private javax.swing.JComboBox SetSwitchatNum;
    private javax.swing.JComboBox SetSwitchonLine;
    private javax.swing.JComboBox SimSpeedSel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup10;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.ButtonGroup buttonGroup8;
    private javax.swing.ButtonGroup buttonGroup9;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
	private javax.swing.JTextField redSuggSpeed;
	private javax.swing.JTextField greenSuggSpeed;
	private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    // End of variables declaration                   
    public int atoi(String str) {
            if (str == null || str.length() < 1)
                    return 0;

            // trim white spaces
            str = str.trim();

            char flag = '+';

            // check negative or positive
            int i = 0;
            if (str.charAt(0) == '-') {
                    flag = '-';
                    i++;
            } else if (str.charAt(0) == '+') {
                    i++;
            }
            // use double to store result
            double result = 0;

            // calculate value
            while (str.length() > i && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                    result = result * 10 + (str.charAt(i) - '0');
                    i++;
            }

            if (flag == '-')
                    result = -result;

            // handle max and min
            if (result > Integer.MAX_VALUE)
                    return Integer.MAX_VALUE;

            if (result < Integer.MIN_VALUE)
                    return Integer.MIN_VALUE;

            return (int) result;
    }
	
	private int abs(int input)
	{
		if(input < 0)
		{
			return -1*input;
		}
		else{return input;}
		
	}
	
	private String greenLookup(String destination){
		
		if(destination.equals("Pioneer")){
			return "2";
		}
		else if(destination.equals("Edgebrook")){
			return "9";
		}
		else if(destination.equals("Whited")){
			return "22";
		}
		else if(destination.equals("South Bank")){
			return "31";
		}
		else if(destination.equals("Central (1)")){
			return "39";
		}
		else if(destination.equals("Inglewood (1)")){
			return "48";
		}
		else if(destination.equals("Overbrook")){
			return "57";
		}
		else if(destination.equals("Glenbury")){
			return "65";
		}
		else if(destination.equals("Dormont (1)")){
			return "73";
		}
		else if(destination.equals("Mt Lebanon")){
			return "78";
		}
		else if(destination.equals("Castle Shannon")){
			return "96";
		}
		else if(destination.equals("Dormont (2)")){
			return "105";
		}
		else if(destination.equals("Inglewood (2)")){
			return "132";
		}
		else if(destination.equals("Central (2)")){
			return "141";
		}
		
		else{
			return destination;
		}
		
		
	}
	
	private String redLookup(String destination){
		
		if(destination.equals("Shadyside")){
			return "7";
		}
		else if(destination.equals("Herron Ave")){
			return "16";
		}
		else if(destination.equals("Swissville")){
			return "21";
		}
		else if(destination.equals("Penn Station")){
			return "25";
		}
		else if(destination.equals("Steel Plaza")){
			return "35";
		}
		else if(destination.equals("First Avenue")){
			return "45";
		}
		else if(destination.equals("Station Square")){
			return "48";
		}		
		else if(destination.equals("South Hills Junction")){
			return "60";
		}		
		else{
			return destination;
		}					
	}

}