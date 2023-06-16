package background;

import java.awt.Graphics2D;


import javax.imageio.ImageIO;

import main.GamePanel;

public class BackGroundManager {
	GamePanel gp;
	BackGround[] bg;
	int height, width, times;
	
	public BackGroundManager(GamePanel gp) {
		this.gp = gp;
		bg = new BackGround[10];
		height = gp.screenHeight;
		width = 48*gp.tileSize;
		times = (int) Math.ceil(gp.worldWidth[gp.currentMap]/width)+1;
		System.out.println(times + " im gay");
		
		getMapImage();
		}
	
	public void getMapImage() {
        try {
            bg[0] = new BackGround();
            bg[0].image = ImageIO.read(getClass().getResourceAsStream("/maps/mariobackgroundwrld11.png"));

            bg[1] = new BackGround();
            bg[1].image = ImageIO.read(getClass().getResourceAsStream("/maps/black_bg.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if(gp.currentMap == 0) {
            int printedTimes = 0;

            while(printedTimes < times) {
                int worldX = printedTimes * width;
                int worldY = 0;
                int screenX = worldX + gp.player.worldX - gp.player.screenX;

                g2.drawImage(bg[0].image, screenX, worldY, width, height, null);

                printedTimes++;


            }
        } else if (gp.currentMap == 1) {
            int printedTimes = 0;

            while(printedTimes < times) {
                int worldX = printedTimes * width;
                int worldY = 0;
                int screenX = worldX + gp.player.worldX - gp.player.screenX;

                g2.drawImage(bg[1].image, screenX, worldY, width, height, null);
                printedTimes++;


            }
        }
    }
    public void drawOP(Graphics2D g2) {
        if(gp.opponentPanel.currentMap == 0) {
            int printedTimes = 0;

            while(printedTimes < times) {
                int worldX = printedTimes * width;
                int worldY = 0;
                int screenX = worldX + gp.opponentPanel.marioWorldX - gp.opponentPanel.marioScreenX;

                g2.drawImage(bg[0].image, screenX, worldY, width, height, null);

                printedTimes++;


            }
        } else if (gp.opponentPanel.currentMap == 1) {
            int printedTimes = 0;

            while(printedTimes < times) {
                int worldX = printedTimes * width;
                int worldY = 0;
                int screenX = worldX + gp.opponentPanel.marioWorldX - gp.opponentPanel.marioScreenX;

                g2.drawImage(bg[1].image, screenX, worldY, width, height, null);
                printedTimes++;


            }
        }
    }

}
