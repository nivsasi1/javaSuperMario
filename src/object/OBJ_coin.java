package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_coin extends SuperObject{

	public OBJ_coin() {
		name = "coin";
		try {
			
			image = new BufferedImage[3];

			image[0] = ImageIO.read(getClass().getResourceAsStream("/objects/coin0.png"));
			image[1] = ImageIO.read(getClass().getResourceAsStream("/objects/coin1.png"));
			image[2] = ImageIO.read(getClass().getResourceAsStream("/objects/coin2.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision = true;
	}
	

}
