public class Projectile extends Character{
  private int timer = 120, lastHitTimer;
  private int attackSpeed = 60;

  private boolean directionStuck = false;
  private boolean goUp = false;
  private boolean goDown = false;
  private boolean goLeft = false;
  private boolean goRight = false;
  private boolean fly = false;
  private int playerX, playerY;

  private int mouseX, mouseY;

  public Projectile (Sprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, int sheetSize){
    super(sprite, 10, w, h);
    this.sprite = sprite;

    if(sprite != null && sprite instanceof AnimatedSprite) animatedSprite = (AnimatedSprite) sprite;
    updateDirection();
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 8*xZoom, 8*yZoom);
    dead = false;
    move = false;
  }

  public void updateDirection(int direction){
      if(timer >= attackSpeed ){
          if(direction == 0) goRight = true;
          else if (direction == 1) goLeft = true;
          else if(direction == 2) goUp = true;
          else if(direction == 3) goDown = true;
          rect.x = playerX;
          rect.y = playerY;
          fly = true;
          timer = 0;
          lastHitTimer = 0;
     }
  }

  public void updateStats(int [] stats){

  }

  public void updateDirection(){

  }

  public void action(Game game, Player player){
    
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    return true;
  }

}
