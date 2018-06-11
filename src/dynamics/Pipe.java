package dynamics;

import java.awt.Color;
import java.awt.Graphics;

public class Pipe {
	private int xPos, w, h, spaceY, spaceW, retX;
	private boolean birdPassed = false, isGapIncreased = false;
	public Pipe(int xPos, int w, int h, int spaceY, int spaceW,int retX) {
		this.xPos = xPos;
		this.w = w;
		this.h = h;
		this.spaceY = spaceY;
		this.spaceW= spaceW;
		this.retX = retX;
	}
	public void display(Graphics g) {
		g.setColor(new Color(27, 163, 48));
		g.fillRect(this.xPos, 0, this.w, this.spaceY);
		g.fillRect(xPos, this.spaceW + this.spaceY, this.w, this.h - this.spaceW - this.spaceY);
	}
	public boolean isColliding(Bird b) {
		boolean bXin = b.getxPos() + b.getImg().getWidth(null) > this.xPos && b.getxPos()< this.xPos + w;
		boolean bYin = b.getyPos() > spaceY && b.getyPos()+ b.getImg().getHeight(null) < spaceY + spaceW; 
		if(bYin ||!bXin) {
			return false;
		}
		return true;
	}
	public void update(int speed) {
		this.xPos -= speed;
	}
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getW() {
		return w;
	}
	public void setSpaceY(int spaceY) {
		this.spaceY = spaceY;
	}
	public void setBirdPassed(boolean v) {
		this.birdPassed = v;
	}
	public boolean getBirdPassed() {
		return this.birdPassed;
	}
	public void returnX() {
		this.xPos = this.retX;
	}
	public void addRetX(int retX) {
		this.retX += retX;
	}
	public int getRetX() {
		return this.retX;
	}
	public void setRetX(int retX) {
		this.retX = retX;
	}
	public boolean isGapIncreased() {
		return isGapIncreased;
	}
	public void setGapIncreased(boolean isGapIncreased) {
		this.isGapIncreased = isGapIncreased;
	}
	
}
