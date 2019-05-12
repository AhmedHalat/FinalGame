
import java.awt.image.BufferedImage;
    
public class SpriteSheet {
    private int[] pixels;
    private BufferedImage image;
    public final int SIZEX;
    public final int SIZEY;
    private Sprite[] loadedSprites = null;
    private boolean spritesLoaded = false;
    
    private int spriteSizeX;
    //Parameters: a formatted image
    //Sets the variables and pixel array
    //Constructor 
    public SpriteSheet(BufferedImage sheetImage){
    	image = sheetImage;
    	SIZEX = sheetImage.getWidth();
    	SIZEY = sheetImage.getHeight();
    
    	pixels = new int[SIZEX*SIZEY];
    	pixels = sheetImage.getRGB(0, 0, SIZEX, SIZEY, pixels, 0, SIZEX);
    }
    //Parameters: size of the sprite 
    //Loads the sprites from the sprite sheet into an array
    //returns Void
    public void loadSprites(int spriteSizeX, int spriteSizeY){
    	this.spriteSizeX = spriteSizeX;
    	loadedSprites = new Sprite[(SIZEX / spriteSizeX) * (SIZEY / spriteSizeY)];
    
    	int spriteID = 0;
    	for(int y = 0; y < SIZEY; y += spriteSizeY)
    	{
    		for(int x = 0; x < SIZEX; x += spriteSizeX)
    		{
    			loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
    			spriteID++;
    		}
    	}
    
    	spritesLoaded = true;
    }
    //Parameters: x and y position
    //returns a sprite from loaded sprites array
    //returns Sprite from those coordinates
    public Sprite getSprite(int x, int y){
    	if(spritesLoaded)
    	{
    		int spriteID = x + y * (SIZEX / spriteSizeX);
    
    		if(spriteID < loadedSprites.length) 
    			return loadedSprites[spriteID];
        }
    	
    	return null;
    }
    //Parameters: None
    //returns sprites array
    public Sprite[] getLoadedSprites(){
    	return loadedSprites;
    }
    //Parameters: None
    //returns pixels array, to be rendered
    public int[] getPixels(){
    	return pixels;
    }
    //Parameters: None
    //returns image
    public BufferedImage getImage()
    {
    	return image;
    }
    
    }