import java.awt.image.BufferedImage;

/**
 * Creates sprites for characters
 */
public class Sprite{
	private int width, height;
	private int[] pixels; //stores color information

/**
 * default Constructor
 * @param sheet  the sprite sheet that will be used
 * @param startX starting pos of the sprite on the SheetDialog
 * @param startY "
 * @param width  the width of the Spritethe height of the sprite
 * @param height [description]
 */
	public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
		sheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
	}

/**
 * Constructor
 * @param image creates the sprite off of a sole image instead of a spritesheet
 */
	public Sprite(BufferedImage image){
		width = image.getWidth();
		height = image.getHeight();
		pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
	}

	public Sprite(){}

//getters and setters
	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public int[] getPixels(){
		return pixels;
	}
}
