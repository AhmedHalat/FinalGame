import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
/**
* Stores all characters per floor
*/
public class Spawn implements GameObject{
  private ArrayList<Character> characters= new ArrayList <Character> ();
  private ArrayList<Character> items= new ArrayList <Character> ();
  private Character weapon;
  private Rectangle rect;
  private int layer;
  private boolean equipped = false;
  private static int room;
  private static int wave;

  public Spawn(int roomWidth, int roomHeight, int roomType){
    rect = new Rectangle(0,0,0,0);
  }
  public Spawn(){
  }

  /**
  * Add Characters, Items and weapon in different ways
  */
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
  public void changeWeapon(Character weapon){
    this.weapon = weapon;
    equipped = true;
  }

  /**
  * Unequip weapon, stop ticking and rendering it
  */
  public void removeWeapon(){
    weapon = null;
    equipped = false;
  }
  /**
  * Reset spawn for new floor
  */
  public void removeAll(){
    characters.clear();
    items.clear();
  }

  /**
  * Mobs cant move when their not in the same room as the character
  * @param r The rooom the character is in
  */
  public void dontMove(int r){
    for(Character m: characters)
    if (r != m.getRoom()) m.setMove(true);
    else m.setMove(false);
  }

  /**
  * Checks if all characters in a room are dead
  * @param  room Room character is in
  * @return      Bool
  */
  public boolean allDead(int room){
    for (Character character : characters) if(character.isAlive() && character.getRoom() == room) return false;
    return true;
  }

  /**
  * Renders each character and weapon
  * @param renderer
  * @param xZoom
  * @param yZoom
  */
  public void render(RenderHandler renderer, int xZoom, int yZoom){
    //Render characters and their particles if isAlive and particles is true
    for(Character character: characters){
      if(character.isAlive()) character.render(renderer, xZoom, yZoom);
      if(character.particles()) character.renderParticles(renderer, xZoom, yZoom);
    }
    for(Character item: items){
      if(item.isAlive()) item.render(renderer, xZoom, yZoom);
      if(item.particles()) item.renderParticles(renderer, xZoom, yZoom);
    }
    //Renders weapon and particles if weapon is equipped
    if(equipped && weapon.isAlive()) weapon.render(renderer, xZoom, yZoom);
    if(equipped && weapon.particles()) weapon.renderParticles(renderer, xZoom, yZoom);

  }

  /**
  * Ticks all characters
  * @param game    Have different uses per character type
  * @param player
  * @param spawner
  */
  public void update(Game game, Player player, Spawn spawner){
    for(Character character: characters){
      if(character.isAlive()) character.interact(game, player, spawner);
    }
    for(Character item: items){
      if(item.isAlive()) item.interact(game, player, spawner);
    }

    if(equipped && weapon.isAlive()) weapon.interact(game, player, spawner);

  }

  /**
  * Checks if every character wants to use mouse
  * @param  mouseRectangle Mouse postision
  * @param  camera         camera position
  * @param  xZoom
  * @param  yZoom
  * @return                if mouse used
  */
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
    boolean stoppedChecking = false;
    //Checks if any characters were clicked, will always be false
    for(Character character: characters){
      if(character.isAlive()) stoppedChecking = character.handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
      if(stoppedChecking) return stoppedChecking;
    }
    //Checks if any chests were clicked
    for(Character item: items){
      if(item.isAlive()) stoppedChecking = item.handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
      if(stoppedChecking) return stoppedChecking;
    }
    //If nothing was clicked, attack
    if(equipped)stoppedChecking = weapon.handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
    return stoppedChecking;
  }

  /**
  * If arcanre rune book is current equiped and attacking
  * @return
  */
  public boolean hitbox(){
    if(equipped && weapon.getHitBox() != null) return true;
    return false;
  }
  /**
  * If arcanre ray book is current equiped and attacking
  * @return
  */
  public boolean hitline(){
    if(equipped && weapon.hitLine()) return true;
    return false;
  }

  /* GETTERS */
  public Rectangle getHitBox(){
    return weapon.getHitBox();
  }

  public int[] getHitLine(){
    return weapon.getHitLine();
  }

  public int getLayer(){return layer;}

  public Rectangle getRectangle(){return rect;}

  public ArrayList <Character> getCharacters(){return characters;}

  public Character getWeapon(){return weapon;}

}
