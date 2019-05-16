public class Chest extends Character{
  public Chest(Sprite sprite, int x, int y, int w, int h, int xZoom, int yZoom){ //add stats to parameters
    super(sprite, 0, w, h);
    this.sprite = sprite;

    if(sprite != null && sprite instanceof AnimatedSprite) animatedSprite = (AnimatedSprite) sprite;
    updateDirection();
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    dead = false;
    move = false;
  }

  public void updateStats(int [] stats){

  }

  public void updateDirection(){

  }

  public void open(){

  }

  public void action(Game game, Player player){
    // If they  are within range and they clicked F keyListener
    dead = true;
    //Drop items and open animation
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    if(mouseRectangle.intersects(rect)){
      open();
      return true;
    }
    return false;
  }

}
