 


import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
class LoadImage {
    //Parameters: Image name
    // loads images and formats them
    //returns the formatted image
    public static BufferedImage loadImage(String path)
    {
        try 
        {
            BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        }
        catch(IOException exception) 
        {
            exception.printStackTrace();
            return null;
        }
    }
}
