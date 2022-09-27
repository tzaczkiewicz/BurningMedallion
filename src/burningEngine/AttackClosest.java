package burningEngine;

public class AttackClosest extends Behavior{
	
	private int minDistance;
	private Unit minUnit;
	private Tile attackFrom;
	
	public AttackClosest(MapScene m){
		super(m);
	}

	@Override
	public Unit findTarget(Unit attacker) {
		mapScene.resetPath(attacker.getMaxMove());/*important! put this in every behavior. moveUnit does not call constructPath if the first character is filled in*/
		minDistance=99;
		targetFound=false;
		moving=false;
		minUnit=null;
		attackFrom=null;
		consideredUnits=new Unit[5];
		unitsLength=0;
		findMove(attacker);
		if(targetFound){
			System.out.println(attackFrom.xLocation/MapScene.TILE_SIZE+", "+attackFrom.yLocation/MapScene.TILE_SIZE);
			System.out.println(mapScene.getMap()[attackFrom.xLocation/MapScene.TILE_SIZE][attackFrom.yLocation/MapScene.TILE_SIZE].getChain());
			mapScene.moveUnit(attackFrom.xLocation/MapScene.TILE_SIZE, attackFrom.yLocation/MapScene.TILE_SIZE, attacker);
			return minUnit;
		}
		mapScene.removeHighlights();
		return attacker;
	}
	
	public void findMove(Unit attacker){//this is called when you select to move your selected unit.
		//TODO Sets squares to available for moving to, and highlights for player.
		width=mapScene.getMap().length;
		height=mapScene.getMap()[0].length;
		grid=new int[width][height];
		int x=attacker.getXPosition()/MapScene.TILE_SIZE, y=attacker.getYPosition()/MapScene.TILE_SIZE;
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				grid[i][j]=99;
			}
		}
		grid[x][y]=0;
		mapScene.getMap()[x][y].setChain(0);
		mapScene.getMap()[x][y].setPrevious(null);
		findMove(attacker, x, y, x, y, attacker.getMove(), 'f');//'f' will be Unit.getType()
	}
	protected void findMove(Unit u, int x, int y, int prevX, int prevY, int move, char type){//TODO currently, all units are treated as barriers. Should only be ones on team.
		if(move<0)
			return;
		int nextCost;
		boolean isLast=false;
		if(x>0&&mapScene.getMap()[x-1][y].hasUnit()){
			if(mapScene.getMap()[x-1][y].getUnit().getFaction()==Unit.PLAYER)
			if(mapScene.inRange(u, mapScene.getMap()[x-1][y], mapScene.getMap()[x][y])){
				addUnit(mapScene.getMap()[x-1][y].getUnit());
				if(u.getMaxMove()-move<minDistance){
					minDistance=u.getMaxMove()-move;
					minUnit=mapScene.getMap()[x-1][y].getUnit();
					targetFound=true;
					attackFrom=mapScene.getMap()[x][y];
				}
			}
		}
		else if(x>0&&prevX!=x-1){
			nextCost=mapScene.getMap()[x-1][y].getCost(type);
			if(nextCost<=move){
				nextCost+=grid[x][y];
				if(grid[x-1][y]>nextCost){
					grid[x-1][y]=nextCost;
					mapScene.getMap()[x-1][y].setAvailable(true);
					mapScene.getMap()[x-1][y].setPrevious(mapScene.getMap()[x][y]);
					mapScene.getMap()[x-1][y].setChain(mapScene.getMap()[x][y].getChain()+1);
					findMove(u, x-1, y, x, y, move-mapScene.getMap()[x-1][y].getCost(type), type);
				}
				else
					isLast=true;
			}
			else
				isLast=true;
		}
		if(x<width-1&&mapScene.getMap()[x+1][y].hasUnit()){
			if(mapScene.getMap()[x+1][y].getUnit().getFaction()==Unit.PLAYER)
			if(mapScene.inRange(u, mapScene.getMap()[x+1][y], mapScene.getMap()[x][y])){
				addUnit(mapScene.getMap()[x+1][y].getUnit());
				if(u.getMaxMove()-move<minDistance){
					minDistance=u.getMaxMove()-move;
					minUnit=mapScene.getMap()[x+1][y].getUnit();
					targetFound=true;
					attackFrom=mapScene.getMap()[x][y];
				}
			}
		}
		else if(x<width-1&&prevX!=x+1&&!mapScene.getMap()[x+1][y].hasUnit()){
			nextCost=mapScene.getMap()[x+1][y].getCost(type);
			if(nextCost<=move){
				nextCost+=grid[x][y];
				if(grid[x+1][y]>nextCost){
					grid[x+1][y]=nextCost;
					mapScene.getMap()[x+1][y].setAvailable(true);
					mapScene.getMap()[x+1][y].setPrevious(mapScene.getMap()[x][y]);
					mapScene.getMap()[x+1][y].setChain(mapScene.getMap()[x][y].getChain()+1);
					findMove(u, x+1, y, x, y, move-mapScene.getMap()[x+1][y].getCost(type), type);
				}
				else
					isLast=true;
			}
			else
				isLast=true;
		}
		if(y>0&&mapScene.getMap()[x][y-1].hasUnit()){
			if(mapScene.getMap()[x][y-1].getUnit().getFaction()==Unit.PLAYER)
			if(mapScene.inRange(u, mapScene.getMap()[x][y-1], mapScene.getMap()[x][y])){
				addUnit(mapScene.getMap()[x][y-1].getUnit());
				if(u.getMaxMove()-move<minDistance){
					minDistance=u.getMaxMove()-move;
					minUnit=mapScene.getMap()[x][y-1].getUnit();
					targetFound=true;
					attackFrom=mapScene.getMap()[x][y];
				}
			}
		}
		else if(y>0&&prevY!=y-1&&!mapScene.getMap()[x][y-1].hasUnit()){
			nextCost=mapScene.getMap()[x][y-1].getCost(type);
			if(nextCost<=move){
				nextCost+=grid[x][y];
				if(grid[x][y-1]>nextCost){
					grid[x][y-1]=nextCost;
					mapScene.getMap()[x][y-1].setAvailable(true);
					mapScene.getMap()[x][y-1].setPrevious(mapScene.getMap()[x][y]);
					mapScene.getMap()[x][y-1].setChain(mapScene.getMap()[x][y].getChain()+1);
					findMove(u, x, y-1, x, y, move-mapScene.getMap()[x][y-1].getCost(type), type);
				}
				else
					isLast=true;
			}
			else
				isLast=true;
		}
		if(y<height-1&&mapScene.getMap()[x][y+1].hasUnit()){
			if(mapScene.getMap()[x][y+1].getUnit().getFaction()==Unit.PLAYER)
			if(mapScene.inRange(u, mapScene.getMap()[x][y+1], mapScene.getMap()[x][y])){
				addUnit(mapScene.getMap()[x][y+1].getUnit());
				if(u.getMaxMove()-move<minDistance){
					minDistance=u.getMaxMove()-move;
					minUnit=mapScene.getMap()[x][y+1].getUnit();
					targetFound=true;
					attackFrom=mapScene.getMap()[x][y];
				}
			}
		}
		else if(y<height-1&&prevY!=y+1&&!mapScene.getMap()[x][y+1].hasUnit()){
			nextCost=mapScene.getMap()[x][y+1].getCost(type);
			if(nextCost<=move){
				nextCost+=grid[x][y];
				if(grid[x][y+1]>nextCost){
					grid[x][y+1]=nextCost;
					mapScene.getMap()[x][y+1].setAvailable(true);
					mapScene.getMap()[x][y+1].setPrevious(mapScene.getMap()[x][y]);
					mapScene.getMap()[x][y+1].setChain(mapScene.getMap()[x][y].getChain()+1);
					findMove(u, x, y+1, x, y, move-mapScene.getMap()[x][y+1].getCost(type), type);
				}
				else
					isLast=true;
			}
			else
				isLast=true;
		}
		if(isLast){
			findAttack(mapScene.getMap()[x][y], u);
		}
	}
	public void findAttack(Tile t, Unit attacker){//highlights the attackable squares radiating from Tile t. (also sets to attackable)
		//if say an archer (attack range only 2) can't hit a tile, see if that tile can hit a square that's "available".
		int x=t.xLocation/MapScene.TILE_SIZE, y=t.yLocation/MapScene.TILE_SIZE, range=1;//TODO range=weapon.getRange()
		findAttack(t, attacker, x-1, y, range-1);
		findAttack(t, attacker, x+1, y, range-1);
		findAttack(t, attacker, x, y-1, range-1);
		findAttack(t, attacker, x, y+1, range-1);	
	}
	public void findAttack(Tile t, Unit attacker, int x, int y, int range){
		//TODO if mapScene.getMap()[x][y] is a wall, return.
		if(x==attacker.getXPosition()/MapScene.TILE_SIZE&&y==attacker.getYPosition()/MapScene.TILE_SIZE)//tile contains the attacking unit
			return;
		if(x<0||y<0||x>=width||y>=height)//out of bounds
			return;
		if(mapScene.getMap()[x][y].isAttackable()||mapScene.getMap()[x][y].isAvailable())//already highlighted
			return;
		if(mapScene.inRange(attacker, mapScene.getMap()[x][y], t)){
			if(mapScene.getMap()[x][y].hasUnit()){
				if(mapScene.getMap()[x][y].getUnit().getFaction()==Unit.PLAYER){
					if(!targetFound){
						minUnit=mapScene.getMap()[x][y].getUnit();
						minDistance=attacker.getMaxMove()-attacker.getMove();
						targetFound=true;
						attackFrom=t;
					}
					addUnit(mapScene.getMap()[x][y].getUnit());
				}
			}
			mapScene.getMap()[x][y].setAttackable(true);
		}
		if(range<=0){
			return;
		}
		findAttack(t, attacker, x-1, y, range-1);
		findAttack(t, attacker, x+1, y, range-1);
		findAttack(t, attacker, x, y-1, range-1);
		findAttack(t, attacker, x, y+1, range-1);
	}
}
