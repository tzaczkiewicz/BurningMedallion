package burningevents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import burningEngine.*;

public class TextOnEntry extends GameEvent {

	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	private String source;
	private String data;
	
	public TextOnEntry(MapScene m, String source, int left, int right, int up, int down){
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
				try {
					data=StreamToString(mapScene.getClass().getResourceAsStream(source));
				} catch (IOException e) {
					e.printStackTrace();
				}
				mapScene.changeText(data);
				return true;
			}
		}
		
		return false;
	}
	public String StreamToString(InputStream stream) throws IOException{
		char[] buff=new char[1024];
		Reader reader=new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		Writer writer=new StringWriter();
		int c;
		try {
			while((c=reader.read(buff))!=-1){
				writer.write(buff, 0, c);
			}
		}finally{stream.close();}
		return writer.toString();
	}
}
