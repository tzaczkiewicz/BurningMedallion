package burningEngine;

public class BronzeSword extends Weapon{
	public BronzeSword(){
		power=7;
		weight=2;
		accuracy=120;
		evasion=0;
		name="Bronze Sword";
		animation=new Animation("TestAttack", 50, 50);
	}
}
