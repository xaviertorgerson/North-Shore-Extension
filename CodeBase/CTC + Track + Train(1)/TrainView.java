import java.util.Random;

public class TrainView extends javax.swing.JFrame{
	
	TrainModel tm;
	
	public TrainView(int ID){
		tm = new TrainModel(ID);
		
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TrainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		
		initComponents();
		
		
        //</editor-fold>
        /* Create and display the form 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrainView().setVisible(true);
            }
        });*/
		
		
	}
	
	public void update(){
		trackElevationField.setText(String.format("%.1f", tm.elevation) + "ft");
        trackGradeFld.setText(String.format("%.1f", tm.grade) + "ft");
        psngrEnteringFld.setText(Integer.toString(tm.psngrEnter));
        authorityFld.setText(String.format("%.1f", tm.authority) + "mi");
        leftDoorField.setText(Integer.toString(tm.leftDoors));
        rightDoorField.setText(Integer.toString(tm.rightDoors));
        lightsField.setText(Integer.toString(tm.lights));
        acField.setText(Integer.toString(tm.ac));
        heaterField.setText(Integer.toString(tm.heater));
        autoField.setText(Boolean.toString(tm.auto));
        adField.setText(Boolean.toString(tm.ad));
        brakeField.setText(Boolean.toString(tm.srvBrk || tm.psngrBrk));
        ebrakeField.setText(Boolean.toString(tm.eBrk));
        curSpdFld.setText(String.format("%.1f", tm.curSpd));
        psngrCountField.setText(Integer.toString(tm.curPassengers));
        pwrReqField.setText(String.format("%.1f", tm.powReq));
	}
	
	public int enterStation(int embarkers){
		//Disembark a random amount off passengers
		Random rand = new Random();
		int randomNum = rand.nextInt(tm.curPassengers + 1);

		int totalPassengers = tm.curPassengers - randomNum + embarkers;
		if(totalPassengers > tm.maxPassenger)
			totalPassengers = tm.maxPassenger;
		
		tm.curPassengers = totalPassengers;
		
		return totalPassengers;
	}
	
	public void updateVelocity(long timePassed){
		//Convert to ft and sec
		float deltaT = (float)timePassed;
		float curWeight = currentWeight();
		float ftSpd = tm.curSpd * (float)1.46667;
		float powerRequest = tm.powReq / (float)1.34102e-6; //ft * lb / sec
		
		//Find force from train
		float Ftrain;
		if(tm.engineFailure || tm.brakeFailure)
			Ftrain = 0;
		else if(tm.eBrk)
			Ftrain = -1 * (float)8.957 * curWeight;
		else if(tm.srvBrk || tm.psngrBrk)
			Ftrain = -1 * (float)3.937 * curWeight;
		else if(tm.curSpd != 0)
			Ftrain = powerRequest / ftSpd;
		else
			Ftrain = powerRequest;
		
		//Find friciton and gravity
		float Fgrav = -1 * curWeight * 32 * (float)Math.sin(tm.grade/100);
		float Ffriction = (float).7 * curWeight * 32 * (float)Math.cos(tm.grade/100);
				
		//Sum forces acting on train
		float Ftotal = Ftrain + Fgrav - Ffriction;
		
		//Find acceleration of train
		float accel = Ftotal / curWeight;
		if(accel > 1.64)
			accel = (float)1.64;
		
		//Find velocity of train
		float velocity = tm.curSpd * (float)1.46667 + accel * deltaT;
		if(velocity < 0)
			velocity = 0;
		if(velocity > 43.5 * (float)1.46667)
			velocity = (float)43.5 * (float)1.46667;
		
		//Update distance of train
		updateDistance(velocity, deltaT);
		
		//Convert to mph
		tm.curSpd = velocity * (float)0.681818;
	}
	
	private void updateDistance(float velocity, float deltaT){
		float distance = (float).5 * (velocity+ tm.curSpd * (float)1.46667 ) * deltaT;		
		float newDistance = tm.currentDistance + distance;
		assert !Float.isNaN(newDistance);
		tm.currentDistance =  newDistance;
	}
	
	private int currentWeight(){
		return tm.trainWeight + tm.passWeight * tm.curPassengers;
	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        trainModelLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        paramDataTable = new javax.swing.JTable();
        engineFailButton = new javax.swing.JToggleButton();
        brakeFailButton = new javax.swing.JToggleButton();
        singalPickupFailureButton = new javax.swing.JToggleButton();
        trackElevationField = new javax.swing.JTextField();
        trackElevationLabel = new javax.swing.JLabel();
        trackGradeFld = new javax.swing.JTextField();
        trackGradeLabel = new javax.swing.JLabel();
        authorityLabel = new javax.swing.JLabel();
        psngrEnteringFld = new javax.swing.JTextField();
        psngrEnterLabel = new javax.swing.JLabel();
        authorityFld = new javax.swing.JTextField();
        leftDoorField = new javax.swing.JTextField();
        leftDoorLabel = new javax.swing.JLabel();
        rightDoorField = new javax.swing.JTextField();
        rightDoorLabel = new javax.swing.JLabel();
        lightsField = new javax.swing.JTextField();
        lightsLabel = new javax.swing.JLabel();
        autoLabel = new javax.swing.JLabel();
        acField = new javax.swing.JTextField();
        acLabel = new javax.swing.JLabel();
        heaterField = new javax.swing.JTextField();
        heaterLabel = new javax.swing.JLabel();
        autoField = new javax.swing.JTextField();
        adField = new javax.swing.JTextField();
        adLabel = new javax.swing.JLabel();
        brakeField = new javax.swing.JTextField();
        brakeLabel = new javax.swing.JLabel();
        ebrakeField = new javax.swing.JTextField();
        ebrakeLabel = new javax.swing.JLabel();
        curSpdFld = new javax.swing.JTextField();
        curSpdLabel = new javax.swing.JLabel();
        psngrCountField = new javax.swing.JTextField();
        psngrCountLabel = new javax.swing.JLabel();
        pwrReqLabel = new javax.swing.JLabel();
        pwrReqField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Train Model - Blue Team");
		
        trainModelLabel.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        trainModelLabel.setText("Train Model");

        paramDataTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paramDataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Number of cars", tm.numCars},
                {"Number of doors", tm.numDoors},
                {"Train length", tm.length + "ft"},
                {"Train width", tm.width + "ft"},
                {"Train height", tm.height + "ft"},
                {"Train mass (empty)", tm.trainWeight + "lb"},
                {"Maximum passengers", tm.maxPassenger},
                {"Maximum acceleration", tm.maxAcceleration + "ft/s^2"},
                {"Maximum deacceleration", tm.maxDeceleration + "ft/s^2"},
                {"Maximum speed", tm.maxSpd + "mph"}
            },
            new String [] {
                "Train Parameters", "Value"
            }
        ));
        jScrollPane1.setViewportView(paramDataTable);

        engineFailButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        engineFailButton.setText("Train Engine Failure");

        brakeFailButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brakeFailButton.setText("Brake Failure");

        singalPickupFailureButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        singalPickupFailureButton.setText("Signal Pickup Failure");

        trackElevationField.setEditable(false);
        trackElevationField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        trackElevationField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trackElevationField.setText(String.format("%.1f", tm.elevation) + "ft");
        trackElevationField.setToolTipText("");
        trackElevationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trackElevationFieldActionPerformed(evt);
            }
        });

        trackElevationLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        trackElevationLabel.setText("Track Elevation");

        trackGradeFld.setEditable(false);
        trackGradeFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        trackGradeFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trackGradeFld.setText(String.format("%.1f", tm.grade) + "ft");
        trackGradeFld.setToolTipText("");
        trackGradeFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trackGradeFldActionPerformed(evt);
            }
        });

        trackGradeLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        trackGradeLabel.setText("Track Grade");

        authorityLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        authorityLabel.setText("Authority");

        psngrEnteringFld.setEditable(false);
        psngrEnteringFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        psngrEnteringFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        psngrEnteringFld.setText(Integer.toString(tm.psngrEnter));
        psngrEnteringFld.setToolTipText("");
        psngrEnteringFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                psngrEnteringFldActionPerformed(evt);
            }
        });

        psngrEnterLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        psngrEnterLabel.setText("Passengers Entering");

        authorityFld.setEditable(false);
        authorityFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        authorityFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        authorityFld.setText(String.format("%.1f", tm.authority) + "mi");
        authorityFld.setToolTipText("");
        authorityFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authorityFldActionPerformed(evt);
            }
        });

        leftDoorField.setEditable(false);
        leftDoorField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        leftDoorField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        leftDoorField.setText(Integer.toString(tm.leftDoors));
        leftDoorField.setToolTipText("");
        leftDoorField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftDoorFieldActionPerformed(evt);
            }
        });

        leftDoorLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        leftDoorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftDoorLabel.setText("Left Door");

        rightDoorField.setEditable(false);
        rightDoorField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rightDoorField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rightDoorField.setText(Integer.toString(tm.rightDoors));
        rightDoorField.setToolTipText("");
        rightDoorField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightDoorFieldActionPerformed(evt);
            }
        });

        rightDoorLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rightDoorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightDoorLabel.setText("Right Door");

        lightsField.setEditable(false);
        lightsField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lightsField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lightsField.setText(Integer.toString(tm.lights));
        lightsField.setToolTipText("");
        lightsField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lightsFieldActionPerformed(evt);
            }
        });

        lightsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lightsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lightsLabel.setText("Lights");

        autoLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        autoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        autoLabel.setText("Automatic");

        acField.setEditable(false);
        acField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        acField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        acField.setText(Integer.toString(tm.ac));
        acField.setToolTipText("");
        acField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acFieldActionPerformed(evt);
            }
        });

        acLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        acLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        acLabel.setText("AC");

        heaterField.setEditable(false);
        heaterField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        heaterField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        heaterField.setText(Integer.toString(tm.heater));
        heaterField.setToolTipText("");
        heaterField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heaterFieldActionPerformed(evt);
            }
        });

        heaterLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        heaterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heaterLabel.setText("Heater");

        autoField.setEditable(false);
        autoField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        autoField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        autoField.setText(Boolean.toString(tm.auto));
        autoField.setToolTipText("");
        autoField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoFieldActionPerformed(evt);
            }
        });

        adField.setEditable(false);
        adField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        adField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        adField.setText(Boolean.toString(tm.ad));
        adField.setToolTipText("");
        adField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adFieldActionPerformed(evt);
            }
        });

        adLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        adLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adLabel.setText("Advertise");

        brakeField.setEditable(false);
        brakeField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        brakeField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        brakeField.setText(Boolean.toString(tm.srvBrk || tm.psngrBrk));
        brakeField.setToolTipText("");
        brakeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brakeFieldActionPerformed(evt);
            }
        });

        brakeLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        brakeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        brakeLabel.setText("Brake");

        ebrakeField.setEditable(false);
        ebrakeField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ebrakeField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ebrakeField.setText(Boolean.toString(tm.eBrk));
        ebrakeField.setToolTipText("");
        ebrakeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ebrakeFieldActionPerformed(evt);
            }
        });

        ebrakeLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ebrakeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ebrakeLabel.setText("Emergency Brake");

        curSpdFld.setEditable(false);
        curSpdFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        curSpdFld.setForeground(new java.awt.Color(255, 0, 0));
        curSpdFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        curSpdFld.setText(String.format("%.1f", tm.curSpd));
        curSpdFld.setToolTipText("");
        curSpdFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curSpdFldActionPerformed(evt);
            }
        });

        curSpdLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        curSpdLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        curSpdLabel.setText("Current Speed");

        psngrCountField.setEditable(false);
        psngrCountField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        psngrCountField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        psngrCountField.setText(Integer.toString(tm.curPassengers));
        psngrCountField.setToolTipText("");
        psngrCountField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                psngrCountFieldActionPerformed(evt);
            }
        });

        psngrCountLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        psngrCountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        psngrCountLabel.setText("Passenger Count");

        pwrReqLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pwrReqLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pwrReqLabel.setText("Power Request");

        pwrReqField.setEditable(false);
        pwrReqField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pwrReqField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pwrReqField.setText(String.format("%.1f", tm.powReq));
        pwrReqField.setToolTipText("");
        pwrReqField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwrReqFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(trainModelLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pwrReqLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pwrReqField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(64, 64, 64)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ebrakeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ebrakeField, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(psngrCountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(psngrCountField, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(leftDoorField)
                                        .addComponent(leftDoorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(rightDoorField)
                                        .addComponent(rightDoorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lightsField)
                                        .addComponent(lightsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(acField)
                                        .addComponent(acLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(heaterField)
                                        .addComponent(heaterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(autoField)
                                        .addComponent(autoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(adField)
                                    .addComponent(adLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(singalPickupFailureButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(engineFailButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(brakeFailButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(psngrEnterLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(trackElevationLabel)
                                        .addComponent(trackElevationField, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(trackGradeLabel))
                                            .addComponent(trackGradeFld, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(authorityFld, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(authorityLabel))
                                    .addComponent(psngrEnteringFld, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(curSpdFld)
                            .addComponent(curSpdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(brakeField, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(brakeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(trainModelLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(engineFailButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(brakeFailButton)
                        .addGap(18, 18, 18)
                        .addComponent(singalPickupFailureButton)
                        .addGap(18, 18, 18)
                        .addComponent(trackElevationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trackElevationLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(trackGradeFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trackGradeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(authorityFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(authorityLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(psngrEnteringFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(psngrEnterLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(leftDoorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(leftDoorLabel)
                                .addGap(18, 18, 18)
                                .addComponent(rightDoorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rightDoorLabel)
                                .addGap(18, 18, 18)
                                .addComponent(lightsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lightsLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(acField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(acLabel)
                                .addGap(18, 18, 18)
                                .addComponent(heaterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(heaterLabel)
                                .addGap(18, 18, 18)
                                .addComponent(autoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autoLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(adField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(adLabel))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(brakeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(curSpdFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(brakeLabel)
                                            .addComponent(curSpdLabel))))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pwrReqField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pwrReqLabel))
                                    .addComponent(ebrakeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addComponent(ebrakeLabel))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(psngrCountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(psngrCountLabel)))))
                .addGap(17, 17, 17))
        );

        pack();
		this.setLocation(0,500);
		this.setVisible(true);
    }// </editor-fold>                        

    private void trackElevationFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                    
        // TODO add your handling code here:
    }                                                   

    private void trackGradeFldActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void psngrEnteringFldActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }                                                

    private void authorityFldActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void leftDoorFieldActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void rightDoorFieldActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void lightsFieldActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void acFieldActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void heaterFieldActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void autoFieldActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void adFieldActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void brakeFieldActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void ebrakeFieldActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void curSpdFldActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void psngrCountFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void pwrReqFieldActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    // Variables declaration - do not modify                     
    private javax.swing.JTextField acField;
    private javax.swing.JLabel acLabel;
    private javax.swing.JTextField adField;
    private javax.swing.JLabel adLabel;
    private javax.swing.JTextField authorityFld;
    private javax.swing.JLabel authorityLabel;
    private javax.swing.JTextField autoField;
    private javax.swing.JLabel autoLabel;
    private javax.swing.JToggleButton brakeFailButton;
    private javax.swing.JTextField brakeField;
    private javax.swing.JLabel brakeLabel;
    private javax.swing.JTextField curSpdFld;
    private javax.swing.JLabel curSpdLabel;
    private javax.swing.JTextField ebrakeField;
    private javax.swing.JLabel ebrakeLabel;
    private javax.swing.JToggleButton engineFailButton;
    private javax.swing.JTextField heaterField;
    private javax.swing.JLabel heaterLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField leftDoorField;
    private javax.swing.JLabel leftDoorLabel;
    private javax.swing.JTextField lightsField;
    private javax.swing.JLabel lightsLabel;
    private javax.swing.JTable paramDataTable;
    private javax.swing.JTextField psngrCountField;
    private javax.swing.JLabel psngrCountLabel;
    private javax.swing.JLabel psngrEnterLabel;
    private javax.swing.JTextField psngrEnteringFld;
    private javax.swing.JTextField pwrReqField;
    private javax.swing.JLabel pwrReqLabel;
    private javax.swing.JTextField rightDoorField;
    private javax.swing.JLabel rightDoorLabel;
    private javax.swing.JToggleButton singalPickupFailureButton;
    private javax.swing.JTextField trackElevationField;
    private javax.swing.JLabel trackElevationLabel;
    private javax.swing.JTextField trackGradeFld;
    private javax.swing.JLabel trackGradeLabel;
    private javax.swing.JLabel trainModelLabel;
    // End of variables declaration                   

}