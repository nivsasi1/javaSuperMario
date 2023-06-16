package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_powerBIG extends SuperObject {
	public OBJ_powerBIG(){
		name = "MushRoom";
		try {
			
			image = new BufferedImage[1];
	
			image[0] = ImageIO.read(getClass().getResourceAsStream("/objects/powerUp.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision = true;
	}
}