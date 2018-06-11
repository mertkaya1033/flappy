package main;

/**	MainClass.java
 * 
 * Description:	The flappy bird game with little more!!! Keeps your top five high scores 
 * 				with the dates which were accomplished. Also with a choice of birds!!!
 * 
 * @author M Kaya
 * @version 4.1 (Last updated: June 8, 2018)
 */

import javax.swing.JFrame;


public class MainClass {
	static JFrame frame;
	static Display disp;
	public static void main(String[] args) {
		disp = new Display(1500, 750);
		frame = new JFrame("FLAPPY BIRD");
		frame.setSize(1500, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(disp);
		frame.setVisible(true);
		frame.setResizable(false);
	}

}
