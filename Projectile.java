import java.awt.Point;
import java.awt.MouseInfo;
/**
 * There are 2 types of projectiles (Should be renamed Books but too late)
 * Arcanre ray book and arcane rune book
 */
public class Projectile extends Character{
  private int cooldown;
  private int timer = 0;
  private int type;
  private boolean fired = false;
  private Rectangle runeRect;
  private Sprite rune;

  //Arcanre ray book constructor
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
  //Arcanre rune book constructor
  public Projectile(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, int type, String name, int seconds, Sprite rune, Stats stats){ //add stats to parameters
    super(sprite, 0, w, h,stats);
    this.name = name;
    // System.out.println(2);
    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    animatedSprite.setAnimationRange(0, 2);
    dead = false;
    move = false;
    this.type = type;
    this.rune = rune;
    // System.out.println("RUNE SPRITE ADDED");
    if(type == 1){
      runeRect = new Rectangle(x,y,50,50);
      runeRect.generateGraphics(0xFFff00dc);
    }
    cooldown = seconds * 60;

    particle = true;

    particles = new Particle(rect.w, rect.h, 50, 1);
    particles.fill(0xFFF7D80C);

  }

  /**
   * Make the book start attack timer, fired when done shooting
   */
  public void open(){
    move = true;
    timer = 40;
    fired = false;
  }

  /**
   * Renders the books
   * @param renderer
   * @param xZoom
   * @param yZoom
   */
  public void render(RenderHandler renderer, int xZoom, int yZoom){
    renderer.renderSprite(animatedSprite, rect.x, rect.y, 1, 1, false);
    //if rune book, render rune
    if(type ==1) renderer.renderSprite(rune, runeRect.x, runeRect.y, 1, 1, false);
  }

  /**
   * Ticks the book
   * @param game    Used for rendering lines, (renderer cant do that)
   * @param player  Update book location to player
   * @param spawner
   */
  public void action(Game game, Player player, Spawn spawner){
    // If book has finished its closing animation
    if(animatedSprite.getLooped()){
      //If the book cooldown has finished, keep the book closed
      if(timer < cooldown){
        animatedSprite.setStatic();
        dead = true;
        //if timer is done, attack
        if(timer == 0){
          color = 0xFFe81414;
          if(type ==1)runeRect.generateGraphics(0xFF9d3131);
          hitbox = runeRect;
        }
        timer++;
      }
      //If animation still happening
      else{
        //Not dead or done firing,
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

  //never dies, only equipped and UnEquipped
  public boolean isAlive(){
    return true;
  }

  public void updateStats(){
  }

  public void updateDirection(){
  }

  //Projectile is always the last charcter to check mouse, if nothign else uses it, attack
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    Rectangle collision = new Rectangle((int) Math.floor(((mouseRectangle.x + camera.x)/(16.0 * xZoom))), (int) Math.floor((mouseRectangle.y + camera.y)/(16.0 * yZoom)),1 ,1 );

    //DO SOMETHING
    if(!dead)open();
    return true;

  }

}
