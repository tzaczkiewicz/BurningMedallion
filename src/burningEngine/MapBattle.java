package burningEngine;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class MapBattle {
	
	JLayeredPane game;
	JLabel effect;
	Unit attacker, defender;
	int attackLength;
	int attackFrame;
	int activeArray;
	int animationLength;
	char[] attackOrder;
	String[] attackDamage;
	boolean displayingText;

	public MapBattle(JLayeredPane game, JLabel effect, Unit attacker, Unit defender, char[] attackOrder, String[] attackDamage) {
		this.game=game;
		this.effect=effect;
		this.attacker=attacker;
		this.defender=defender;
		this.attackOrder=attackOrder;
		this.attackDamage=attackDamage;
		attackLength=attackOrder.length;
		attackFrame=0;
		activeArray=0;
		displayingText=false;
		effect.setLocation(defender.getXPosition(), defender.getYPosition());
		effect.setIcon(attacker.getWeapon().getAnimation().getImage(attackFrame));
		effect.setForeground(Color.WHITE);
		game.add(effect, JLayeredPane.POPUP_LAYER);
		animationLength=attacker.getWeapon().getAnimation().getMaxFrames();
	}
	
	public boolean displaySelf(){
		if(attackFrame>=animationLength&&!displayingText){
			attackFrame=0;
			displayingText=true;
			effect.setIcon(null);
			effect.setText(attackDamage[activeArray]);
			animationLength=30;
			if(attackDamage[activeArray]!="Miss"){
				if(attackOrder[activeArray]=='A'||attackOrder[activeArray]=='a'){
					defender.takeDamage(new Integer(attackDamage[activeArray]).intValue());
				}
				else if(attackOrder[activeArray]=='D'||attackOrder[activeArray]=='d'){
					attacker.takeDamage(new Integer(attackDamage[activeArray]).intValue());
				}
			}
		}
		else if(attackFrame>=animationLength&&displayingText){
			attackFrame=0;
			displayingText=false;
			effect.setText("");
			activeArray++;
			if(activeArray>=attackOrder.length){
				game.remove(effect);
				if(!attacker.isAlive()){
					attacker.removeSelf();
				}
				if(!defender.isAlive()){
					defender.removeSelf();
				}
				return true;
			}
			else if(attackOrder[activeArray]=='A'||attackOrder[activeArray]=='a'){
				if(!attacker.isAlive()){
					game.remove(effect);
					attacker.removeSelf();
					return true;
				}
				if(!defender.isAlive()){
					game.remove(effect);
					defender.removeSelf();
					return true;
				}
				effect.setLocation(defender.getXPosition(), defender.getYPosition());
				effect.setIcon(attacker.getWeapon().getAnimation().getImage(attackFrame));
				animationLength=attacker.getWeapon().getAnimation().getMaxFrames();
			}
			else if(attackOrder[activeArray]=='D'||attackOrder[activeArray]=='d'){
				if(!attacker.isAlive()){
					game.remove(effect);
					attacker.removeSelf();
					return true;
				}
				if(!defender.isAlive()){
					game.remove(effect);
					defender.removeSelf();
					return true;
				}
				effect.setLocation(attacker.getXPosition(), attacker.getYPosition());
				effect.setIcon(defender.getWeapon().getAnimation().getImage(attackFrame));
				animationLength=defender.getWeapon().getAnimation().getMaxFrames();
			}
			else if(attackOrder[activeArray]=='n'){
				game.remove(effect);
				if(!attacker.isAlive()){
					attacker.removeSelf();
				}
				if(!defender.isAlive()){
					defender.removeSelf();
				}
				return true;
			}
		}
		else if(!displayingText){
			if(attackOrder[activeArray]=='A'||attackOrder[activeArray]=='a'){
				effect.setIcon(attacker.getWeapon().getAnimation().getImage(attackFrame));
			}
			else if(attackOrder[activeArray]=='D'||attackOrder[activeArray]=='d'){
				effect.setIcon(defender.getWeapon().getAnimation().getImage(attackFrame));
			}
		}
		attackFrame++;
		return false;
	}
	
	public Unit getAttacker(){
		return attacker;
	}
	public Unit getDefender(){
		return defender;
	}

}
