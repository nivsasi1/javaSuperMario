package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	
	GamePanel gp;
	KeyHandler keyH;
	public final int screenY;
	public int collisonSound = 0;
	public boolean collisonSound1 = false;
	public int hashCoin = 0;
	public boolean goombaKilled = false;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = x;
		screenY =  gp.tileSize*10;
		solidArea = new Rectangle(gp.tileSize/6,gp.tileSize/12,gp.tileSize-gp.tileSize/6,gp.tileSize-gp.tileSize/6);
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		setDefaultValues(0);
		getPlayerImage();
	}
	
	public void getPlayerImage() {
		try {
			
			upR = ImageIO.read(getClass().getResourceAsStream("/player/mario-jump-right.png"));
			upL = ImageIO.read(getClass().getResourceAsStream("/player/mario-jump-left.png"));
			leftW = ImageIO.read(getClass().getResourceAsStream("/player/mario-walk-left.png"));
			rightW = ImageIO.read(getClass().getResourceAsStream("/player/mario-walk-right.png"));
			leftS = ImageIO.read(getClass().getResourceAsStream("/player/mario-stand-left.png"));
			rightS = ImageIO.read(getClass().getResourceAsStream("/player/mario-stand-right.png"));	
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDefaultValues(int status) {
		//Player start location
		x = gp.tileSize;
		y = gp.tileSize*10;

		screenX = 0;
		speed = gp.tileSize/10;
		direction = "rightS";
		
		//player lives
		if (status == 0) {
			maxLife = 3;
			life = maxLife;
			
		} 
		if(status == 1) {
			gp.killGoomba();
			gp.startGoomba();
			gp.resetScore();
		}

	}
	
	
	public void update() {
		//System.out.println(x +" " + screenX);
		gp.checkObject(this, gp);
		if(y > gp.tileSize*13) {
			playerDeath();
		} if(goombaKilled) {
			goombaMiniJump();
		} else if(keyH.upPressed == true || keyH.downPressed == true || keyH.rightPressed == true || keyH.leftPressed == true) {
			getSleep();
			if(keyH.upPressed == true && isStanding()) {
				if (direction.equals("rightS") || direction.equals("rightW"))
					direction = "upR";
				else
					direction = "upL";
				jump = true;
				gp.playSE(3);
				jump();
			}
			if(keyH.downPressed == true) {
				direction = "down";
				checkTP();
			}
			if(keyH.rightPressed == true && !jump) {
				direction = "rightS";
				checkTP();
				rightW();
			}
			
			if(keyH.leftPressed == true && !jump) {
				direction = "leftS";
				leftW();
			}
			
			spriteCounter++;
			if(spriteCounter > 10) {
				if(spriteNum==1)
					spriteNum = 2;
				else if (spriteNum == 2)
					spriteNum = 1;
				spriteCounter = 0;
			}
		} else 
			spriteNum = 2;
	}
	
	public void playerDeath() {
		if (life == 1)
			gameOver();
		else {
			life--;
			//play death sound + teleport to start
			gp.playSE(7);
			
			setDefaultValues(1);
			System.out.println(life);
		}
	}
	
	public void gameOver() {
		//make game over screen
		gp.playSE(8);
		gp.killGoomba();
		gp.startGoomba();
		setDefaultValues(1);
		gp.gameState = gp.gameOverState;
		gp.reason = gp.outOflives;
		
	}
	
	public boolean isStanding() {
		int marioX = (int)((screenX+x+solidArea.x)/gp.tileSize);
		int marioX2 = (int)(screenX+x+solidArea.width)/gp.tileSize;

		int marioY = (int)((y/gp.tileSize));
		if(marioY > 0 && marioY < gp.maxWorldRow-1) {
		//System.out.println(y + " " + y/gp.tileSize);
		int tileNum1 = gp.tileM.mapTileNum[gp.currentMap][marioX][marioY+1];
		int tileNum2 = gp.tileM.mapTileNum[gp.currentMap][marioX2][marioY+1];
		//System.out.println(tileNum1 + " " + tileNum2);
		if(marioX > 0 || marioX < gp.maxWorldCol[gp.currentMap] || marioY > 0 || marioY < gp.maxScreenRow)
			if(gp.tileM.tile[tileNum2].collision || gp.tileM.tile[tileNum1].collision)
				return true;
		}
		return false;
	}
	public boolean isWalledR() {
		int marioX = (int)Math.ceil((screenX+x)/gp.tileSize);
		int marioY = (y/gp.tileSize);
		if(marioY > 0 && marioY < gp.maxWorldRow-1) {
		if(marioX > 0 || marioX < gp.maxWorldCol[gp.currentMap] || marioY > 0 || marioY < gp.maxScreenRow)
			if(!isStanding() && gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][marioX+1][marioY+1]].collision) {
				//gp.playSE(6);
				return true;
			}
			if((gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][marioX+1][marioY]].collision && gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][marioX+1][marioY+1]].collision)) {
				//gp.playSE(6);
				return true;
			}
		}
		return false;
	}	
	public boolean isWalledU() {
		int marioX = (int)((screenX+x+solidArea.x)/gp.tileSize);
		int marioX2 = (int)(screenX+x+solidArea.width)/gp.tileSize;
		int marioY = (y/gp.tileSize);
		if(marioY > 0 && marioY < gp.maxWorldRow-1) {
		int tileNum1 = gp.tileM.mapTileNum[gp.currentMap][marioX][marioY];
		int tileNum2 = gp.tileM.mapTileNum[gp.currentMap][marioX2][marioY];
//		if(tileNum1 == 2)
//			gp.tileM.changeMap("map01.txt",marioX, marioY);
//		if(tileNum2 == 2)
//			gp.tileM.changeMap("map01.txt",marioX2, marioY);
		if(marioX > 0 || marioX < gp.maxWorldCol[gp.currentMap] || marioY > 0 || marioY < gp.maxScreenRow)
			if((gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision))
				return true;
		}
		return false;
	}
	public boolean isWalledL() {
		int marioX = (int)Math.ceil((screenX+x)/gp.tileSize);
		int marioY = (y/gp.tileSize);
		if(marioY > 0 && marioY < gp.maxWorldRow-1) {
		if(marioX > 0 || marioX < gp.maxWorldCol[gp.currentMap] || marioY > 0 || marioY < gp.maxScreenRow)
			if(!isStanding() && gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][marioX][marioY+1]].collision) {
				//gp.playSE(6);
				return true;
			}
			if((gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][marioX][marioY]].collision && gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][marioX][marioY+1]].collision)) {
				//gp.playSE(6);
				return true;
			}
		}
		return false;
	}	
	public void gravitation() {
		int side;
		if (direction.equals("rightS") || direction.equals("rightW") || direction.equals("upR"))
			side = 1;
		else
			side = 0;

			if(!isStanding() && !jump && !goombaKilled) {		
			y+=gp.tileSize/6;
			getSleep();
			if(keyH.rightPressed == true) {
				direction = "upR";
				side = 1;				
				}
			if(keyH.leftPressed == true) {
				direction = "upL";
				side = 0;
				}else if(side == 1)
					direction = "upR";
				else
					direction = "upL";
			gp.repaint();
			if (side == 1)
				direction = "rightS";
			else 
				direction = "leftS";
			}

	}
	
	public void getSleep() {
		double drawInterval = 1000000000/60; // 0.1666 s
		double nextDrawTime = System.nanoTime() + drawInterval;
		double remainingTime = 0;
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
	public void goombaMiniJump() {
		int side, minJump, fastFall;
		if (direction.equals("rightS") || direction.equals("rightW") || direction.equals("upR"))
			side = 1;
		else
			side = 0;
		minJump = (int) (y-gp.tileSize*1);
		fastFall =((int)(speed*1.5));
		while(y>minJump && goombaKilled == true) {
			y-=fastFall;
			getSleep();
			if(keyH.rightPressed == true) {
				direction = "upR";
				rightW();
				side = 1;
			}
			if(keyH.leftPressed == true) {
				direction = "upL";
				leftW();
				side = 0;
			} else if(side == 1)
				direction = "upR";
			else
				direction = "upL";
			if(isWalledU())
				goombaKilled=false;
			gp.repaint();
		}
		if (isStanding())
			if (side == 1)
				direction = "rightS";
			else 
				direction = "leftS";
		goombaKilled = false;
	}
	public void jump() {
		jump = true;
		int side, minJump, maxHeight, fastFall;
		if (direction.equals("rightS") || direction.equals("rightW") || direction.equals("upR"))
			side = 1;
		else
			side = 0;
		minJump = y-gp.tileSize*3;
		maxHeight = y-gp.tileSize*4-20;
		fastFall =((int)(speed*1.5));
		while(y>minJump && jump == true) { 
			if(keyH.upPressed && minJump>maxHeight) {
				minJump-=speed;
			}
				y-=fastFall;
				getSleep();
				if(keyH.rightPressed == true) {
					direction = "upR";
					rightW();
					side = 1;
				}
				if(keyH.leftPressed == true) {
					direction = "upL";
					leftW();
					side = 0;
				} else if(side == 1)
					direction = "upR";
				else
					direction = "upL";
				if(isWalledU())
					jump=false;
				gp.repaint();
		}
		if (isStanding())
			if (side == 1)
				direction = "rightS";
			else 
				direction = "leftS";
		jump = false;
	}
	public void rightW() {
		if (!isWalledR()) {
		x+=speed;
		if (x >= gp.screenWidth/2 && (x+screenX)<gp.maxWorldCol[gp.currentMap]*gp.tileSize-(gp.screenWidth/2+gp.tileSize)) {
			moveScreen();
		}else
			gp.repaint();
		}else {
			collisonSound++;
			if(collisonSound == 10) {
				gp.playSE(6);
				collisonSound = 0;
			}
		}
	}
	public void leftW() {
		if (!isWalledL()) {
		if (x <= 0)
			gp.repaint();
		else {
			x-=speed;
			gp.repaint();
		}}else {
			collisonSound++;
			if(collisonSound == 10) {
				gp.playSE(6);
				collisonSound = 0;
			}
		}

	}
	public void moveScreen() {
		screenX += speed;
		x -= speed;
		gp.repaint();
	}

	public void pickUpObject(int index) {
		if (index != 999) {
			String objectName = gp.obj[index].name;
			if(objectName.equals("coin"))
				hashCoin++;
			gp.playSE(5);
			gp.obj[index] = null;
			System.out.println("Coins: " + hashCoin);
		}
	}
	
	public void draw(Graphics2D g2) {
		int index = gp.checkObject(this, gp);
		pickUpObject(index);
		BufferedImage image = null;
		
		switch(direction) {
		case "upR":
			image = upR;
			break;
		case "upL":
			image = upL;
			break;
		case "rightS":
			if(!isStanding())
				image = upR;
			else {
			if (spriteNum == 1)
				image = rightW;
			if (spriteNum == 2)
				image = rightS;
			}
			break;
		case "leftS":
			if(!isStanding())
				image = upL;
			else {
			if (spriteNum == 1)
				image = leftW;
			if (spriteNum == 2)
				image = leftS;
			}
			break;
		case "down":
			break;

		}
		gravitation();
		if(x >= gp.screenWidth/2 && (x+screenX)<gp.maxWorldCol[gp.currentMap]*gp.tileSize-(gp.screenWidth/2+gp.tileSize)) {
			g2.drawImage(image, screenX, y, gp.tileSize, gp.tileSize, null);
		}else {
			g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
		}
		gp.checkCollisionWithMonster(this, gp);
	}

	public boolean hit(int map, int col, int row, String reqDirection) {
		boolean hit = false;
		if(map == gp.currentMap) {
			int marioX = (int)((screenX+x+solidArea.x)/gp.tileSize);
			int marioX2 = (int)(screenX+x+solidArea.width)/gp.tileSize;

			int marioY = (int)((y/gp.tileSize));
			if (marioX == col && marioY == row && direction == reqDirection)
				hit = true;	
			if (marioX2 == col && marioY == row && direction == reqDirection)
				hit = true;	
		} return hit;
	}
	public void teleport(int map, int col, int row) {
		gp.currentMap = map;		
		double check = (double) gp.tileSize*7.9;
		System.out.println(check + " im gay");
		if(col*gp.tileSize>check) {
			x = (int)check;
			screenX = col*gp.tileSize-x;
			y = gp.tileSize*row;
		}else {
			x = gp.tileSize*col;
			y = gp.tileSize*row;
			screenX=0;
		}
		getSleep();
		gp.repaint();
	}
	public void checkTP() {
		if(hit(0, 56, 6, "down") == true)
			teleport(1, 1, 1);
		if(hit(0, 57, 6, "down") == true)
			teleport(1, 1, 1);
		if(hit(0, 58, 6, "down") == true)
			teleport(1, 1, 1);
		if(hit(1,12,10,"rightS") == true)
			teleport(0,163,8);
		if(hit(1,12,9,"rightS") == true)
			teleport(0,163,8);
		for(int i = 0; i < 16; i++) {
			if(hit(0,197,i,"rightS") || hit(0,197,i,"rightW") || hit(0,197,i,"upR")) {
				gp.playSE(4);
				gp.gameState = gp.gameWinState;
			}
		}
	}
	
	public void drawOpponent(Graphics2D g2) {
		BufferedImage image = null;
		if(gp.opponentPanel.direction!=null) {
			switch(gp.opponentPanel.direction) {
			case "upR":
				image = upR;
				break;
			case "upL":
				image = upL;
				break;
			case "rightS":
					image = rightW;
					if (gp.opponentPanel.spriteNum == 1)
						image = rightW;
					if (gp.opponentPanel.spriteNum == 2)
						image = rightS;
				break;
			case "leftS":
					image = leftW;
					if (gp.opponentPanel.spriteNum == 1)
						image = leftW;
					if (gp.opponentPanel.spriteNum == 2)
						image = leftS;
				break;
			case "down":
				break;
	
			}
			if(x >= gp.screenWidth/2 && (gp.opponentPanel.marioX+gp.opponentPanel.marioScreenX)<gp.maxWorldCol[gp.opponentPanel.currentMap]*gp.tileSize-(gp.screenWidth/2+gp.tileSize)) {
				g2.drawImage(image, gp.opponentPanel.marioScreenX, gp.opponentPanel.marioY, gp.tileSize, gp.tileSize, null);
			}else {
				g2.drawImage(image, gp.opponentPanel.marioX, gp.opponentPanel.marioY, gp.tileSize, gp.tileSize, null);
			}
		}
	}
	
}
