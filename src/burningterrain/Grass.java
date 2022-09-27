package burningterrain;

import javax.swing.JLayeredPane;
import burningEngine.*;

public class Grass extends Terrain {

	public Grass(JLayeredPane pane) {
		super(pane);
		name="Grass";
		footMove=1;
		background=new Animation("Grass", game, MapScene.TILE_SIZE, MapScene.TILE_SIZE);
		// TODO Auto-generated constructor stub
	}

}
