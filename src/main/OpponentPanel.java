package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import entity.Player;
import object.OBJ_Heart;
import object.OBJ_coin;
import tile.TileManager;

@SuppressWarnings("serial")
public class OpponentPanel extends JPanel implements Runnable{

	
	final int originalTileSize = 12;
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; //48
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 13;
	public final int screenWidth = tileSize * maxScreenCol; //768
	public final int screenHeight = tileSize *maxScreenRow; //624
	
	//map data
	public int mapScreenX, mapScreenY;
	public int currentMap;
	
	//mario data
	public String direction;
	public int marioScreenX, marioX, marioY;
	public TileManager tileMOpponent;
	public Player opPlayer;
	Thread opponentThread;
	int FPS = 120;

	GamePanel gp;
	public int marioWorldX;
	public int spriteNum;
	BufferedImage goombaImage;
	BufferedImage coinImage;
	BufferedImage hearts;

	public int[] aliveGoombaX;
	public int[] aliveGoombaY;
	public boolean[] isAliveGoombas;
	public int opGameState;
	public int finalScore;
	
	public int life;
	public int time;
	public int coins;
	
	
	
	public OpponentPanel(GamePanel gp) {
		this.gp = gp;
		this.tileMOpponent = new TileManager(gp);
		this.opPlayer = new Player(gp, null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true);	
		this.setFocusable(true);
		try {
			OBJ_coin coin = new OBJ_coin();
			coinImage = coin.image[0];
			OBJ_Heart heart = new OBJ_Heart();
			hearts = heart.image[0];
			goombaImage = ImageIO.read(getClass().getResourceAsStream("/monster/goomba1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startOpponentThread() {
		opponentThread = new Thread(this);
		opponentThread.start();
	}
	
	public void update() {
		System.out.println(finalScore);
	}
	
	public void drawGamewPlayScreen(Graphics g) {
		Font arial_30 = new Font("Arial", Font.PLAIN, 30);
		g.setFont(arial_30);
		g.setColor(Color.white);
		drawPlatyerLife(g);
		g.drawImage(coinImage, gp.tileSize/2, (int)(gp.tileSize*1.7), gp.tileSize, gp.tileSize, null);
		g.drawString("x " + coins, (int)(gp.tileSize*1.625) , (int)(gp.tileSize*2.5));	
		g.drawString("Time: " + time, gp.tileSize*11, (int)(gp.tileSize*1.354));
	
	}
	
	public void drawPlatyerLife(Graphics g) {
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		
		int i = 0;
		while (i < life) {
			g.drawImage(hearts, x, y, gp.tileSize, gp.tileSize, null);
			i++;
			x+=(int)gp.tileSize*1.5;
		}
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		gp.backGround.drawOP(g2);
		tileMOpponent.drawOpponent(g2); 
			
//			for(int i = 0; i < obj.length; i++) {
//				if(obj[i] != null)
//					obj[i].draw(g2, this);
//			}

		opPlayer.drawOpponent(g2);
		if(aliveGoombaX != null && this.currentMap == 0) {
			for(int i = 0; i<aliveGoombaX.length; i++) {
				if(isAliveGoombas[i])
					g.drawImage(goombaImage, aliveGoombaX[i], aliveGoombaY[i], gp.tileSize, gp.tileSize, null);
			}
		}		
		drawGamewPlayScreen(g);
		g2.dispose();
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		double drawInterval = 1000000000/FPS; // 0.1666 s
		double nextDrawTime = System.nanoTime() + drawInterval;
		double remainingTime = 0;
		
		while(opponentThread != null) {
			//System.out.println("the game thread is running");
			//update info
			update();
			
			//draw updated info to the screen
			repaint();
			
			try {
				remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime/1000000;
				
				if(remainingTime < 0)
					remainingTime = 0;
				
				Thread.sleep((long) remainingTime);
				
				nextDrawTime += drawInterval;
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}


}
