package burningEngine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class InactiveMenu extends JPanel implements ActionListener {

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
	
	public InactiveMenu(MapScene m, JLayeredPane g) {
		name="Default";
		this.setLayout(null);
		this.setSize(100, 80);
		mapScene=m;
		game=g;
		background=new Animation("UnitMenu", game, 100, 80);
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
		inspect=new JButton("Inspect");
		this.add(inspect);
		inspect.setSize(94,20);
		inspect.setLocation(3,37);
		cancel=new JButton("Cancel");
		this.add(cancel);
		cancel.setSize(94,20);
		cancel.setLocation(3,57);
		this.add(backgroundLabel);
		
		inspect.addActionListener(this);
		cancel.addActionListener(this);
	}
	
	public void addSelf(Unit u, int x, int y){
		name=u.getName();
		nameLabel.setText(name);
		hpLabel.setText(u.getHitpoints()+"/"+u.getMaxHP());
		this.setSize(100, 80);
		this.setLocation(x, y);
		game.add(this, JLayeredPane.POPUP_LAYER);
	}
	public void removeSelf(){
		name="Default";
		game.remove(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(cancel))
			mapScene.cancelPressed();
		else if(e.getSource().equals(endTurn))
			mapScene.endTurnPressed();
		else if(e.getSource().equals(inspect))
			mapScene.inspectPressed();
	}

}
