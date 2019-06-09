import java.util.Comparator;
import java.lang.Override;
import java.lang.Comparable;

public class Mob extends Character{
  private int sheetSize;
  private int room;
  private int rectangle;
  private int cooldown;
  private int type;
  private int direction; //down left right up
  public Mob(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, int sheetSize, int room){ //add stats to parameters
    super(sprite, 3, w, h);
    this.sprite = sprite;
    this.sheetSize = sheetSize;
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    dead = false;
    move = true;
    direction = 0;
    this.room = room;
    type = (int) (Math.random()*(3-0+1))+0;
    animatedSprite.setAnimationRange(direction * sheetSize + 3* type, (direction * sheetSize) + 3*type + 2);

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
    int preDirection = direction;
    collisionCheckRectangle.x = rect.x;
    collisionCheckRectangle.y = rect.y;
    if(rect.intersects(player.getRectangle())){
      hit(player);
      return;
    }
    else if(cooldown > 30){
      cooldown = 0;
    if(Math.abs(rect.x - player.getRectangle().x) > Math.abs(rect.y - player.getRectangle().y) ){
      if(rect.x < player.getRectangle().x) direction = 2;
      else if(rect.x > player.getRectangle().x) direction = 1;
    }
    else{
      if(rect.y < player.getRectangle().y) direction = 0;
      else if(rect.y > player.getRectangle().y)direction = 3;
    }
    }
    if(direction == 0) collisionCheckRectangle.y += speed;
    else if(direction == 1) collisionCheckRectangle.x -= speed;
    else if(direction == 2) collisionCheckRectangle.x += speed;
    else if(direction == 3) collisionCheckRectangle.y -= speed;

    if(preDirection != direction)animatedSprite.setAnimationRange(direction * sheetSize + 3* type, (direction * sheetSize) + 3*type + 3);

    animatedSprite.update(game, player, spawner);
    didMove(game, player, spawner);
    cooldown++;
  }

  public void updateStats(){
    speed = stats.getSpeed();
  }

  public void updateDirection(){
    if(animatedSprite != null) animatedSprite.setAnimationRange(direction * 3, (direction * 3) + 2);
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
