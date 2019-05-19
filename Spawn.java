import java.util.ArrayList;

public class Spawn implements GameObject{
  private ArrayList<Character> characters;
  private Rectangle rect;
  private int layer;
  private static int room;
  private static int wave;

  public Spawn(int roomWidth, int roomHeight, int roomType){
    rect = new Rectangle(0,0,0,0);
  }

  public void addCharacter(Character character, int multiple){
    for(int i = 0; i < multiple; i++) characters.add(character);
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    for(int i = 0; i < characters.size(); i++) characters.get(i).render(renderer, xZoom, yZoom);
  }

  public void update(Game game, Player player){
    for(int i = 0; i < characters.size(); i++) characters.get(i).update(game, player, wave);
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
      return false;
  }

  public int getLayer(){
      return layer;
  }

  public Rectangle getRectangle(){
      return rect;
  }
}
