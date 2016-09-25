import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class BlockPanel extends JPanel{
	int width;
	int length;
	String data2;
	
	private final JLabel  	blckName 	= new JLabel("Block #");
	private final JLabel  	blckType 	= new JLabel("Type Blank");
	private final JLabel  	occupancy 	= new JLabel("Track Empty");
	private final JLabel  	failStat 	= new JLabel("Track Working");
	private final JLabel	heatStat	= new JLabel("Heat OFF");
	
	public BlockPanel(int x,int y,int w,int l){
		super();
		System.out.println("in block panel x= "+x);
		width=w;
		length=l;
		
		this.setBounds(x,y,w,l);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		GridLayout layout=new GridLayout(0,1,0,4);
		setLayout(layout);
		
		this.add(placeJComponent(blckName,10,10,100,30));
		this.add(placeJComponent(blckType,10,40,100,30));
		this.add(placeJComponent(occupancy,100,70,100,30));
		this.add(placeJComponent(failStat,10,100,100,30));
		this.setVisible(true);
	}
	public BlockPanel(){
		super();
	}
	public void setData(String[] data){
		System.out.println("in block panel");
		for(String s:data){
			System.out.println(s);
		}
		if(data[3].equals("FAILURE")){
			failStat.setForeground(Color.red);
		}
		data2=data[0];
		blckName.setText(data[0]);
		blckType.setText(data[1]);
		occupancy.setText(data[2]);
		failStat.setText(data[3]);
		heatStat.setText(data[4]);
	}
	public void update(){
		
	}
	public void setBBounds(int x,int y,int w,int l){
		this.setBounds(x,y,w,l);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		GridLayout layout=new GridLayout(0,1,0,5);
		setLayout(layout);
		
		this.add(placeJComponent(blckName,10,10,100,30));
		this.add(placeJComponent(blckType,10,40,100,30));
		this.add(placeJComponent(occupancy,100,70,100,30));
		this.add(placeJComponent(failStat,10,100,100,30));
		this.add(placeJComponent(heatStat,10,100,100,30));
		this.setVisible(true);
	}
	
	private <T extends JComponent> T placeJComponent(T component,int x,int y,int l,int w){
		component.setBounds(x,y,l,w);
		return component;
	}
}