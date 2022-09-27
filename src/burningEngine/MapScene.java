package burningEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Pattern;
import burningevents.*;
import burningterrain.*;

import javax.swing.*;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class MapScene extends Scene{
	public static final int TILE_SIZE=50;
	public static final int WINDOW_WIDTH=4;
	public static final int WINDOW_HEIGHT=25;
	
	//Robot may cause issues with other people's computers.
	
	private BurningMedallion engine;
	//private Unit[][] units;
	private Unit[] playerUnits;//the player units that are carried through each mission
	private Unit[] mapUnits;//Units only available during a single map(both enemy and ally, but never player controlled)
	private Unit selectedUnit;
	private Tile[][] map;
	private Random random;
	
	private BattleInfoBox battleInfoBox;
	private UnitMenu unitMenu;
	private InactiveMenu inactiveMenu;
	private InspectPanel inspectPanel;
	private Animation transparentSquare;
	private Animation tileOutline;
	private Animation battleBackground;
	private Animation unitSelect;
	private Animation mapDot;
	private Animation arrow;
	private Animation blueHighlight;
	private Animation orangeHighlight;
	private Animation yellowHighlight;
	private JLabel attackEffect;
	
	private JProgressBar loadingBar;
	private Bar fade;
	private boolean fadeOut=false;//if false, fade in. if true, fade out.
	private boolean fading=false;//if false, fade is not changing. if true, it is.
	
	private BronzeSpear bronzeSpear;
	
	private BattleScene battleScene;
	private MapBattle mapBattle;
	private String[] attackDamage;
	private char[] attackOrder;
	private int livingPlayers;
	private int activePlayers;
	private int playerUnitLength;
	private int mapUnitLength;
	private int mapUnitCounter;
	private Unit mapUnitTarget;
	private boolean mapUnitHasTarget;
	private int width;//width in tiles
	private int height;//height in tiles
	private int xOffset, yOffset;
	
	private GameEvent[] beginningEvents;
	private int beginningEventsLength;
	private GameEvent[] middleEvents;
	private int middleEventsLength;
	private GameEvent[] endEvents;
	private int endEventsLength;
	private GameEvent activeEvent;
	private static final int BEGINNING=1;
	private static final int MIDDLE=2;
	private static final int END=3;
	
	private JTextArea text;
	
	private boolean nextLevel;
	private String nextMap;
	private boolean inBattle, startBattle;
	private boolean upPressed, rightPressed, leftPressed, downPressed;
	private boolean selectActive;//is true if the unitSelect animation is active
	private boolean selectedUnitActive;//is true if selecting an active unit
	
	private int turn;
	public static final int PLAYER_TURN=0;
	public static final int ENEMY_TURN=1;
	public static final int ALLY_TURN=2;
	public static final int OTHER_TURN=3;
	
	private Tile previousTile=null;
	private char[] unitPath;
	private int unitPathNum=0;
	
	private int[][] grid;
	private int[][] startSquares;//holds a list of the coordinates to start squares. Loaded in loadmap.
	
	private AttackClosest attackClosest;
	
	private int clickAction;//Action taken while clicking
	private static final int SELECT=0;
	private static final int MOVE=1;
	private static final int MENU=2;
	private static final int ATTACK=3;
	private static final int ITEM=4;
	private static final int TRADE=5;
	private static final int WAIT=6;
	private static final int MOVING_TO_MENU=7;
	private static final int ENEMY=8;
	private static final int MAP_MENU=9;
	private static final int TEXT=10;
	private static final int INSPECT=11;
	private static final int ATTACK_CONFIRM=12;
	private static final int ATTACKING=13;
	private static final int TALKING=14;
	
	private static final int LEVEL_ONLY=0;
	private static final int GIVEN_STATS=1;
	
	public int getClickAction(){
		return clickAction;
	}
	public Unit getPlayerUnit(int i){
		return playerUnits[i];
	}
	public int getPlayerUnitLength(){
		return playerUnitLength;
	}
	public void addPlayerUnit(Unit u){
		if(playerUnitLength==playerUnits.length){
			playerUnits=(Unit[]) extend(playerUnits);
		}
		u.setFaction(Unit.PLAYER);
		playerUnits[playerUnitLength]=u;
		playerUnitLength++;
	}
	public void removePlayerUnit(int number){
		if(number>=playerUnitLength)
			return;
		for(int i=number; i<playerUnitLength-1; i++){
			playerUnits[i]=playerUnits[i+1];
		}
		playerUnitLength--;
	}
	public void addEnemyUnit(Unit u){
		if(mapUnitLength==mapUnits.length){
			mapUnits=extendUnits(mapUnits);
		}
		u.setFaction(Unit.ENEMY);
		mapUnits[mapUnitLength]=u;
		mapUnitLength++;
	}
	public void removeEnemyUnit(int number){
		if(number>=mapUnitLength)
			return;
		for(int i=number; i<mapUnitLength-1; i++){
			mapUnits[i]=mapUnits[i+1];
		}
		mapUnitLength--;
	}
	public int getMapUnitLength(){
		return mapUnitLength;
	}
	public Unit[] getMapUnits(){
		return mapUnits;
	}
	public Unit getMapUnit(int i){
		return mapUnits[i];
	}
	public int getXOffset(){
		return xOffset;
	}
	public int getYOffset(){
		return yOffset;
	}
	public synchronized void displaySelf(){
		/*if(startBattle){
			inBattle=true;
			startBattle=false;
			engine.setState(3);
			battleScene=new BattleScene(game, units[0], units[1], battleBackground, new char[]{'l', 'r', 'l', 'l', 'l', 'l', 'l', 'l'}, 'n', 0);
		}*/
		
		if(nextLevel){
			nextLevel();
		}
			
		else{
			//If a map battle is occuring, continue it
			if(clickAction==ATTACKING){
				if(mapBattle.displaySelf()){
					if(!mapBattle.getAttacker().isAlive()){
						if(mapBattle.getAttacker().getFaction()==Unit.PLAYER){
							livingPlayers--;
							if(activePlayers>livingPlayers)
								activePlayers=livingPlayers;
						}
						else if(mapBattle.getDefender().getFaction()==Unit.PLAYER){
							//Player unit killed opponent
							mapBattle.getDefender().gainCombatExp(mapBattle.getAttacker(), 1);
						}
					}
					else if(!mapBattle.getDefender().isAlive()){
						if(mapBattle.getDefender().getFaction()==Unit.PLAYER){
							livingPlayers--;
							if(activePlayers>livingPlayers)
								activePlayers=livingPlayers;
						}
						else if(mapBattle.getAttacker().getFaction()==Unit.PLAYER){
							//Player unit killed opponent
							mapBattle.getAttacker().gainCombatExp(mapBattle.getDefender(), 1);
						}
					}
					else{//Neither died, award combat xp
						if(mapBattle.getAttacker().getFaction()==Unit.PLAYER){
							mapBattle.getAttacker().gainCombatExp(mapBattle.getDefender(), 2);
						}
						else if(mapBattle.getDefender().getFaction()==Unit.PLAYER){
							mapBattle.getDefender().gainCombatExp(mapBattle.getAttacker(), 2);
						}
					}
					inBattle=false;
					//game.setLocation(-xOffset, -yOffset);
					engine.setState(2);
					if(turn==PLAYER_TURN)
						clickAction=SELECT;
					else{
						clickAction=ENEMY;
					}
				}
			}
			
			else if(turn==ENEMY_TURN){
				doEnemyTurn();
			}
			
			else if(activePlayers<=0){
				endTurn();
			}
			
			if(clickAction==MOVING_TO_MENU){
				if(!selectedUnit.isMoving()){
					selectUnit(selectedUnit.getXPosition()/TILE_SIZE, selectedUnit.getYPosition()/TILE_SIZE, selectedUnit);
				}
			}
			
			if(clickAction==MOVE){
				doMove();
			}
			
			for(int i=0; i<40; i++){
				if(downPressed&&yOffset<(height*TILE_SIZE-600)){
					yOffset+=1;
				}
				if(leftPressed&&xOffset>0){
					xOffset-=1;
				}
				if(upPressed&&yOffset>0){
					yOffset-=1;
				}
				if(rightPressed&&xOffset<(width*TILE_SIZE-800)){
					xOffset+=1;
				}
				game.setLocation(-xOffset, -yOffset);
			}
			for(int i=0; i<playerUnitLength; i++){
				playerUnits[i].animate();
			}
			for(int i=0; i<mapUnitLength; i++){
				mapUnits[i].animate();
			}
		}
		
		/*for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				map[i][j].displayBackgrounds();
			}
		}*/
		
		//update fade
		if(fadeOut&&fade.getAlpha()<255){//screen goes black
			if(!fading){
				fade.setVisible(true);
				fading=true;
			}
			if(fade.getAlpha()>245)
				fade.setAlpha(255);
			else
				fade.setAlpha(fade.getAlpha()+10);
		}
		if(!fadeOut&&fade.getAlpha()>0){//screen unblacks
			if(!fading){
				fade.setVisible(true);
				fading=true;
			}
			if(fade.getAlpha()<10)
				fade.setAlpha(0);
			else
				fade.setAlpha(fade.getAlpha()-10);
			if(fade.getAlpha()==0){
				fade.setVisible(false);
				fading=false;
			}
		}
		
	}
	
	public void doMove(){
		Point tempPoint=MouseInfo.getPointerInfo().getLocation();
		int tempX=(int) (tempPoint.getX()-engine.getLocationOnScreen().getX())-WINDOW_WIDTH+xOffset;
		int tempY=(int) (tempPoint.getY()-engine.getLocationOnScreen().getY())-WINDOW_HEIGHT+yOffset;
		tempX/=TILE_SIZE;
		tempY/=TILE_SIZE;
		if(tempX<0)
			tempX=0;
		if(tempY<0)
			tempY=0;
		if(tempX>=width)
			tempX=width-1;
		if(tempY>=height)
			tempY=height-1;
		if(tempX==previousTile.xLocation/TILE_SIZE&&tempY==previousTile.yLocation/TILE_SIZE){
			//same tile
		}
		else if(!map[tempX][tempY].isAvailable()){
			//cannot move here cause not available
		}
		else if(map[tempX][tempY].equals(selectedUnit.getTile())){
			//tile containing current unit
			for(int i=0; i<unitPath.length; i++){
				unitPath[i]='n';
			}
			unitPathNum=0;
			removePaths();
		}
		else if(map[tempX][tempY].getPath().isVisible()&&!map[tempX][tempY].equals(previousTile)){
			//moved onto an already filled square
			previousTile=map[tempX][tempY];
			truncatePath(map[tempX][tempY]);
		}
		else if(tempX==previousTile.xLocation/TILE_SIZE+1&&tempY==previousTile.yLocation/TILE_SIZE&&unitPathNum<unitPath.length){//1 tile to the right
			//previousTile=map[tempX][tempY];
			selectedUnit.reduceMove(map[tempX][tempY].getCost('f'));
			unitPath[unitPathNum]='r';
			map[tempX][tempY].setPreviousDirection('r');
			unitPathNum++;
		}
		else if(tempX==previousTile.xLocation/TILE_SIZE-1&&tempY==previousTile.yLocation/TILE_SIZE&&unitPathNum<unitPath.length){//1 tile to the left
			//previousTile=map[tempX][tempY];
			selectedUnit.reduceMove(map[tempX][tempY].getCost('f'));
			unitPath[unitPathNum]='l';
			map[tempX][tempY].setPreviousDirection('l');
			unitPathNum++;
		}
		else if(tempY==previousTile.yLocation/TILE_SIZE-1&&tempX==previousTile.xLocation/TILE_SIZE&&unitPathNum<unitPath.length){//1 tile up
			//previousTile=map[tempX][tempY];
			selectedUnit.reduceMove(map[tempX][tempY].getCost('f'));
			unitPath[unitPathNum]='u';
			map[tempX][tempY].setPreviousDirection('u');
			unitPathNum++;
		}
		else if(tempY==previousTile.yLocation/TILE_SIZE+1&&tempX==previousTile.xLocation/TILE_SIZE&&unitPathNum<unitPath.length){//1 tile down
			//previousTile=map[tempX][tempY];
			selectedUnit.reduceMove(map[tempX][tempY].getCost('f'));
			unitPath[unitPathNum]='d';
			map[tempX][tempY].setPreviousDirection('d');
			unitPathNum++;
		}
		else if(map[tempX][tempY].isAvailable()){
			constructPath(map[tempX][tempY]);
		}
		else{
			previousTile=selectedUnit.getTile();
			for(int i=0; i<unitPath.length; i++)
				unitPath[i]='n';
			unitPathNum=0;
		}
		if(selectedUnit.getMove()<0){
			constructPath(map[tempX][tempY]);
		}
		else if(!map[tempX][tempY].getPath().isVisible()&&map[tempX][tempY].isAvailable()){
			if(selectedUnit.getFaction()==Unit.PLAYER){
				switch(map[tempX][tempY].getPreviousDirection()){
				case 'u':
					map[tempX][tempY].changePath(arrow, ArrowValues.UP);
					if(previousTile.getPreviousDirection()=='r')
						previousTile.changePath(arrow, ArrowValues.UPLEFT);
					else if(previousTile.getPreviousDirection()=='u')
						previousTile.changePath(arrow, ArrowValues.UPDOWN);
					else if(previousTile.getPreviousDirection()=='l')
						previousTile.changePath(arrow, ArrowValues.UPRIGHT);
					break;
				case 'r':
					map[tempX][tempY].changePath(arrow, ArrowValues.RIGHT);
					if(previousTile.getPreviousDirection()=='u')
						previousTile.changePath(arrow, ArrowValues.DOWNRIGHT);
					else if(previousTile.getPreviousDirection()=='d')
						previousTile.changePath(arrow, ArrowValues.UPRIGHT);
					else if(previousTile.getPreviousDirection()=='r')
						previousTile.changePath(arrow, ArrowValues.LEFTRIGHT);
					break;
				case 'd':
					map[tempX][tempY].changePath(arrow, ArrowValues.DOWN);
					if(previousTile.getPreviousDirection()=='d')
						previousTile.changePath(arrow, ArrowValues.UPDOWN);
					else if(previousTile.getPreviousDirection()=='r')
						previousTile.changePath(arrow, ArrowValues.DOWNLEFT);
					else if(previousTile.getPreviousDirection()=='l')
						previousTile.changePath(arrow, ArrowValues.DOWNRIGHT);
					break;
				case 'l':
					map[tempX][tempY].changePath(arrow, ArrowValues.LEFT);
					if(previousTile.getPreviousDirection()=='d')
						previousTile.changePath(arrow, ArrowValues.UPLEFT);
					else if(previousTile.getPreviousDirection()=='l')
						previousTile.changePath(arrow, ArrowValues.LEFTRIGHT);
					else if(previousTile.getPreviousDirection()=='u')
						previousTile.changePath(arrow, ArrowValues.DOWNLEFT);
					break;
				}
				previousTile=map[tempX][tempY];
			}
		}
	}
	
	public void doEnemyTurn(){
		if(mapUnitCounter<mapUnitLength){
			if(!mapUnits[mapUnitCounter].isAlive()){
				mapUnitCounter++;
			}
			else if(mapUnits[mapUnitCounter].isMoving()){
				
			}
			else if(!mapUnitHasTarget){
				mapUnitTarget=mapUnits[mapUnitCounter].getBehavior().findTarget(mapUnits[mapUnitCounter]);
				if(!mapUnitTarget.equals(mapUnits[mapUnitCounter])){
					mapUnitHasTarget=true;
				}
				else{
					mapUnitCounter++;
					removeHighlights();
				}
			}
			else if(mapUnitHasTarget){
				attack(mapUnits[mapUnitCounter], mapUnitTarget);
				mapUnitHasTarget=false;
				mapUnitCounter++;
				removeHighlights();
			}
		}
		else
			endTurn();
	}
	
	public void endTurn(){
		if(turn==PLAYER_TURN){
			mapUnitHasTarget=false;
			mapUnitCounter=0;
			turn=ENEMY_TURN;
			clickAction=ENEMY;
			System.out.println("End events length: "+endEventsLength);
			for(int i=0; i<endEventsLength; i++){
				System.out.println("Triggering ends");
				if(endEvents[i].trigger())
					removeEvent(END, i--);
			}
			for(int i=0; i<playerUnitLength; i++){
				playerUnits[i].startTurn();
			}
			activePlayers=livingPlayers;
		}
		else if(turn==ENEMY_TURN){
			turn=PLAYER_TURN;
			for(int i=0; i<mapUnitLength; i++){
				mapUnits[i].startTurn();
			}
			if(clickAction!=TEXT)
				clickAction=SELECT;
		}
	}
	public void clearPath(){
		int x=selectedUnit.getXPosition()/TILE_SIZE, y=selectedUnit.getYPosition()/TILE_SIZE;
		int i=0;
		for(; i<unitPath.length; i++){
			if(unitPath[i]=='u')
				y--;
			else if(unitPath[i]=='d')
				y++;
			else if(unitPath[i]=='r')
				x++;
			else if(unitPath[i]=='l')
				x--;
			unitPath[i]='n';
			//setting a path's visibility to false causes a repaint, changing it to a transparent square doesn't.
			map[x][y].changePath(transparentSquare, 0);
			//Flags the path for removal, as it should display no path, not a transparent path.
			map[x][y].flagPath();
		}
		selectedUnit.getTile().removePath();
		selectedUnit.setMove(selectedUnit.getMaxMove());
	}
	public void truncatePath(Tile endTile){
		int x=selectedUnit.getXPosition()/TILE_SIZE, y=selectedUnit.getYPosition()/TILE_SIZE;
		int endX=endTile.xLocation/TILE_SIZE, endY=endTile.yLocation/TILE_SIZE;
		int i;
		for(i=0; i<unitPath.length; i++){
			if(unitPath[i]=='u')
				y--;
			else if(unitPath[i]=='d')
				y++;
			else if(unitPath[i]=='r')
				x++;
			else if(unitPath[i]=='l')
				x--;
			if(x==endX&&y==endY){
				unitPathNum=i+1;
				i++;
				break;
			}
		}
		for(; i<unitPath.length; i++){
			if(unitPath[i]=='u')
				y--;
			else if(unitPath[i]=='d')
				y++;
			else if(unitPath[i]=='r')
				x++;
			else if(unitPath[i]=='l')
				x--;
			if(unitPath[i]!='n')
				selectedUnit.reduceMove(0-map[x][y].getCost('f'));
			unitPath[i]='n';
			map[x][y].removePath();
		}
		switch(endTile.getPreviousDirection()){
		case 'u':
			endTile.changePath(arrow, ArrowValues.UP);
			break;
		case 'r':
			endTile.changePath(arrow, ArrowValues.RIGHT);
			break;
		case 'd':
			endTile.changePath(arrow, ArrowValues.DOWN);
			break;
		case 'l':
			endTile.changePath(arrow, ArrowValues.LEFT);
			break;
		}
	}
	public void constructPath(Tile t, Unit u){/*0 movement squares will mess this up if made*/
		int currentX, currentY, prevX, prevY;
		char direction='n';
		char prevDirection='n';
		previousTile=t;
		unitPath=new char[u.getMaxMove()];
		unitPathNum=0;
		u.setMove(u.getMaxMove());
		removePaths();
		for(int i=0; i<unitPath.length; i++){
			unitPath[i]='n';
		}
		for(int i=t.getChain()-1; i>-1; i--){
			if(i>=unitPath.length)
				i=unitPath.length-1;
			currentX=t.xLocation/TILE_SIZE;
			currentY=t.yLocation/TILE_SIZE;
			prevX=t.getPrevious().xLocation/TILE_SIZE;
			prevY=t.getPrevious().yLocation/TILE_SIZE;
			u.setMove(u.getMove()-map[currentX][currentY].getCost('f'));
			if(u.getFaction()==Unit.PLAYER)
				map[currentX][currentY].changePath(mapDot, 0);
			if(currentX==prevX+1&&currentY==prevY){
				unitPath[i]='r';
				prevDirection='r';
				unitPathNum++;
			}
			else if(currentX==prevX-1&&currentY==prevY){
				unitPath[i]='l';
				prevDirection='l';
				unitPathNum++;
			}
			else if(currentY==prevY+1&&currentX==prevX){
				unitPath[i]='d';
				prevDirection='d';
				unitPathNum++;
			}
			else if(currentY==prevY-1&&currentX==prevX){
				unitPath[i]='u';
				prevDirection='u';
				unitPathNum++;
			}
			else{
				unitPath[i]='n';
				prevDirection='n';
				unitPathNum++;
			}
			direction=prevDirection;
			t=t.getPrevious();
		}
		
	}
	public void constructPath(Tile t){
		int currentX, currentY, prevX, prevY;
		char direction='n';
		char prevDirection='n';
		previousTile=t;
		unitPathNum=0;
		selectedUnit.setMove(selectedUnit.getMaxMove());
		//removePaths(t.xLocation/TILE_SIZE-selectedUnit.getMaxMove(), t.yLocation/TILE_SIZE-selectedUnit.getMaxMove(), t.xLocation/TILE_SIZE+selectedUnit.getMaxMove(), t.yLocation/TILE_SIZE+selectedUnit.getMaxMove());
		clearPath();
		for(int i=0; i<unitPath.length; i++){
			unitPath[i]='n';
		}
		for(int i=t.getChain()-1; i>-1; i--){
			currentX=t.xLocation/TILE_SIZE;
			currentY=t.yLocation/TILE_SIZE;
			prevX=t.getPrevious().xLocation/TILE_SIZE;
			prevY=t.getPrevious().yLocation/TILE_SIZE;
			selectedUnit.setMove(selectedUnit.getMove()-map[currentX][currentY].getCost('f'));
			/*if(selectedUnit.getFaction()==Unit.PLAYER)
				map[currentX][currentY].changePath(mapDot, 0);*/
			if(currentX==prevX+1&&currentY==prevY){
				unitPath[i]='r';
				prevDirection='r';
				unitPathNum++;
			}
			else if(currentX==prevX-1&&currentY==prevY){
				unitPath[i]='l';
				prevDirection='l';
				unitPathNum++;
			}
			else if(currentY==prevY+1&&currentX==prevX){
				unitPath[i]='d';
				prevDirection='d';
				unitPathNum++;
			}
			else if(currentY==prevY-1&&currentX==prevX){
				unitPath[i]='u';
				prevDirection='u';
				unitPathNum++;
			}
			else{
				unitPath[i]='n';
				prevDirection='n';
				unitPathNum++;
			}
			if(selectedUnit.getFaction()==Unit.PLAYER)
			switch(prevDirection){
			case 'u':
				if(direction=='n')
					map[currentX][currentY].changePath(arrow, ArrowValues.UP);
				else if(direction=='r')
					map[currentX][currentY].changePath(arrow, ArrowValues.DOWNRIGHT);
				else if(direction=='u')
					map[currentX][currentY].changePath(arrow, ArrowValues.UPDOWN);
				else if(direction=='l')
					map[currentX][currentY].changePath(arrow, ArrowValues.DOWNLEFT);
				break;
			case 'r':
				if(direction=='n')
					map[currentX][currentY].changePath(arrow, ArrowValues.RIGHT);
				else if(direction=='u')
					map[currentX][currentY].changePath(arrow, ArrowValues.UPLEFT);
				else if(direction=='d')
					map[currentX][currentY].changePath(arrow, ArrowValues.DOWNLEFT);
				else if(direction=='r')
					map[currentX][currentY].changePath(arrow, ArrowValues.LEFTRIGHT);
				break;
			case 'd':
				if(direction=='n')
					map[currentX][currentY].changePath(arrow, ArrowValues.DOWN);
				else if(direction=='d')
					map[currentX][currentY].changePath(arrow, ArrowValues.UPDOWN);
				else if(direction=='r')
					map[currentX][currentY].changePath(arrow, ArrowValues.UPRIGHT);
				else if(direction=='l')
					map[currentX][currentY].changePath(arrow, ArrowValues.UPLEFT);
				break;
			case 'l':
				if(direction=='n')
					map[currentX][currentY].changePath(arrow, ArrowValues.LEFT);
				else if(direction=='d')
					map[currentX][currentY].changePath(arrow, ArrowValues.DOWNRIGHT);
				else if(direction=='l')
					map[currentX][currentY].changePath(arrow, ArrowValues.LEFTRIGHT);
				else if(direction=='u')
					map[currentX][currentY].changePath(arrow, ArrowValues.UPRIGHT);
				break;
			}
			t.setPreviousDirection(prevDirection);
			direction=prevDirection;
			t=t.getPrevious();
		}
		removeFlags();
		
	}
	public void highlightAttack(Tile t){//highlights the attackable squares radiating from Tile t. (also sets to attackable)
		//if say an archer (attack range only 2) can't hit a tile, see if that tile can hit a square that's "available".
		int x=t.xLocation/TILE_SIZE, y=t.yLocation/TILE_SIZE, range=1;//TODO range=weapon.getRange()
		highlightAttack(t, x-1, y, range-1);
		highlightAttack(t, x+1, y, range-1);
		highlightAttack(t, x, y-1, range-1);
		highlightAttack(t, x, y+1, range-1);	
	}
	public void highlightAttack(Tile t, int x, int y, int range){
		//TODO if map[x][y] is a wall, return.
		if(x==selectedUnit.getXPosition()/TILE_SIZE&&y==selectedUnit.getYPosition()/TILE_SIZE)//tile contains the attacking unit
			return;
		if(x<0||y<0||x>=width||y>=height)//out of bounds
			return;
		if(map[x][y].isAttackable()||map[x][y].isAvailable())//already highlighted
			return;
		if(!map[x][y].hasUnit())//Highlight, but don't set as attackable if no unit
		{
			map[x][y].changeHighlight(orangeHighlight,  0, 0.5f);
			return;
		}
		if(selectedUnit.getFaction()!=map[x][y].getUnit().getFaction() && inRange(selectedUnit, map[x][y], t)){
			map[x][y].changeHighlight(orangeHighlight,  0, 0.5f);
			map[x][y].setAttackable(true);
		}
			
		if(range<=0){
			return;
		}
		highlightAttack(t, x-1, y, range-1);
		highlightAttack(t, x+1, y, range-1);
		highlightAttack(t, x, y-1, range-1);
		highlightAttack(t, x, y+1, range-1);
	}
	public void highlightAttackableEnemies(Unit u){
		int x=u.getXPosition()/TILE_SIZE, y=u.getYPosition()/TILE_SIZE, range=1;//TODO range=weapon.getRange()
		highlightAttackableEnemies(u, x-1, y, range-1);
		highlightAttackableEnemies(u, x+1, y, range-1);
		highlightAttackableEnemies(u, x, y-1, range-1);
		highlightAttackableEnemies(u, x, y+1, range-1);
	}
	public void highlightAttackableEnemies(Unit u, int x, int y, int range){
		//TODO if map[x][y] is a wall, return.
		if(x==selectedUnit.getXPosition()/TILE_SIZE&&y==selectedUnit.getYPosition()/TILE_SIZE)//tile contains the attacking unit
			return;
		if(x<0||y<0||x>=width||y>=height)//out of bounds
			return;
		if(map[x][y].isAttackable()||map[x][y].isAvailable())//already highlighted
			return;
		if(map[x][y].hasUnit()&&inRange(selectedUnit, map[x][y], u.getTile())){
			map[x][y].changeHighlight(orangeHighlight,  0, 0.5f);
			map[x][y].setAttackable(true);
		}
		if(map[x][y].hasUnit()&&selectedUnit.canTalk(map[x][y].getUnit())){
			map[x][y].changeHighlight(yellowHighlight, 0, 0.5f);
			map[x][y].setTalkable(true);
			System.out.println("bleh");
		}
		if(range<=0){
			return;
		}
		highlightAttackableEnemies(u, x-1, y, range-1);
		highlightAttackableEnemies(u, x+1, y, range-1);
		highlightAttackableEnemies(u, x, y-1, range-1);
		highlightAttackableEnemies(u, x, y+1, range-1);
	}
	public void highlightSingleAttack(Tile t){//highlights this tile and sets to attackable.
		if(t.hasUnit())
			if(t.getUnit().getFaction()!=selectedUnit.getFaction()){//should be if t's unit is not on the same team as selectedUnit
				t.changeHighlight(orangeHighlight, 0, 0.5f);
				t.setAttackable(true);
			}
	}
	public void highlightSingleTalk(Tile t){//highlights this tile and sets to talkable.
		if(t.hasUnit())
		{
			t.changeHighlight(yellowHighlight, 0, 0.5f);
			t.setTalkable(true);
		}
	}
	public void highlightMove(){//this is called when you select to move your selected unit.
		//TODO Sets squares to available for moving to, and highlights for player.
		grid=new int[width][height];
		boolean[][] knownGrid=new boolean[width][height];
		int x=selectedUnit.getXPosition()/TILE_SIZE, y=selectedUnit.getYPosition()/TILE_SIZE;
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				grid[i][j]=99;
				knownGrid[i][j]=false;
			}
		}
		grid[x][y]=0;
		map[x][y].setChain(0);
		map[x][y].setPrevious(null);
		highlightMove(selectedUnit, x, y, x, y, selectedUnit.getMove(), 'f');//'f' will be Unit.getType()
	}
	private void highlightMove(Unit u, int x, int y, int prevX, int prevY, int move, char type){//TODO currently, all units are treated as barriers. Should only be ones on team.
		if(move<0)
			return;
		int nextCost;
		boolean isLast=false;
		if(x>0&&map[x-1][y].hasUnit()&&map[x-1][y].getUnit().getFaction()!=selectedUnit.getFaction()){
			if(inRange(u, map[x-1][y], map[x][y]))
				highlightSingleAttack(map[x-1][y]);
		}
		if(x>0&&map[x-1][y].hasUnit()&&selectedUnit.canTalk(map[x-1][y].getUnit())){
			highlightSingleTalk(map[x-1][y]);
		}
		else if(x>0&&prevX!=x-1){
			nextCost=map[x-1][y].getCost(type);
			if(nextCost<=move){
				nextCost+=grid[x][y];
				if(grid[x-1][y]>nextCost){
					grid[x-1][y]=nextCost;
					map[x-1][y].changeHighlight(blueHighlight, 0, 0.5f);
					if(!map[x-1][y].hasUnit())
						map[x-1][y].setAvailable(true);
					map[x-1][y].setPrevious(map[x][y]);
					map[x-1][y].setChain(map[x][y].getChain()+1);
					highlightMove(u, x-1, y, x, y, move-map[x-1][y].getCost(type), type);
				}
				else
					isLast=true;
			}
			else
				isLast=true;
		}
		if(x<width-1&&map[x+1][y].hasUnit()&&map[x+1][y].getUnit().getFaction()!=selectedUnit.getFaction()){
			if(inRange(u, map[x+1][y], map[x][y]))
				highlightSingleAttack(map[x+1][y]);
		}
		if(x<width-1&&map[x+1][y].hasUnit()&&selectedUnit.canTalk(map[x+1][y].getUnit())){
			highlightSingleTalk(map[x+1][y]);
		}
		else if(x<width-1&&prevX!=x+1){
			nextCost=map[x+1][y].getCost(type);
			if(nextCost<=move){
				nextCost+=grid[x][y];
				if(grid[x+1][y]>nextCost){
					grid[x+1][y]=nextCost;
					map[x+1][y].changeHighlight(blueHighlight, 0, 0.5f);
					if(!map[x+1][y].hasUnit())
						map[x+1][y].setAvailable(true);
					map[x+1][y].setPrevious(map[x][y]);
					map[x+1][y].setChain(map[x][y].getChain()+1);
					highlightMove(u, x+1, y, x, y, move-map[x+1][y].getCost(type), type);
				}
				else
					isLast=true;
			}
			else
				isLast=true;
		}
		if(y>0&&map[x][y-1].hasUnit()&&map[x][y-1].getUnit().getFaction()!=selectedUnit.getFaction()){
			if(inRange(u, map[x][y-1], map[x][y]))
				highlightSingleAttack(map[x][y-1]);
		}
		if(y>0&&map[x][y-1].hasUnit()&&selectedUnit.canTalk(map[x][y-1].getUnit())){
			highlightSingleTalk(map[x][y-1]);
		}
		else if(y>0&&prevY!=y-1){
			nextCost=map[x][y-1].getCost(type);
			if(nextCost<=move){
				nextCost+=grid[x][y];
				if(grid[x][y-1]>nextCost){
					grid[x][y-1]=nextCost;
					map[x][y-1].changeHighlight(blueHighlight, 0, 0.5f);
					if(!map[x][y-1].hasUnit())
						map[x][y-1].setAvailable(true);
					map[x][y-1].setPrevious(map[x][y]);
					map[x][y-1].setChain(map[x][y].getChain()+1);
					highlightMove(u, x, y-1, x, y, move-map[x][y-1].getCost(type), type);
				}
				else
					isLast=true;
			}
			else
				isLast=true;
		}
		if(y<height-1&&map[x][y+1].hasUnit()&&map[x][y+1].getUnit().getFaction()!=selectedUnit.getFaction()){
			if(inRange(u, map[x][y+1], map[x][y]))
				highlightSingleAttack(map[x][y+1]);
		}
		if(y<height-1&&map[x][y+1].hasUnit()&&selectedUnit.canTalk(map[x][y+1].getUnit())){
			highlightSingleTalk(map[x][y+1]);
		}
		else if(y<height-1&&prevY!=y+1){
			nextCost=map[x][y+1].getCost(type);
			if(nextCost<=move){
				nextCost+=grid[x][y];
				if(grid[x][y+1]>nextCost){
					grid[x][y+1]=nextCost;
					map[x][y+1].changeHighlight(blueHighlight, 0, 0.5f);
					if(!map[x][y+1].hasUnit())
						map[x][y+1].setAvailable(true);
					map[x][y+1].setPrevious(map[x][y]);
					map[x][y+1].setChain(map[x][y].getChain()+1);
					highlightMove(u, x, y+1, x, y, move-map[x][y+1].getCost(type), type);
				}
				else
					isLast=true;
			}
			else
				isLast=true;
		}
		if(isLast&&map[x][y].hasUnit()){
			map[x][y].removeHighlight();
		}
		if(isLast&&!map[x][y].hasUnit()){//If you're standing on a unit of your own faction, you can't attack from here.
			highlightAttack(map[x][y]);
		}
	}
	public void highlightSquares(Unit u){//this one is called when you hover over a unit, shows movable squares and attackable squares.
		//TODO if u=selectedUnit, highlight in blue, else red(enemy)?
		/*grid=new int[width][height];
		boolean[][] knownGrid=new boolean[width][height];
		int x=u.getXPosition()/TILE_SIZE, y=u.getYPosition()/TILE_SIZE;
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				grid[i][j]=99;
				knownGrid[i][j]=false;
			}
		}
		grid[x][y]=0;
		map[x][y].setChain(0);
		map[x][y].setPrevious(null);
		highlightMove(u, x, y, x, y, u.getMaxMove(), 'f');//'f' will be Unit.getType()*/
	}
	public boolean inRange(Unit attack, Unit defend){/*returns true if defend within range of attack. Will later check weapon ranges
		for things like bows and stuff. Should be int[]=attack.getWeapon.getRanges()? Then check for each range in array. Or, maybe code
		like -2=ONLY range 2 while +2=up to 2. 2-3 would be {-2, -3} while 1-2 would be {2}. Then return true if all ranges are true*/
		return withinX(attack, defend, 1);//TODO weapon.getRange()
	}
	public boolean inRange(Unit attack, Tile start, Tile target){/*returns true if attack can hit target from start*/
		if(target.hasUnit()){
			if(target.getUnit().equals(attack))
				return true;
			return false;
		}
		return (withinX(attack, start, target, 1));//TODO weapon.getRange()
	}
	public boolean withinX(Unit attack, Unit defend, int x){//returns true if defend is within x squares of attack.
		//For a bow (range 2 ONLY), attack if(withinX(2))&&!withinX(1))
				int xAttack=attack.getXPosition()/TILE_SIZE, yAttack=attack.getYPosition()/TILE_SIZE;
				int xDefend=defend.getXPosition()/TILE_SIZE, yDefend=defend.getYPosition()/TILE_SIZE;
				//TODO if r>1, check for wall between units
				for(int i=-1; i<x; i++){
					if(xAttack<xDefend)
						xAttack++;
					else if(xAttack>xDefend)
						xAttack--;
					else if(yAttack<yDefend)
						yAttack++;
					else if(yAttack>yDefend)
						yAttack--;
					else
						return true;
				}	
				return false;
	}
	public boolean withinX(Unit attack, Tile start, Tile target, int x){
		//For a bow (range 2 ONLY), attack if(withinX(2))&&!withinX(1))
		int xAttack=start.xLocation/TILE_SIZE, yAttack=start.yLocation/TILE_SIZE;
		int xDefend=target.xLocation/TILE_SIZE, yDefend=target.yLocation/TILE_SIZE;
		//TODO if r>1, check for wall between units
		for(int i=-1; i<x; i++){
			if(xAttack<xDefend)
				xAttack++;
			else if(xAttack>xDefend)
				xAttack--;
			else if(yAttack<yDefend)
				yAttack++;
			else if(yAttack>yDefend)
				yAttack--;
			else
				return true;
		}	
		return false;
	}
	public void attack(Unit attacker, Unit defender){//attacker attacks first
		//do stat calculations here
		int attackLength=attacker.getNumberOfAttacks(defender)+defender.getNumberOfAttacks(attacker);
		attackOrder=new char[attackLength];
		attackDamage=new String[attackLength];
		
		for(int i=0; i<attackLength; i++){
			attackOrder[i]='n';
			attackDamage[i]="";
		}
		combatStep(attacker, defender, 0);
		attackOrder[0]='A';
		if(defender.isAlive()){
			combatStep(defender, attacker, 1);
			attackOrder[1]='D';
		}
		else
			attackOrder[1]='n';
		if(attacker.isAlive()&&defender.isAlive()){
			if(defender.getSpeed()>=attacker.getSpeed()+4){//defender double attacks
				combatStep(defender, attacker, 2);
				attackOrder[2]='D';
			}
			if(attacker.getSpeed()>=defender.getSpeed()+4){//attacker double attacks
				combatStep(attacker, defender, 2);
				attackOrder[2]='A';
			}
		}
		
		
		clickAction=ATTACKING;
		startBattle=false;
		//engine.setState(BurningMedallin.BATTLE);
		System.out.println("Defender: "+defender.getHitpoints()+", Attacker: "+attacker.getHitpoints());
		mapBattle=new MapBattle(game, attackEffect, attacker, defender, attackOrder, attackDamage);
		if(defender.getFaction()==Unit.PLAYER&&!defender.isAlive()){
			livingPlayers--;
			if(defender.hasTurn())
				activePlayers--;
		}
		if(attacker.getFaction()==Unit.PLAYER&&!attacker.isAlive()){
			livingPlayers--;
			if(attacker.hasTurn())
				activePlayers--;
		}
		//battleScene=new BattleScene(game, left, right, battleBackground, order, 'n', 0, xOffset, yOffset);
		//inBattle=false;
	}
	private void combatStep(Unit attacker, Unit defender, int orderNum){
		//Damage now done in MapBattle.java, so keep that in mind.
		if(attacker.getWeapon().physical){//physical attack
			if(random.nextInt(100)+(attacker.getAccuracy()-defender.getEvasion())<=100){//miss
				attackDamage[orderNum]="Miss";
			}
			else if(defender.getArmor()>=attacker.getMight()){//0 damage
				attackDamage[orderNum]="0";
			}
			else{
				if(random.nextInt(100)+(attacker.getCrit()-defender.getDeflect())>100){//crit
					//defender.takeDamage(3*(attacker.getMight()-defender.getArmor()));
					attackDamage[orderNum]=Integer.toString(3*(attacker.getMight()-defender.getArmor()));
				}
				else{
					//defender.takeDamage(attacker.getMight()-defender.getArmor());
					attackDamage[orderNum]=Integer.toString(attacker.getMight()-defender.getArmor());
				}
			}
		}
		else{//magic attack
			if(random.nextInt(100)+(attacker.getAccuracy()-defender.getEvasion())<=100){//miss
				attackDamage[orderNum]="Miss";
			}
			else if(defender.getWard()>=attacker.getMight()){//0 damage
				attackDamage[orderNum]="0";
			}
			else{
				if(random.nextInt(100)+(attacker.getCrit()-defender.getDeflect())>100){//crit
					//defender.takeDamage(3*(attacker.getMight()-defender.getWard()));
					attackDamage[orderNum]=Integer.toString(3*(attacker.getMight()-defender.getWard()));
				}
				else{
					//defender.takeDamage(attacker.getMight()-defender.getWard());
					attackDamage[orderNum]=Integer.toString(attacker.getMight()-defender.getWard());
				}
			}
		}
	}
	public Object[] extend(Object[] u){
		Object[] temp=new Object[u.length*2];
		for(int i=0; i<u.length; i++){
			temp[i]=u[i];
		}
		return temp;
	}
	public Unit[] extendUnits(Unit[] u){
		Unit[] temp=new Unit[u.length*2];
		for(int i=0; i<u.length; i++){
			temp[i]=u[i];
		}
		return temp;
	}
	public void addEvent(int place, GameEvent e){
		switch(place){
		case BEGINNING:
			if(beginningEventsLength>=beginningEvents.length)
				beginningEvents=(GameEvent[])extend(beginningEvents);
			beginningEvents[beginningEventsLength++]=e;
			break;
		case MIDDLE:
			if(middleEventsLength>=middleEvents.length)
				middleEvents=(GameEvent[])extend(middleEvents);
			middleEvents[middleEventsLength++]=e;
			break;
		case END:
			if(endEventsLength>=endEvents.length)
				endEvents=(GameEvent[])extend(endEvents);
			endEvents[endEventsLength++]=e;
			break;
		}
	}
	public void removeEvent(int place, int number){
		switch(place){
		case BEGINNING:
			if(number>=beginningEventsLength)
				return;
			for(int i=number; i<beginningEventsLength-1; i++){
				beginningEvents[i]=beginningEvents[i+1];
			}
			beginningEventsLength--;
			break;
		case MIDDLE:
			if(number>=middleEventsLength)
				return;
			for(int i=number; i<middleEventsLength-1; i++){
				middleEvents[i]=middleEvents[i+1];
			}
			middleEventsLength--;
			break;
		case END:
			if(number>=endEventsLength)
				return;
			for(int i=number; i<endEventsLength-1; i++){
				endEvents[i]=endEvents[i+1];
			}
			endEventsLength--;
			break;
		}
	}
	public void loadMap(String source){
		clickAction=WAIT;
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[i].length; j++){
				map[i][j].removeSelf();;
			}
		}
		String data = "";
		String unitSource = "";
		String eventSource = "";
		String[] tempstr;
		String[] args;
		InputStream stream = getClass().getResourceAsStream(source);
		try {
			data=StreamToString(stream);
		} catch (IOException e){
			e.printStackTrace();
		}
		Pattern splitter=Pattern.compile("[ \n\r]");
		tempstr=splitter.split(data);
		args=tempstr;
		int n=0;
		for(int i=0; i<tempstr.length; i++){
			while(tempstr[i].equals(""))
				i++;
			args[n++]=tempstr[i];
		}
		n=0;
		int playerCount=0;
		width=new Integer(args[n++]);
		height=new Integer(args[n++]);
		livingPlayers=0;
		activePlayers=0;
		fade.setSize(width*TILE_SIZE, height*TILE_SIZE);
		game.setSize(width*TILE_SIZE, height*TILE_SIZE);
		map=new Tile[width][height];
		fade.setColor(new Color(0,0,0,255));
		loadingBar.setMinimum(0);
		loadingBar.setValue(0);
		loadingBar.setMaximum(width*height);
		loadingBar.setVisible(true);
		int temp;
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				temp=new Integer(args[n++]);
				map[j][i]=new Tile(game, j*TILE_SIZE, i*TILE_SIZE, TerrainManifest.translate(temp, game));
				map[j][i].changeGrid(tileOutline, 0);
				loadingBar.setValue(loadingBar.getValue()+1);
				//TODO make start space not overlap with terrain type
				
				/*if(temp==1){
					addUnit(new Unit(spearWoman, game));
					map[j][i]=new Tile(game, j*TILE_SIZE, i*TILE_SIZE);
					map[j][i].changeGrid(tileOutline, 0);
					//map[j][i].displayBackgrounds();
					units[unitLength-1].setLocation(j*TILE_SIZE, i*TILE_SIZE);
					units[unitLength-1].setTile(map[j][i]);
					units[unitLength-1].equipWeapon(bronzeSpear);
					map[j][i].setUnit(units[unitLength-1]);
				}*/
			}
		}
		temp=new Integer(args[n++]).intValue();//Sets start squares
		startSquares=new int[temp][2];
		for(int i=0; i<temp; i++){
			while(playerCount<playerUnitLength&&!playerUnits[playerCount].isAlive()){/*skips dead units*/
				playerCount++;
			}
			int tempX=new Integer(args[n++]).intValue();
			int tempY=new Integer(args[n++]).intValue();
			map[tempX][tempY].setStart(true);
			startSquares[i][0]=tempX;
			startSquares[i][1]=tempY;
			if(playerCount<playerUnitLength){/*puts the next unit in array on start square*/
				map[tempX][tempY].setUnit(playerUnits[playerCount]);
				playerUnits[playerCount].setTile(map[tempX][tempY]);
				playerUnits[playerCount].setPreviousX(tempX*TILE_SIZE);
				playerUnits[playerCount].setPreviousY(tempY*TILE_SIZE);
				playerUnits[playerCount++].setLocation(tempX*TILE_SIZE, tempY*TILE_SIZE);
				livingPlayers++;
				activePlayers++;
			}
		}
		/*
		temp=new Integer(args[n++]).intValue();//sets events
		for(int i=0; i<temp; i++){
			int x=new Integer(args[n++]);
			if(x==1)
				addEvent(END, new NextLevelOnRout(this));
			if(x==2){
				addEvent(END, new TextOnRout(this, "testText2.txt"));
			}
		}
		
		//addEvent(END, new TextOnEntry(this, "testText1.txt", 0, 20, 9, 24));
		*/
		unitSource=args[n++];
		eventSource=args[n++];
		
		game.setVisible(true);
		loadingBar.setVisible(false);
		fadeIn();
		loadUnits(unitSource);
		loadEvents(eventSource);
		clickAction=SELECT;
	}
	public void loadUnits(String source){
		String data = "";
		String[] temp;
		String[] args;
		InputStream stream = getClass().getResourceAsStream(source);
		try {
			data=StreamToString(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pattern splitter=Pattern.compile("[ \n\r]");
		temp=splitter.split(data);
		args=temp;
		int n=0;
		for(int i=0; i<temp.length; i++){
			while(temp[i].equals(""))
				i++;
			args[n++]=temp[i];
		}
		n=0;
		int length=new Integer(args[n++]);
		int type, faction, tempX, tempY;
		for(int i=0; i<length; i++){
			faction=new Integer(args[n++]);
			if(faction==Unit.PLAYER){
				type=new Integer(args[n++]);
				if(type==LEVEL_ONLY){//Format: faction, type, rank, level, xloc, yloc
					addPlayerUnit(new Unit(RankManifest.translate(new Integer(args[n++])), game));
					playerUnits[playerUnitLength-1].setLevel(new Integer(args[n++]));
					playerUnits[playerUnitLength-1].setFaction(faction);
					tempX=new Integer(args[n++]);
					tempY=new Integer(args[n++]);
					playerUnits[playerUnitLength-1].setLocation(tempX*TILE_SIZE, tempY*TILE_SIZE);
					playerUnits[playerUnitLength-1].setPreviousX(tempX*TILE_SIZE);
					playerUnits[playerUnitLength-1].setPreviousY(tempY*TILE_SIZE);
					playerUnits[playerUnitLength-1].setTile(map[tempX][tempY]);
					map[tempX][tempY].setUnit(playerUnits[playerUnitLength-1]);
					playerUnits[playerUnitLength-1].equipWeapon(playerUnits[playerUnitLength-1].getRank().getDefaultWeapon());
					playerUnits[playerUnitLength-1].setHasTurn(true);
				}
				else if(type==GIVEN_STATS){
					addPlayerUnit(new Unit(RankManifest.translate(new Integer(args[n++])), game));
					playerUnits[playerUnitLength-1].setFaction(faction);
					playerUnits[playerUnitLength-1].setLevel(new Integer(args[n++]));
					playerUnits[playerUnitLength-1].setMaxMove(new Integer(args[n++]));
					playerUnits[playerUnitLength-1].setMaxHP(new Integer(args[n++]));
					playerUnits[playerUnitLength-1].setHitpoints(playerUnits[playerUnitLength-1].getMaxHP());
					playerUnits[playerUnitLength-1].setStrength(new Integer(args[n++]));
					playerUnits[playerUnitLength-1].setDefense(new Integer(args[n++]));
					playerUnits[playerUnitLength-1].setSpeed(new Integer(args[n++]));
					tempX=new Integer(args[n++]);
					tempY=new Integer(args[n++]);
					playerUnits[playerUnitLength-1].setLocation(tempX*TILE_SIZE, tempY*TILE_SIZE);
					playerUnits[playerUnitLength-1].setPreviousX(tempX*TILE_SIZE);
					playerUnits[playerUnitLength-1].setPreviousY(tempY*TILE_SIZE);
					playerUnits[playerUnitLength-1].setTile(map[tempX][tempY]);
					map[tempX][tempY].setUnit(playerUnits[playerUnitLength-1]);
					playerUnits[playerUnitLength-1].equipWeapon(playerUnits[playerUnitLength-1].getRank().getDefaultWeapon());
					playerUnits[playerUnitLength-1].setHasTurn(true);
				}
				livingPlayers++;
				activePlayers++;
			}
			if(faction==Unit.ENEMY){
				type=new Integer(args[n++]);
				System.out.println(type);
				if(type==LEVEL_ONLY){
					addEnemyUnit(new Unit(RankManifest.translate(new Integer(args[n++])), game));
					mapUnits[mapUnitLength-1].setLevel(new Integer(args[n++]));
					mapUnits[mapUnitLength-1].setFaction(faction);
					tempX=new Integer(args[n++]);
					tempY=new Integer(args[n++]);
					mapUnits[mapUnitLength-1].setLocation(tempX*TILE_SIZE, tempY*TILE_SIZE);
					mapUnits[mapUnitLength-1].setPreviousX(tempX*TILE_SIZE);
					mapUnits[mapUnitLength-1].setPreviousY(tempY*TILE_SIZE);
					mapUnits[mapUnitLength-1].setTile(map[tempX][tempY]);
					map[tempX][tempY].setUnit(mapUnits[mapUnitLength-1]);
					mapUnits[mapUnitLength-1].equipWeapon(mapUnits[mapUnitLength-1].getRank().getDefaultWeapon());
					mapUnits[mapUnitLength-1].setBehavior(attackClosest);
					mapUnits[mapUnitLength-1].setHasTurn(true);
				}
				else if(type==GIVEN_STATS){
					addEnemyUnit(new Unit(RankManifest.translate(new Integer(args[n++])), game));
					mapUnits[mapUnitLength-1].setFaction(faction);
					mapUnits[mapUnitLength-1].setLevel(new Integer(args[n++]));
					mapUnits[mapUnitLength-1].setMaxMove(new Integer(args[n++]));
					mapUnits[mapUnitLength-1].setMaxHP(new Integer(args[n++]));
					mapUnits[mapUnitLength-1].setHitpoints(mapUnits[mapUnitLength-1].getMaxHP());
					mapUnits[mapUnitLength-1].setStrength(new Integer(args[n++]));
					mapUnits[mapUnitLength-1].setDefense(new Integer(args[n++]));
					mapUnits[mapUnitLength-1].setSpeed(new Integer(args[n++]));
					tempX=new Integer(args[n++]);
					tempY=new Integer(args[n++]);
					mapUnits[mapUnitLength-1].setLocation(tempX*TILE_SIZE, tempY*TILE_SIZE);
					mapUnits[mapUnitLength-1].setPreviousX(tempX*TILE_SIZE);
					mapUnits[mapUnitLength-1].setPreviousY(tempY*TILE_SIZE);
					mapUnits[mapUnitLength-1].setTile(map[tempX][tempY]);
					map[tempX][tempY].setUnit(mapUnits[mapUnitLength-1]);
					mapUnits[mapUnitLength-1].equipWeapon(mapUnits[mapUnitLength-1].getRank().getDefaultWeapon());
					mapUnits[mapUnitLength-1].setBehavior(attackClosest);
					mapUnits[mapUnitLength-1].setHasTurn(true);
				}
			}
		}
	}
	public void loadEvents(String source){
		String data = "";
		String[] temp;
		String[] args;
		String unitName;
		InputStream stream = getClass().getResourceAsStream(source);
		try {
			data=StreamToString(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pattern splitter=Pattern.compile("[ \n\r]");
		temp=splitter.split(data);
		args=temp;
		int n=0;
		for(int i=0; i<temp.length; i++){
			while(temp[i].equals(""))
				i++;
			args[n++]=temp[i];
		}
		n=0;
		
		int loop=new Integer(args[n++]).intValue();//sets events
		for(int i=0; i<loop; i++){
			int position;
			int x=new Integer(args[n++]);
			
			switch (x){
			case 1:
				position=new Integer(args[n++]);
				addEvent(position, new NextLevelOnRout(this, args[n++]));
				break;
			case 2:
				position=new Integer(args[n++]);
				addEvent(position, new TextOnRout(this, args[n++]));
				break;
			case 3:
				unitName=args[n++];
				for(int j=0; j<mapUnits.length; j++){
					if(mapUnits[j].getName().equals(unitName)){
						System.out.println(unitName);
						mapUnits[j].setEvent(new RecruitmentTalk(this, mapUnits[j], args[n++]));
						break;
					}
				}
				break;
			}
		}
	}
	public void removeFlags(){
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				map[i][j].removeFlags();
			}
		}
	}
	public void removePaths(){
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				map[i][j].removePath();
			}
		}
	}
	public void removePaths(int xStart, int yStart, int xEnd, int yEnd){
		if(xStart<0)
			xStart=0;
		if(yStart<0)
			yStart=0;
		if(xEnd>width)
			xEnd=width;
		if(yEnd>width)
			yEnd=width;
		for(int x=xStart; x<xEnd; x++){
			for(int y=yStart; y<yEnd; y++){
					map[x][y].removePath();
			}
		}
	}
	public void removeHighlights(){
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
					map[i][j].removeHighlight();
			}
		}
	}
	public void removeHighlights(int xStart, int yStart, int xEnd, int yEnd){
		if(xStart<0)
			xStart=0;
		if(yStart<0)
			yStart=0;
		if(xEnd>width)
			xEnd=width;
		if(yEnd>height)
			yEnd=height;
		for(int x=xStart; x<xEnd; x++){
			for(int y=yStart; y<yEnd; y++){
					map[x][y].removeHighlight();
			}
		}
	}
	public BattleScene getBattleScene(){
		return battleScene;
	}
	public Tile[][] getMap(){
		return map;
	}
	
	public void fadeBlack(){
		fade.setAlpha(255);
		fade.setVisible(true);
	}
	
	public void fadeIn(){
		fadeOut=false;
		
	}
	public void fadeOut(){
		fadeOut=true;
	}
	public void fadeToggle(){
		fadeOut=!fadeOut;
	}
	
	public MapScene(BurningMedallion e, JLayeredPane g) {
		super(g);
		engine=e;
		xOffset=0;
		yOffset=0;
		mapUnitCounter=0;
		mapUnitHasTarget=false;
		turn=PLAYER_TURN;
		playerUnits=new Unit[5];
		mapUnits=new Unit[5];
		livingPlayers=0;
		activePlayers=0;
		playerUnitLength=0;
		mapUnitLength=0;
		nextLevel=false;
		nextMap="";
		startBattle=false;
		inBattle=false;
		selectActive=false;
		selectedUnitActive=false;
		upPressed=rightPressed=leftPressed=downPressed=false;
		clickAction=WAIT;
		map=new Tile[0][0];
		random=new Random();
		
		attackEffect=new JLabel();
		attackEffect.setSize(50, 50);
		beginningEvents=new GameEvent[1];
		beginningEventsLength=0;
		middleEvents=new GameEvent[1];
		middleEventsLength=0;
		endEvents=new GameEvent[5];
		endEventsLength=0;
		
		text=new JTextArea();
		game.add(text, new Integer(500));
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setSize(800, 100);
		text.setLocation(0,500);
		text.setText("bleh");
		text.setVisible(false);
		
		
		loadingBar=new JProgressBar();
		loadingBar.setLocation(100, 275);
		loadingBar.setSize(600, 50);
		loadingBar.setStringPainted(true);
		game.add(loadingBar, new Integer(1001));
		fade=new FadeBar(0, 0, game.getWidth(), game.getHeight(), 100, 100, new Color(0,0,0,0));
		game.add(fade, new Integer(1000));
		
		unitPath=new char[]{'n'};
		
		attackClosest=new AttackClosest(this);
		
		battleInfoBox=new BattleInfoBox(this);
		inspectPanel=new InspectPanel(this);
		unitMenu=new UnitMenu(this, game);
		inactiveMenu=new InactiveMenu(this, game);
		transparentSquare=new Animation("TransparentSquare", game, TILE_SIZE, TILE_SIZE);
		battleBackground=new Animation("WhiteBackground", game, 800, 600);
		tileOutline=new Animation("TileOutline", game, TILE_SIZE, TILE_SIZE);
		unitSelect=new Animation("UnitSelect", game, TILE_SIZE, TILE_SIZE);
		mapDot=new Animation("MapDot", game, TILE_SIZE, TILE_SIZE);
		arrow=new Animation("Arrow", game, TILE_SIZE, TILE_SIZE);
		blueHighlight=new Animation("BlueHighlight", game, TILE_SIZE, TILE_SIZE);
		orangeHighlight=new Animation("OrangeHighlight", game, TILE_SIZE, TILE_SIZE);
		yellowHighlight=new Animation("YellowHighlight", game, TILE_SIZE, TILE_SIZE);
		
		
		bronzeSpear=new BronzeSpear();
	}
	public void selectUnit(int x, int y, Unit u){
		unitSelect.addSelf();
		unitSelect.setLocation(x*TILE_SIZE, y*TILE_SIZE);
		unitSelect.displayStatic();
		selectActive=true;
		selectedUnitActive=true;
		int xOffsetTiles=xOffset/TILE_SIZE;
		int yOffsetTiles=yOffset/TILE_SIZE;
		
		selectedUnit=u;
		System.out.println(selectedUnit.getMove());
		if(selectedUnit.getMove()<selectedUnit.getMaxMove()){
			highlightAttack(selectedUnit.getTile());
		}
		else{
			highlightMove();
		}
		unitPath=new char[selectedUnit.getMaxMove()];
		unitPathNum=0;
		for(int v=0; v<unitPath.length; v++){
			unitPath[v]='n';
		}
		previousTile=selectedUnit.getTile();
		if(x<13+xOffsetTiles&&y<8+yOffsetTiles)
			unitMenu.addSelf(selectedUnit, (x+1)*TILE_SIZE, y*TILE_SIZE);
		else if(x>=13+xOffsetTiles&&y<8+yOffsetTiles)
			unitMenu.addSelf(selectedUnit, (x-2)*TILE_SIZE, y*TILE_SIZE);
		else if(x>=13+xOffsetTiles&&y>=8+yOffsetTiles)
			unitMenu.addSelf(selectedUnit, (x-2)*TILE_SIZE, (y-3)*TILE_SIZE);
		else
			unitMenu.addSelf(selectedUnit, (x+1)*TILE_SIZE, (y-3)*TILE_SIZE);
		engine.requestFocus();
		clickAction=MENU;
	}
	public void selectInactive(int x, int y, Unit u){
		unitSelect.addSelf();
		unitSelect.setLocation(x*TILE_SIZE, y*TILE_SIZE);
		unitSelect.displayStatic();
		selectActive=true;
		selectedUnitActive=false;
		int xOffsetTiles=xOffset/TILE_SIZE;
		int yOffsetTiles=yOffset/TILE_SIZE;
		
		selectedUnit=u;
		highlightMove();
		unitPath=new char[selectedUnit.getMaxMove()];
		unitPathNum=0;
		for(int v=0; v<unitPath.length; v++){
			unitPath[v]='n';
		}
		previousTile=selectedUnit.getTile();
		if(x<13+xOffsetTiles&&y<10+yOffsetTiles)
			inactiveMenu.addSelf(selectedUnit, (x+1)*TILE_SIZE, y*TILE_SIZE);
		else if(x>=13+xOffsetTiles&&y<10+yOffsetTiles)
			inactiveMenu.addSelf(selectedUnit, (x-2)*TILE_SIZE, y*TILE_SIZE);
		else if(x>=13+xOffsetTiles&&y>=10+yOffsetTiles)
			inactiveMenu.addSelf(selectedUnit, (x-2)*TILE_SIZE, (y-1)*TILE_SIZE);
		else
			inactiveMenu.addSelf(selectedUnit, (x+1)*TILE_SIZE, (y-1)*TILE_SIZE);
		engine.requestFocus();
		clickAction=MENU;
	}
	public void resetPath(int size){
		unitPath=new char[size];
		for(int i=0; i<unitPath.length; i++){
			unitPath[i]='n';
		}
	}
	public void moveUnit(int x, int y, Unit u){
		if(!map[x][y].isAvailable())
			return;
		int tempX=x, tempY=y;
		x=x-u.getXPosition()/TILE_SIZE;
		y=y-u.getYPosition()/TILE_SIZE;
		x=tempX;
		y=tempY;
		int tempMove=u.getMaxMove();
		if(unitPath[0]=='n'){
			if(selectActive){
				constructPath(map[x][y]);
			}
			else 
				constructPath(map[x][y], u);
		}
		x=u.getXPosition()/TILE_SIZE;
		y=u.getYPosition()/TILE_SIZE;
		u.setMoveOrder(unitPath);//end all move orders in 'n'
		u.getTile().removeUnit();
		u.setTile(previousTile);
		u.getTile().setUnit(u);
		removePaths();
		removeHighlights();//TODO change the extra 1s to weapon.getRange()
	}
	public void moveSelectedUnit(int x, int y){
		if(selectedUnit.getXPosition()/TILE_SIZE==x&&selectedUnit.getYPosition()/TILE_SIZE==y){
			cancelMoveSelection();
			return;
		}
		if(!map[x][y].isAvailable()||!selectedUnitActive)
			return;
		int tempX=x, tempY=y;
		x=x-selectedUnit.getXPosition()/TILE_SIZE;
		y=y-selectedUnit.getYPosition()/TILE_SIZE;
		x=tempX;
		y=tempY;
		int tempMove=selectedUnit.getMaxMove();
		if(unitPath[0]=='n'){
			constructPath(map[x][y]);
		}
		x=selectedUnit.getXPosition()/TILE_SIZE;
		y=selectedUnit.getYPosition()/TILE_SIZE;
		selectedUnit.setMoveOrder(unitPath);//end all move orders in 'n'
		selectedUnit.getTile().removeUnit();
		selectedUnit.setTile(previousTile);
		selectedUnit.getTile().setUnit(selectedUnit);
		clickAction=MOVING_TO_MENU;
		//selectedUnit=null;
		unitSelect.removeSelf();
		selectActive=false;
		removePaths();
		removeHighlights(x-tempMove-1, y-tempMove-1, x+tempMove+1+1, y+tempMove+1+1);//TODO change the extra 1s to weapon.getRange()
	}
	public synchronized void cancelMoveSelection(){
		selectedUnit=null;
		unitSelect.removeSelf();
		selectActive=false;
		selectedUnitActive=false;
		clickAction=SELECT;
		previousTile=null;
		removePaths();
		removeHighlights();
		return;
	}
	public void activateNextLevel(String next){
		nextLevel=true;
		nextMap=next;
		fadeBlack();
		loadingBar.setValue(0);
		loadingBar.setVisible(true);
	}
	public void nextLevel(){
		xOffset=0;
		yOffset=0;
		nextLevel=false;

		mapUnitCounter=0;
		beginningEventsLength=0;
		middleEventsLength=0;
		endEventsLength=0;
		mapUnitHasTarget=false;
		turn=PLAYER_TURN;
		for(int i=0; i<playerUnitLength; i++){
			if(playerUnits[i].isAlive()){
				playerUnits[i].setMove(playerUnits[i].getMaxMove());
				playerUnits[i].setHitpoints(playerUnits[i].getMaxHP());
			}
				
		}
		mapUnits=new Unit[5];
		mapUnitLength=0;
		startBattle=false;
		inBattle=false;
		selectActive=false;
		upPressed=rightPressed=leftPressed=downPressed=false;
		clickAction=WAIT;
		fadeIn();
		loadMap(nextMap);
	}

	public synchronized void mouseClicked(MouseEvent arg0) {
		int x, y, i;
		x=arg0.getX()+xOffset-WINDOW_WIDTH;
		y=arg0.getY()+yOffset-WINDOW_HEIGHT;
		x=x/TILE_SIZE;//this gets how many tiles over from 0 the click was
		y=y/TILE_SIZE;
		//System.out.println(MouseInfo.getNumberOfButtons());
		//System.out.println(x+", "+y);
		if(clickAction==TEXT&&turn==PLAYER_TURN){
			text.setVisible(false);
			engine.requestFocus();
			clickAction=SELECT;
		}
		else if(clickAction==SELECT){
			for(i=0; i<playerUnitLength; i++){
				if(!playerUnits[i].isAlive()){//skip checking if dead
				
				}
				else if(!playerUnits[i].hasTurn()&&playerUnits[i].getXPosition()==x*TILE_SIZE && playerUnits[i].getYPosition()==y*TILE_SIZE){
					selectInactive(x, y, playerUnits[i]);
					break;
				}
				else if(playerUnits[i].getXPosition()==x*TILE_SIZE && playerUnits[i].getYPosition()==y*TILE_SIZE){
					selectUnit(x, y, playerUnits[i]);
					break;
			}
		}
			if(selectActive==false){//Only check mapunits if a player unit wasn't selected.
				for(i=0; i<mapUnitLength; i++){
					if(!mapUnits[i].isAlive()){//skip checking if dead
						
					}
					else if(mapUnits[i].getXPosition()==x*TILE_SIZE && mapUnits[i].getYPosition()==y*TILE_SIZE){
						selectInactive(x, y, mapUnits[i]);
						break;
					}
				}
			}
		}
		else if(clickAction==MOVE){
			if(arg0.getButton()==3){//right-click to cancel
				cancelMoveSelection();
			}
			else
				moveSelectedUnit(x, y);
			/*
			moveSelectedUnit code was here
			*/
		}
		else if(clickAction==ATTACK){//TODO when units have different factions, player units should only attack enemy units
			if(selectedUnit.getXPosition()==x*TILE_SIZE&&selectedUnit.getYPosition()==y*TILE_SIZE){
				cancelPressed();
			}		
			else for(i=0; i<mapUnitLength; i++){
				if(!mapUnits[i].isAlive()){
					
				}
				else if(mapUnits[i].getXPosition()==x*TILE_SIZE && mapUnits[i].getYPosition()==y*TILE_SIZE){
					if(mapUnits[i]!=selectedUnit){
						if(selectedUnit.canTalk(mapUnits[i]) && withinX(selectedUnit, mapUnits[i], 1)){
							mapUnits[i].getEvent().trigger(selectedUnit);
							selectedUnit.endTurn();
							selectedUnitActive=false;
							unitSelect.removeSelf();
							selectActive=false;
							removeHighlights();
						}
						else if(inRange(selectedUnit, mapUnits[i])){
							battleInfoBox.open(selectedUnit, mapUnits[i]);
							battleInfoBox.setLocation(unitMenu.getLocation());
							if(battleInfoBox.getX()+200>800+xOffset){
								battleInfoBox.setLocation(battleInfoBox.getX()-100, battleInfoBox.getY());
							}
							game.add(battleInfoBox, JLayeredPane.POPUP_LAYER);
							clickAction=ATTACK_CONFIRM;
						}
					}
					else{
						selectedUnit=null;
						unitSelect.removeSelf();
						selectActive=false;
						clickAction=SELECT;
						previousTile=null;
						removePaths();
						return;
					}
				}
			}//TODO see beginning of loop.
		}
		else if(clickAction==TALKING){
			if(activeEvent.trigger()){
				clickAction=SELECT;
			}
		}
		if(selectedUnit!=null){
			System.out.println(selectedUnit.getTile().xLocation+ ", "+selectedUnit.getTile().yLocation);
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void attackConfirmPressed(Unit attacker, Unit defender){
		removeHighlights();
		selectedUnit.endTurn();
		activePlayers--;
		game.remove(battleInfoBox);
		engine.requestFocus();
		attack(attacker, defender);
		
		selectedUnit=null;
		unitSelect.removeSelf();
		selectActive=false;
		clickAction=ATTACKING;
		previousTile=null;
		removePaths();
		return;
	}
	
	public synchronized void attackCancelPressed(){
		game.remove(battleInfoBox);
		engine.requestFocus();
		clickAction=ATTACK;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public synchronized void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
			case 37://left
				if(clickAction==MENU||clickAction==INSPECT){
					break;
				}
				leftPressed=true;
				break;
			case 38://up
				if(clickAction==MENU||clickAction==INSPECT){
					break;
				}
				upPressed=true;
				break;
			case 39://right
				if(clickAction==MENU||clickAction==INSPECT){
					break;
				}
				rightPressed=true;
				break;
			case 40://down
				if(clickAction==MENU||clickAction==INSPECT){
					break;
				}
				downPressed=true;
				break;
				
			case 49://1 hotkey for move
				if(clickAction==MENU&&selectedUnitActive){
					movePressed();
				}
				break;
			case 50://2 hotkey for attack
				if(clickAction==MENU&&selectedUnitActive){
					attackPressed();
				}
				break;
			case 51://3 hotkey for item
				if(clickAction==MENU&&selectedUnitActive){
					//clickAction=ITEM;
					//unitMenu.removeSelf();
				}
				break;	
			case 52://4 hotkey for trade
				if(clickAction==MENU&&selectedUnitActive){
					//clickAction=TRADE;
					//if(selectedUnitActive)
					//	unitSelect.removeSelf();
					//else
					//	inactiveMenu.removeSelf();
					//selectedUnitActive=false;
				}
				break;
			case 53://5 hotkey for cancel
				if(clickAction==MENU&&selectedUnitActive){
					cancelPressed();
				}
				break;
				
			case 10://enter
				if(clickAction==SELECT)
					endTurn();
				break;
			case 88://x
				if(clickAction==MENU){
					cancelPressed();
				}
				if(clickAction==MOVE){
					cancelMoveSelection();//TODO change 1 to weapon.getRange()
				}
				if(clickAction==ATTACK){
					cancelPressed();
				}
				if(clickAction==INSPECT){
					closeInspect();
				}
				break;
		}
		//System.out.println(arg0.getKeyCode());
	}


	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case 37://left
			leftPressed=false;
			break;
		case 38://up
			upPressed=false;
			break;
		case 39://right
			rightPressed=false;
			break;
		case 40://down
			downPressed=false;
			break;
		}
	}


	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void movePressed(){//WARNING: Some people may get a crash by moving the mouse? If people complain, remove this robot.
		if(clickAction!=MENU)
			return;
		engine.requestFocus();
		if(!selectedUnitActive)
			return;
		clickAction=MOVE;
		if(selectedUnit.getMove()!=selectedUnit.getMaxMove()){
			selectedUnit.getTile().removeUnit();
			selectedUnit.moveBack();
			selectedUnit.setMove(selectedUnit.getMaxMove());
			unitSelect.setLocation(selectedUnit.xPosition, selectedUnit.yPosition);
			map[selectedUnit.xPosition/TILE_SIZE][selectedUnit.yPosition/TILE_SIZE].setUnit(selectedUnit);
			selectedUnit.setTile(map[selectedUnit.xPosition/TILE_SIZE][selectedUnit.yPosition/TILE_SIZE]);
			previousTile=selectedUnit.getTile();
			removeHighlights();
			highlightMove();
		}
		//highlightMove();
		unitMenu.removeSelf();
	}
	public synchronized void attackPressed(){
		if(clickAction!=MENU)
			return;
		engine.requestFocus();
		if(!selectedUnitActive)
			return;
		clickAction=ATTACK;
		unitMenu.removeSelf();
		removeHighlights();
		highlightAttackableEnemies(selectedUnit);

	}
	public synchronized void cancelPressed(){
		if(clickAction!=MENU&&clickAction!=ATTACK)
			return;
		engine.requestFocus();
		if(selectedUnit.getMove()!=selectedUnit.getMaxMove()){
			selectedUnit.getTile().removeUnit();
			selectedUnit.moveBack();
			selectedUnit.setMove(selectedUnit.getMaxMove());
			unitSelect.setLocation(selectedUnit.xPosition, selectedUnit.yPosition);
			map[selectedUnit.xPosition/TILE_SIZE][selectedUnit.yPosition/TILE_SIZE].setUnit(selectedUnit);
			selectedUnit.setTile(map[selectedUnit.xPosition/TILE_SIZE][selectedUnit.yPosition/TILE_SIZE]);
			previousTile=selectedUnit.getTile();
		}
		selectedUnit=null;
		previousTile=null;
		clickAction=SELECT;
		unitSelect.removeSelf();
		if(selectedUnitActive)
			unitMenu.removeSelf();
		else
			inactiveMenu.removeSelf();
		selectedUnitActive=false;
		selectActive=false;
		removeHighlights();
	}
	public synchronized void endTurnPressed(){
		if(clickAction!=MENU)
			return;
		engine.requestFocus();
		selectedUnit.endTurn();
		clickAction=SELECT;
		if(selectedUnitActive)
			unitMenu.removeSelf();
		else
			inactiveMenu.removeSelf();
		selectedUnitActive=false;
		unitSelect.removeSelf();
		selectActive=false;
		activePlayers--;
		removeHighlights();
	}
	public synchronized void inspectPressed(){
		if(clickAction!=MENU)
			return;
		clickAction=INSPECT;
		inspectPanel.open(selectedUnit);
		inspectPanel.setLocation(180+xOffset, 60+yOffset);
		game.add(inspectPanel, JLayeredPane.DRAG_LAYER);
		engine.requestFocus();
	}
	public synchronized void closeInspect(){
		engine.requestFocus();
		game.remove(inspectPanel);
		clickAction=MENU;
	}
	public JTextArea getText() {
		return text;
	}
	public void setText(JTextArea text) {
		this.text = text;
	}
	public void changeText(String s){
		text.setText(s);
		text.setLocation(0,500);
		clickAction=TEXT;
		text.setVisible(true);
		if(!text.getParent().equals(game))
			game.add(text);
	}
	public void setDialog(String s, GameEvent e){
		text.setText(s);
		activeEvent=e;
		text.setLocation(0,500);
		clickAction=TALKING;
		text.setVisible(true);
		if(!text.getParent().equals(game))
			game.add(text);
	}
	public void endDialog(){
		text.setVisible(false);
		activePlayers--;
	}
	public void increaseActivePlayers(int number){
		activePlayers+=number;
	}

}
