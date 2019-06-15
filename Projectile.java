import java.awt.Point;
import java.awt.MouseInfo;

public class Projectile extends Character{
  private int cooldown;
  private int timer = 0;
  private int type;
  private boolean fired = false;
  private Rectangle runeRect;

  public Projectile(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, int type, String name, int seconds, Stats stats){
    super(sprite, 0, w, h,stats);
    this.name = name;
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    animatedSprite.setAnimationRange(0, 2);
    dead = false;
    move = false;
    this.type = type;
    if(type == 1){
      runeRect = new Rectangle(x,y,50,50);
      runeRect.generateGraphics(0xFFff00dc);
    }
    cooldown = seconds * 60;

    particle = true;

    particles = new Particle(rect.w, rect.h, 50, 1);
    particles.fill(0xFFF7D80C);

  }

  public Projectile(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, Sprite rune, int type, Stats stats){
    super(sprite, 0, w, h,stats);
    this.sprite = sprite;

    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    animatedSprite.setAnimationRange(0, 2);
    dead = false;
    move = false;
    int seconds = 3;
    type = 1;
    runeRect = new Rectangle(x,y,20,20);
    runeRect.generateGraphics(0xFFff00dc);
    cooldown = seconds * 60;

    particle = true;

    particles = new Particle(rect.w, rect.h, 50, 1);
    particles.fill(0xFFF7D80C);

  }

  public void updateStats(){

  }

  public void updateDirection(){

  }

  public void open(){
    move = true;
    timer = 40;
    fired = false;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    renderer.renderSprite(animatedSprite, rect.x, rect.y, 1, 1, false);
    if(type ==1) renderer.renderRectangle(runeRect, xZoom, yZoom, false);
  }


  public void action(Game game, Player player, Spawn spawner){
    // If they  are within range and they clicked F keyListener
    if(animatedSprite.getLooped()){
      if(timer < cooldown){
        animatedSprite.setStatic();
        dead = true;
        if(timer == 0){
          color = 0xFFe81414;
          if(type ==1)runeRect.generateGraphics(0xFF9d3131);
          hitbox = runeRect;
        }
        timer++;
      }
      else{
        timer = 0;
        dead = false;
        move = false;
        fired = false;
        color = 0xFFE7DF25;
        if(type ==1)runeRect.generateGraphics(0xFFff00dc);
        hitbox = null;
        line = new int[4];
        animatedSprite.reset();
      }
    }
    else if(move){
      if(fired)animatedSprite.update(game, player, spawner);
      else{
        if(timer < 30) game.line2();
        if(timer < 15) game.line3();
        if(timer <= 0){
          game.hideLine();
          fired = true;
        }
        timer--;
      }
    }
    int mouseX =  (int)(MouseInfo.getPointerInfo().getLocation().x-game.getCanvas().getLocationOnScreen().x- game.getWidth()/2 );
    int mouseY =  (int)(MouseInfo.getPointerInfo().getLocation().y-game.getCanvas().getLocationOnScreen().y - game.getHeight()/2);

    double magnitude = (1/Math.sqrt(Math.pow(mouseX, 2) + Math.pow(mouseY, 2)))*70.0;
    mouseX = player.getRectangle().x +(int)(mouseX*magnitude);
    mouseY = player.getRectangle().y +(int)(mouseY*magnitude);
    rect = new Rectangle(mouseX, mouseY, 100, 100);

    if(type == 0) ray(game, player);
    else if(!fired && type == 1) rune(game, player);

  }

  public void ray(Game game, Player player){
    line[0] = MouseInfo.getPointerInfo().getLocation().x -game.getCanvas().getLocationOnScreen().x;
    line[1] = MouseInfo.getPointerInfo().getLocation().y-game.getCanvas().getLocationOnScreen().y;
    line[2] =(game.getWidth()/2+ rect.x + rect.w/4 - game.getRenderer().getCamera().x - game.getRenderer().getCamera().w/2);
    line[3] = game.getHeight()/2 + rect.y +rect.h/4 - game.getRenderer().getCamera().y - game.getRenderer().getCamera().h/2;

    if(!fired)game.drawLine(0xFFf61f19, line[0], line[1], line[2], line[3], 5);
  }

  public boolean hitLine(){
    if(move && !fired) return true;
    return false;
  }

  public void rune(Game game, Player player){
    game.hideLine();
    runeRect.x = player.getRectangle().x+ MouseInfo.getPointerInfo().getLocation().x -game.getCanvas().getLocationOnScreen().x - game.getWidth()/2 - runeRect.w ;
    runeRect.y =  game.getRenderer().getCamera().y +MouseInfo.getPointerInfo().getLocation().y-game.getCanvas().getLocationOnScreen().y - runeRect.h;
  }


  public boolean isAlive(){
    return !false;
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    Rectangle collision = new Rectangle((int) Math.floor(((mouseRectangle.x + camera.x)/(16.0 * xZoom))), (int) Math.floor((mouseRectangle.y + camera.y)/(16.0 * yZoom)),1 ,1 );

    //DO SOMETHING
    if(!dead)open();
    return true;

  }

}
