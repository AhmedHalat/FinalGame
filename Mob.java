import java.util.Comparator;
import java.lang.Override;
import java.lang.Comparable;

public class Mob extends Character implements Comparable <Mob>{
  private int sheetSize;
  // private Rectangle rect;
  public Mob(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, int sheetSize){ //add stats to parameters
    super(sprite, 3, w, h);
    this.sprite = sprite;
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    dead = false;
    move = true;
  }

  public Mob(int x, int y, int w, int h, int xZoom, int yZoom){ //add stats to parameters
    super(3, w, h);
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    dead = false;
    move = true;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    rect.generateGraphics(0xFFff0005);
    renderer.renderRectangle(rect, xZoom, yZoom, false);
  }


  public void action(Game game, Player player, Spawn spawner){
    collisionCheckRectangle.x = rect.x;
    collisionCheckRectangle.y = rect.y;
    if(rect.intersects(player.getRectangle())){
       hit();
       return;
     }
    else if(Math.abs(rect.x - player.getRectangle().x) > Math.abs(rect.y - player.getRectangle().y) ){
      if(rect.x < player.getRectangle().x)collisionCheckRectangle.x += speed;
      else if(rect.x > player.getRectangle().x) collisionCheckRectangle.x -= speed;
      didMove(game);
    }
    else{
      if(rect.y < player.getRectangle().y) collisionCheckRectangle.y += speed;
      else if(rect.y > player.getRectangle().y) collisionCheckRectangle.y -= speed;
      didMove(game);
    }
  }

  public void updateStats(int [] stats){
    speed = stats[0];
  }

  public void updateDirection(){
    if(animatedSprite != null) animatedSprite.setAnimationRange(direction * sheetSize, (direction * sheetSize) + sheetSize-1);
  }

  public void hit(){
    dead = true;
    move = false;
  }

  public boolean isAlive(){
    return !dead;
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    return false;
  }

public boolean equals(Object o){
  Mob mob2 = (Mob) o;
  return this.rect.intersects(mob2.rect);
}

@Override
  public int compareTo(Mob mob2){
    if(this.rect.intersects(mob2.rect)) return 0;
    return 1;
  }

}
