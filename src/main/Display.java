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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import dynamics.Bird;
import dynamics.Button;
import dynamics.Pipe;

public class Display extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {

	private Timer tm = new Timer(10, this);
	private Bird flappy;
	private Pipe pipes[];
	private Image ground[], background[];
	private int retGroundX,retBackgroundX, randSpaceY, groundX[], backgroundX[], retPipes;
	private Image skins[];
	private Image currentSkin;
	private boolean gameOver = false, isHighScore = false;
	private Font font;
	private int speed = 3;
	private int score = 0, prevScore = 0, highScore = 0;
	private int frameW, frameH;
	private String scene = "menu";
	private int scoreX, scoreY;

	private Button bPlayGame;
	private Button bInstructions;
	private Button bMainMenu;
	private Button bTryAgain;
	private Button bHighScores;
	private Button bClear;
	private Button bRight;
	private Button bLeft;
	private Button bStart;
	private BufferedWriter writer;
	private BufferedReader reader;
	/********************************************************************************/
	public Display(int frameW, int frameH) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		this.frameW = frameW;
		this.frameH = frameH;
		this.bPlayGame = new Button(this,"Play Game", frameW/2, frameH/2 + 50, 35, "gameplay");//change it to skin
		this.bInstructions = new Button(this, "Instructions", frameW/2, frameH/2 + 174, 35, "instructions");
		this.bMainMenu = new Button(this,"Main Menu", frameW/2, frameH/2 + 112, 35, "menu");
		this.bTryAgain = new Button(this,"Try Again", frameW/2, frameH/2 + 50, 35, "false");
		this.bHighScores = new Button(this,"High Scores", frameW/2, frameH/2 + 112, 35, "high scores");
		this.bClear = new Button(this,"Clear", frameW/2, frameH - 78, 35, "clear");
		this.bRight = new Button(this,"   >   ", frameW/2 + 150, frameH/2 + 200, 35, "1");
		this.bLeft = new Button(this,"   <   ", frameW/2 - 150, frameH/2 + 200, 35, "-1");
		this.bStart = new Button(this,"Start", frameW/2 , frameH/2 + 200, 35, "gameplay");
		this.init();
		this.updateHighScore();

	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(scene.equals("menu")) {
			mainMenu(g);
		}else if(scene.equals("instructions")) {
			instructions(g);
		}else if(scene.equals("gameplay")) {
			gamePlay(g);
		}else if(scene.equals("skins")) {
			this.skins(g);
		}else if(scene.equals("high scores")) {
			highScores(g);	
		}
		tm.start();
	}

	/********************************************************************************/
	public void mainMenu(Graphics g) {
		for(int i = 0; i < background.length; i++) {
			g.drawImage(background[i], backgroundX[i], 0, null);
		}
		flappy.display(g);
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
		bHighScores.display(g);
	}
	public void gamePlay(Graphics g) {
		//PROCESSING........................................................................................................................
		//increases the speed every 6 points
		flappy.setxPos(100);
		if(!gameOver) {
			if(score % 5 == 0 && score != prevScore) {
				speed++;
				prevScore = score;
//					increaseGap();
				
			}
			for(int i = 0; i < pipes.length; i++) {
				/*
				 * when pipe travels beyond the screen, makes it return to its "return position" and 
				 * randomizes the y position of the space
				 */
				if(pipes[i].getxPos() + pipes[i].getW() < 0) { 
					this.randSpaceY = (int)(Math.random() * 300) + 125;
					
					pipes[i].returnX();
					if(pipes[i].isGapIncreased()) {
						pipes[i].setRetX(this.retPipes);
						pipes[i].setGapIncreased(false);
					}
					pipes[i].setSpaceY(randSpaceY);
					pipes[i].setBirdPassed(false);
				}
				//increments the score if the bird successfully passed the pipe
				if(!pipes[i].getBirdPassed() && flappy.getxPos() > pipes[i].getxPos()) {
					score++;
					pipes[i].setBirdPassed(true);
				}
				if(!gameOver)gameOver = pipes[i].isColliding(flappy); //checks if the bird is colliding with the pipe
				pipes[i].update(speed);
			}
			for(int i = 0; i < ground.length; i++) {
				groundX[i]-=speed;
				if(groundX[i] + ground[i].getWidth(null)  <= 0) {
					groundX[i] = retGroundX + groundX[i] + ground[i].getWidth(null);
				}
			}
			for(int i = 0; i < background.length; i++) {
				backgroundX[i]-=speed;
				if(backgroundX[i] + background[i].getWidth(null)  <= 0) {
					backgroundX[i] = retBackgroundX + backgroundX[i] + background[i].getWidth(null);
				}
			}
			font =  new Font("Comic Sans MS", Font.BOLD, 70);
			scoreX =frameW - this.getFontMetrics(font).stringWidth(""+score) - this.getFontMetrics(font).getDescent();
			scoreY = this.getFontMetrics(font).getAscent();
			//checks if the bird is touching the ground
			if(!gameOver)gameOver = flappy.getyPos() + flappy.getImg().getHeight(null) >= frameH - ground[0].getHeight(null);
		}else {
			font =  new Font("Comic Sans MS", Font.BOLD, 70);
			scoreX = frameW/2 - this.getFontMetrics(font).stringWidth(""+score)/2;
			scoreY = frameH/2 -  this.getFontMetrics(font).getHeight()/2 + 20;
			if(score > highScore) {
				highScore = score;
				isHighScore = true;
				this.addHighScore(highScore);
			}
		}
		//OUTPUT........................................................................................................................
		for(int i = 0; i < background.length; i++) {
			g.drawImage(background[i], backgroundX[i], 0, null);
		}
		flappy.display(g);
		for(int i = 0; i < pipes.length; i++) {
			pipes[i].display(g);
		}
		for(int i = 0; i < ground.length; i++) {
			g.drawImage(ground[i], groundX[i], frameH - ground[i].getHeight(null), null);
		}
		

		font = new Font("Comic Sans MS", Font.BOLD, 70);
		g.setFont(font);
		if(!isHighScore && score <= highScore)g.setColor(new Color(0,0,0,100));
		else g.setColor(new Color(255,255,0,100));
		g.drawString(""+ score, scoreX, scoreY);

		tm.start();
		if(gameOver) {
			font =  new Font("Comic Sans MS", Font.BOLD, 70);
			g.setFont(font);
			g.setColor(Color.white);
			g.drawString("GAME OVER",  frameW/2 - this.getFontMetrics(font).stringWidth("GAME OVER")/2, frameH/2 -  this.getFontMetrics(font).getHeight()/2 -75);
			font =  new Font("Comic Sans MS", Font.BOLD, 25);
			this.bMainMenu.setPos(frameW/2, frameH/2 + 112);
			this.bMainMenu.display(g);
			this.bTryAgain.display(g);
		}
		else flappy.update();
	}
	public void highScores(Graphics g) {
		background(g);
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, frameW, frameH);
		this.drawTextMiddle(g, "HIGH SCORES", this.frameW/2, 100, 65, new Color(255, 100, 100));
		this.displayHighScores(g);
		bMainMenu.setPos(frameW/2, frameH-140);
		bMainMenu.display(g);
		bClear.display(g);
	}
	public void instructions(Graphics g) {
		background(g);
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, frameW, frameH);
		this.drawTextMiddle(g, "INSTRUCTIONS", this.frameW/2, 100, 58, new Color(255, 100, 100));
		bMainMenu.setPos(frameW/2, frameH-140);
		bMainMenu.display(g);
	}
	public void skins(Graphics g) {
		this.background(g);
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, frameW, frameH);
//		g.drawImage(this.currentSkin, frameW/2 - this.currentSkin.getWidth(null)/2, frameH/2 - this.currentSkin.getHeight(null)/2, null);
		this.bRight.display(g);
		this.bLeft.display(g);
		this.bStart.display(g);
	}
	
	public void init() {
		gameOver = false;
		isHighScore = false;
		speed = 3; score = 0; prevScore = 0; retPipes = 0;
		Image birdImage = new ImageIcon("images/flappyBird.png").getImage();
		Image groundImage = new ImageIcon("images/ground.png").getImage();
		Image backgroundImage = new ImageIcon("images/background.png").getImage();
		flappy = new Bird(birdImage, frameW/2 - birdImage.getWidth(null)/2, frameH/2 - 50);
		pipes = new Pipe[3];
		ground = new Image[3];
		groundX = new int[3];
		background = new Image[3];
		backgroundX = new int[3];
		
		int i;
		for(i = 0; i < ground.length; i++) {
			ground[i] = groundImage;
			groundX[i] = i * ground[i].getWidth(null);
		}
		retGroundX = (i-1) * ground[i-1].getWidth(null);
		
		for(i = 0; i < background.length; i++) {
			background[i] = backgroundImage;
			backgroundX[i] = i * background[i].getWidth(null);
		}
		retBackgroundX = (i-1) * background[i-1].getWidth(null);
		
		for(i = 0; i < pipes.length; i++) {
			this.randSpaceY = (int)(Math.random() * 250) + 125;
			pipes[i] = new Pipe(800 + i * 300, 50, this.frameH, this.randSpaceY, 200, 300 * pipes.length-50);
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
		String currentMinute = LocalTime.now().getMinute() + "";
		String currentDate = LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear();
		if(Integer.parseInt(currentMinute) == 0) currentMinute+="0";
		else if(Integer.parseInt(currentMinute) < 10) {
			char[]copy = new char[2];
			copy[1] = currentMinute.toCharArray()[0];
			copy[0] = '0';
			currentMinute = copy.toString();
		}
		String currentTime = LocalTime.now().getHour() + ":" +currentMinute;
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
	public void updateHighScore() {
		String in = "";
		int a = 0;
		try {
			reader = new BufferedReader(new FileReader("high_scores.txt"));
			in = reader.readLine();
			while(in != null && !in.equals("") ) {
				for(int i =0; i < in.length(); i++) {
					if(in.charAt(i) == '\t') {
						a++;
						if(a == 2) highScore = Integer.parseInt(in.substring(i+1, in.length()));
					}
				}
				a = 0;
				in = reader.readLine();
			}
		}catch(IOException e) {
			System.out.println("file not found");
			this.clearFile();
		}
	}
	/********************************************************************************/
	public void displayHighScores(Graphics g) {
		String in = "", date = "", time = "", scoreO = "", keeper = "", display[] = null; 
		int i = 0;  
		int counter = 0;
		try {
			reader = new BufferedReader(new FileReader("high_scores.txt"));
			in = reader.readLine();
			while(in != null && !in.equals("") ) {
				counter++;
				in = reader.readLine();
			}
			reader.close();
			display = new String[counter];
			counter = 0;
			
			reader = new BufferedReader(new FileReader("high_scores.txt"));
			in = reader.readLine();
			while(in != null && !in.equals("") ) {
				counter++;
				date = this.untilTab(in)[0];
				keeper = this.untilTab(in)[1];
				time = this.untilTab(keeper)[0];
				scoreO = this.untilTab(keeper)[1];
				if(counter < 11) {
					display[i] = "score: " + scoreO +"     "+ date + "  "+time;
				}
				i++;
				in = reader.readLine();
			}
			counter = 1;
			for(i = display.length-1; i >= 0 ; i--) {
				if(counter<5)this.drawTextMiddle(g, display[i], frameW/2, counter * 40 + 140, 30, Color.white);
				counter++;
			}
		}catch(IOException e) {
			System.out.println("file not found");
		}
	}
	public void drawTextMiddle(Graphics g, String txt, int x, int y, int fontSize, Color color) {
		int xPos, yPos;
		font = new Font("Comic Sans MS", Font.BOLD, fontSize);
		g.setFont(font);
		g.setColor(color);
		xPos = x - this.getFontMetrics(font).stringWidth(txt)/2;
		yPos = y + this.getFontMetrics(font).getHeight()/2;
		g.drawString(txt, xPos, yPos);
	}
	public void drawText(Graphics g, String txt, int x, int y, int fontSize, Color color) {
		font = new Font("Comic Sans MS", Font.BOLD, fontSize);
		g.setFont(font);
		g.setColor(color);
		g.drawString(txt, x, y);
	}
	public String[] untilTab(String str) {
		String[]ret = null;
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == '\t') {
				ret = new String[2];
				ret[0] = str.substring(0, i);
				ret[1] = str.substring(i+1, str.length());
				return ret;
			}
		}
		return null;
	}
	public void background(Graphics g) {
		for(int i = 0; i < background.length; i++) {
			g.drawImage(background[i], backgroundX[i], 0, null);
		}
		for(int i = 0; i < ground.length; i++) {
			g.drawImage(ground[i], groundX[i], frameH - ground[i].getHeight(null), null);
		}
	}
/*
//	private void increaseGap() {
//		int i;
//		for(i = 0; i < pipes.length; i++) {
//			pipes[i].addRetX(50 * (i+1) );
//			pipes[i].setGapIncreased(true);
//		}
//		retPipes = pipes[i-1].getRetX() + 50;
//
//	}
*/
	/********************************************************************************/
	public void actionPerformed(ActionEvent event) {
			repaint();
	}	
	/********MOUSEMOTIONLISTENER*******/
	public void mouseMoved(MouseEvent e) {
		if(scene.equals("menu")) {
			this.bPlayGame.mouseOver(e);
			this.bInstructions.mouseOver(e);
			this.bHighScores.mouseOver(e);
		}else if(scene.equals("instructions")) {
			this.bMainMenu.mouseOver(e);
		}else if(scene.equals("gameplay")) {
			this.bTryAgain.mouseOver(e);
			this.bMainMenu.mouseOver(e);
		}else if(scene.equals("skins")) {

		}else if(scene.equals("high scores")) {
			this.bMainMenu.mouseOver(e);
			this.bClear.mouseOver(e);
		}
	}
	public void mouseDragged(MouseEvent e) {}

	/********MOUSELISTENER*******/
	public void mousePressed(MouseEvent e) {
		String check;
		if(scene.equals("menu")) {
			check = bPlayGame.clicked(e);
			if(!check.equals("")) scene = check;
			
			check = bInstructions.clicked(e);
			if(!check.equals("")) scene = check;
			
			check = bHighScores.clicked(e);
			if(!check.equals("")) scene = check;
		}else if(scene.equals("instructions")) {
			check = bMainMenu.clicked(e);
			if(!check.equals("")) scene = check;
		}else if(scene.equals("gameplay")) {
			flappy.clicked();
			
			if(gameOver) {
				check = bTryAgain.clicked(e);
				if(!check.equals("")) init();
				
				check = bMainMenu.clicked(e);
				if(!check.equals("")) {
					init();
					scene = check;
				}
			}
		}else if(scene.equals("skins")) {
		}else if(scene.equals("high scores")) {
			check = bMainMenu.clicked(e);
			if(!check.equals("")) scene = check;
			check = bClear.clicked(e);
			if(!check.equals("")) {
				this.clearFile();
				this.highScore = 0;
			}
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
