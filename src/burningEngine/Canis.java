package burningEngine;

import javax.swing.JLayeredPane;

public class Canis extends Rank {
	
	public Canis(JLayeredPane g){
		super();
		game=g;
		name="Canis";
		className="Scrapper";
		baseMove=5;
		baseHitpoints=20;
		baseStrength=7;
		baseDefense=7;
		baseSpeed=8;
		baseSkill=8;
		baseLuck=5;
		baseMagic=1;
		baseResistance=1;
		maxLevel=20;
		maxMove=10;
		maxHitpoints=60;
		maxStrength=30;
		maxDefense=26;
		maxSpeed=28;
		maxSkill=27;
		maxLuck=20;
		maxMagic=20;
		maxResistance=25;
		hpGrowth=85;
		strengthGrowth=60;
		defenseGrowth=45;
		speedGrowth=60;
		skillGrowth=60;
		luckGrowth=70;
		magicGrowth=10;
		resistanceGrowth=50;
		defaultWeapon=new BronzeSword();
		
		move=baseMove;
		
		idle=new Animation("CanisIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		greyIdle=new Animation("CanisGreyIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		up=new Animation("CanisUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		down=new Animation("CanisDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		left=new Animation("CanisLeft", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		right=new Animation("CanisRight", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
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
