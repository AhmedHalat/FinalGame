
import java.awt.image.BufferedImage;

public class Sprite
{
    protected int width, height;
    protected int[] pixels;
    //Parameters: Spritesheet, startXY - position of sprite on sheet, width and height of sprite
    //sets variables and pixel array values
    //Constructor
    public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width*height];
        sheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
    }
    //Parameters: Image - image of sprite
    //sets variables and pixel array
    //2nd Constructor
    public Sprite(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();

        pixels = new int[width*height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
    }
    //Parameters: None
    //Used by animated sprite. Necessairy
    //Third constructor
    public Sprite() {}

    //Parameters: None
    //returns width of sprite
    public int getWidth(){
        return width;
    }
    //Parameters: None
    //returns sprite height
    public int getHeight(){
        return height;
    }
    //Parameters: None 
    //returns pixels array
    public int[] getPixels(){
        return pixels;
    }
}