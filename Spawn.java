import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Spawn implements GameObject{
  private ArrayList<Character> characters= new ArrayList <Character> ();
  private Rectangle rect;
  private int layer;
  private static int room;
  private static int wave;

  public Spawn(int roomWidth, int roomHeight, int roomType){
    rect = new Rectangle(0,0,0,0);
  }

  public Spawn(){

  }

  public void addCharacter(Character character, int multiple){
    for(int i = 0; i < multiple; i++) characters.add(character);
  }

  public boolean allDead(){
    for (Character character : characters)
      if(!character.isAlive()) return true;
    return false;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    for(Character character: characters){
      if(character.isAlive()) character.render(renderer, xZoom, yZoom);
      if(character.particles()) character.renderParticles(renderer, xZoom, yZoom);
    }
  }

  public void update(Game game, Player player, Spawn spawner){
    for(Character character: characters){
      if(character.isAlive()) character.interact(game, player, spawner);
    }
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
    boolean stoppedChecking = false;
    for(Character character: characters){
      if(character.isAlive()) stoppedChecking = character.handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
      if(stoppedChecking) break;
    }
    return stoppedChecking;
  }

  public int getLayer(){
    return layer;
  }

  public Rectangle getRectangle(){
    return rect;
  }
}
