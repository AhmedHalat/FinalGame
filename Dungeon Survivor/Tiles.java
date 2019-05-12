 

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Tiles 
{
    private SpriteSheet spriteSheet;
    private ArrayList<Tile> tilesList = new ArrayList<Tile>();

    //Parameters: File containing tiles, tile sheet
    //reads in file and creates an array list containing every tile
    //Constructor
    public Tiles(File tilesFile, SpriteSheet spriteSheet){
        this.spriteSheet = spriteSheet;
        try {
            Scanner scanner = new Scanner(tilesFile);
            while(scanner.hasNextLine()) 
            {
                String line = scanner.nextLine();
                if(!line.startsWith("//"))
                {
                    String[] splitString = line.split("-");
                    String tileName = splitString[0];
                    int spriteX = Integer.parseInt(splitString[1]);
                    int spriteY = Integer.parseInt(splitString[2]);
                    Tile tile = new Tile(tileName, spriteSheet.getSprite(spriteX, spriteY));
                    if(splitString.length >= 4) {
			tile.collidable = true;
			tile.collisionType = Integer.parseInt(splitString[3]);
		    }
                    

                    tilesList.add(tile);
                }
            }
        } 
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    //Parameters: tileID - tile position in tileList, renderer - object used to render, xyPosition on map, zooms
    //
    //returns 
    public void renderTile(int tileID, RenderHandler renderer, int xPosition, int yPosition, int xZoom, int yZoom)
    {
        if(tileID >= 0 && tilesList.size() > tileID)
        {
            renderer.renderSprite(tilesList.get(tileID).sprite, xPosition, yPosition, xZoom, yZoom, false);
        }
        else
        {
            System.out.println("TileID " + tileID + " is not within range " + tilesList.size() + ".");
        }
    }
    //Parameters: None
    //returns tile arrayList size
    public int size(){
        return tilesList.size();
    }
    //Parameters: None
    //Creates an array of all the sprites of tilesList
    //returns sprites array
    public Sprite[] getSprites(){
        Sprite[] sprites = new Sprite[size()];

        for(int i = 0; i < sprites.length; i++)
            sprites[i] = tilesList.get(i).sprite;

        return sprites;
    }
    //Parameters: the tile id
    //gets the collision type of the tile from tile list
    //returns the collision type of a specific tile
    public int collisionType(int tileID) {
        return tilesList.get(tileID).collisionType;
    } 
    //Sub class used to create array list
    class Tile 
    {
        public String tileName;
        public Sprite sprite;
        public boolean collidable = false;
        public int collisionType = -1;
        //Parameters: Tile name and sprite
        //Used to set 2 values to one index on the array list and easily retrieve any value, ex: by using .sprit
        //Constructor
        public Tile(String tileName, Sprite sprite) 
        {
            this.tileName = tileName;
            this.sprite = sprite;
        }
    }
}