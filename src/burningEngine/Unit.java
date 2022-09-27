package burningEngine;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import burningevents.GameEvent;

public class Unit {
	private Rank rank;
	private Weapon weapon;
	private boolean armed;
	private JLabel image;
	private Bar HPBar;
	private boolean barShowing;
	private Point point;
	private JLayeredPane game;
	private Animation active;
	private Tile tile;
	private boolean hasTile;
	private boolean hasTurn;
	private boolean moving;
	private int[][] velocity;
	private int frame;
	private int maxFrames;
	private int previousX=0, previousY=0;
	int xPosition=0, yPosition=0; /*used directly?*/
	private int xVelocity=0, yVelocity=0;
	private char[] moveOrder;//end all move orders in 'n' (or, not u d l or r)
	private int moveOrderNumber;
	private int moveOrderCounter;
	
	private boolean alive;
	private int level;
	private int exp;
	private int maxMove;
	private int move;
	private int maxHP;
	private int hitpoints;
	private int strength;
	private int defense;
	private int speed;
	private int skill;
	private int luck;
	private int magic;
	private int resistance;
	private StatChanges statChanges;
	
	private String[] talkTargets;
	
	private Behavior behavior;
	private GameEvent event;
	
	private int faction;
	public static final int UNSET=-1;
	public static final int PLAYER=0;
	public static final int ENEMY=1;
	public static final int ALLY=2;
	public static final int OTHER=3;
	
	public Unit(Rank r, JLayeredPane g){
		moveOrder=new char[1];
		moveOrder[0]='n';
		moveOrderNumber=0;
		moveOrderCounter=0;
		
		rank=r;
		alive=true;
		hasTile=false;
		moving=false;
		
		level=1;
		exp=0;
		maxMove=rank.getMove();
		setMove(maxMove);
		maxHP=rank.baseHitpoints;
		hitpoints=maxHP;
		strength=rank.baseStrength;
		defense=rank.baseDefense;
		speed=rank.baseSpeed;
		magic=rank.baseMagic;
		skill=rank.baseSkill;
		resistance=rank.baseResistance;
		luck=rank.baseLuck;
		statChanges=new StatChanges();
		
		talkTargets=new String[0];
		
		armed=false;
		
		faction=UNSET;
		game=g;
		frame=0;
		image=new JLabel();
		game.add(image, JLayeredPane.PALETTE_LAYER);
		setActiveAnimation(rank.getIdle());
		velocity=active.velocity;
		HPBar=new Bar(0, 0, 44, 5, hitpoints, maxHP, new Color(0, 255, 0));
		game.add(HPBar, new Integer(101));/*1 more than Palette layer*/
		maxFrames=active.maxFrames;
		point=new Point();
	}
	
	public void setBehavior(Behavior b){
		behavior=b;
	}
	public Behavior getBehavior(){
		return behavior;
	}
	
	public void setEvent(GameEvent e){
		event=e;
	}
	public GameEvent getEvent(){
		return event;
	}
	
	public void setMoveOrder(char[] c){
		moveOrder=c;
		moveOrderNumber=0;
		moveOrderCounter=0;
		image.setLocation(tile.xLocation, tile.yLocation);
		moving=true;
	}
	public boolean isMoving(){
		return moving;
	}
	public Rank getRank(){
		return rank;
	}
	public int getXPosition(){
		return xPosition;
	}
	public int getYPosition(){
		return yPosition;
	}
	public Tile getTile(){
		return tile;
	}
	public Animation getIdle(){
		if(hasTurn)
			return rank.getIdle();
		return rank.getGreyIdle();
	}
	public Animation getBattleIdle(){
		return rank.getBattleIdle();
	}
	public Animation getAttack(){
		return rank.getAttack();
	}
	public Animation getCritical(){
		return rank.getCritical();
	}
	public void equipWeapon(Weapon w){
		weapon=w;
		armed=true;
	}
	public void unequipWeapon(){
		armed=false;
	}
	public void breakWeapon(){
		armed=false;
	}
	public Weapon getWeapon(){
		return weapon;
	}
	public boolean isArmed(){
		return armed;
	}
	
	public int getHitpoints(){
		return hitpoints;
	}
	public int getMight(){
		if(!armed)
			return 0;
		if(weapon.physical)
			return strength+weapon.power;
		return magic+weapon.power;
	}
	public int getAccuracy(){
		return skill*2+luck+weapon.accuracy;
	}
	public int getSpeed(){
		if(!armed)
			return speed;
		if(weapon.weight>strength)
			return speed+strength-weapon.weight;
		return speed;
	}
	public void setSpeed(int x){
		speed=x;
	}
	public int getDefense(){//add armor later?
		return defense;
	}
	public void setDefense(int x){
		defense=x;
	}
	public int[] getAllRanges(){
		int[] x={1};
		return x;//Will be set of ranges of all weapons
	}
	public int getMaxRange(){
		return 1;//Returns highest range of all weapons, regardless of minimum range
	}
	public void takeDamage(int d){
		hitpoints=hitpoints-d;
		HPBar.changeCurrent(-d);
		//HPBar.setBackground(new Color((int)(255-255*HPBar.getPercentage()), (int)(255*HPBar.getPercentage()), 0));
		if(hitpoints<=0)
			die();
	}
	public void die(){
		alive=false;
	}
	
	public boolean canTalk(Unit u){
		String[] targets=u.getTalkTargets();
		for(int i=0; i<targets.length; i++){
			if(getName().equals(targets[i])){
				return true;
			}
		}
		return false;
	}
	public String[] getTalkTargets(){
		return talkTargets;
	}
	public void clearTalkTargets(){
		talkTargets=new String[0];
	}
	public void setTalkTargets(String[] targets){
		talkTargets=targets;
	}
	
	//Method for gaining xp through combat
	public void gainCombatExp(Unit target, int flag){
		/* flag:
		 * 1 = Combat: kill
		 * 2 = Combat: Deal damage
		 * 3 = Combat: No damage dealt
		 */
		
		if(level>=rank.maxLevel) return;//cannot gain exp if max level
		switch (flag){
			case 1:
				exp+=100+target.level;
				statChanges.setExp(100);
				break;
			case 2:
				exp+=30;
				statChanges.setExp(30);
				break;
			case 3:
				exp+=1;
				statChanges.setExp(1);
				break;
		}
		
		statChanges.reset();
		while(exp>=100){
			levelUp();
			exp-=100;
		}
		
	}
	
	//Method for gaining xp through support actions, eg healing. Exp based on action taken rather than target.
	public void gainSupportExp(){

		if(level>=rank.maxLevel) return;//cannot gain exp if max level
		
		statChanges.reset();
		while(exp>=100){
			exp-=100;
			levelUp();
		}
	}
	
	public void levelUp(){
		if(level>=rank.maxLevel) return;//cannot gain level up if max level. This return should never happen (except >100 xp on level before max).
		Random rand=new Random();
		int tempGrowth;
		level+=1;
		statChanges.addLevel();
		if(level==rank.maxLevel){
			statChanges.setExp(statChanges.getExp()-exp);
			exp=0;
		}
		
		tempGrowth=rank.hpGrowth;
		while(maxHP<rank.maxHitpoints && tempGrowth>=100){
			maxHP+=1;
			statChanges.addMaxHP();
			tempGrowth-=100;
		}
		if(maxHP<rank.maxHitpoints && rand.nextInt(100)<tempGrowth){
			maxHP+=1;
			statChanges.addMaxHP();
		}
		
		tempGrowth=rank.strengthGrowth;
		while(strength<rank.maxStrength && tempGrowth>=100){
			strength+=1;
			statChanges.addStrength();
			tempGrowth-=100;
		}
		if(strength<rank.maxStrength && rand.nextInt(100)<tempGrowth){
			strength+=1;
			statChanges.addStrength();
		}
		
		tempGrowth=rank.defenseGrowth;
		while(defense<rank.maxDefense && tempGrowth>=100){
			defense+=1;
			statChanges.addDefense();
			tempGrowth-=100;
		}
		if(defense<rank.maxDefense && rand.nextInt(100)<tempGrowth){
			defense+=1;
			statChanges.addDefense();
		}
		
		tempGrowth=rank.speedGrowth;
		while(speed<rank.maxSpeed && tempGrowth>=100){
			speed+=1;
			statChanges.addSpeed();
			tempGrowth-=100;
		}
		if(speed<rank.maxSpeed && rand.nextInt(100)<tempGrowth){
			speed+=1;
			statChanges.addSpeed();
		}
		
		tempGrowth=rank.skillGrowth;
		while(skill<rank.maxSkill && tempGrowth>=100){
			skill+=1;
			statChanges.addSkill();
			tempGrowth-=100;
		}
		if(skill<rank.maxSkill && rand.nextInt(100)<tempGrowth){
			skill+=1;
			statChanges.addSkill();
		}
		
		tempGrowth=rank.luckGrowth;
		while(luck<rank.maxLuck && tempGrowth>=100){
			luck+=1;
			statChanges.addLuck();
			tempGrowth-=100;
		}
		if(luck<rank.maxLuck && rand.nextInt(100)<tempGrowth){
			luck+=1;
			statChanges.addLuck();
		}
		
		tempGrowth=rank.magicGrowth;
		while(magic<rank.maxMagic && tempGrowth>=100){
			magic+=1;
			statChanges.addMagic();
			tempGrowth-=100;
		}
		if(magic<rank.maxMagic && rand.nextInt(100)<tempGrowth){
			magic+=1;
			statChanges.addMagic();
		}
		
		tempGrowth=rank.resistanceGrowth;
		while(resistance<rank.maxResistance && tempGrowth>=100){
			resistance+=1;
			statChanges.addResistance();
			tempGrowth-=100;
		}
		if(resistance<rank.maxResistance && rand.nextInt(100)<tempGrowth){
			resistance+=1;
			statChanges.addResistance();
		}
	}
	
	public boolean isAlive(){
		return alive;
	}
	public void resurrect(Tile t){
		alive=true;
		tile=t;
		t.setUnit(this);
	}
	
	public int getFaction(){
		return faction;
	}
	public void setFaction(int f){
		faction=f;
		if(faction==ENEMY && active.equals(rank.getIdle())){
			refreshIdleAnimation();
		}
	}
	public boolean isSameFaction(Unit u){
		if(u.getFaction()==faction)
			return true;
		return false;
	}
	public boolean isWall(Unit u){
		if(faction==ENEMY&&u.getFaction()!=ENEMY)
			return true;
		if(faction!=ENEMY&&u.getFaction()==ENEMY)
			return true;
		return false;
	}
	public Animation getActiveAnimation(){
		return active;
	}
	public void animate(){
		if(!rank.isEmptySpace()){
		if(frame>=maxFrames)
			frame=0;
		xVelocity=active.velocity[frame][0];
		yVelocity=active.velocity[frame][1];
		if(moving){
			switch(moveOrder[moveOrderNumber]){
				case 'u':
					moveY(-10);
					moveOrderCounter++;
					if(faction==PLAYER)
						setActiveAnimation(rank.getUp(), frame);
					else
						setActiveAnimation(rank.getEnemyUp(), frame);
					break;
				case 'd':
					moveY(10);
					moveOrderCounter++;
					if(faction==PLAYER)
						setActiveAnimation(rank.getDown(), frame);
					else
						setActiveAnimation(rank.getEnemyDown(), frame);
					break;
				case 'r':
					moveX(10);
					moveOrderCounter++;
					if(faction==PLAYER)
						setActiveAnimation(rank.getRight(), frame);
					else
						setActiveAnimation(rank.getEnemyRight(), frame);
					break;
				case 'l':
					moveX(-10);
					moveOrderCounter++;
					if(faction==PLAYER)
						setActiveAnimation(rank.getLeft(), frame);
					else
						setActiveAnimation(rank.getEnemyLeft(), frame);
					break;
				case 'n':
					xPosition=tile.xLocation;/*just in case*/
					yPosition=tile.yLocation;
					if(faction==PLAYER)
						setActiveAnimation(rank.getIdle(), frame);
					else
						setActiveAnimation(rank.getEnemyIdle(), frame);
					moving=false;
					break;
			}
			if(moveOrderCounter==5){
				moveOrderCounter=0;
				moveOrderNumber++;
				if(moveOrderNumber==moveOrder.length){
					moveOrderNumber=0;
					moveOrder[0]='n';
				}
			}
		}
		image.setIcon(active.getImage(frame));
		increment();
		}
	}
	public void displayStatic(){
		image.setIcon(active.getImage(frame));
		//image.setSize(image.getPreferredSize());
		//image.repaint();	
	}
	public void increment(){
		point=image.getLocation();
		point.setLocation(point.getX()+active.getXVelocity(), point.getY()+active.getYVelocity());
		image.setLocation(point);
		frame++;
		
	}
	public void moveX(int x){
		point=image.getLocation();
		point.setLocation(point.getX()+x, point.getY());
		image.setLocation(point);
		HPBar.setLocation((int)point.getX()+3, (int)point.getY()+40);
		xPosition=xPosition+x;
	}
	public void moveY(int y){
		point=image.getLocation();
		point.setLocation(point.getX(), point.getY()+y);
		image.setLocation(point);
		HPBar.setLocation((int)point.getX()+3, (int)point.getY()+40);
		yPosition=yPosition+y;
	}
	public void setLocation(int x, int y){
		//System.out.println(x+","+y);
		point.move(x, y);
		image.setLocation(point);
		HPBar.setLocation((int)point.getX()+3, (int)point.getY()+40);
		xPosition=x;
		yPosition=y;
	}
	public void setActiveAnimation(Animation a){
		active=a;
		frame=0;
		maxFrames=active.maxFrames;
		image.setIcon(active.getImage(frame));
		image.setSize(image.getPreferredSize());
	}
	public void setActiveAnimation(Animation a, int f){
		active=a;
		frame=f;
		maxFrames=active.maxFrames;
		if(frame>=active.maxFrames){
			frame=0;
		}
		image.setIcon(active.getImage(frame));
		image.setSize(image.getPreferredSize());
	}
	public void refreshIdleAnimation(){
		if(faction==PLAYER)
			setActiveAnimation(rank.getIdle());
		else
			setActiveAnimation(rank.getEnemyIdle());
	}
	public void setTile(Tile t){
		tile=t;
		hasTile=true;
	}
	public boolean hasTile(){
		return hasTile;
	}
	public void setVisible(boolean t){
		image.setVisible(t);
		HPBar.setVisible(t);
	}
	public void showBar(boolean t){
		HPBar.setVisible(t);
		barShowing=t;
	}
	public boolean isBarShowing(){
		return barShowing;
	}
	public void removeSelf(){
		tile.removeUnit();
		game.remove(image);
		game.remove(HPBar);
		hasTile=false;
	}
	
	public BufferedImage createResizedCopy(Image originalImage, 
            int scaledWidth, int scaledHeight, 
            boolean preserveAlpha)
{
    //System.out.println("resizing...");
    int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
    Graphics2D g = scaledBI.createGraphics();
    if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
    }
    g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
    g.dispose();
    return scaledBI;
}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getMove() {
		return move;
	}
	public void setMove(int move) {
		this.move = move;
	}
	public void reduceMove(int x){
		move=move-x;
	}
	public int getMaxMove(){
		return maxMove;
	}
	public void setMaxMove(int maxMove){
		this.maxMove=maxMove;
	}
	public int getMaxHP(){
		return maxHP;
	}
	public void setMaxHP(int x){
		maxHP=x;
		HPBar.setMax(x);
		//HPBar.setBackground(new Color((int)(255-255*HPBar.getPercentage()), (int)(255*HPBar.getPercentage()), 0));
	}
	public void setHitpoints(int x){
		hitpoints=x;
		HPBar.setCurrent(x);
		//HPBar.setBackground(new Color((int)(255-255*HPBar.getPercentage()), (int)(255*HPBar.getPercentage()), 0));
	}
	public int getStrength(){
		return strength;
	}
	public void setStrength(int x){
		strength=x;
	}
	public boolean hasTurn() {
		return hasTurn;
	}
	public void setHasTurn(boolean hasTurn) {
		this.hasTurn = hasTurn;
	}
	public void endTurn(){
		this.hasTurn=false;
		if(faction==PLAYER)
			setActiveAnimation(rank.getGreyIdle());
		else
			setActiveAnimation(rank.getEnemyGreyIdle());
		previousX=xPosition;
		previousY=yPosition;
		move=maxMove;
	}
	public void startTurn(){
		this.hasTurn=true;
		move=maxMove;
		if(faction==PLAYER)
			setActiveAnimation(rank.getIdle());
		else
			setActiveAnimation(rank.getEnemyIdle());
	}
	public void setPreviousX(int x){
		previousX=x;
	}
	public int getPreviousX(){
		return previousX;
	}
	public void setPreviousY(int y){
		previousY=y;
	}
	public int getPreviousY(){
		return previousY;
	}
	public void moveBack(){
		setLocation(previousX, previousY);
	}
	
	public int getNumberOfAttacks(Unit def){
		//if weapon=gun, do different calculation
		if(speed>=def.getSpeed()+4)
			return 2;
		else
			return 1;
	}
	
	
	
	public String getName(){
		return rank.getName();
	}
	public int getAttackSpeed(){
		if(weapon.weight>strength){
			return (speed-(weapon.weight-strength));
		}
		return speed;
	}
	public int getEvasion(){
		return getAttackSpeed()*2+luck+weapon.evasion;
	}
	public int getCrit(){
		return skill+weapon.crit+rank.critBonus();
	}
	public int getDeflect(){
		return luck;
	}

	public int getResistance() {
		return resistance;
	}

	public int getSkill() {
		return skill;
	}
	public int getLuck(){
		return luck;
	}
	public int getMagic(){
		return magic;
	}
	public int getArmor(){
		return defense;
	}
	public int getWard(){//Magic damage resist
		return resistance;
	}
	
}
