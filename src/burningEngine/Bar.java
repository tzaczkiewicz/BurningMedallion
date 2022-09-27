package burningEngine;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class Bar extends JLabel {
	private Color color;
	private int width;
	private int height;
	private double percentage;
	private int current;
	private int maximum;
	
	private static final long serialVersionUID=1L;
	
	
	/*public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.fillRect(100, 100, 100, 100);
		System.out.println("bleh");
	}*/
	public Bar(){
		this.setOpaque(true);
		this.setBackground(Color.red);
		setText("100");
	}
	public Bar(int x, int y, int w, int h, int curr, int max, Color c){
		super("", JLabel.CENTER);
		this.setOpaque(true);
		//setText("99");
		width=w;
		height=h;
		current=curr;
		maximum=max;
		percentage=(double)curr/max;
		color=c;
		this.setLocation(x, y);
		this.setSize((int)(width*percentage), height);
		this.setBackground(c);
	}
	
	/*protected void paintComponent(Graphics g)
	{
		g.setColor( getBackground() );
		g.fillRect(getX(), getY(), getSize().width, getSize().height);
		super.paintComponent(g);
	}*/

	public void setXPosition(int x){
		this.setLocation(x, getY());
	}
	public void setYPosition(int y){
		this.setLocation(getX(), y);
	}
	public void setWidth(int w){
		width=w;
		this.setSize((int)(width*percentage), height);
	}
	public void setHeight(int h){
		height=h;
	}
	public double getPercentage(){
		if(percentage<1&&percentage>=0)
			return percentage;
		if(percentage<0)
			return 0;
		return 1;
	}
	public void setCurrent(int c){
		current=c;
		percentage=((double)current/maximum);
		this.setSize((int)(width*percentage), height);
	}
	public void changeCurrent(int add){
		current=current+add;
		percentage=((double)current/maximum);
		this.setSize((int)(width*percentage), height);
	}
	public void setMax(int m){
		maximum=m;
		percentage=((double)current/maximum);
		this.setSize((int)(width*percentage), height);
	}
	public void setColor(Color c){
		color=c;
		this.setBackground(color);
	}
	public int getAlpha(){
		return color.getAlpha();
	}
	public void setAlpha(int a){
		color=new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
		this.setBackground(color);
	}
}
