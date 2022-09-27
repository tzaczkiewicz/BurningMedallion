package burningEngine;

public class BronzeSpear extends Weapon{
	public BronzeSpear() {
		power=5;
		weight=2;
		accuracy=100;
		evasion=30;
		name="Bronze Spear";
		animation=new Animation("TestAttack", 50, 50);
	}
}
