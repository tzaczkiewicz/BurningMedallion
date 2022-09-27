package burningevents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import burningEngine.MapScene;
import burningEngine.Unit;

public class TextOnRout extends GameEvent{

	private String source;
	private String data;
	
	public TextOnRout(MapScene m, String source){
		mapScene=m;
		this.source=source;
		try {
			data=StreamToString(mapScene.getClass().getResourceAsStream(source));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			mapScene.changeText(data);
			return true;
		}
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
