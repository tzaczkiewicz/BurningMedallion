package burningEngine;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.*;

import org.apache.commons.io.IOUtils;

import java.awt.image.BufferedImage;


public class Animation {
	//public static final String BASE_FOLDER="test/BurningMedallion/";
	ImageIcon[] frames;
	JLabel image;
	Point point;
	JLayeredPane game;
	int[][] velocity;
	String source;
	int enemyHitFrame=0;
	int frame;
	int repeat;
	int repeated;
	int maxRepeat;
	int maxFrames;
	int xVelocity=0, yVelocity=0;
	int width, height;
	
	
	public Animation(String s, JLayeredPane g, int x, int y){
		game=g;
		source=s;
		point=new Point();
		String data = "";
		String[] temp;
		String[] args;
		InputStream stream = getClass().getResourceAsStream(source+"/"+source+".txt");
		try {
			data=StreamToString(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pattern splitter=Pattern.compile("[ \n\r]");
		temp=splitter.split(data);
		args=temp;
		int n=0;
		for(int i=0; i<temp.length; i++){
			while(temp[i].equals(""))
				i++;
			args[n++]=temp[i];
		}
		n=0;
		maxFrames=new Integer(args[n++]);
		frames=new ImageIcon[maxFrames];
		velocity=new int[maxFrames][2];
		repeated=0;
		for(frame=0; frame<maxFrames; frame++){
			maxRepeat=new Integer(args[n++]);
			velocity[frame][0]=new Integer(args[n++]);
			velocity[frame][1]=new Integer(args[n++]);
			if(maxRepeat>0){/*a positive repeat starts by reading a new frame*/
				
				Toolkit tk = Toolkit.getDefaultToolkit();
				URL url = getClass().getResource(source+"/"+source+(frame-repeated)+".png");
				//System.out.println(url.toString());
				Image img = tk.createImage(url);
				tk.prepareImage(img, -1, -1, null);
				
				//frames[frame]=new ImageIcon(source+"/"+source+(frame-repeated)+".png");
				frames[frame]=new ImageIcon(img);
				frames[frame].setImage(createResizedCopy(frames[frame].getImage(), x, y, false));
			}
			else{/*A negative repeat keeps the previous image, but adds a new velocity*/
				frames[frame]=frames[frame-1];
			}
			if(maxRepeat>1)
			for(repeat=1; repeat<maxRepeat; repeat++){
				frame++;
				frames[frame]=frames[frame-1];
				velocity[frame][0]=velocity[frame-1][0];
				velocity[frame][1]=velocity[frame-1][1];
				repeated++;
			}
		}
		frame=0;
		image=new JLabel();
	}
	public Animation(String s, int x, int y){
		this(s, new JLayeredPane(), x, y);
	}
	public void addSelf(){
		game.add(image);
	}
	public void animate(){
		if(frame>=maxFrames)
			frame=0;
		xVelocity=velocity[frame][0];
		yVelocity=velocity[frame][1];
		
		image.setIcon(frames[frame]);
		image.setSize(image.getPreferredSize());
		//image.repaint();
		
		increment();
	}
	public void displayStatic(){
		image.setIcon(frames[frame]);
		image.setSize(image.getPreferredSize());
		//image.repaint();	
	}
	public void increment(){
		point=image.getLocation();
		point.setLocation(point.getX()+getXVelocity(), point.getY()+getYVelocity());
		image.setLocation(point);
		frame++;
		
	}
	public int getMaxFrames(){
		return maxFrames;
	}
	public int getFrame(){
		return frame;
	}
	public ImageIcon getImage(int f){
		return frames[f];
	}
	public JLabel getLabel(){
		return image;
	}
	public int getXVelocity(){
		return xVelocity;
	}
	public int getYVelocity(){
		return yVelocity;
	}
	public void restart(){
		frame=0;
	}
	public void setEnemyHitFrame(int x){
		enemyHitFrame=x;
	}
	public int getEnemyHitFrame(){
		return enemyHitFrame;
	}
	public void moveX(int x){
		point=image.getLocation();
		point.setLocation(point.getX()+x, point.getY());
		image.setLocation(point);
	}
	public void moveY(int y){
		point=image.getLocation();
		point.setLocation(point.getX(), point.getY()+y);
		image.setLocation(point);
	}
	public void setLocation(int x, int y){
		//System.out.println(x+","+y);
		point.move(x, y);
		image.setLocation(point);
	}
	public void setVisible(boolean t){
		image.setVisible(t);
	}
	public void removeSelf(){
		game.remove(image);
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
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
	
	public BufferedImage createResizedCopy(Image originalImage, 
            int scaledWidth, int scaledHeight, 
            boolean preserveAlpha)
{
    //System.out.println("resizing...");
    int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
    Graphics2D g = scaledBI.createGraphics();
    if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
    }
    g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
    g.dispose();
    return scaledBI;
}

}
