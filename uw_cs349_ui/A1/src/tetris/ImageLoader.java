package tetris;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
/**
 * Created by Joy on 16/10/1.
 */
public class ImageLoader {

    public static Image[] loadImage( int width) throws IOException{

        //BufferedImage load = ImageIO.read(new File("/rec/tetris.png"));
        BufferedImage load = ImageIO.read(new File("res/tetris.png"));
        Image[] images = new Image[load.getWidth()/width];
        for (int i = 0; i < images.length; i++){
            images[i] = load.getSubimage(i*width,0,width,width);
        }
        return images;
    }
}
