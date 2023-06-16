package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
	
	public int x;
	public int y;
	public int worldX;
	public int worldY;
	public int speed;
	public int screenX;
	public boolean jump = false;
	public boolean falling = true;
	public int solidAreaDefaultX, solidAreaDefaultY;
	
	//char lifes
	public int maxLife;	
	public int life;
	
	public BufferedImage upR, upL, leftW, rightW, leftS ,rightS, deathPic;
	public String direction;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public double gravity = 0.0;
	
	public Rectangle solidArea, ofirRectangle;
	public boolean collisionOn = false;
}
