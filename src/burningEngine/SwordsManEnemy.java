package burningEngine;

import javax.swing.JLayeredPane;

public class SwordsManEnemy extends Rank{

	public SwordsManEnemy(JLayeredPane g){
		super();
		game=g;
		name="Swordsman";
		baseMove=7;
		baseHitpoints=34;
		baseStrength=10;
		baseDefense=8;
		baseSpeed=8;
		baseSkill=14;
		baseLuck=10;
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
		defaultWeapon=new BronzeSword();
		
		move=7;
		
		idle=new Animation("SwordsManUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		greyIdle=new Animation("SwordsManDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		up=new Animation("SwordsManUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		down=new Animation("SwordsManDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		left=new Animation("SwordsManLeft", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		right=new Animation("SwordsManRight", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
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
