/**
 * Stationary character type that simply opens when clicked and drops an item
 */
public class Chest extends Character{
  public Chest(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom){ //add stats to parameters
    super(sprite, 0, w, h);
    this.sprite = sprite;
    this.name = "Chest";

    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    animatedSprite.setAnimationRange(0, 9);
    dead = false;
    move = false;

    particles = new Particle(rect.w, rect.h, 50, 1);
    particles.fill(0xFFF7D80C);
  }

  public void open(){
    move = true;
  }

  /**
   * Do chest actions, only called when dead == false, Character abstract method
   * @param game    GameObjects
   * @param player
   * @param spawner
   */
  public void action(Game game, Player player, Spawn spawner){
    //If the animations already looped, ie chest is open
    if(animatedSprite.getLooped()){
      //make chest stay open and drop an item
      animatedSprite.setStatic();
      if(!dead)game.createWeaponDrop(0, name);
      //kill chest so action isnt called anymore and render particle
      dead = true;
      particle = true;
    }
    //if chest is oppening, update sprite
    else if(move)animatedSprite.update(game, player, spawner);
    //Drop items and open animation
  }

  //Chest never disappears
  public boolean isAlive(){
    return true;
  }

  //Chest doesnt have stats or direction
  public void updateStats(){
  }

  public void updateDirection(){
  }
  /**
   * Checks if chest was clicked
   * @param  mouseRectangle Stores mouse location to check collision
   * @param  camera         camera location over Map
   * @param  xZoom
   * @param  yZoom
   * @return                When true, game will stop checking if mouse collided w/ smthng else
   */
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    //If the chest is already open, false
    if(move) return false;
    //Create a collision rectangle and check if clicked
    Rectangle collision = new Rectangle((int) Math.floor(((mouseRectangle.x + camera.x)/(16.0 * xZoom))), (int) Math.floor((mouseRectangle.y + camera.y)/(16.0 * yZoom)),1 ,1 );
    if(collision.intersects(rect)){
      //Open chest
      open();
      return true;
    }
    return false;
  }

}
