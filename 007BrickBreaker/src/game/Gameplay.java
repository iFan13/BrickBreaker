package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	//dimension scaling
	private static final int WIDTH = 720, HEIGHT =WIDTH*3/4; //modify to 4:3 aspect ratio
	private static int borderWidth = (int)Math.round(Double.valueOf(WIDTH)*0.005);
	private static int paddleSize = (int)Math.round(Double.valueOf(WIDTH)*0.125);
	private static int ballSize = (int)Math.round(Double.valueOf(WIDTH)*0.025);
	
	private BrickMap levelmap;
	private boolean play = false;
	private boolean winFlag = false;
	private int score = 0;
	private int brickRows = 3;
	private int brickColumns = 8; 
	private int totalBricks = brickRows*brickColumns;
	private Timer timer;
	private int delay = 8;
	
	private int playerX = WIDTH	/4;
	private int playerStartX = playerX;
	
	
	private int ballposX = (int) (Double.valueOf(borderWidth)+Math.round(Double.valueOf(WIDTH)*0.33));
	private int ballStartX = ballposX;
	private int ballposY = (int) (Double.valueOf(borderWidth)+Math.round(Double.valueOf(HEIGHT)*0.80));
	private int ballStartY = ballposY;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private int brickWidth = (int) Math.round(Double.valueOf(WIDTH)*0.1);
	private int brickHeight = (int) Math.round(Double.valueOf(HEIGHT)*0.1);

	public Gameplay() {
		canvasSetup();
		levelmap = new BrickMap(brickRows, brickColumns, brickWidth, brickHeight);
		
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		timer = new Timer(delay,this);
		timer.start();
		
	}
	
	private void canvasSetup() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//borders
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, borderWidth, HEIGHT); //left border
		g.fillRect(0, 0, WIDTH-borderWidth, borderWidth); //top border
		g.fillRect(WIDTH-borderWidth, 0, WIDTH-borderWidth, HEIGHT); //right border
		
		//score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD,(int) Math.round(Double.valueOf(HEIGHT)*0.05)));		
		g.drawString(""+score, (int) Math.round(Double.valueOf(WIDTH)*0.95), (int) Math.round(Double.valueOf(HEIGHT)*0.08));
		
		//level's map
		levelmap.draw((Graphics2D)g);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, HEIGHT-(HEIGHT/10), paddleSize, 10);
		
		//ball
		g.setColor(Color.RED);
		g.fillOval(ballposX, ballposY, ballSize, ballSize);
		if(ballposY > HEIGHT) {
			play = false;
			ballXdir=0;
			ballYdir=0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD,(int) Math.round(Double.valueOf(HEIGHT)*0.1)));
			int textWidth = g.getFontMetrics().stringWidth("Game Over, Score"+score);
			int textHeight = g.getFontMetrics().getMaxAscent();
			g.drawString("Game Over, Score: "+score, (int) Math.round(Double.valueOf(WIDTH)*0.5)-textWidth/2 , (int) (Math.round(Double.valueOf(HEIGHT)*.5)-textHeight/3));
			g.setFont(new Font("serif", Font.BOLD,(int) Math.round(Double.valueOf(HEIGHT)*0.05)));
			textWidth = g.getFontMetrics().stringWidth("Press Enter to Restart");
			g.drawString("Press Enter to Restart", (int)Math.round(Double.valueOf(WIDTH)*0.5)-textWidth/2 , (int)(Math.round(Double.valueOf(HEIGHT)*.5)+textHeight/4));
		}
		
		if(winFlag ==true) {
			play = false;
			ballXdir=0;
			ballYdir=0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD,(int) Math.round(Double.valueOf(HEIGHT)*0.1)));
			int textWidth = g.getFontMetrics().stringWidth("Game Over, Score"+score);
			int textHeight = g.getFontMetrics().getMaxAscent();
			g.drawString("You Win! Score: "+score, (int) Math.round(Double.valueOf(WIDTH)*0.5)-textWidth/2 , (int) (Math.round(Double.valueOf(HEIGHT)*.5)-textHeight/3));
			g.setFont(new Font("serif", Font.BOLD,(int) Math.round(Double.valueOf(HEIGHT)*0.05)));
			textWidth = g.getFontMetrics().stringWidth("Press Enter to Restart");
			g.drawString("Press Enter to Restart", (int)Math.round(Double.valueOf(WIDTH)*0.5)-textWidth/2 , (int)(Math.round(Double.valueOf(HEIGHT)*.5)+textHeight/4));			
		}
		
		g.dispose();
		
		
		
	}
	
	public void moveRight() {
		play = true;
		playerX+=(int) Math.round(Double.valueOf(WIDTH)*0.02); //move 2% of screen
	}
	
	private void moveLeft() {
		play = true;
		playerX-=(int) Math.round(Double.valueOf(WIDTH)*0.02); //move 2% of screen
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(play) {	
			//start movement of ball
			ballposX += ballXdir; 
			ballposY += ballYdir;
			
			//paddle collision
			if(new Rectangle(ballposX, ballposY, ballSize, ballSize).intersects(new Rectangle(playerX,HEIGHT-(HEIGHT/10),paddleSize,10))) {
				ballYdir = -ballYdir;
			}
			
			//brick collision
			
			A: for(int i = 0; i<levelmap.map.length; i++) {
				for(int j = 0; j<levelmap.map[0].length; j++) {
					if(levelmap.map[i][j]>0) {
						int brickX = j*levelmap.brickWidth+levelmap.brickWidth;
						int brickY = i*levelmap.brickHeight+levelmap.brickHeight;
						int brickWidth= levelmap.brickWidth;
						int brickHeight = levelmap.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, ballSize, ballSize);
						Rectangle brickRect = rect;
						
						//brick disappears
						if(ballRect.intersects(brickRect)) {
							levelmap.setBrickValue(0,i,j);
							totalBricks--;
							score++; //score increments
							if(totalBricks == 0) {
								winFlag = true;
							}
							
							
							//collision reaction with brick
							if(ballposX+19 <= brickRect.x || ballposX+1 > brickRect.x+brickRect.width) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							break A;	
							
						}
						
					}
				}
			}
							
			//wall collision detection
			if(ballposX < borderWidth) {
				ballXdir = -ballXdir;
			}
			if(ballposY < borderWidth) {
				ballYdir = -ballYdir;
			}
			if(ballposX > WIDTH-borderWidth-ballSize) {
				ballXdir = -ballXdir;
			}
		}
		
		repaint();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX+paddleSize+borderWidth+(int) Math.round(Double.valueOf(WIDTH)*0.02) >= WIDTH) {
				playerX = WIDTH-paddleSize-borderWidth;
			} else {
				moveRight();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX-(int) Math.round(Double.valueOf(WIDTH)*0.02) <= borderWidth) {
				playerX = borderWidth;
			} else {
				moveLeft();
			}
			
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play=true;
				ballposX = ballStartX;
				ballposY = ballStartY;
				ballXdir = -1;
				ballYdir = -2;
				playerX = playerStartX;
				score = 0;
				totalBricks = brickRows*brickColumns;
				levelmap = new BrickMap(brickRows, brickColumns, brickWidth, brickHeight);
				repaint();
			}
		}
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
