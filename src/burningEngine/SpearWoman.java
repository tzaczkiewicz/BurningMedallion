package burningEngine;

import javax.swing.JLayeredPane;

public class SpearWoman extends Rank{
	
	
	public SpearWoman(JLayeredPane g){
		super();
		game=g;
		name="Spear Woman";
		baseMove=7;
		baseHitpoints=28;
		baseStrength=6;
		baseDefense=6;
		baseSpeed=18;
		baseSkill=10;
		baseLuck=14;
		baseMagic=1;
		baseResistance=1;
		maxLevel=20;
		maxMove=10;
		maxHitpoints=60;
		maxStrength=20;
		maxDefense=20;
		maxSpeed=20;
		maxSkill=20;
		maxLuck=20;
		maxMagic=20;
		maxResistance=20;
		hpGrowth=100;
		strengthGrowth=50;
		defenseGrowth=50;
		speedGrowth=50;
		skillGrowth=50;
		luckGrowth=50;
		magicGrowth=50;
		resistanceGrowth=50;
		defaultWeapon=new BronzeSpear();
		
		move=7;
		
		idle=new Animation("CanisIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		greyIdle=new Animation("SpearWomanGreyIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		up=new Animation("SpearWomanUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		down=new Animation("SpearWomanDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		left=new Animation("SpearWomanLeft", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		right=new Animation("SpearWomanRight", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		/*mapAttack;*/
		battleIdle=new Animation("SpearWomanBattleIdle", g, 350, 280);
		attack=new Animation("SpearWomanAttack", g, 350, 280);
		attack.setEnemyHitFrame(10);
		/*
		critical;
		getHit;
		die;
		mapDie;*/
	}
}
