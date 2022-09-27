package burningEngine;

import javax.swing.JLayeredPane;


public final class RankManifest {
	
	public static final int CANIS=1;
	public static final int FELIX=2;
	public static final int SWORDSMAN=3;
	public static final int MAGE=4;
	public static final int HEALER=5;
	public static final int ARCHER=6;
	public static final int SPEARWOMANRED=7;
	public static final int FIGHTER=9;
	public static final int ZORO=10;
	private static Rank canis;
	private static Rank felix;
	private static Rank spearWoman;
	private static Rank spearWomanRed;
	private static Rank fighter;
	private static Rank axeDudeEnemy;
	private static Rank swordsMan;
	private static Rank mage;
	private static Rank healer;
	private static Rank archer;
	private static Rank zoro;
	
	public static void init(JLayeredPane g){
		canis=new Canis(g);
		fighter=new Fighter(g);
		swordsMan=new SwordsMan(g);
		spearWomanRed=new SpearWomanRed(g);
		axeDudeEnemy=new AxeDudeEnemy(g);
		zoro=new Zoro(g);
		
		/*
		mage=new Mage(g);
		healer=new Healer(g);
		archer=new Archer(g);
		*/
	}
	
	public static Rank translate(int t){
		switch(t){
		case CANIS:
			return canis;
		case FIGHTER:
			return fighter;
		case SWORDSMAN:
			return swordsMan;
		case SPEARWOMANRED:
			return spearWomanRed;
		case ZORO:
			return zoro;
		
		default:
			return spearWoman;
		}
	}
}
