package burningEngine;

import javax.swing.JLayeredPane;

public class AxeDudeEnemy extends Rank {

	public AxeDudeEnemy(JLayeredPane g){
		super();
		game=g;
		name="Axe Dude";
		baseMove=7;
		baseHitpoints=48;
		baseStrength=18;
		baseDefense=3;
		baseSpeed=6;
		baseSkill=12;
		baseLuck=4;
		baseMagic=0;
		baseResistance=0;
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
		defaultWeapon=new BronzeAxe();
		
		move=7;
		
		idle=new Animation("AxeDudeUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		greyIdle=new Animation("AxeDudeDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		up=new Animation("AxeDudeUp", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		down=new Animation("AxeDudeDown", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		left=new Animation("AxeDudeLeft", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		right=new Animation("AxeDudeRight", g, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
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
