package dynamics;

import java.awt.Color;
import java.awt.Graphics;

public class Bird {
	private int xPos, yPos, w, h;
	double fall;
	public Bird(int x, int y, int w, int h) {
		this.xPos = x;
		this.yPos = y;
		this.w = w;
		this.h = h;
		fall = 0;
	}
	public void display(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(xPos, yPos, w, h);
		update();
	}
	public void update() {
		int add = (int)Math.round((-0.10*Math.pow((fall+3), 2) + 10)); 
		if(add < -8) {
			this.yPos += 8;
		}else {
			this.yPos -= add;
		}
		fall+=0.3;
	}
	public void clicked() {
		fall = 0;
	}
	
}
