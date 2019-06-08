import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class Spawn implements GameObject{
  private ArrayList<Character> characters= new ArrayList <Character> ();
  private ArrayList<Character> items= new ArrayList <Character> ();
  private Rectangle rect;
  private int layer;
  private static int room;
  private static int wave;

  public Spawn(int roomWidth, int roomHeight, int roomType){
    rect = new Rectangle(0,0,0,0);
  }

  public Spawn(){}

  public void addCharacter(Character character, int multiple){
    for(int i = 0; i < multiple; i++) characters.add(character);
  }

  public void addCharacter(Set <Character> character){
    Iterator <Character> iter = character.iterator();
  	while (iter.hasNext()) characters.add(iter.next());
  }

  public void addItem(Character item, int multiple){
    for(int i = 0; i < multiple; i++) items.add(item);
  }

  public void removeCharacters(){
    characters.clear();
  }

  public boolean allDead(int room){
    for (Character character : characters) if(character.isAlive() && character.getRoom() == room) return false;
    return true;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    for(Character character: characters){
      if(character.isAlive()) character.render(renderer, xZoom, yZoom);
      if(character.particles()) character.renderParticles(renderer, xZoom, yZoom);
    }
    for(Character item: items){
      if(item.isAlive()) item.render(renderer, xZoom, yZoom);
      if(item.particles()) item.renderParticles(renderer, xZoom, yZoom);
    }
  }

  public void update(Game game, Player player, Spawn spawner){
    for(Character character: characters){
      if(character.isAlive()) character.interact(game, player, spawner);
    }
    for(Character item: items){
      if(item.isAlive()) item.interact(game, player, spawner);
    }
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
    boolean stoppedChecking = false;
    for(Character character: characters){
      if(character.isAlive()) stoppedChecking = character.handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
      if(stoppedChecking) break;
    }
    for(Character item: items){
      if(item.isAlive()) stoppedChecking = item.handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
      if(stoppedChecking) break;
    }
    return stoppedChecking;
  }


  public int getLayer(){return layer;}

  public Rectangle getRectangle(){return rect;}

  public ArrayList <Character> getCharacters(){return characters;}
}
