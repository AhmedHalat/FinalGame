import java.awt.Point;
import java.awt.MouseInfo;

public class Projectile extends Character{
  private int cooldown;
  private int timer = 0;

  public Projectile(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom){ //add stats to parameters
    super(sprite, 0, w, h);
    this.sprite = sprite;

    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    animatedSprite.setAnimationRange(0, 2);
    dead = false;
    move = false;
    int seconds = 3;
    cooldown = seconds * 60;

    particle = true;

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

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    renderer.renderSprite(animatedSprite, rect.x, rect.y, 1, 1, false);

  }


  public void action(Game game, Player player, Spawn spawner){
    // If they  are within range and they clicked F keyListener
    if(animatedSprite.getLooped()){
      if(timer < cooldown){
        animatedSprite.setStatic();
        dead = true;
        if(timer == 0) color = 0xFFe81414;
        timer++;
      }
      else{
        timer = 0;
        dead = false;
        move = false;
        color = 0xFFE7DF25;
        animatedSprite.reset();
      }
    }
    else if(move)animatedSprite.update(game, player, spawner);
    int mouseX= player.getRectangle().x + (int)Math.round(0.3 * (MouseInfo.getPointerInfo().getLocation().x-game.getCanvas().getLocationOnScreen().x- game.getWidth()/2 ));
    int mouseY= player.getRectangle().y + (int)Math.round(0.3 *(MouseInfo.getPointerInfo().getLocation().y-game.getCanvas().getLocationOnScreen().y - game.getHeight()/2) );
    //mouseY-=game.getHeight();
    rect = new Rectangle(mouseX, mouseY, 100, 100);
    if(!dead)game.drawLine(MouseInfo.getPointerInfo().getLocation().x-game.getCanvas().getLocationOnScreen().x, MouseInfo.getPointerInfo().getLocation().y-game.getCanvas().getLocationOnScreen().y
    , game.getWidth()/2+ rect.x + rect.w/4 - game.getRenderer().getCamera().x - game.getRenderer().getCamera().w/2
    ,game.getHeight()/2 + rect.y +rect.h/4 - game.getRenderer().getCamera().y - game.getRenderer().getCamera().h/2);
    else game.hideLine();
  }

  public boolean isAlive(){
    return !false;
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    Rectangle collision = new Rectangle((int) Math.floor(((mouseRectangle.x + camera.x)/(16.0 * xZoom))), (int) Math.floor((mouseRectangle.y + camera.y)/(16.0 * yZoom)),1 ,1 );

    //DO SOMETHING
    open();
    return true;

  }

}
