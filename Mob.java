import java.util.Comparator;
import java.lang.Override;
import java.lang.Comparable;

public class Mob extends Character{
  private int sheetSize;
  private int room;
  // private Rectangle rect;
  public Mob(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, int sheetSize, int room){ //add stats to parameters
    super(sprite, 3, w, h);
    this.sprite = sprite;
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    dead = false;
    move = true;
    this.room = room;
  }

  public Mob(int x, int y, int w, int h, int xZoom, int yZoom,int room){ //add stats to parameters
    super(3, w, h);
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    dead = false;
    move = true;
    this.room = room;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    rect.generateGraphics(0x0Fff0005);
    renderer.renderRectangle(rect, xZoom, yZoom, false);
    // System.out.println(this.stats.getHealthLeft()*1.0/this.stats.getHealth()*100);
    Rectangle r = new Rectangle(rect.x, rect.y-rect.h-5, (int) Math.round(this.stats.getHealthLeft()*1.0/this.stats.getHealth()*10), 5);
    r.generateGraphics(0xFFff000F);
    renderer.renderRectangle(r, xZoom, yZoom, false);
  }

  public void action(Game game, Player player, Spawn spawner){
    collisionCheckRectangle.x = rect.x;
    collisionCheckRectangle.y = rect.y;
    if(rect.intersects(player.getRectangle())){
       hit(player);
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

  public void updateStats(){
    speed = stats.getSpeed();
  }

  public void updateDirection(){
    if(animatedSprite != null) animatedSprite.setAnimationRange(direction * sheetSize, (direction * sheetSize) + sheetSize-1);
  }

  public void hit(Player player){
    this.stats.setHealthLeft(this.stats.getHealthLeft() - (player.stats.getDamage()));
    if (this.stats.getHealthLeft() <= 0){
      player.getStats().setExp(player.getStats().getExp()+1);
      dead = true;
      move = false;
    }
  }

  public boolean isAlive(){
    return !dead;
  }

  public int getRoom(){
    return this.room;
  }


  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    return false;
  }

}
