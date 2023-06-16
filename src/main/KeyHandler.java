package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import entity.Entity;
import entity.Player;

public class KeyHandler implements KeyListener{
	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed;

	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
		
	}
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(gp.gameState == gp.gameOverState || gp.gameState == gp.gameWinState) {
			gameOverState(code);
		}
			
		if(gp.gameState == gp.titleState) {
			if(code == KeyEvent.VK_W || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {
				if(gp.ui.commandNum>0)
					gp.ui.commandNum--;
				else
					gp.ui.commandNum = 3;
			}
			if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
				if(gp.ui.commandNum < 3)
					gp.ui.commandNum++;
				else 
					gp.ui.commandNum = 0;
			}
			if(code == KeyEvent.VK_ENTER) {
				if(gp.ui.commandNum == 0) {
					
					//start timer
					gp.playTime = 360;
					startTimer();
					
					gp.gameState = gp.playState;
				}
				else if(gp.ui.commandNum == 1) {			
					gp.gameMode = 1;
					gp.resizeGame(gp, gp.gbc);
					gp.gameState = gp.playState;
				}else if(gp.ui.commandNum == 2){
					gp.gameMode = 2;
					gp.resizeGame(gp, gp.gbc);
					gp.gameState = gp.playState;
				}
				else if(gp.ui.commandNum == 3)
					System.exit(0);
				else if(gp.ui.commandNum == 5) {
					gp.gameMode = 0;
					gp.resizeGame(gp, gp.gbc);

					gp.player.setDefaultValues(0);
					
					gp.aSetter.setObject();
					gp.player.hashCoin = 0;
					
					gp.killGoomba();
					gp.startGoomba();
					gp.resetScore();
					
					//start timer
					gp.playTime = 360;
					startTimer();
					gp.ui.commandNum = 0;
				}
			}
		}
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {
			upPressed = true;			
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if(code == KeyEvent.VK_ENTER) {
			if(gp.gameState == gp.pauseState)
				System.exit(0);
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if(code == KeyEvent.VK_ESCAPE) {
			if(gp.gameState == gp.playState)
				gp.gameState = gp.pauseState;
			else if (gp.gameState == gp.pauseState)
				gp.gameState = gp.playState;
		}
	}

	public void startTimer() {
		//start timer
		if (gp.timer != null)
			gp.timer.cancel();
		gp.timer = new Timer();
		gp.task = new TimerTask() {
		@Override
		public void run() {
			if(gp.gameState != gp.pauseState && gp.gameState != gp.gameWinState && gp.gameState != gp.gameOverState)
				gp.playTime -= 1;
			if(gp.playTime == 0) {
				gp.reason = gp.timeUp;
				gp.gameState = gp.gameOverState;
			}
		}	
	};
	gp.timer.scheduleAtFixedRate(gp.task, 1000, 1000);
	}
	
	public void gameOverState(int code) {
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {
			if(gp.ui.commandNum>0)
				gp.ui.commandNum--;
			else
				gp.ui.commandNum = 2;
			if(gp.reason == gp.otherWon || gp.reason == gp.otherLost) 
				if(gp.ui.commandNum == 0)
					gp.ui.commandNum = 2;
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			if(gp.ui.commandNum < 2)
				gp.ui.commandNum++;
			else 
				gp.ui.commandNum = 0;
			if(gp.reason == gp.otherWon || gp.reason == gp.otherLost) 
				if(gp.ui.commandNum == 0)
					gp.ui.commandNum = 1;
		}
		if(code == KeyEvent.VK_ENTER) {
			//in case you win, and want to retry, we need to reset mario, goomba, score, coind and timer
				if(gp.ui.commandNum == 0) {
					gp.player.setDefaultValues(0);
					
					gp.aSetter.setObject();
					gp.player.hashCoin = 0;
					
					gp.killGoomba();
					gp.startGoomba();
					gp.resetScore();
					gp.resizeGame(gp, gp.gbc);
					//start timer
					gp.playTime = 360;
					startTimer();
					gp.gameState = gp.playState;	
			
				}
				else if(gp.ui.commandNum == 1) {
					gp.ui.commandNum = 5;
					gp.gameState = gp.titleState;
				}
				else if(gp.ui.commandNum == 2) {
					System.exit(0);
				}
		}
			
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
	}		
}


