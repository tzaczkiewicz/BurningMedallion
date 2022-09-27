package burningEngine;

import javax.swing.JLayeredPane;

public class Zoro extends Rank {
	public Zoro(JLayeredPane g){
		super();
		game=g;
		name="Zoro";
		className="Swordsman";
		baseMove=7;
		baseHitpoints=38;
		baseStrength=12;
		baseDefense=10;
		baseSpeed=9;
		baseSkill=15;
		baseLuck=11;
		baseMagic=1;
		baseResistance=2;
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
		hpGrowth=120;
		strengthGrowth=70;
		defenseGrowth=70;
		speedGrowth=60;
		skillGrowth=70;
		luckGrowth=50;
		magicGrowth=50;
		resistanceGrowth=50;
		defaultWeapon=new BronzeSword();
		
		move=7;
		
		idle=new Animation("ZoroIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		enemyIdle=new Animation("ZoroEnemyIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		greyIdle=new Animation("ZoroGreyIdle", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		up=new Animation("ZoroUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		enemyUp=new Animation("ZoroEnemyUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		down=new Animation("ZoroDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		enemyDown=new Animation("ZoroEnemyDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		left=new Animation("ZoroLeft", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		enemyLeft=new Animation("ZoroEnemyLeft", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		right=new Animation("ZoroRight", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		enemyRight=new Animation("ZoroEnemyRight", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
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
