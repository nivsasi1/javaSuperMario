package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
		
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][][];
	int spriteCounter = 0;
	int spriteNum = 0;
	BufferedImage[] question;
	public int mapScreenX, mapScreenY;
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[23];
		mapTileNum = new int[gp.maxMap][][];
		mapTileNum[0] = new int[gp.maxWorldCol[0]][gp.maxWorldRow];
		mapTileNum[1] = new int[gp.maxWorldCol[1]][gp.maxWorldRow];
		
		getTileImage();
		loadMap("/maps/map01.txt", 0);
		loadMap("/maps/map0102.txt", 1);
		this.mapScreenX = 0;
		this.mapScreenY = 0;
		}
	
	public void getTileImage() {
		try {
			setup(0, "floor-tile", true);
			setup(1, "flying-brick", true);
			setup(2, "question-brick1", true);
	
			question = new BufferedImage[4];
			question[0] = ImageIO.read(getClass().getResourceAsStream("/tiles/question-brick1.png"));
			question[1] = ImageIO.read(getClass().getResourceAsStream("/tiles/question-brick2.png"));
			question[2] = ImageIO.read(getClass().getResourceAsStream("/tiles/question-brick3.png"));
			question[3] = ImageIO.read(getClass().getResourceAsStream("/tiles/question-brick-broken.png"));
			
			setup(3, "unmoveable-block", true);
			setup(4, "image_part_001", true);
			setup(5, "image_part_002", true);
			setup(6, "image_part_003", true);
			setup(7, "image_part_004", true);
						
			tile[9] = new Tile();
			tile[9].collision = false;
			
			setup(11, "floor-tile-11", true);
			setup(12, "flying-brick-12", true);
			setup(13, "left-long-tube-13", true);
			setup(14, "right-down-14", true);
			setup(15, "right-up-15", true);
			setup(16, "left-down-16", true);
			setup(17, "left-up-17", true);
			setup(18, "left-down-18", true);
			setup(19, "left-up-19", true);
			setup(20, "right-long-tube-20", true);
			setup(21, "amood", true);
			setup(22, "topflag", true);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setup(int index, String imageName, boolean collision) {
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));;
			tile[index].collision = collision;
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String filePath, int map) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			System.out.println(col + "  " + gp.maxWorldCol[map] + " " + map);

			while(col<gp.maxWorldCol[map] && row < gp.maxWorldRow) {
				String line = br.readLine();

				while(col<gp.maxWorldCol[map]) {
					String[] numbers = line.split("	");
					int	num = Integer.parseInt(numbers[col]);
					if(map == 0)
						System.out.println(num);
					mapTileNum[map][col][row] = num;
					col++;

					}
				if (col == gp.maxWorldCol[map]) {
					col = 0;
					row++;
				}
				
			}
			br.close();
		}catch(Exception e) {
			
		}
		System.out.println("im loadMap");
	}

	public void draw(Graphics2D g2) {
		int worldCol = 0;
		int worldRow = 0;
		spriteCounter++;
		if(spriteNum != 0 && spriteCounter == 20) {
			 if(spriteNum == 1)
				spriteNum++;
			else if(spriteNum==2)
				spriteNum=0;
			spriteCounter=0;
		} else if (spriteNum==0 && spriteCounter ==40) {
			spriteNum++;
			spriteCounter=0;
		}
		while(worldCol < gp.maxWorldCol[gp.currentMap] && worldRow < gp.maxWorldRow)
		{			
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
			int worldX = worldCol * gp.tileSize;
//			int worldY = worldRow * gp.tileSize;
			int screenX = worldX + gp.player.worldX - gp.player.screenX;
			int screenY = worldRow * gp.tileSize;
			if(tileNum == 2){
				g2.drawImage(question[spriteNum], screenX, screenY, gp.tileSize, gp.tileSize, null);
			}
			if(tileNum >= 0 && tileNum <=7 && tileNum != 2 || (tileNum <=tile.length && tileNum >=11)) { 
				g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			} worldCol++;
			if(worldCol == gp.maxWorldCol[gp.currentMap]) {
				worldCol = 0;
				worldRow++;
			}
			this.mapScreenX = screenX;
			this.mapScreenY = screenY;
		}
	}
	
	public void drawOpponent(Graphics2D g2) {
		int worldCol = 0;
		int worldRow = 0;
		spriteCounter++;
		if(spriteNum != 0 && spriteCounter == 20) {
			 if(spriteNum == 1)
				spriteNum++;
			else if(spriteNum==2)
				spriteNum=0;
			spriteCounter=0;
		} else if (spriteNum==0 && spriteCounter ==40) {
			spriteNum++;
			spriteCounter=0;
		}
		while(worldCol < gp.maxWorldCol[gp.opponentPanel.currentMap] && worldRow < gp.maxWorldRow)
		{			
			int tileNum = mapTileNum[gp.opponentPanel.currentMap][worldCol][worldRow];
			int worldX = worldCol * gp.tileSize;
//			int worldY = worldRow * gp.tileSize;
			int screenX = worldX + gp.opponentPanel.marioWorldX - gp.opponentPanel.marioScreenX;
			int screenY = worldRow * gp.tileSize;
			if(tileNum == 2){
				g2.drawImage(question[spriteNum], screenX, screenY, gp.tileSize, gp.tileSize, null);
			}
			if(tileNum >= 0 && tileNum <=7 && tileNum != 2 || (tileNum <=tile.length && tileNum >=11)) { 
				g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			} worldCol++;
			if(worldCol == gp.maxWorldCol[gp.opponentPanel.currentMap]) {
				worldCol = 0;
				worldRow++;
			}
		}
	}
	
	
}