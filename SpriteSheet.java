import java.awt.image.BufferedImage;

/**
 * creates a spritesheet
 */
public class SpriteSheet {
	private int[] pixels;//contains color information
	private BufferedImage image;
	public final int SIZEX;
	public final int SIZEY;
	private Sprite[] loadedSprites = null;
	private boolean spritesLoaded = false; //used to debug
	private int spriteSizeX;

	/**
	 * defualt Constructor
	 * @param sheetImage the image that will be used
	 */
	public SpriteSheet(BufferedImage sheetImage) {
		image = sheetImage;
		SIZEX = sheetImage.getWidth();
		SIZEY = sheetImage.getHeight();
		pixels = new int[SIZEX*SIZEY];
		pixels = sheetImage.getRGB(0, 0, SIZEX, SIZEY, pixels, 0, SIZEX);
	}

/**
 * loads the sprites based on sizes
 * @param spriteSizeX width
 * @param spriteSizeY height
 */
	public void loadSprites(int spriteSizeX, int spriteSizeY){
		this.spriteSizeX = spriteSizeX;
		loadedSprites = new Sprite[(SIZEX / spriteSizeX) * (SIZEY / spriteSizeY)];
		//loads the sprite on the spritesheet and creates a new Sprite
		int spriteID = 0;
		for(int y = 0; y < SIZEY; y += spriteSizeY)
		  for(int x = 0; x < SIZEX; x += spriteSizeX){
				loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
				spriteID++;
			}
		spritesLoaded = true;
	}

	//getters and setters
	public Sprite getSprite(int x, int y){
		if(spritesLoaded){
			int spriteID = x + y * (SIZEX / spriteSizeX);
			if(spriteID < loadedSprites.length) return loadedSprites[spriteID];
			else System.out.println("SpriteID of " + spriteID + " is out of the range with a length of " + loadedSprites.length + ".");
		}
		else System.out.println("SpriteSheet could not get a sprite with no loaded sprites.");
		return null;
	}

	public int[] getPixels(){
		return pixels;
	}

	public BufferedImage getImage(){
		return image;
	}

	public Sprite[] getLoadedSprites(){
		return loadedSprites;
	}

}
