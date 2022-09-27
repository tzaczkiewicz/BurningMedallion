package burningEngine;

import java.awt.Color;
import java.awt.Graphics;

public class FadeBar extends Bar {/*Only for full screen fades. It seems fillRect works if it starts on the first row or column*/
	public FadeBar(int x, int y, int w, int h, int curr, int max, Color c){
		super(x, y, w, h, curr, max, c);
		this.setOpaque(false);
	}
	
	protected void paintComponent(Graphics g)
	{
		g.setColor( getBackground() );
		g.fillRect(getX(), getY(), getSize().width, getSize().height);
		super.paintComponent(g);
	}
}
