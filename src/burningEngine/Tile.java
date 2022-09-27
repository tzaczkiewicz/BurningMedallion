package burningEngine;


import java.awt.Graphics;

import javax.swing.*;

import burningterrain.Terrain;

public class Tile {
	JLayeredPane game;
	
	Unit unit; //can be null
	JLabel grid;
	JLabel background;
	JLabel path;
	JLabel highlight;//highlighted square for movement
	AlphaIcon highlightIcon;
	int xLocation, yLocation;
	int footCost;//movement cost for foot units
	
	private Terrain terrain;
	
	private Tile previous;//The previous tile for pathfinding
	private char previousDirection='n';//The previous tile's direction for movement arrow purposes
	private int chain;//which tile this is in the pathfinding chain
	
	private boolean available;//true if a unit is allowed to move to this square;
	private boolean attackable;//true if in range of currently selected unit's attack
	private boolean talkable;//true if next to selected unit and can be talked to
	private boolean containsUnit;
	private boolean startTile;//true if this is a player unit start square
	private boolean pathFlag;//true if the path should be visible
	
	
	public Tile(JLayeredPane g, int x, int y, Terrain t){
		xLocation=x;
		yLocation=y;
		available=false;
		attackable=false;
		talkable=false;
		containsUnit=false;
		startTile=false;
		footCost=1;
		/*unit=u;
		if(unit!=null)
			unit.setTile(this);*/
		game=g;
		grid=new JLabel();
		background=new JLabel();
		path=new JLabel();
		highlight=new JLabel();
		grid.setLocation(xLocation, yLocation);
		background.setLocation(xLocation, yLocation);
		path.setLocation(xLocation, yLocation);
		highlight.setLocation(xLocation, yLocation);
		grid.setSize(MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		background.setSize(MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		path.setSize(MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		highlight.setSize(MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		path.setIcon(null);
		grid.setIcon(null);
		setTerrain(t);
		highlight.setIcon(null);
		highlight.setVisible(false);
		background.setVisible(true);
		path.setVisible(false);
		pathFlag=false;
		grid.setVisible(false);
		int count=game.getComponentCount()/4;
		if(count>=2000)
			count=1999;
		game.add(path, new Integer(-4000+count));
		game.add(grid, new Integer(-6000+count));
		game.add(highlight, new Integer(-8000+count));
		game.add(background, new Integer(-10000+count));
	}
	public void setTerrain(Terrain t){
		terrain=t;
		background.setIcon(terrain.getBackground().getImage(0));
		footCost=t.getFootMove();
	}
	public Unit getUnit(){
		return unit;
	}
	public void setUnit(Unit u){
		unit=u;
		containsUnit=true;
	}
	public void removeUnit(){
		containsUnit=false;
	}
	public boolean hasUnit(){
		return containsUnit;
	}
	public void changeGrid(Animation a, int x){
		grid.setIcon(a.getImage(x));
		grid.setVisible(true);
	}
	public void removeGrid(){
		grid.setVisible(false);
	}
	public JLabel getGrid(){
		return grid;
	}
	public void adjustGridOpacity(int percentage){
		//not sure how to do this.
	}
	public void changeBackground(Animation a, int x){
		background.setIcon(a.getImage(x));
		background.setVisible(true);
	}
	public void removeBackground(){
		background.setVisible(false);
	}
	public JLabel getBackground(int x){
		return background;
	}
	public void changePath(Animation a, int x){
		path.setIcon(a.getImage(x));
		path.setVisible(true);
		pathFlag=true;
	}
	public void removePath(){
		path.setVisible(false);
		pathFlag=false;
		previousDirection='n';
	}
	public void flagPath(){
		previousDirection='n';
		pathFlag=false;
	}
	public void removeFlags(){
		if(!pathFlag)
			path.setVisible(false);
	}
	public JLabel getPath(){
		return path;
	}
	public void changeHighlight(Animation a, int x, float f){
		highlightIcon=new AlphaIcon(a.getImage(x), f);
		highlight.setIcon(highlightIcon);
		highlight.setVisible(true);
	}
	public void removeHighlight(){
		highlight.setVisible(false);
		available=false;
		attackable=false;
		talkable=false;
	}
	public JLabel getHighlight(){
		return highlight;
	}
	public void displayBackgrounds(){
		
	}
	public void setAvailable(boolean b){
		available=b;
	}
	public void setAttackable(boolean b){
		attackable=b;
	}
	public void setTalkable(boolean b){
		talkable=b;
	}
	public boolean isTalkable(){
		return talkable;
	}
	
	public int getCost(char type){
		if(type=='f')
			return footCost;
		return 0;
	}
	public void setFootCost(int c){
		footCost=c;
	}
	
	public void setPrevious(Tile t){
		previous=t;
	}
	public Tile getPrevious(){
		return previous;
	}
	public void setChain(int x){
		chain=x;
	}
	public int getChain(){
		return chain;
	}
	
	public boolean isAvailable(){
		return available;
	}
	public boolean isAttackable(){
		return attackable;
	}
	public void removeSelf(){
		game.remove(background);
		game.remove(grid);
		game.remove(path);
		game.remove(highlight);
	}
	public void setStart(boolean isStart){
		startTile=isStart;
	}
	public boolean isStart(){
		return startTile;
	}
	/*public void changeFrame(int selectedAnimation, int f){
		
	}*/
	public char getPreviousDirection() {
		return previousDirection;
	}
	public void setPreviousDirection(char previousDirection) {
		this.previousDirection = previousDirection;
	}
}
