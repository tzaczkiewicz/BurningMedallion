package burningterrain;

import javax.swing.JLayeredPane;

public final class TerrainManifest {
	public static final int GRASS=1;
	public static final int DIRT=2;
	private static Terrain Grass;
	private static Terrain Dirt;
	
	public static void init(JLayeredPane g){
		Grass=new Grass(g);
		Dirt=new Dirt(g);
	}
	
	public static Terrain translate(int t, JLayeredPane g){/*CALL INIT BEFORE THIS*/
		switch(t){
		case GRASS:
			return Grass;
		case DIRT:
			return Dirt;
		default:
			return Grass;
		}
	}
}
