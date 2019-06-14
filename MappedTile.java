//Tile ID in the tileSet and the position of the tile in the map
public class MappedTile
{
  public int layer, id, x, y;
  //Parameters: layer of tile, tile id, x-y position of tile
  //Sets variables to be easily accesed
  //Constructor
  public MappedTile(int layer, int id, int x, int y)
  {
    this.layer = layer;
    this.id = id;
    this.x = x;
    this.y = y;
  }
}
