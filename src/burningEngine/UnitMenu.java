package burningEngine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UnitMenu extends JPanel implements ActionListener{//This class is the menu that pops up when you click a unit.
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;//The name of the Unit that's selected, displayed at the top
	JLabel nameLabel;
	JLabel hpLabel;
	JButton move;
	JButton attack;
	JButton item;
	JButton trade;
	JButton inspect;
	JButton endTurn;
	JButton cancel;
	
	
	Animation background;
	JLabel backgroundLabel;
	MapScene mapScene;
	JLayeredPane game;
	
	
	public UnitMenu(MapScene m, JLayeredPane g){
		name="Default";
		this.setLayout(null);
		this.setSize(100,180);
		mapScene=m;
		game=g;
		background=new Animation("UnitMenu", game, 100, 180);
		backgroundLabel=new JLabel();
		backgroundLabel.setIcon(background.getImage(0));
		backgroundLabel.setSize(backgroundLabel.getPreferredSize());
		backgroundLabel.setLocation(0,0);
		setBackground(Color.gray);
		nameLabel=new JLabel();
		
		nameLabel.setText(name);
		nameLabel.setSize(100,17);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setLocation(0,3);
		this.add(nameLabel);
		hpLabel=new JLabel();
		hpLabel.setText("0/0");
		hpLabel.setSize(100,17);
		hpLabel.setHorizontalAlignment(JLabel.CENTER);
		hpLabel.setLocation(0,20);
		this.add(hpLabel);
		move=new JButton("Move");
		this.add(move);
		move.setSize(94,20);
		move.setLocation(3,37);
		attack=new JButton("Attack");
		this.add(attack);
		attack.setSize(94,20);
		attack.setLocation(3,57);
		item=new JButton("Item");
		this.add(item);
		item.setSize(94,20);
		item.setLocation(3,77);
		trade=new JButton("Trade");
		this.add(trade);
		trade.setSize(94,20);
		trade.setLocation(3,97);
		inspect=new JButton("Inspect");
		this.add(inspect);
		inspect.setSize(94,20);
		inspect.setLocation(3,117);
		endTurn=new JButton("End Turn");
		this.add(endTurn);
		endTurn.setSize(94,20);
		endTurn.setLocation(3,137);
		cancel=new JButton("Cancel");
		this.add(cancel);
		cancel.setSize(94,20);
		cancel.setLocation(3,157);
		this.add(backgroundLabel);
		
		move.addActionListener(this);
		attack.addActionListener(this);
		item.addActionListener(this);
		trade.addActionListener(this);
		inspect.addActionListener(this);
		endTurn.addActionListener(this);
		cancel.addActionListener(this);
	}
	public void addSelf(Unit u, int x, int y){
		name=u.getName();
		nameLabel.setText(name);
		hpLabel.setText(u.getHitpoints()+"/"+u.getMaxHP());
		this.setSize(100,180);
		this.setLocation(x, y);
		game.add(this, JLayeredPane.POPUP_LAYER);
	}
	public void removeSelf(){
		name="Default";
		game.remove(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(move))
			mapScene.movePressed();
		else if(e.getSource().equals(attack))
			mapScene.attackPressed();
		else if(e.getSource().equals(cancel))
			mapScene.cancelPressed();
		else if(e.getSource().equals(endTurn))
			mapScene.endTurnPressed();
		else if(e.getSource().equals(inspect))
			mapScene.inspectPressed();
	}
	
}
