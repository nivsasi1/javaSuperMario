package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import object.OBJ_Heart;
import object.OBJ_coin;
import java.util.Timer;
import java.util.TimerTask;

public class UI {

	GamePanel gp;
	public Font arial_30 = new Font("Arial", Font.PLAIN, 30);
	
	BufferedImage coinImage;
	BufferedImage hearts;
	int lol;
	Graphics2D g2;
	public int commandNum = 0;
	public int finalScore = 0;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		OBJ_coin coin = new OBJ_coin();
		coinImage = coin.image[0];
		OBJ_Heart heart = new OBJ_Heart();
		hearts = heart.image[0];
	}
	
	public void draw(Graphics2D g2) {
		this.g2 = g2;
		g2.setFont(arial_30);
		g2.setColor(Color.white);
		if (gp.gameState == gp.titleState) {
			drawTitleScreen();
		} else if(gp.gameState == gp.playState) {
			drawGamewPlayScreen();
		} else if (gp.gameState == gp.pauseState) {
			drawPauseScreen();
		} else if (gp.gameState == gp.gameOverState) {
			drawGameOverScreen();
		} else if (gp.gameState == gp.gameWinState) {
			drawGamewWinScreen();
		}
	}
	
	public void drawGamewPlayScreen() {
		drawPlatyerLife();
		g2.drawImage(coinImage, gp.tileSize/2, (int)(gp.tileSize*1.7), gp.tileSize, gp.tileSize, null);
		g2.drawString("x " + gp.player.hashCoin, (int)(gp.tileSize*1.625) , (int)(gp.tileSize*2.5));	
		g2.drawString("Time: " + gp.playTime, gp.tileSize*11, (int)(gp.tileSize*1.354));
	
	}
	
	//todo
	public void drawGamewWinScreen() {

		int coins = gp.player.hashCoin;
		int score = gp.score;
		int time = gp.playTime;
		
		finalScore = coins*100 + score + time*10;
		
		g2.setColor(new Color(0,0,0,150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,82.5F));
		
		text = "You WONNN";
	
		x = getXforCenteredText(text);
		y = gp.tileSize*3;
		//shadow
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		//main color
		g2.setColor(Color.white);
		g2.drawString(text, x, y);

		
		
		g2.setFont(g2.getFont().deriveFont(37.5f));
		text = "Your Score IS ";
		x = getXforCenteredText(text + finalScore);
		y += gp.tileSize*2;
		g2.setColor(Color.PINK);
		g2.drawString(text + finalScore, x, y);
		
		//retry
		if(gp.reason == gp.otherLost) {
			g2.setFont(g2.getFont().deriveFont(37.5f));
			text = "You won cuz he is bad :)";
			x = getXforCenteredText(text);
			y += gp.tileSize*2;
			g2.setColor(Color.PINK);
			g2.drawString(text, x, y);
		}
		else {
		g2.setFont(g2.getFont().deriveFont(30f));
		text = "Wanna get better Score? Retry";
		x = getXforCenteredText(text);
		y += gp.tileSize*3;
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", x-40, y);
		}
		}
		//back to title screen
		g2.setFont(g2.getFont().deriveFont(30f));
		text = "Back to Starter Screen?";
		x = getXforCenteredText(text);
		y += (int)(gp.tileSize*1.5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", x-40, y);
		}
		
		//quit
		g2.setFont(g2.getFont().deriveFont(30f));
		text = "Quit :(";
		x = getXforCenteredText(text);
		y += (int)(gp.tileSize*1.5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(commandNum == 2) {
			g2.drawString(">", x-40, y);
		}
		
	}

	public void drawGameOverScreen() {
		g2.setColor(new Color(0,0,0,150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,82.5F));
		
		text = "Game Over";
		
			
		
		x = getXforCenteredText(text);
		y = gp.tileSize*3;
		//shadow
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		//main color
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		if(gp.reason == gp.timeUp) {
			g2.setFont(g2.getFont().deriveFont(37.5f));
			text = "Your time was Up";
			x = getXforCenteredText(text);
			y += gp.tileSize*2;
			g2.setColor(Color.PINK);
			g2.drawString(text, x, y);
		}
		if(gp.reason == gp.outOflives) {
			g2.setFont(g2.getFont().deriveFont(37.5f));
			text = "No more lives, retry";
			x = getXforCenteredText(text);
			y += gp.tileSize*2;
			g2.setColor(Color.PINK);
			g2.drawString(text, x, y);
		}

		if(gp.reason == gp.otherWon) {
			g2.setFont(g2.getFont().deriveFont(15f));
			text = "You Lose, you score is: ";
			int coins = gp.player.hashCoin;
			int score = gp.score;
			int time = gp.playTime;
			
			finalScore = coins*100 + score + time*10;
			String text2 = " and his: ";
			x = getXforCenteredText(text + finalScore + text2 + gp.opponentPanel.finalScore);
			y += gp.tileSize*2;
			g2.setColor(Color.PINK);
			g2.drawString(text + finalScore + text2 + gp.opponentPanel.finalScore, x, y);
		} else {
		//retry
		
		g2.setFont(g2.getFont().deriveFont(37.5f));
		text = "Retry";
		x = getXforCenteredText(text);
		y += gp.tileSize*2;
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", x-40, y);
		}
		}
		//back to title screen
		g2.setFont(g2.getFont().deriveFont(30f));
		text = "Back to Starter Screen?";
		x = getXforCenteredText(text);
		y += (int)(gp.tileSize*1.5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", x-40, y);
		}
		
		//quit
		g2.setFont(g2.getFont().deriveFont(30f));
		text = "Quit";
		x = getXforCenteredText(text);
		y += (int)(gp.tileSize*1.5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(commandNum == 2) {
			g2.drawString(">", x-40, y);
		}
	}

	public void drawPlatyerLife() {
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		
		int i = 0;
		while (i < gp.player.life) {
			g2.drawImage(hearts, x, y, gp.tileSize, gp.tileSize, null);
			i++;
			x+=(int)gp.tileSize*1.5;
		}
	}
	
	public void drawTitleScreen() { 
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		//title
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 63F));
		String text = "Super Mario Bros";
		int x = getXforCenteredText(text);
		int y = gp.tileSize*3;
		//shadow
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		//main color
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//mario image
		x = gp.screenWidth/2 - (gp.tileSize*2)/2;
		y += gp.tileSize*2;
		g2.drawImage(gp.player.rightS, x, y, gp.tileSize*2, gp.tileSize*2, null);
		
		//menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
		
		text = "NEW GAME";
		x = getXforCenteredText(text);
		y += gp.tileSize*3.5;
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "BE THE SERVER";
		x = getXforCenteredText(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "JOIN OTHER SERVER";
		x = getXforCenteredText(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 2) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "QUIT";
		x = getXforCenteredText(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		if(commandNum == 3) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
	}
	
	public void drawPauseScreen() {
		String text = "PAUSED";
		int x = getXforCenteredText(text);
		int y = gp.screenHeight/2;
		
		g2.drawString(text, x, y);
	}
	
	
	public int getXforCenteredText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	
}
