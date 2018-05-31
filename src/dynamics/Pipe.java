package dynamics;

import java.awt.Color;
import java.awt.Graphics;

public class Pipe {
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getSpaceY() {
		return spaceY;
	}
	public void setSpaceY(int spaceY) {
		this.spaceY = spaceY;
	}
	public int getSpaceW() {
		return spaceW;
	}
	public void setSpaceW(int spaceW) {
		this.spaceW = spaceW;
	}
	private int xPos, w, h, spaceY, spaceW;
	public Pipe(int xPos, int w, int h, int spaceY, int spaceW) {
		this.xPos = xPos;
		this.w = w;
		this.h = h;
		this.spaceY = spaceY;
		this.spaceW= spaceW;
	}
	public void display(Graphics g, int speed) {
		g.setColor(new Color(155, 255, 147));
		g.fillRect(this.xPos, 0, this.w, this.spaceY);
		g.fillRect(xPos, this.spaceW + this.spaceY, this.w, this.h - this.spaceW - this.spaceY);
		update(speed);
	}
	public boolean collisionCheck(Bird b) {
		return false;
	}
	public void update(int speed) {
		this.xPos -= speed;
	}
}
