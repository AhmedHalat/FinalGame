import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Used to create board tiles
 */
public class Tiles{
	private SpriteSheet spriteSheet;
	private ArrayList<Tile> tilesList = new ArrayList<Tile>(); //tile is at the bottom of this class

	//This will only work assuming the sprites in the spriteSheet have been loaded.
	/**
	 * creates tiles based off of text file and SpriteSheet
	 * @param tilesFile   text file containging the name, x, y and collision type of every tile used
	 * @param spriteSheet sprite sheet containing every tile
	 */
	public Tiles(File tilesFile, SpriteSheet spriteSheet) {
		this.spriteSheet = spriteSheet;
		//access the text file containging tile information
		try{
			Scanner scanner = new Scanner(tilesFile);
			//parse through every line to get the desired information
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				if(!line.startsWith("//")){
					String[] splitString = line.split("-");
					String tileName = splitString[0];
					int spriteX = Integer.parseInt(splitString[1]);
					int spriteY = Integer.parseInt(splitString[2]);
					Tile tile = new Tile(tileName, spriteSheet.getSprite(spriteX, spriteY));
					//not every tile needs a collision type, therefore only add if existant
					if(splitString.length >= 4) {
						tile.collidable = true;
						tile.collisionType = Integer.parseInt(splitString[3]);
					}
					//add the tile
					tilesList.add(tile);
				}
			}
		}catch(FileNotFoundException e){e.printStackTrace();}
	}

	/**
	 * renders tiles
	 * @param tileID    the id of the Tile
	 * @param renderer  where it renders to
	 * @param xPosition x
	 * @param yPosition y
	 * @param xZoom     zoom ration
	 * @param yZoom     zoom ration
	 */
	public void renderTile(int tileID, RenderHandler renderer, int xPosition, int yPosition, int xZoom, int yZoom){
		if(tileID >= 0 && tilesList.size() > tileID)renderer.renderSprite(tilesList.get(tileID).sprite, xPosition, yPosition, xZoom, yZoom, false);
		else System.out.println("TileID " + tileID + " is not within range " + tilesList.size() + ".");
	}

	//getters and setters
	public int size(){return tilesList.size();}

	public Sprite[] getSprites(){
		Sprite[] sprites = new Sprite[size()];
		for(int i = 0; i < sprites.length; i++) sprites[i] = tilesList.get(i).sprite;
		return sprites;
	}

	public int collisionType(int tileID){
		if(tileID >= 0 && tilesList.size() > tileID) return tilesList.get(tileID).collisionType;
		else System.out.println("TileID " + tileID + " is not within range " + tilesList.size() + ".");
		return -1;
	}

	/**
	 * Contains tile information such as name
	 */
	class Tile{
		public String tileName;
		public Sprite sprite;
		public boolean collidable = false;
		public int collisionType = -1;

		public Tile(String tileName, Sprite sprite){
			this.tileName = tileName;
			this.sprite = sprite;
		}
	}
}
