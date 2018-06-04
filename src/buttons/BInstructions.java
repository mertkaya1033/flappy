package buttons;

import javax.swing.JPanel;

import dynamics.Button;

public class BInstructions extends Button {

	public BInstructions(JPanel panel, int x, int y, int fontSize) {
		super(panel, "Instructions", x, y, fontSize);

	}
	public String job(){
		return "instructions";
	}
	public String clicked(){
		if(mouseIn)	{
			mouseIn = false;
			return this.job();
		}
		return "";
	}
}
