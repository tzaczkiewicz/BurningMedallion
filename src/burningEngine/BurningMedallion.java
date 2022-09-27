package burningEngine;

import javax.swing.*;

import burningterrain.TerrainManifest;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BurningMedallion extends JFrame implements ActionListener, MouseListener, KeyListener{
	public static final long FRAMES_PER_SECOND=30;
	public static final long LOOP_TIME_NANOS=1000000000/FRAMES_PER_SECOND;/*nanos in a second divided by FPS*/
	public static final long LOOP_TIME_MILIS=1000/FRAMES_PER_SECOND;
	public static final long NANOS_TO_MILIS=1000000;
	private JLayeredPane pane;
	
	
	public static final int MENU=0;
	public static final int OPTIONS=1;
	public static final int MAP=2;
	public static final int BATTLE=3;
	
	private int state=MAP;/*0=Menu, 1=Options Menu, 2=MapScene, 3=BattleScene(no focus? Or perhaps "press escape to skip")*/
	
	private MapScene mapScene;
	
	/**
	 * @param args
	 */

	
	/*public void init(){
		setSize(800,600);
		//setResizable(false);
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		pane=new JLayeredPane();
		this.add(pane);
		pane.setSize(800,600);
		pane.setLocation(0, 0);
		//pane.setVisible(false); //Tiles appearing can be my loading screen for now.
		//setTitle("Burning Medallion");
		this.addKeyListener(this);
		this.addMouseListener(this);
		
		TerrainManifest.init(pane);
		RankManifest.init(pane);
		mapScene=new MapScene(this, pane);
		play();
	}*/
	public BurningMedallion(){
		setSize(804,625);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		pane=new JLayeredPane();
		this.add(pane);
		pane.setSize(800,600);
		pane.setLocation(0, 0);
		//pane.setVisible(false); //Tiles appearing can be my loading screen for now.
		//setTitle("Burning Medallion");
		this.addKeyListener(this);
		this.addMouseListener(this);
		
		TerrainManifest.init(pane);
		RankManifest.init(pane);
		mapScene=new MapScene(this, pane);
		play();
	}
	
	public void play(){
		long loop, elapsed;
		mapScene.loadMap("testMap1.txt");
		this.requestFocus();
		while(true){
			loop=System.nanoTime();
			mapScene.displaySelf();
			/*if(!this.hasFocus())
				this.requestFocusInWindow();*/
			this.repaint();
			
			
			
			elapsed=System.nanoTime()-loop;
			if(elapsed<LOOP_TIME_NANOS){
				try {
					Thread.sleep((LOOP_TIME_NANOS-elapsed)/NANOS_TO_MILIS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void setState(int x){
		state=x;
	}
	
	public static void main(String[] args) {
		new BurningMedallion();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("bleh1");
		if(state==2)
			mapScene.mouseClicked(arg0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("bleh2");
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("bleh3");
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("bleh4");
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("bleh5");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(state==MAP)
			mapScene.keyPressed(arg0);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(state==MAP)
			mapScene.keyReleased(arg0);
		if(state==BATTLE)
			mapScene.getBattleScene().keyReleased(arg0);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(state==MAP)
			mapScene.keyTyped(arg0);
	}

}
