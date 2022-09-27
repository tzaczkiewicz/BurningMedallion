package burningEngine;

import javax.swing.JLayeredPane;

public class Fighter extends Rank {

	public Fighter(JLayeredPane g){
		super();
		game=g;
		name="Fighter";
		className="Fighter";
		baseMove=5;
		baseHitpoints=21;
		baseStrength=8;
		baseMagic=0;
		baseSkill=7;
		baseSpeed=5;
		baseLuck=3;
		baseDefense=4;
		baseResistance=0;
		maxLevel=20;
		maxMove=10;
		maxHitpoints=60;
		maxStrength=29;
		maxMagic=20;
		maxSkill=26;
		maxSpeed=25;
		maxLuck=30;
		maxDefense=25;
		maxResistance=23;
		hpGrowth=90;
		strengthGrowth=65;
		magicGrowth=5;
		skillGrowth=60;
		speedGrowth=45;
		luckGrowth=40;
		defenseGrowth=45;
		resistanceGrowth=5;
		defaultWeapon=new BronzeAxe();
		
		move=baseMove;
		
		idle=new Animation("FighterIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		greyIdle=new Animation("FighterGreyIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		up=new Animation("FighterUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		down=new Animation("FighterDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		left=new Animation("FighterLeft", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		right=new Animation("FighterRight", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		enemyIdle=idle;
		enemyUp=up;
		enemyDown=down;
		enemyLeft=left;
		enemyRight=right;
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
