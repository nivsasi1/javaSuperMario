package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import background.BackGroundManager;
import entity.Entity;
import entity.Player;
import monster.Goomba;
import object.SuperObject;
import server.Client;
import server.Server;
import tile.TileManager;

//not sure
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable{
	//Screen setting
		final int originalTileSize = 12;
		final int scale = 3;
		
		public final int tileSize = originalTileSize * scale; //36
		public final int maxScreenCol = 16;
		public final int maxScreenRow = 13;
		
		public final int maxScreenColWhenOnline = 32;
		public final int screenWidth2 = tileSize * maxScreenColWhenOnline; //768*2
		public int gameMode = 0; //768
		//gameMode = 0 solo, = 1 server, =2 client
		public final int screenWidth = tileSize * maxScreenCol; //768
		public final int screenHeight = tileSize *maxScreenRow; //624
		
		
		
		//world 1-1 settings
		public final int maxMap = 10;
		public int currentMap = 0;
		public final int[] maxWorldCol = {212,17};
		public final int maxWorldRow = 13;
		public final int worldWidth[] = {tileSize * maxWorldCol[0],tileSize * maxWorldCol[1]};
		public final int worldHeight = tileSize * maxWorldRow;

		int FPS = 120;
		
		
		public TileManager tileM = new TileManager(this);
		BackGroundManager backGround = new BackGroundManager(this);
		KeyHandler keyH = new KeyHandler(this);
		Sound music = new Sound();
		Sound se = new Sound();
		public AssetSetter aSetter = new AssetSetter(this);
		Thread gameThread;
		public UI ui= new UI(this);
		//Entity Object
		public Player player = new Player(this, keyH);
		public SuperObject obj[] = new SuperObject[20];
		GridBagConstraints gbc;
		int[][] array = {{3, 23, 10, 16,0}, {3, 41, 10, 16,0}, {3, 52, 10, 16,0}, {3, 53, 10, 16,0}, {3, 81, 2, 16,0}, {3, 83, 2, 16,0}, {3, 98, 10, 16,0}, {3, 100, 10, 16,0}, {3, 115, 10, 16,0}, {3, 117, 10, 16,0}, {3, 125, 10, 16,0}, {3, 127, 10, 16,0}, {3, 130, 10, 16,0}, {3, 132, 10, 16,0}, {3, 174, 10, 16,0}, {3, 176, 10, 16,0}};
		public List<Goomba> goombas = new ArrayList<>();
		public int[] aliveGoombaX;
		public int[] aliveGoombaY;
		public boolean[] isAliveGoombas;

		//public Goomba gom = new Goomba(this, 3, 41, 9, 16);
		//public Goomba gom1 = new Goomba(this, 3, 23, 9, 16);
		
		public int gameState;
		public final int titleState = 0;
		public final int playState = 1;
		public final int pauseState = 2;
		public final int gameOverState = 3;
		public final int gameWinState = 4;
		JPanel panel;
		public OpponentPanel opponentPanel;
		
		public int score = 0;
		
		Server server;
		Client client;
		JFrame window;
		
		public int reason = 0;
		public final int timeUp = 1;
		public final int outOflives = 2;
		public final int otherWon = 3;
		public final int otherLost = 4;
		
		public Timer timer;
		public TimerTask task;
		public int playTime = 360;
		
		
		
		public GamePanel(JPanel panel, GridBagConstraints gbc, JFrame window) {
			this.gbc = gbc;
			this.window = window;
			this.panel = panel;
			this.setPreferredSize(new Dimension(screenWidth, screenHeight));
			this.setDoubleBuffered(true);	
			this.addKeyListener(keyH);
			this.setFocusable(true);
			resizeGame(this, this.gbc);
		}

		public void resizeGame(GamePanel gamePanel, GridBagConstraints gbc) {
				
			//this not working, trying to go back to single player after multiplayer
			if(gamePanel.gameMode == 0) {
					if (gamePanel.opponentPanel!=null){
						System.out.println("hey");
						opponentPanel.opponentThread.interrupt();
				
						gamePanel.panel.remove(gamePanel.opponentPanel);
						opponentPanel = null;

						gamePanel.window.pack();
						if(gamePanel.server != null)
							gamePanel.server.end();
						else if(gamePanel.client != null)
							gamePanel.client.end();
					}
				}
			
				if(gamePanel.gameMode != 0) {
					opponentPanel = new OpponentPanel(gamePanel);
					//gbc.gridy = 1;
					panel.add(opponentPanel, gbc);
				}
				gamePanel.window.pack();
				//server
				if(gamePanel.gameMode == 1) {
					server = new Server(this);
					server.connect();
					server.startThread();
					keyH.startTimer();

					opponentPanel.startOpponentThread();
					gamePanel.ui.commandNum = 0;
				}
				//client
				if(gamePanel.gameMode == 2) {
					client= new Client(this);
					client.connect();
					client.startThread();
					keyH.startTimer();
					opponentPanel.startOpponentThread();
					gamePanel.ui.commandNum = 0;

				}	
		}

		
		public void startGameThread() {
			gameThread = new Thread(this);
			gameThread.start();
			startGoomba();
		}
		
		public void resetScore() {
			score = 0;
		}
		
		public void startGoomba() {
			int x,y,z,s,t;
		
			for(int i=0; i<array.length; i++) {
				x = array[i][0];
				y = array[i][1];
				z = array[i][2];
				s = array[i][3];
				t = array[i][4];
			    Goomba goomba = new Goomba(this, x, y, z, s, t); // Set the parameters as desired
			    goombas.add(goomba);
			    goomba.start();
			}
		}
		public void killGoomba() {
			for (Goomba goomba : goombas) {
					goomba.alive = false;
			        goomba.interrupt();
			}
			goombas.clear();
		}
		public void setUpGame() {
			aSetter.setObject();
			playMusic(0);
			gameState = titleState;
		}
		
		public int getScreenHeight() {
			return screenHeight;
		}
		
		public int getTileSize() {
			return tileSize;
		}
		
		@Override
		public void run() {
			
			double drawInterval = 1000000000/FPS; // 0.1666 s
			double nextDrawTime = System.nanoTime() + drawInterval;
			double remainingTime = 0;
			
			while(gameThread != null) {
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
		
		public void update() {
			for (Goomba goomba : goombas)
			    if (!goomba.getRuninng()) {
					goomba.alive = false;
					goomba.interrupt();

			    }
			
			if (gameState == playState) {
				player.update();
			}
			
			if(opponentPanel != null) {
				if(opponentPanel.opGameState == gameOverState) {
					reason = otherLost;
					gameState = gameWinState;
				}
				 else if (opponentPanel.opGameState == gameWinState) {
					reason = otherWon;
					gameState = gameOverState;
				}
			}
				else if (gameState == pauseState) {
				//
			}
		}
		
		
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			

			if (gameState == titleState) {
				ui.draw(g2);
			} else {
				backGround.draw(g2);
				tileM.draw(g2); 
				
				for(int i = 0; i < obj.length; i++) {
					if(obj[i] != null)
						obj[i].draw(g2, this);
				}

				player.draw(g2);
				if(goombas != null) {
					int i = 0;
					aliveGoombaX = new int[goombas.size()];
					aliveGoombaY = new int[goombas.size()];
					isAliveGoombas = new boolean[goombas.size()];
					if(goombas!=null) {
						for (Goomba goomba : goombas) {
						    if (goomba != null && goomba.isAlive()) {
						        goomba.draw(g);
						        aliveGoombaX[i] = goomba.x;
						        aliveGoombaY[i] = goomba.y;
						        isAliveGoombas[i] = goomba.alive;
						        i++;
						    } else isAliveGoombas[i] = false;
						}
					}
				}
				ui.draw(g2);

			}		

			g2.dispose();
			
		}

		
		
		//to do simpler func for coins
		public void checkCollisionWithMonster(Entity entity, GamePanel gp) {
			if(goombas !=null) {
				try {
				for (Goomba goomba : goombas) {
					if(goomba != null) {
						if(gp.currentMap == goomba.getMap()) {
							if (goomba.x > -48 && goomba.x < ((gp.maxScreenCol+4)*gp.tileSize)/2 && goomba.isAlive()) {
								entity.solidArea.x = entity.x;
								entity.solidArea.y = entity.y;
								
								goomba.gomSolidArea.x = goomba.x;
								goomba.gomSolidArea.y = goomba.y;
								
								if (entity.solidArea.y + entity.solidArea.height <= goomba.gomSolidArea.y + 10 && entity.solidArea.y + entity.solidArea.height >= goomba.gomSolidArea.y - 10) {
								    entity.solidArea.height += 10; // increase Mario's solid area height temporarily
								    if (entity.solidArea.intersects(goomba.gomSolidArea)) {
								        // Mario is on top of the Goomba, kill the Goomba
								        System.out.println("hey im dead");
								        playSE(1);
								    	killThread(goomba);
								    	score += 100;
								    	System.out.println(score);
								    	System.out.println(goomba.isAlive());
								    }
								    entity.solidArea.height -= 10; // reset Mario's solid area height back to its original value
								}
								else if(goomba.isAlive()){
									switch(entity.direction) {
									case "upR":
										if(entity.solidArea.intersects(goomba.gomSolidArea)) {
									    	killThread(goomba);
									} break;
									case "upL":
										if(entity.solidArea.intersects(goomba.gomSolidArea)) {
											System.out.println("from upL");
									} break;
									case "leftS":
										if(entity.solidArea.intersects(goomba.gomSolidArea)) {
												player.playerDeath();	
												System.out.println("from leftS");
									} break;
									case "rightS":
										if(entity.solidArea.intersects(goomba.gomSolidArea)) {	
											player.playerDeath();
										    //System.out.println(entity.solidArea.y + entity.solidArea.height + " " + goomba.gomSolidArea.y);
											System.out.println("from rightS");
									} break;
									
									}
								}
							}
						}
						//entity.solidArea.x = entity.solidAreaDefaultX;
						//entity.solidArea.y = entity.solidAreaDefaultY;
						if (goomba != null) {
							goomba.gomSolidArea.x = 0;
							goomba.gomSolidArea.y = 0;
						}
					}
				}
				} catch (ConcurrentModificationException e) {
				    System.out.println("Caught ConcurrentModificationException: " + e.getMessage());
				}
			}
		}
		
		private void killThread(Goomba goomba) {
			goomba.alive = false;
			goomba.interrupt();
			player.goombaKilled = true;
		}


		public int checkObject(Entity entity, GamePanel gp) {			
			int index = 999;
			for (int i = 0; i < gp.obj.length; i++) {
				if(gp.obj[i] != null) {
					if(gp.currentMap == obj[i].map) {
						entity.solidArea.x = entity.x + entity.screenX + entity.solidArea.x;
						entity.solidArea.y = entity.y + entity.solidArea.y;
						
						gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
						gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

						switch(entity.direction) {
						case "upR":
							if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
								if(gp.obj[i].collision == true) {
									System.out.println("upR");
									index = i;
								}
						} break;
						case "upL":
							if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
								if(gp.obj[i].collision == true){
									index = i;
								}
						} break;
						case "leftS":
							if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
								if(gp.obj[i].collision == true){
									index = i;
								}
						} break;
						case "rightS":
							if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
								if(gp.obj[i].collision == true){
									index = i;
								}
						} break;
						case "down":
							if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
								if(gp.obj[i].collision == true){
									index = i;
								}
						} break;
							
					}
					}
					entity.solidArea.x = entity.solidAreaDefaultX;
					entity.solidArea.y = entity.solidAreaDefaultY;
					if (gp.obj[i] != null) {
					gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
					gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
					}
				}
			}
			return index;
	}
		
		public void playMusic(int i) {
			music.setFile(i);
			music.play();
			music.loop();
		}
		public void stopMusic() {
			se.stop();
		}
		public void playSE(int i) {
			se.setFile(i);
			se.play();
		}
}
