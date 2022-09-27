package burningEngine;

import javax.swing.ImageIcon;

public abstract class Weapon {
	public int power=0;
	public int weight=0;
	public int accuracy=0;
	public int evasion=0;
	public int crit=0;
	public int maxDurability=1;
	public Animation animation;
	public boolean physical=true;
	private boolean forged=false;
	private boolean[] range;
	
	private int durability=1;
	
	public String name="Default";
	
	public Weapon(){
		range=new boolean[2];
		range[0]=true;
		range[1]=false;
	}
	
	public Animation getAnimation(){
		return animation;
	}
	
	public ImageIcon getImage(int x){
		return animation.getImage(x);
	}
	
	public int getDurability(){
		return durability;
	}
	public void setDurability(int x){
		durability=x;
		if(x>maxDurability)
			durability=maxDurability;
		if(x<0)
			durability=0;
	}
	public void damageWeapon(){
		durability=durability-1;
		if(durability<0)
			durability=0;
	}
	public boolean isForged(){
		return forged;
	}
	public void setForged(boolean x){
		forged=x;
	}
	
	public String getType(){
		if(physical)
			return "Physical";
		return "Magical";
	}
	public boolean[] getRange(){
		return range;
	}
	public String getRangeString(){
		if(range[0]&&range[1])
			return "1-2";
		if(range[1])
			return "2";
		if(range[0])
			return "1";
		return "0";
	}
}
