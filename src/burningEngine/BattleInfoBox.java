package burningEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BattleInfoBox extends JPanel implements ActionListener{
	
	private MapScene mapScene;
	private Unit attacker;
	private Unit defender;
	
	private JButton confirm;
	private JButton cancel;
	
	private JLabel title;
	
	private JLabel aName;
	private JLabel aHP;
	private JLabel aWep;
	private JLabel aDamage;
	private JLabel aAccuracy;
	private JLabel aCrit;
	private JLabel aHits;
	
	private JLabel dName;
	private JLabel dHP;
	private JLabel dWep;
	private JLabel dDamage;
	private JLabel dAccuracy;
	private JLabel dCrit;
	private JLabel dHits;
	
	private static final int WIDTH=200;
	private static final int HEIGHT=180;


	public BattleInfoBox(MapScene m){
		mapScene=m;
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(null);
		
		title=new JLabel("Matchup");
		title.setSize(200, 20);
		title.setHorizontalTextPosition(JLabel.CENTER);
		title.setLocation(0, 0);
		this.add(title);
		
		aName=new JLabel();
		aName.setSize(100, 20);
		aName.setLocation(0, 20);
		this.add(aName);
		aHP=new JLabel();
		aHP.setSize(100, 20);
		aHP.setLocation(0, 40);
		this.add(aHP);
		aWep=new JLabel();
		aWep.setSize(100, 20);
		aWep.setLocation(0, 60);
		this.add(aWep);
		aDamage=new JLabel();
		aDamage.setSize(100, 20);
		aDamage.setLocation(0, 80);
		this.add(aDamage);
		aAccuracy=new JLabel();
		aAccuracy.setSize(100, 20);
		aAccuracy.setLocation(0, 100);
		this.add(aAccuracy);
		aCrit=new JLabel();
		aCrit.setSize(100, 20);
		aCrit.setLocation(0, 120);
		this.add(aCrit);
		aHits=new JLabel();
		aHits.setSize(100, 20);
		aHits.setLocation(0, 140);
		this.add(aHits);
		
		dName=new JLabel();
		dName.setSize(100, 20);
		dName.setLocation(100, 20);
		this.add(dName);
		dHP=new JLabel();
		dHP.setSize(100, 20);
		dHP.setLocation(100, 40);
		this.add(dHP);
		dWep=new JLabel();
		dWep.setSize(100, 20);
		dWep.setLocation(100, 60);
		this.add(dWep);
		dDamage=new JLabel();
		dDamage.setSize(100, 20);
		dDamage.setLocation(100, 80);
		this.add(dDamage);
		dAccuracy=new JLabel();
		dAccuracy.setSize(100, 20);
		dAccuracy.setLocation(100, 100);
		this.add(dAccuracy);
		dCrit=new JLabel();
		dCrit.setSize(100, 20);
		dCrit.setLocation(100, 120);
		this.add(dCrit);
		dHits=new JLabel();
		dHits.setSize(100, 20);
		dHits.setLocation(100, 140);
		this.add(dHits);

		confirm=new JButton("Confirm");
		confirm.setSize(100, 20);
		confirm.setLocation(0, 160);
		confirm.addActionListener(this);
		this.add(confirm);
		cancel=new JButton("Cancel");
		cancel.setSize(100, 20);
		cancel.setLocation(100, 160);
		cancel.addActionListener(this);
		this.add(cancel);
	}
	
	public void open(Unit att, Unit def){
		int temp;
		attacker=att;
		defender=def;
		aName.setText(attacker.getName());
		aHP.setText(attacker.getHitpoints()+"/"+attacker.getMaxHP());
		aWep.setText(attacker.getWeapon().name);
		if(attacker.getWeapon().physical)
			aDamage.setText("Dmg: "+(attacker.getMight()-defender.getArmor()));
		else
			aDamage.setText("Dmg: "+(attacker.getMight()-defender.getWard()));
		temp=attacker.getAccuracy()-defender.getEvasion();
		if(temp>100){
			aAccuracy.setText("Hit %: 100");
		}
		else if(temp<0){
			aAccuracy.setText("Hit %: 0");
		}
		else{
			aAccuracy.setText("Hit %: "+(temp));
		}
		temp=attacker.getCrit()-defender.getDeflect();
		if(temp<0){
			aCrit.setText("Crit %: 0");
		}
		else if(temp>100){
			aCrit.setText("Crit %: 100");
		}
		else{
			aCrit.setText("Crit %: "+(temp));
		}
		aHits.setText("Attacks: "+attacker.getNumberOfAttacks(defender));

		dName.setText(defender.getName());
		dHP.setText(defender.getHitpoints()+"/"+defender.getMaxHP());
		dWep.setText(defender.getWeapon().name);
		if(defender.getWeapon().physical)
			dDamage.setText("Dmg: "+(defender.getMight()-attacker.getArmor()));
		else
			dDamage.setText("Dmg: "+(defender.getMight()-attacker.getWard()));
		temp=defender.getAccuracy()-attacker.getEvasion();
		if(temp>100){
			dAccuracy.setText("Hit %: 100");
		}
		else if(temp<0){
			dAccuracy.setText("Hit %: 0");
		}
		else{
			dAccuracy.setText("Hit %: "+(temp));
		}
		temp=defender.getCrit()-attacker.getDeflect();
		if(temp<0){
			dCrit.setText("Crit %: 0");
		}
		else if(temp>100){
			dCrit.setText("Crit %: 100");
		}
		else{
			dCrit.setText("Crit %: "+(temp));
		}
		dHits.setText("Attacks: "+defender.getNumberOfAttacks(attacker));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(confirm)){
			mapScene.attackConfirmPressed(attacker, defender);
		}
		else if(e.getSource().equals(cancel)){
			mapScene.attackCancelPressed();
		}
	}
	
}
