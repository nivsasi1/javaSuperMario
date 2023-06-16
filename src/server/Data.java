package server;

import java.io.Serializable;
import main.GamePanel;

@SuppressWarnings("serial")
public class Data implements Serializable {
	transient GamePanel gp;
	
	//map data
	public int mapScreenX, mapScreenY;
	public int currentMap;
	
	//mario data
	public String direction;
	public int marioScreenX, marioX, marioY, marioWorldX;
	
	public int spriteNum;
	
	public int[] aliveGoombaX;
	public int[] aliveGoombaY;
	public boolean[] isAliveGoombas;
	
	public int gameState;
	public int finalScore;
	
	public int lives;
	public int time;
	public int coins;
	
	
	public Data(GamePanel gp) {
		this.gp = gp;
	}

	public void upload() {
		this.marioX = gp.player.x;
		this.marioY = gp.player.y;
		this.marioScreenX = gp.player.screenX;
		this.marioWorldX = gp.player.worldX;
		this.mapScreenX = gp.tileM.mapScreenX;
		this.mapScreenY = gp.tileM.mapScreenY;
		this.currentMap = gp.currentMap;
		this.direction = gp.player.direction;
		this.spriteNum = gp.player.spriteNum;
		if(gp.aliveGoombaX != null) {
			aliveGoombaX = new int[gp.aliveGoombaX.length];
			aliveGoombaY = new int[gp.aliveGoombaY.length];
			isAliveGoombas = new boolean[gp.isAliveGoombas.length];
			for (int i = 0; i < gp.isAliveGoombas.length; i++) {
			       this.aliveGoombaX[i] = gp.aliveGoombaX[i];
			       this.aliveGoombaY[i] = gp.aliveGoombaY[i];
			       this.isAliveGoombas[i] = gp.isAliveGoombas[i];
			}
		}
		this.gameState = gp.gameState;
		if(gp.gameState == gp.gameWinState)
			this.finalScore = gp.ui.finalScore;
		else
			this.finalScore = 0;
		this.lives = gp.player.life;
		this.time = gp.playTime;
		this.coins = gp.player.hashCoin;
	}

	public void download(GamePanel gamePanel) {
		
		gamePanel.opponentPanel.mapScreenX = this.mapScreenX;
		gamePanel.opponentPanel.mapScreenY = this.mapScreenY;
		gamePanel.opponentPanel.currentMap = this.currentMap;
		gamePanel.opponentPanel.direction = this.direction;
		gamePanel.opponentPanel.marioScreenX = this.marioScreenX;
		gamePanel.opponentPanel.marioX = this.marioX;
		gamePanel.opponentPanel.marioY = this.marioY;
		gamePanel.opponentPanel.marioWorldX = this.marioWorldX;
		gamePanel.opponentPanel.spriteNum = this.spriteNum;
		if(this.aliveGoombaX != null && gamePanel.aliveGoombaX != null) {
			gamePanel.opponentPanel.aliveGoombaX = new int[this.aliveGoombaX.length];
			gamePanel.opponentPanel.aliveGoombaY = new int[this.aliveGoombaY.length];
			gamePanel.opponentPanel.isAliveGoombas = new boolean[this.isAliveGoombas.length];
			try{
				for (int i = 0; i < gamePanel.isAliveGoombas.length; i++) {
						gamePanel.opponentPanel.aliveGoombaX[i] = aliveGoombaX[i];
						gamePanel.opponentPanel.aliveGoombaY[i] = aliveGoombaY[i];
					
					gamePanel.opponentPanel.isAliveGoombas[i] = isAliveGoombas[i];
				}				
			} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
			}	
		}
		gamePanel.opponentPanel.opGameState = this.gameState;
		gamePanel.opponentPanel.finalScore = this.finalScore;

		gamePanel.opponentPanel.life = this.lives;
		gamePanel.opponentPanel.time = this.time;
		gamePanel.opponentPanel.coins = this.coins;

	}
}
