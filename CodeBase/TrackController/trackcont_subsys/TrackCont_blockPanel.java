/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackcont_subsys;

/**
 *
 * @author Jeff
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class TrackCont_blockPanel extends JPanel{
    static final int HEIGHT=160;
    static final int WIDTH=120;
    boolean top;
    Block block;
    
    public TrackCont_blockPanel(int x,int y,Block tb,boolean top){
        super();
        this.setBounds(x,y,WIDTH,HEIGHT);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        block=tb;
        this.top=top;
        repaint();
        this.setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        if(!block.getFailureStatus())
            g2.setColor(Color.black);
        else
            g2.setColor(Color.red);
        
        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        
        /*
        if(top){
            g.drawString(("#:"+block.blockNum), 2, 25);
            g.drawString(("Sp:"+block.speedLimit+"mph"), 2,52);
            g.drawString(("Ath:"+block.authority+"mi"), 2, 79);
        }else{
            g.drawString(("#:"+block.blockNum), 2, 70);
            g.drawString(("Sp:"+block.speedLimit+"mph"), 2,97);
            g.drawString(("Ath:"+block.authority+"mi"), 2, 124);
        }*/
        
        int lineWidth=WIDTH;
        int lineHeight=HEIGHT;
        int translate=40;
        if(!top)
            translate=-40;
        
        checkInfastructure(g,g2,lineWidth,lineHeight,translate);
        checkIfNearSwitch(g,g2,lineWidth,lineHeight,translate);
        checkIfTrainPresent(g,g2,lineWidth,lineHeight,translate);
        
        /*if(block.getHeater()){ // heater is turned on
            g2.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            if(top){
                g.drawString("H", lineWidth-35, lineHeight-5);
            }else{
                
            }
        }*/
    }
    public void checkInfastructure(Graphics g,Graphics2D g2,int lineWidth,int lineHeight,int translate){
        switch (block.getInfrastructure()){
            case "": //block is empty, has no disernable type
                g2.draw(new Line2D.Float(0, lineHeight/2+translate, lineWidth, lineHeight/2+translate));
                break;
            case "switch":
                //switch points up (-<)
                if(block.getSwitch().getState0().getNumber()==block.getNumber()+1){
                    g2.draw(new Line2D.Float(0, lineHeight/2+translate, lineWidth, lineHeight/2+translate));
                    g2.draw(new Line2D.Float(lineWidth/2, (lineHeight/2+translate)+10, lineWidth/2, lineHeight));
                    if(!block.getSwitch().getState()){ //block is a switch pointing down
                        g2.setColor(Color.blue);
                        g2.draw(new Line2D.Float(lineWidth/2, (lineHeight/2+translate)+10, lineWidth/2, lineHeight));
                    }
                    else{ //block is a switch pointing forrward
                        g2.setColor(Color.blue);
                        g2.draw(new Line2D.Float(lineWidth/2+10, (lineHeight/2+translate), lineWidth, (lineHeight/2+translate)));
                    }
                }else{
                    //Switch points down (>-)
                    g2.draw(new Line2D.Float(0, (lineHeight/2+translate), lineWidth, (lineHeight/2+translate)));
                    g2.draw(new Line2D.Float(lineWidth/2, (lineHeight/2+translate)+10, lineWidth/2, lineHeight));
                    if(!block.getSwitch().getState()){ //block is a switch pointing down
                        g2.setColor(Color.blue);
                        g2.draw(new Line2D.Float(lineWidth/2, (lineHeight/2+translate)+10, lineWidth/2, lineHeight));
                    }
                    else{ //block is a switch pointing forrward
                        g2.setColor(Color.blue);
                        g2.draw(new Line2D.Float(0, (lineHeight/2+translate), lineWidth/2-10, (lineHeight/2+translate)));
                    }
                }
                break;
            case "crossing": //block is a crossing
                g2.draw(new Line2D.Float(0, lineHeight/2+translate, lineWidth, lineHeight/2+translate));
                if(!block.getCrossing().getState()){ //crossing inactive
                    g2.draw(new Line2D.Float(7*lineWidth/8, (lineHeight/2+translate)-translate/2, lineWidth, (lineHeight/2+translate)-translate/2));
                    g2.draw(new Line2D.Float(0, (lineHeight/2+translate)+translate/2, lineWidth/8, (lineHeight/2+translate)+translate/2));
                }else{ //crossing active
                    g2.setColor(Color.orange);
                    g2.draw(new Line2D.Float(0, (lineHeight/2+translate)-translate/2, lineWidth, (lineHeight/2+translate)-translate/2));
                    g2.draw(new Line2D.Float(0, (lineHeight/2+translate)+translate/2, lineWidth, (lineHeight/2+translate)+translate/2));
                }
                break;
            /*case 3: //block is a light
                g2.draw(new Line2D.Float(0, lineHeight/2, lineWidth, lineHeight/2));
                if(block.state){ //light is green
                    g2.setColor(Color.green);
                }else{ //light is red
                    g2.setColor(Color.red);
                }
                g2.draw(new Line2D.Float(lineWidth-lineWidth/8, lineHeight/8, lineWidth, lineHeight/8));
                break;*/
        }
    }
    public void checkIfNearSwitch(Graphics g,Graphics2D g2,int lineWidth,int lineHeight,int translate){
        if(block.getPrev().getInfastructure("switch")){
            g2.draw(new Line2D.Float(lineWidth/2, lineHeight/2, lineWidth, lineHeight/2));
            g2.draw(new Line2D.Float(lineWidth/2, lineHeight/2, lineWidth/2, 0));
            g2.draw(new Line2D.Float(0, lineHeight/2, lineWidth, lineHeight/2));
        }else if(block.getNext().getInfastructure("switch")){
            g2.draw(new Line2D.Float(lineWidth/2, lineHeight/2, lineWidth, lineHeight/2));
            g2.draw(new Line2D.Float(lineWidth/2, lineHeight/2, lineWidth/2, 0));
            g2.draw(new Line2D.Float(0, lineHeight/2, lineWidth, lineHeight/2));
        }
    }
    public void checkIfTrainPresent(Graphics g,Graphics2D g2,int lineWidth,int lineHeight,int translate){
        if(block.getTrainPresent()!=0){ //block is occupied
            g2.setColor(Color.green);
            g2.setStroke(new BasicStroke(20));
            //g2.draw(new Ellipse2D.Float(lineWidth/4,(lineHeight/2+translate/2), lineWidth-lineWidth/2, (lineHeight/2+translate)-(lineHeight/4+translate)));
             g2.draw(new Line2D.Float(lineWidth/4, (lineHeight/2+translate), 3*lineWidth/4, (lineHeight/2+translate)));
        }
    }
}
