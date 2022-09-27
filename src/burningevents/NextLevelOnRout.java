package burningevents;

import burningEngine.*;

public class NextLevelOnRout extends GameEvent{
	
	private String nextLevel;

	public NextLevelOnRout(MapScene m, String nextLevel){
		mapScene=m;
		this.nextLevel=nextLevel;
	}
	
	public boolean trigger(){
		int count=0;
		Unit[] temp=mapScene.getMapUnits();
		int length=mapScene.getMapUnitLength();
		for(int i=0; i<length; i++){
			if(temp[i].isAlive()){
				count++;
			}
		}
		if(count>0)
			return false;
		else{
			mapScene.activateNextLevel(nextLevel);
			return true;
		}
	}
	
}
