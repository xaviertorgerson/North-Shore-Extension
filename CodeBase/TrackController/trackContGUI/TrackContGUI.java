import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

//ADD heater status/ make labels in csv more informative/ add help tab

public class TrackContGUI extends JFrame {
	private int TOTALSECTIONS;
	
	private final JButton 	nextTC   	= new JButton(">");
	private final JButton 	prevTC   	= new JButton("<");
	private final JButton 	plcButt   	= new JButton("ENTER");
	private final JLabel 	sectName 	= new JLabel("Sect0");
	private final JTextArea plcFileEnt	= new JTextArea("Enter PLC file location here");
	private final JLabel	curentAct	= new JLabel("Action: \t\tStandby");
	private final JLabel	speedLab	= new JLabel("0mph");
	private final JLabel	authLab		= new JLabel("2mi");
	private BlockPanel[][] sections;
	String speeds[];
	
	int sectNum=0;
	
	public TrackContGUI(){
		super();
		
		this.setTitle("Track Controller - Blue Team");
		this.getContentPane().setLayout(null);
		this.setBounds(10, 10, 580, 440);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);
		
		this.add(makeButton(nextTC,160,40,50,30,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextTrackCont();
			}
		}));
		this.add(makeButton(prevTC,10,40,50,30,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prevTrackCont();
			}
		}));
		this.add(makeButton(plcButt,160,10,90,30,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enterPLCCode();
			}
		}));
		this.add(placeJComponent(sectName,60,40,100,30));
		this.add(placeJComponent(plcFileEnt,10,10,150,30));
		this.add(placeJComponent(curentAct,10,350,150,30));
		this.add(placeJComponent(speedLab,10,320,150,30));
		this.add(placeJComponent(authLab,10,290,150,30));
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSections();
		update(0);
		speedLab.setText(speeds[0]);
	}
	
	private void setSections(){
		try(BufferedReader br=new BufferedReader(new FileReader("testTrackContGUI.csv"))){
			String curLineS;
			boolean firstLine=true;
			int blockNum=0;
			int sectLength=0;
			int sNum=0;
			BlockPanel [] blocks;
			while((curLineS=br.readLine())!=null){
				String[] data=curLineS.split(",");
				for(String s:data){
					System.out.print(s+",");
				}
				System.out.println();
				if(firstLine){
					TOTALSECTIONS=Integer.parseInt(data[0]);
					sections=new BlockPanel[TOTALSECTIONS][4];
					speeds=new String[TOTALSECTIONS];
					firstLine=false;
				}else{
					if(data[0].equals("S")){
						System.out.println("in S\n");
						sectLength=Integer.parseInt(data[2]);
						sections[sNum]=new BlockPanel[sectLength];
						speeds[sNum]=new String("Speed limit: \t\t"+data[3]+"mph");
						for(blockNum=0;blockNum<sectLength;++blockNum){
							curLineS=br.readLine();
							data=curLineS.split(",");
							sections[sNum][blockNum]=new BlockPanel();
							sections[sNum][blockNum].setData(data);
						}
						sNum++;
					}
				}
			}
		}catch(IOException e){
			System.out.println("failed to load file");
		}
	}

	private JButton makeButton(JButton b,int x,int y,int l,int w,ActionListener actLis){
		b.setBounds(x, y, l, w);
		b.addActionListener(actLis);
		return b;
	}
	
	private <T extends JComponent> T placeJComponent(T component,int x,int y,int l,int w){
		component.setBounds(x,y,l,w);
		return component;
	}
	
	public void nextTrackCont(){
		System.out.println("next Track Cont");
		if(sectNum<TOTALSECTIONS-1){
			sectNum++;
			sectName.setText("Sect"+sectNum);
			update(sectNum);
			speedLab.setText(speeds[sectNum]);
		}
	}
	public void prevTrackCont(){
		System.out.println("prev Track Cont");
		if(sectNum>0){
			sectNum--;
			sectName.setText("Sect"+sectNum);
			update(sectNum);
			speedLab.setText(speeds[sectNum]);
		}
	}
	public void enterPLCCode(){
		System.out.println("enter PLC");
	}
	public void update(int sNum){
		for(int i=0;i<4;++i){
			sections[sNum][i].setBBounds(10+(100*i),100,100,100);
			this.add(sections[sNum][i]);
		}
	}
	
	public static void main(String[] args){
		System.out.println("hello");
		new TrackContGUI();
	}
}