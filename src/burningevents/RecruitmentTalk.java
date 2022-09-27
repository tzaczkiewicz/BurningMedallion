package burningevents;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Pattern;

import burningEngine.MapScene;
import burningEngine.Rank;
import burningEngine.RankManifest;
import burningEngine.Unit;

public class RecruitmentTalk extends GameEvent {
	
	private Unit owner;
	private String[] targets;
	private String[][] text;
	private int activeDialog;//Which dialog array runs
	private int activeLine;//Where in the dialog array it currently is

	public RecruitmentTalk(MapScene m, Unit owner, String source){
		mapScene=m;
		this.owner=owner;
		parseSource(source);
		activeDialog=activeLine=-1;
	}
	
	public void parseSource(String source){
		/*
		 * Format:
		 * First char: number of classes that can trigger this
		 * then groups of
		 * 1: Number of conversation lines
		 * 2: Rank name that triggers this conversation
		 * 3+: Conversation line
		 */
		String data = "";
		String[] temp;
		String[] args;
		InputStream stream = getClass().getResourceAsStream(source);
		try {
			data=StreamToString(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pattern splitter=Pattern.compile("[\n\r]");
		temp=splitter.split(data);
		args=temp;
		int n=0;
		for(int i=0; i<temp.length; i++){
			while(temp[i].equals(""))
				i++;
			args[n++]=temp[i];
		}
		n=0;
		text=new String[new Integer(args[n++]).intValue()][];
		targets=new String[text.length];
		for(int i=0; i<text.length; i++){
			text[i]=new String[new Integer(args[n++])];
			targets[i]=args[n++];//targets are the names that the owner of this event can talk to
			for(int j=0; j<text[i].length; j++){
				text[i][j]=args[n++].replace("*", "\n");
			}
		}
		owner.setTalkTargets(targets);
	}
	
	public boolean trigger(){
		if(activeDialog==-1||activeLine==-1)
			return false;
		if(activeLine>=text[activeDialog].length){
			//Change unit allegiance here
			Unit[] temp=mapScene.getMapUnits();
			for(int i=0; i<temp.length; i++){
				if(temp[i].equals(owner)){
					mapScene.removeEnemyUnit(i);
					mapScene.addPlayerUnit(owner);
					mapScene.increaseActivePlayers(1);
					mapScene.endDialog();
					owner.refreshIdleAnimation();
					owner.setTalkTargets(new String[0]);
					return true;
				}
			}
			mapScene.endDialog();
			owner.setTalkTargets(new String[0]);
			owner.endTurn();
			return true;
		}
		mapScene.setDialog(text[activeDialog][activeLine++], this);
		return false;
	}
	public boolean trigger(Unit starter){
		for(int i=0; i<targets.length; i++){
			if(starter.getName().equals(targets[i])){//targets[i] should line up with text[i].
				activeDialog=i;
				activeLine=0;
			}
		}
		return trigger();
	}
	
	private String StreamToString(InputStream stream) throws IOException{
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
