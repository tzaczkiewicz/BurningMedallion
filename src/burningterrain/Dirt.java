package burningterrain;

import javax.swing.JLayeredPane;
import burningEngine.*;

public class Dirt extends Terrain {

	public Dirt(JLayeredPane pane) {
		super(pane);
		name="Dirt";
		footMove=2;
		background=new Animation("Dirt", game, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		// TODO Auto-generated constructor stub
	}

}
