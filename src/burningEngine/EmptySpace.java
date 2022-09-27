package burningEngine;

import javax.swing.JLayeredPane;


public class EmptySpace extends Rank{
	public EmptySpace(JLayeredPane g){
		game=g;
		idle=new Animation("EmptySpace", g, 50, 50);
		/*up=idle;
		down;
		left;
		right;
		mapAttack;
		attack;
		critical;
		getHit;
		die;
		mapDie;*/
	}
	public boolean isEmptySpace(){
		return true;
	}
}
