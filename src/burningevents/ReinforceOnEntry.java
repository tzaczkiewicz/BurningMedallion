package burningevents;

import burningEngine.*;

public class ReinforceOnEntry extends GameEvent {

	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	private String source;
	
	public ReinforceOnEntry(MapScene m, String source, int left, int right, int up, int down){
		mapScene=m;
		minX=left*MapScene.TILE_SIZE;
		minY=up*MapScene.TILE_SIZE;
		maxX=right*MapScene.TILE_SIZE;
		maxY=down*MapScene.TILE_SIZE;
		this.source=source;
	}
	public boolean trigger(){
		for(int i=0; i<mapScene.getPlayerUnitLength(); i++){
			if(mapScene.getPlayerUnit(i).isAlive()&&minX<=mapScene.getPlayerUnit(i).getXPosition()&&mapScene.getPlayerUnit(i).getXPosition()<=maxX&&mapScene.getPlayerUnit(i).getYPosition()>=minY&&mapScene.getPlayerUnit(i).getYPosition()<=maxY){
				mapScene.loadUnits(source);
				return true;
			}
		}
		
		return false;
	}
}
