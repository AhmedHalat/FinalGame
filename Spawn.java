import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

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

  public void addCharacter(Set <Character> character){
    Iterator <Character> iter = character.iterator ();
  	while (iter.hasNext()) characters.add(iter.next());
  }

  public void removeCharacters(){
    characters.clear();
  }

  public boolean allDead(){
    Boolean alive = true;
    for (Character character : characters)
      if(!character.isAlive()) alive = false;
    return alive;
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
