package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends SuperObject{

	public OBJ_Heart() {
		name = "Heart";
		try {
			
			image = new BufferedImage[1];

			image[0] = ImageIO.read(getClass().getResourceAsStream("/objects/heart-icon.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision = true;
	}
	

}