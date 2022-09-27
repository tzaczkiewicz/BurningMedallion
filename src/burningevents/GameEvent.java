package burningevents;

import burningEngine.*;

public abstract class GameEvent {

	MapScene mapScene;
	
	
	public boolean trigger(){
		return false;
	}
	public boolean trigger(Unit starter){//Unit that starts the event
		return false;
	}
}
