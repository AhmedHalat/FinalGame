public class Chest extends Character{

  public Chest(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom){ //add stats to parameters
    super(sprite, 0, w, h);
    this.sprite = sprite;

    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    animatedSprite.setAnimationRange(0, 8);
    dead = false;
    move = false;
  }

  public void updateStats(int [] stats){

  }

  public void updateDirection(){

  }

  public void open(){
    System.out.println("OPENING");
  }

  public void action(Game game, Player player){
    // If they  are within range and they clicked F keyListener
    animatedSprite.update(game, player);
    //Drop items and open animation
  }

  public boolean isAlive(){
    return !dead;
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    if(mouseRectangle.intersects(rect)){
      //DO SOMETHING
      open();
      return true;
    }
    return false;
  }

  public String toString(){
    return " ";
  }

}
