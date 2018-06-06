package dynamics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Bird {
	private int xPos, yPos;
	double fall;
	Image img;
	public Bird(Image img, int x, int y) {
		this.xPos = x;
		this.yPos = y;
		this.img = img;
		fall = 0;
		
	}
	public void display(Graphics g) {
		g.drawImage(img, xPos, yPos, null);
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
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public void clicked() {
		fall = 0;
	}
	public Image getImg() {
		return img;
	}

	
}
