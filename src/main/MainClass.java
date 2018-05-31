package main;

import javax.swing.JFrame;

public class MainClass {
	static JFrame frame;
	static Display disp;
	public static void main(String[] args) {
		disp = new Display();
		frame = new JFrame("FLAPPY BIRD");
		frame.setSize(500, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(disp);
		frame.setVisible(true);
		frame.setResizable(false);

	}

}
