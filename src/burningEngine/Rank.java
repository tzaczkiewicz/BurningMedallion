package burningEngine;

import javax.swing.JLayeredPane;

public abstract class Rank {/*note: Put defaults here so there can't be a missing animation*/
	JLayeredPane game;
	String name;//character name
	String className;//rank name
	int move;
	public int baseMove;
	public int baseHitpoints;
	public int baseStrength;
	public int baseDefense;
	public int baseSpeed;
	public int baseSkill;
	public int baseLuck;
	public int baseMagic;
	public int baseResistance;
	public int maxLevel;
	public int maxMove;
	public int maxHitpoints;
	public int maxStrength;
	public int maxDefense;
	public int maxSpeed;
	public int maxSkill;
	public int maxLuck;
	public int maxMagic;
	public int maxResistance;
	public int hpGrowth;
	public int strengthGrowth;
	public int defenseGrowth;
	public int speedGrowth;
	public int skillGrowth;
	public int luckGrowth;
	public int magicGrowth;
	public int resistanceGrowth;
	public Weapon defaultWeapon;
	
	public Rank[] talkTargets;
	
	
	Animation idle;
	Animation greyIdle;
	Animation up;
	Animation down;
	Animation left;
	Animation right;
	Animation enemyIdle;
	Animation enemyUp;
	Animation enemyDown;
	Animation enemyLeft;
	Animation enemyRight;
	Animation mapAttack;
	Animation battleIdle;
	Animation attack;
	Animation critical;
	Animation getHit;
	Animation die;
	Animation mapDie;
	
	public Rank(){
		baseMove=0;
		baseHitpoints=0;
		baseStrength=0;
		baseDefense=0;
		baseSpeed=0;
		baseSkill=0;
		baseLuck=0;
		baseMagic=0;
		baseResistance=0;
		maxLevel=20;
		maxMove=1;
		maxHitpoints=1;
		maxStrength=1;
		maxDefense=1;
		maxSpeed=1;
		maxSkill=1;
		maxLuck=1;
		maxMagic=1;
		maxResistance=1;
		hpGrowth=10;
		strengthGrowth=10;
		defenseGrowth=10;
		speedGrowth=10;
		skillGrowth=10;
		luckGrowth=10;
		magicGrowth=10;
		resistanceGrowth=10;
	}
	
	public Weapon getDefaultWeapon(){
		return defaultWeapon;
	}
	
	public int getMove(){
		return move;
	}
	public String getName(){
		return name;
	}
	public String getClassName(){
		return className;
	}
	public int critBonus(){//for classes with a crit bonus
		return 0;
	}
	
	public Animation getUp(){
		return up;
	}
	public Animation getDown(){
		return down;
	}
	public Animation getLeft(){
		return left;
	}
	public Animation getRight(){
		return right;
	}
	public Animation getIdle(){
		return idle;
	}
	public Animation getGreyIdle(){
		return greyIdle;
	}
	public Animation getEnemyIdle(){
		if(enemyIdle==null)
			return idle;
		return enemyIdle;
	}
	public Animation getEnemyGreyIdle(){
		return greyIdle;
	}
	public Animation getEnemyUp(){
		if(enemyUp==null)
			return up;
		return enemyUp;
	}
	public Animation getEnemyDown(){
		if(enemyDown==null)
			return down;
		return enemyDown;
	}
	public Animation getEnemyLeft(){
		if(enemyLeft==null)
			return left;
		return enemyLeft;
	}
	public Animation getEnemyRight(){
		if(enemyRight==null)
			return right;
		return enemyRight;
	}
	public Animation getBattleIdle(){
		return battleIdle;
	}
	public Animation getAttack(){
		return attack;
	}
	public Animation getCritical(){
		return critical;
	}
	public boolean isEmptySpace(){
		return false;
	}
}
