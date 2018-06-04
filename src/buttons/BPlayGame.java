package buttons;

import javax.swing.JPanel;

import dynamics.Button;

public class BPlayGame extends Button {

	public BPlayGame(JPanel panel, int x, int y, int fontSize) {
		super(panel, "Play Game", x, y, fontSize);
	}
	public String job(){
		return "gameplay";
	}
	public String clicked(){
		if(mouseIn)	{
			mouseIn = false;
			return this.job();
		}
		return "";
	}
}
