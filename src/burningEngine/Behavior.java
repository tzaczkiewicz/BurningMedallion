package burningEngine;

public abstract class Behavior {
	
	protected int[][] grid;
	protected Tile map[][];
	protected int width;
	protected int height;
	protected MapScene mapScene;
	protected Unit[] consideredUnits;
	protected int unitsLength;
	
	protected boolean targetFound;
	protected boolean moving;
	
	public Behavior(MapScene m){
		mapScene=m;
	}
	
	public boolean hasTarget(){
		return targetFound;
	}
	public boolean isMoving(){
		return moving;
	}
	
	protected void extend(){
		Unit[] temp=new Unit[consideredUnits.length*2];
		for(int i=0; i<unitsLength; i++){
			temp[i]=consideredUnits[i];
		}
		consideredUnits=temp;
	}
	protected void addUnit(Unit u){
		if(unitsLength>=consideredUnits.length)
			extend();
		for(int i=0; i<unitsLength; i++){
			if(u.equals(consideredUnits[i])){
				return;
			}
		}
		consideredUnits[unitsLength]=u;
		unitsLength++;
	}
	abstract Unit findTarget(Unit attacker);//finds a target based on the attacker's position and stats.
	abstract void findMove(Unit attacker);
	protected abstract void findMove(Unit u, int x, int y, int prevX, int prevY, int move, char type);
	abstract void findAttack(Tile t, Unit attacker);
	abstract void findAttack(Tile t, Unit attacker, int x, int y, int range);
}
