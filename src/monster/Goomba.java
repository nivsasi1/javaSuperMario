package monster;

import javax.imageio.ImageIO;
import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Goomba extends Thread implements Serializable{

	boolean running;
	public int x;
	public int y;
	int width;
	int hits;
	GamePanel gp;
	int dirx, diry;
	transient BufferedImage goombaImage;
	public Rectangle gomSolidArea;
	long goomba_speed;
	//direction 0 = right   @@@   1 = left
	int direction;
	int screenX;
	int screen2X;
	public boolean alive;
	int startX, startY;
	int map;


	public Goomba(GamePanel gp, int ball_speed, int x, int y, int width, int map) {
		this.running = true;
		this.map = map;
		this.x = x * gp.tileSize;
		this.y = y * gp.tileSize;
		startX = this.x;
		startY = this.y;
		System.out.println(this.x + " " + this.y);
		this.width = gp.tileSize;
		this.gp = gp;
		gomSolidArea = new Rectangle(0,0,gp.tileSize,gp.tileSize);
		try {
			goombaImage = ImageIO.read(getClass().getResourceAsStream("/monster/goomba1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		direction = 1;
		this.goomba_speed = gp.player.speed / 2;
		screenX = 0;
		screen2X = 0;
		alive = false;
	}
	

	public void setDefaultValues() {
		x = startX;
		y = startY;
		screen2X = 0;

		alive = false;
	}

	public void update() {
		int currentMapX = gp.player.x + gp.player.screenX;
		int currentMapTile = currentMapX/gp.tileSize;
		int staticGoombaTile = this.startX/gp.tileSize;
		if(!alive && (this.map == gp.currentMap) && (currentMapTile >= staticGoombaTile-(gp.maxScreenCol/2+1)) && currentMapTile <= staticGoombaTile+(gp.maxScreenCol/2+1)) {
        	alive = true;
        	System.out.println(gp.player.x + " " + gp.player.screenX + " " + this.x);
        }
		screenX = gp.player.screenX;
        if (alive) {
			if(gp.gameState == gp.playState) {
				screenX = gp.player.screenX;
				if(isWalledL())
					direction = 0;
				if(isWalledR())
					direction = 1;
					
				
				
				//when tp its not - the extra screenX so goombas are out of bound, to check
				if(direction == 0)
					if (screen2X < screenX)
						this.x = (int) ((this.x + this.goomba_speed) - (screenX - screen2X));
					else
						this.x = (int) ((this.x + this.goomba_speed));
	
				else
					if(screen2X < screenX)
						this.x = (int) ((this.x - this.goomba_speed) - (screenX - screen2X));
					else
						this.x = (int) ((this.x - this.goomba_speed));
				if(screenX > screen2X)
					screen2X = screenX;
			} 
        }else if(screen2X < screenX) 
			x-=(screenX - screen2X);
        if(this.map == gp.currentMap)
        	screen2X = screenX;
		
        if(this.x <= -(gp.tileSize*6)) {
			this.alive = false;
        	this.interrupt();
        }
        if(gp.player.x + gp.player.screenX > this.startX + gp.tileSize*gp.maxScreenCol){
			this.alive = false;
        	this.interrupt();
        }	
    }
	
	public boolean getRuninng() {
		return this.running;
	}
	
	public void draw(Graphics g) {
		if(this.getMap() == gp.currentMap) {
			int gomX = (int)((x/gp.tileSize));
			int gomY = (int)((y/gp.tileSize));
			screenX = gp.player.screenX;
	
			if(gomY > gp.maxWorldRow || gomX+screenX < 0) {
				System.out.println("im dead");
				running = false;
				alive = false;
			}
			gravitation();
			
			g.drawImage(goombaImage, this.x, this.y, width, width, null);
		}
	}
	
	
	
	public void gravitation() {
		if(!isStanding()) {
			y+=gp.tileSize/6;
		}
	}
	
	public boolean isStanding() {
		int gomX = (int)((gp.player.screenX+x+gomSolidArea.x)/gp.tileSize);
		int gomX2 = (int)(gp.player.screenX+x+gomSolidArea.width)/gp.tileSize;

		int gomY = (int)((y/gp.tileSize));
		if(gomY > 0 && gomY < gp.maxWorldRow-1) {
			int tileNum1 = gp.tileM.mapTileNum[gp.currentMap][gomX][gomY+1];
			int tileNum2 = gp.tileM.mapTileNum[gp.currentMap][gomX2][gomY+1];

			if(gomX > 0 || gomX < gp.maxWorldCol[gp.currentMap] || gomY > 0 || gomY < gp.maxScreenRow)
				if(gp.tileM.tile[tileNum2].collision || gp.tileM.tile[tileNum1].collision)
					return true;
		}
		return false;
	}
	
	public boolean isWalledL() {
		
		int gomX = (int)Math.ceil((x + screenX)/gp.tileSize);
		int gomY = (y/gp.tileSize);
		if(gomY > 0 && gomY < gp.maxWorldRow-1) {
		if(gomX > 0 || gomX < gp.maxWorldCol[gp.currentMap] || gomY > 0 || gomY < gp.maxScreenRow)
			if(!isStanding() && gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][gomX][gomY+1]].collision) {
				return true;
			}
			if((gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][gomX][gomY]].collision && gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][gomX][gomY+1]].collision)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isWalledR() {

		int gomX = (int)Math.ceil((x + screenX)/gp.tileSize);
		int gomY = (y/gp.tileSize);
		if(gomY > 0 && gomY < gp.maxWorldRow-1) {
		if(gomX > 0 || gomX < gp.maxWorldCol[gp.currentMap] || gomY > 0 || gomY < gp.maxScreenRow)
			if(!isStanding() && gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][gomX+1][gomY+1]].collision) {
				return true;
			}
			if((gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][gomX+1][gomY]].collision && gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][gomX+1][gomY+1]].collision)) {
				return true;
			}
		}
		return false;
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
	
	public void run() {
	    double drawInterval = 1000000000/60; // 0.1666 s
	    double nextDrawTime = System.nanoTime() + drawInterval;
	    double remainingTime = 0;
	    
	    try {
	        while(!Thread.currentThread().isInterrupted()) {
	            remainingTime = nextDrawTime - System.nanoTime();
	            remainingTime = remainingTime / 1000000;
	            
	            if (remainingTime < 0)
	                remainingTime = 0;
	            
	            Thread.sleep((long) remainingTime);  
	            update();
	            nextDrawTime += drawInterval;
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	        // Handle interruption, break the loop or perform necessary cleanup
	    }
	}

	public int getMap() {
		return map;
	}

	
}
