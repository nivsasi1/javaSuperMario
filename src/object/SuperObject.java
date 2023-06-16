package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entity.Player;
import main.GamePanel;

public class SuperObject {

	public BufferedImage image[];
	public String name;
	public boolean collision = false;
	public int map;
	public int worldX, worldY;
	int spriteNum = 0;
	int spriteCntr = 0;
	public Rectangle solidArea = new Rectangle(0,0,38,38);
	public int solidAreaDefaultX = 0;
	public int solidAreaDefaultY = 0;
	public boolean collisioned = false;
	
	public void draw(Graphics2D g2, GamePanel gp) {
		int screenX, screenY;
		if(map == 1)
			screenX = worldX;
		else
			screenX = worldX-gp.player.screenX;
		screenY = worldY;

		if(gp.currentMap == map) {
			spriteCntr++;
			if(spriteNum != 0 && spriteCntr == 20) {
				 if(spriteNum == 1)
					spriteNum++;
				else if(spriteNum==2)
					spriteNum=0;
				 spriteCntr=0;
			} else if (spriteNum==0 && spriteCntr ==40) {
				spriteNum++;
				spriteCntr=0;
			}

			g2.drawImage(image[spriteNum], screenX, screenY, gp.tileSize-10, gp.tileSize-10, null);
		}

	}
}
