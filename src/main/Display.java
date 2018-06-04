package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import buttons.BInstructions;
import buttons.BPlayGame;
import dynamics.Bird;
import dynamics.Pipe;

public class Display extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {

	private Timer tm = new Timer(10, this);
	private Bird flappy;
	private Pipe pipes[];
	private Image ground[];
	private int retGroundX, randSpaceY, groundX[];
	private boolean gameOver = false;
	private Font font = new Font("Comic Sans MS", Font.BOLD, 70);
	private int speed = 3;
	private int score = 0, prevScore = 0, highScore = 0;
	private int frameW, frameH;
	private String scene = "menu";
	private BufferedWriter writer;
	
	private BPlayGame bPlayGame;
	private BInstructions bInstructions;
	/********************************************************************************/
	public Display(int frameW, int frameH) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		this.frameW = frameW;
		this.frameH = frameH;
		this.bPlayGame = new BPlayGame(this, frameW/2, frameH/2 + 50, 35);
		this.bInstructions = new BInstructions(this, frameW/2, frameH/2 + 112, 35);
		this.init();
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(scene.equals("menu")) {
			mainMenu(g);
		}else if(scene.equals("instructions")) {

		}else if(scene.equals("gameplay")) {
			gamePlay(g);
		}else if(scene.equals("skins")) {

		}
		tm.start();
	}

	/********************************************************************************/
	public void mainMenu(Graphics g) {
		g.setColor(new Color(188, 214, 255));
		g.fillRect(0, 0, frameW, frameH);
		flappy.display(g);
		for(int i = 0; i < ground.length; i++) {
			g.drawImage(ground[i], groundX[i], frameH - ground[i].getHeight(null), null);
		}
		font = new Font("Comic Sans MS", Font.BOLD, 70);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("FLAPPY BIRD",frameW/2 - this.getFontMetrics(font).stringWidth("FLAPPY BIRD")/2, 267);
		bPlayGame.display(g);
		bInstructions.display(g);

	}
	public void gamePlay(Graphics g) {
		//PROCESSING........ 
		//increases the speed every 6 points
		flappy.setxPos(100);
		if(score % 6 == 0 && score != prevScore) {
			speed++;
			prevScore = score;
		}
		for(int i = 0; i < pipes.length; i++) {
			/*
			 * when pipe travels beyond the screen, makes it return to its "return position" and 
			 * randomizes the y position of the space
			 */
			if(pipes[i].getxPos() + pipes[i].getW() < 0) { 
				this.randSpaceY = (int)(Math.random() * 300) + 125;
				pipes[i].returnX();
				pipes[i].setSpaceY(randSpaceY);
				pipes[i].setBirdPassed(false);
			}
			//increments the score if the bird successfully passed the pipe
			if(!pipes[i].getBirdPassed() && flappy.getxPos() > pipes[i].getxPos()) {
				score++;
				pipes[i].setBirdPassed(true);
			}
			if(!gameOver)gameOver = pipes[i].isColliding(flappy); //checks if the bird is colliding with the pipe
		}
		for(int i = 0; i < ground.length; i++) {
			groundX[i]-=speed;
			if(groundX[i] + ground[i].getWidth(null)  <= 0) {
				groundX[i] = retGroundX + groundX[i] + ground[i].getWidth(null);
			}
		}
		
		//checks if the bird is touching the ground
		if(!gameOver)gameOver = flappy.getyPos() + flappy.getImg().getHeight(null) >= frameH - ground[0].getHeight(null);

		//OUTPUT
		g.setColor(new Color(188, 214, 255));
		g.fillRect(0, 0, frameW, frameH);
		flappy.display(g);
		for(int i = 0; i < pipes.length; i++) {
			pipes[i].display(g, speed);
		}
		for(int i = 0; i < ground.length; i++) {
			g.drawImage(ground[i], groundX[i], frameH - ground[i].getHeight(null), null);
		}

		font = new Font("Comic Sans MS", Font.BOLD, 70);
		g.setFont(font);
		g.setColor(new Color(0,0,0,100));
		g.drawString(""+ score, frameW - this.getFontMetrics(font).stringWidth(""+score) - this.getFontMetrics(font).getDescent(), this.getFontMetrics(font).getAscent());

		tm.start();
		if(gameOver) {
			if(score > highScore) {
				highScore = score;
				this.addHighScore(highScore);
			}
			font =  new Font("Comic Sans MS", Font.BOLD, 70);
			g.setFont(font);
			g.setColor(Color.red);
			g.drawString("GAME OVER", frameW/2 - this.getFontMetrics(font).stringWidth("GAME OVER")/2, frameH/2 -  this.getFontMetrics(font).getHeight()/2);
			font =  new Font("Comic Sans MS", Font.BOLD, 25);
			g.setFont(font);
			g.setColor(Color.red);
			g.drawString("Click to play again", frameW/2 - this.getFontMetrics(font).stringWidth("Press R to play again")/2, frameH/2 -  this.getFontMetrics(font).getHeight()/2);
		}
		flappy.update();
	}
	public void init() {
		Image birdImage = new ImageIcon("images/flappyBird.png").getImage();
		flappy = new Bird(birdImage, frameW/2 - birdImage.getWidth(null)/2, frameH/2 - 50);
		pipes = new Pipe[5];
		ground = new Image[3];
		groundX = new int[3];
		int i;
		for(i = 0; i < ground.length; i++) {
			ground[i] =  new ImageIcon("images/ground.png").getImage();
			groundX[i] = i * ground[i].getWidth(null);
		}
		retGroundX = (i-1) * ground[i-1].getWidth(null);
		for(i = 0; i < pipes.length; i++) {
			this.randSpaceY = (int)(Math.random() * 300) + 125;
			pipes[i] = new Pipe(800 + i * 300, 50, this.frameH, this.randSpaceY, 200, 300 * pipes.length);
		}
	}
	public void clearFile() {
		try {
			writer = new BufferedWriter(new FileWriter("high_scores.txt", false));
			writer.close();
		} catch (IOException e) {
			System.out.println("file not found");
		}
	}
	public void addHighScore(int score) {
		addHighScore(score, true);
	}
	public void addHighScore(int score, boolean appendFile) {
		String currentDate = LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear();
		String currentTime = LocalTime.now().getHour() + ":" + LocalTime.now().getMinute();
		try {
			writer = new BufferedWriter(new FileWriter("high_scores.txt", appendFile));
			writer.append(currentDate + '\t' + currentTime+ '\t' + score);
			System.out.println(currentDate + '\t' + currentTime+ '\t' + score);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			System.out.println("file not found");
		}
	}
	/********************************************************************************/
	public void actionPerformed(ActionEvent event) {
		if(!gameOver)
			repaint();
	}
	/********MOUSEMOTIONLISTENER*******/ 
	public void mouseMoved(MouseEvent e) {
		if(scene.equals("menu")) {
			this.bPlayGame.mouseOver(e);
			this.bInstructions.mouseOver(e);
		}else if(scene.equals("instructions")) {

		}else if(scene.equals("gameplay")) {
			
		}else if(scene.equals("skins")) {

		}
	}
	public void mouseDragged(MouseEvent e) {}

	/********MOUSELISTENER*******/
	public void mousePressed(MouseEvent e) {
		String check;
		if(scene.equals("menu")) {
			check = bPlayGame.clicked();
			if(!check.equals("")) scene = check;
			check = bInstructions.clicked();
			if(!check.equals("")) scene = check;
			
		}else if(scene.equals("instructions")) {

		}else if(scene.equals("gameplay")) {
			flappy.clicked();
			if(gameOver) {
				gameOver = false;
				speed = 3;
				score = 0;
				prevScore = 0;
				init();
			}
		}else if(scene.equals("skins")) {

		}
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	/********KEYLISTENER*******/
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
