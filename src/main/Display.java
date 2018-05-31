package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import dynamics.Bird;
import dynamics.Pipe;

public class Display extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
	
	private Timer tm = new Timer(10, this);
	private Bird flappy = new Bird(100, 300, 30, 30);
	private Pipe myPipe = new Pipe(800, 50, 750, 300, 200);
	private Pipe pipes[];
	private int retX, randSpaceY;
	/********************************************************************************/
	public Display() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		pipes = new Pipe[10];
		int i;
		for(i = 0; i < pipes.length; i++) {
			pipes[i] = new Pipe(800 + i * 300, 50, 750, 300, 200);
		}
		retX = 800 + (i-1) * 300;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(new Color(188, 214, 255));
		flappy.display(g);
//		myPipe.display(g, 2);
		for(int i = 0; i < pipes.length; i++) {
			pipes[i].display(g, 2);
			if(pipes[i].getxPos() + pipes[i].getW() < 0) {
				pipes[i].setxPos(retX);
			}
		}
		tm.start();
	}
	
	/********************************************************************************/
	public void actionPerformed(ActionEvent event) {
		repaint();
	}
	
	/********MOUSEMOTIONLISTENER*******/ 
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	
	/********MOUSELISTENER*******/
	public void mousePressed(MouseEvent e) {
		flappy.clicked();
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	/********KEYLISTENER*******/
	public void keyPressed(KeyEvent e) {

	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
