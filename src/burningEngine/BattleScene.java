package burningEngine;

import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class BattleScene{
	private Unit leftUnit, rightUnit;
	private Animation leftAnimation, rightAnimation;
	private Animation background;
	private JLabel leftLabel, rightLabel, windowLabel, backgroundLabel;
	private JLayeredPane game;
	private Point point;
	private int leftFrame, rightFrame, leftMaxFrames, rightMaxFrames;
	private char[] attacks;/*l=left attacks and misses, L=left attacks and hits,
		r=right attacks and misses, R=right attacks and hits, c=left crits, C=right crits*/
	private int attackNumber;
	private boolean hits;/*true means hit upon reaction frame, false means miss upon reaction frame*/
	private char dies;/*l=left, r=right, b=both*/
	private int xpGained;
	private char sideAttacking;/*l=left, r=right, n=neither*/
	private int xOffset, yOffset;
	public boolean endDisplay;/*when this is true, displaySelf() runs the XP window*/

	public BattleScene(JLayeredPane g, Unit l, Unit r, Animation b, char[] a, char d, int xp, int xOff, int yOff) {
		game=g;
		leftUnit=l;
		rightUnit=r;
		attacks=a;
		attackNumber=-1;
		dies=d;
		xpGained=xp;
		background=b;
		leftAnimation=leftUnit.getBattleIdle();
		rightAnimation=rightUnit.getBattleIdle();
		leftFrame=rightFrame=0;
		leftMaxFrames=leftAnimation.maxFrames;
		rightMaxFrames=rightAnimation.maxFrames;
		endDisplay=false;
		
		xOffset=xOff;
		yOffset=yOff;
		
		leftLabel=new JLabel();
		rightLabel=new JLabel();
		backgroundLabel=new JLabel();
		//windowLabel=new JLabel();
		game.add(leftLabel, JLayeredPane.MODAL_LAYER);
		game.add(rightLabel, JLayeredPane.MODAL_LAYER);
		game.add(backgroundLabel, JLayeredPane.MODAL_LAYER);
		//game.add(windowLabel);
		leftLabel.setLocation(50+xOffset, 20+yOffset);
		rightLabel.setLocation(100+xOffset, 20+yOffset);
		backgroundLabel.setLocation(xOffset, yOffset);
		//game.setLocation(0,0);
		backgroundLabel.setIcon(background.getImage(0));
		backgroundLabel.setSize(backgroundLabel.getPreferredSize());
		//set location here
		
		if(attacks.length==0){
			System.out.println("ERROR, no attack array");
		}
		nextAttack();
		
	}
	
	public void endDisplay(){//skip to end of display
		endDisplay=true;
	}
	
	public boolean displaySelf(){/*returns true when animations end*/
		if(!endDisplay){
			//System.out.println(sideAttacking);
			if(sideAttacking=='l'){
				if(leftFrame==leftMaxFrames){
					if(!nextAttack())
						endDisplay=true;
						
				}
				if(rightFrame==rightMaxFrames)
					rightFrame=0;
			}
		
			else if(sideAttacking=='r'){
				if(rightFrame==rightMaxFrames){
					if(!nextAttack())
						endDisplay=true;
				}
				if(leftFrame==leftMaxFrames)
					leftFrame=0;
			}
			
			else{
				
			}
			animate();

		}
		else{
			
			animate();
			//animateWindow();
			if(leftFrame==leftMaxFrames){/*this will later be when the window is done animating*/
				deallocate();
				return true;
			}
		}
		return false;
	}
	public void deallocate(){
		game.remove(leftLabel);
		game.remove(rightLabel);
		game.remove(backgroundLabel);
		//game.remove(windowLabel);
	}
	
	
	public boolean nextAttack(){/*returns false if next attack doesn't exist, true otherwise*/
		/*if returns false, that's when the program should set both to idle (unless one dies) and run the XP animation*/
		attackNumber++;
		
		if(attackNumber>=attacks.length)/*when out of attacks*/
		{
			/*if(dies=='l'){
				leftAnimation=leftUnit.getDie();
				rightAnimation=rightUnit.getBattleIdle();
			}
			if(dies=='r'){
				leftAnimation=leftUnit.getBattleIdle();
				rightAnimation=rightUnit.getDie();
			}*/
			//don't forget an else here when removing comments
			leftAnimation=leftUnit.getBattleIdle();
			rightAnimation=rightUnit.getBattleIdle();
			rightFrame=leftFrame=0;
			leftMaxFrames=leftAnimation.maxFrames;
			rightMaxFrames=rightAnimation.maxFrames;
			//set xp gain window up here
		
			return false;
		}
		switch(attacks[attackNumber]){
		case 'l':
			leftAnimation=leftUnit.getAttack();
			rightAnimation=rightUnit.getBattleIdle();
			leftFrame=rightFrame=0;
			leftMaxFrames=leftAnimation.maxFrames;
			rightMaxFrames=rightAnimation.maxFrames;
			sideAttacking='l';
			hits=false;
			break;
		case 'L':
			leftAnimation=leftUnit.getAttack();
			rightAnimation=rightUnit.getBattleIdle();
			leftFrame=rightFrame=0;
			leftMaxFrames=leftAnimation.maxFrames;
			rightMaxFrames=rightAnimation.maxFrames;
			sideAttacking='l';
			hits=true;
			break;
		case 'r':
			rightAnimation=rightUnit.getAttack();
			leftAnimation=leftUnit.getBattleIdle();
			leftFrame=rightFrame=0;
			leftMaxFrames=leftAnimation.maxFrames;
			rightMaxFrames=rightAnimation.maxFrames;
			sideAttacking='r';
			hits=false;
			break;
		case 'R':
			rightAnimation=rightUnit.getAttack();
			leftAnimation=leftUnit.getBattleIdle();
			leftFrame=rightFrame=0;
			leftMaxFrames=leftAnimation.maxFrames;
			rightMaxFrames=rightAnimation.maxFrames;
			sideAttacking='r';
			hits=true;
			break;
		case 'c':
			leftAnimation=leftUnit.getCritical();
			rightAnimation=rightUnit.getBattleIdle();
			leftFrame=rightFrame=0;
			leftMaxFrames=leftAnimation.maxFrames;
			rightMaxFrames=rightAnimation.maxFrames;
			sideAttacking='l';
			hits=true;
			break;
		case 'C':
			rightAnimation=rightUnit.getCritical();
			leftAnimation=leftUnit.getBattleIdle();
			leftFrame=rightFrame=0;
			leftMaxFrames=leftAnimation.maxFrames;
			rightMaxFrames=rightAnimation.maxFrames;
			sideAttacking='r';
			hits=true;
			break;
		case 'n':
			endDisplay();
		default:
			rightAnimation=rightUnit.getBattleIdle();
			leftAnimation=leftUnit.getBattleIdle();
			leftFrame=rightFrame=0;
			leftMaxFrames=leftAnimation.maxFrames;
			rightMaxFrames=rightAnimation.maxFrames;
			sideAttacking='n';
			hits=false;
			break;
		}
		leftLabel.setIcon(leftAnimation.getImage(0));
		leftLabel.setSize(leftLabel.getPreferredSize());
		rightLabel.setIcon(rightAnimation.getImage(0));
		rightLabel.setSize(rightLabel.getPreferredSize());
		return true;
	}
	
	public void animate(){
		if(leftFrame==leftMaxFrames)
			leftFrame=0;
		if(rightFrame==rightMaxFrames)
			rightFrame=0;
		
		leftLabel.setIcon(leftAnimation.getImage(leftFrame));
		rightLabel.setIcon(rightAnimation.getImage(rightFrame));
		
		increment();
	}
	public void animateWindow(){
		/*handles the xp gain window's frames, transitions to levelUp window if necessary, and then handles that*/
	}
	
	/*public void displayStatic(){
		
	}*/
	
	public void increment(){
		leftFrame++;
		rightFrame++;
	}

	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode()==88)
			endDisplay();
		
	}
}
