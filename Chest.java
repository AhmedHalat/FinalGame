public class Chest extends Character{

  public Chest(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom){ //add stats to parameters
    super(sprite, 0, w, h);
    this.sprite = sprite;

    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    animatedSprite.setAnimationRange(0, 9);
    dead = false;
    move = false;

    particles = new Particle(rect.w, rect.h, 50, 1);
    particles.fill(0xFFF7D80C);
  }

  public void updateStats(int [] stats){

  }

  public void updateDirection(){

  }

  public void open(){
    move = true;
  }

  public void action(Game game, Player player, Spawn spawner){
    // If they  are within range and they clicked F keyListener
    if(animatedSprite.getLooped()){
      animatedSprite.setStatic();
      dead = true;
      particle = true;
    }
    else if(move)animatedSprite.update(game, player, spawner);
    //Drop items and open animation
  }

  public boolean isAlive(){
    return !false;
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    Rectangle collision = new Rectangle((int) Math.floor(((mouseRectangle.x + camera.x)/(16.0 * xZoom))), (int) Math.floor((mouseRectangle.y + camera.y)/(16.0 * yZoom)),1 ,1 );
    System.out.println("Collision"+collision);
    System.out.println("Rect"+rect);
    if(collision.intersects(rect)){
      //DO SOMETHING
      open();
      return true;
    }
    return false;
  }

}
