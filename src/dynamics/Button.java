package dynamics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class Button{

	private int xPos, yPos, width, height, fontSize, txtXpos, txtYpos, roundSize,x ,y;
	private String txt;
	private Color colour, txtColour;
	private Font font;
	private boolean mouseIn = false;
	private String job;
	private JPanel panel;
	
	public Button(JPanel panel,String txt, int x, int y, int fontSize, String job) {
		this.colour = new Color(255, 200, 0);
		this.txtColour = Color.WHITE;
		this.panel = panel;
		
		this.x = x;
		this.y = y;
		this.txt = txt;
		this.job = job;
		this.fontSize = fontSize;
		this.font = new Font("Comic Sans MS", Font.PLAIN, this.fontSize);
		
		this.width = panel.getFontMetrics(this.font).stringWidth(txt);
		this.height = panel.getFontMetrics(this.font).getHeight();
		this.roundSize = this.width / 5;
		
		this.xPos = (x - this.roundSize / 2) - (width / 2);
		this.yPos = y - this.height / 2;
		
		this.txtXpos = x - this.width / 2;
		this.txtYpos = y + this.height /2 - panel.getFontMetrics(font).getDescent();
	}
	public void display(Graphics g) {
		
		g.setFont(this.font);
		g.setColor(colour);
		g.fillRoundRect(xPos, yPos, width + roundSize, height, roundSize, height);

		g.setColor(txtColour);
		g.drawString(txt, txtXpos, txtYpos);
		if(this.mouseIn) {
			g.setColor(Color.blue);
			g.drawRoundRect(xPos, yPos, width + roundSize, height, roundSize, height);		
		}
	}
	public void setxPos(int x) {
		this.x = x;
		this.xPos = (x - this.roundSize / 2) - (width / 2);
		this.txtXpos = x - this.width / 2;	
	}
	public void setyPos(int y) {
		this.y = y;
		this.yPos = y - this.height / 2;
		this.txtYpos = y + this.height /2 - panel.getFontMetrics(font).getDescent();	
	}
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		this.xPos = (x - this.roundSize / 2) - (width / 2);
		this.yPos = y - this.height / 2;
		this.txtXpos = x - this.width / 2;
		this.txtYpos = y + this.height /2 - panel.getFontMetrics(font).getDescent();
	}
	public void mouseOver(MouseEvent e) {
		boolean isMouseXIn = e.getX() > this.x  - (this.width / 2 + this.roundSize / 2)&& e.getX() < this.x + (this.width / 2 + this.roundSize / 2);
		boolean isMouseYIn = e.getY() > this.y - this.height / 2 && e.getY() < this.y + this.height / 2;
		if(isMouseXIn && isMouseYIn) {
			this.mouseIn = true;
		}else {
			this.mouseIn = false;
		}
	}
	public String clicked(MouseEvent e){
		mouseOver(e);
		if(mouseIn)	{
			mouseIn = false;
			return this.job;
		}
		return "";
	}
	
}