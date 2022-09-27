package burningEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.*;

public abstract class Scene {
	JLayeredPane game;
	JLabel[] background;
	int backgroundLength;
	int[] backgroundFrame;

	public Scene(JLayeredPane g){
		game=g;
	}
	public void displaySelf(){
	}
	public Animation[] extend(Animation[] a){
		Animation[] temp=new Animation[a.length*2];
		for(int i=0; i<a.length; i++){
			temp[i]=a[i];
		}
		return temp;
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
