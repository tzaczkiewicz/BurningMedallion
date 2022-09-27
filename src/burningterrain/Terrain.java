package burningterrain;

import javax.swing.JLayeredPane;
import burningEngine.*;

public abstract class Terrain {
	protected String name;
	protected int footMove;//value foot soldiers deduct from their move
	protected Animation background;//change to animation for moving backgrounds? Probably won't, but may for rivers.
	protected JLayeredPane game;
	
	
	public Terrain(JLayeredPane pane){
		name="Default";
		footMove=1;
		game=pane;
		//background=grass picture?
	}
	public Animation getBackground(){
		return background;
	}
	public int getFootMove(){
		return footMove;
	}
}
