//Tile ID in the tileSet and the position of the tile in the map
public class MappedTile
{
  public int layer, id, x, y;
  /**
   * constructor for MappedTile
   * @param layer the layer the tile exists on
   * @param id    the id of the tile
   * @param x     the x location
   * @param y     the y
   */
  public MappedTile(int layer, int id, int x, int y)
  {
    this.layer = layer;
    this.id = id;
    this.x = x;
    this.y = y;
  }
}
