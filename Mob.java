import java.util.Comparator;
import java.lang.Override;
import java.lang.Comparable;
import java.util.Arrays;
import java.awt.geom.Line2D;
/**
 * Enemies are all mobs
 * Characters, randomly generated in game and added to spawner
 */
public class Mob extends Character{
  private int sheetSize;
  private int room;
  private int rectangle;
  private int cooldown;
  private int damageCooldown;
  private int type;
  private int direction; //down left right up

  public Mob(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, int sheetSize, int room, Stats stats){ //add stats to parameters
    super(sprite, 3, w, h, room, stats);
    this.sprite = sprite;
    this.animatedSprite = sprite;
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
  /**
   * Renders mob and healthbar
   * @param renderer
   * @param xZoom
   * @param yZoom
   */
  public void render(RenderHandler renderer, int xZoom, int yZoom){
    Rectangle r = new Rectangle(rect.x, rect.y-rect.h-5, (int) Math.round((this.stats.getHealthLeft()*1.0/this.stats.getHealth())*rect.w), 5);
    r.generateGraphics(0xFFff000F);
    renderer.renderSprite(animatedSprite, rect.x, rect.y, xZoom, yZoom, false);
    renderer.renderRectangle(r, xZoom, yZoom, false);
  }

  /**
   * Makes mobs do actions, called if !dead
   * @param game    Used for map collisions
   * @param player  used for attacking
   * @param spawner used for weapon collisions
   */
  public void action(Game game, Player player, Spawn spawner){
    Rectangle hitbox = rect;
    // hitbox.w*=3;
    // hitbox.h*=3;
    int preDirection = direction;
    collisionCheckRectangle.x = rect.x;
    collisionCheckRectangle.y = rect.y;
    //If spawner has a hitbox or player, check if they intersect
    if (spawner.hitbox() && hitbox.intersects(spawner.getHitBox()) && hitbox.intersects(player.getRect())){
      mobHit(player,spawner);
      // Cooldown timer for hitting
      if (damageCooldown%5 != 0 && damageCooldown != 0) {damageCooldown = 0; return;}
      playerHit(player);
      damageCooldown++;
      return;
    }
    //If the mob is touching the player
    else if(hitbox.intersects(player.getRect())){
      if (damageCooldown%5 != 0 && damageCooldown != 0) {damageCooldown = 0; return;}
      playerHit(player);
      damageCooldown++;
      return;
    }
    //If the spawner hitbox is hitting (without player as well)
    else if(spawner.hitbox() && hitbox.intersects(spawner.getHitBox())){
      mobHit(player,spawner);
      damageCooldown++;
      return;
    }
    //If spawner hitbox is a ray (ie Arcane Ray Book)
    else if(spawner.hitline()){
      //Gets the line and turns into start and end points
      int [] line = spawner.getHitLine();
        int x1 = line[0];
        int y1 = line[1];
        int x2 = line[2];
        int y2 = line[3];
        //Creates mob lines, 1 along each dimension
      int [][] mobLine = {
        {rect.x,rect.y,rect.x+rect.w * 3,rect.y},
        {rect.x,rect.y+rect.h *3,rect.x+rect.w *3,rect.y+rect.h *3},
        {rect.x,rect.y,rect.x,rect.y+rect.h *3},
        {rect.x+rect.w *3,rect.y,rect.x+rect.w*3,rect.y+rect.h*3}
      };
      //Check to see if ray intersects with any mob line
      for(int[] mobSide : mobLine){
        //Turning mob line into start and endpoint (translating from tile x-y coordinates, to mouse x-y coordinates)
        int x3 = mobSide[0] + game.getWidth()/2 - game.getRenderer().getCamera().x - game.getRenderer().getCamera().w/2;
        int y3 = mobSide[1]+ game.getHeight()/2 - game.getRenderer().getCamera().y - game.getRenderer().getCamera().h/2;
        int x4 = mobSide[2] + game.getWidth()/2 - game.getRenderer().getCamera().x - game.getRenderer().getCamera().w/2;
        int y4 = mobSide[3] + game.getHeight()/2 - game.getRenderer().getCamera().y - game.getRenderer().getCamera().h/2;
        //Creating both lines and checking collisions
        Line2D line1 = new Line2D.Float(x1, y1, x2, y2);
        Line2D line2 = new Line2D.Float(x3, y3, x4, y4);
        boolean result = line2.intersectsLine(line1);
        if(result) {
          mobHit(player,spawner);
          return;
        }
      }
    }
    //Walking cooldown
    else if(cooldown > 30){
      cooldown = 0;
      //Decide which way to move, set direction for updateDirection()
      if(Math.abs(rect.x - player.getRectangle().x) > Math.abs(rect.y - player.getRectangle().y) ){
        if(rect.x < player.getRectangle().x) direction = 2;
        else if(rect.x > player.getRectangle().x) direction = 1;
      }
      else{
        if(rect.y < player.getRectangle().y) direction = 0;
        else if(rect.y > player.getRectangle().y) direction = 3;
      }
    }
    if(direction == 0) collisionCheckRectangle.y += speed;
    else if(direction == 1) collisionCheckRectangle.x -= speed;
    else if(direction == 2) collisionCheckRectangle.x += speed;
    else if(direction == 3) collisionCheckRectangle.y -= speed;
    //Change sprite direction
    if(preDirection != direction)animatedSprite.setAnimationRange(direction * sheetSize + 3* type, (direction * sheetSize) + 3*type + 3);
    //Update and call didMove (in character)
    animatedSprite.update(game, player, spawner);
    didMove(game, player, spawner);
    cooldown++;
  }

  //Updating stuff
  public void updateStats(){
    speed = stats.getSpeed();
  }
  public void updateDirection(){
    if(animatedSprite != null) animatedSprite.setAnimationRange(direction * 3, (direction * 3) + 2);
  }

  /**
   * Making the mob get hit
   * @param player to get player stats
   * @param s      to get weapon stats
   */
  public void mobHit(Player player,Spawn s){
    //calculate damage
    int damage = Math.abs(player.getStats().getDamage()+s.getWeapon().getStats().getDamage()-this.stats.getDefense()/3);
    if (damage == 0) damage = 2;
    if (move) this.stats.setHealthLeft(this.stats.getHealthLeft() - damage);
    //kill them
    if (this.stats.getHealthLeft() <= 0){
      player.getStats().setExp(player.getStats().getExp()+1 + (int) Math.round(player.getStats().getLuck()*Math.random()));
      dead = true;
      move = false;
    }
  }

  /**
   * Making mob hit player
   * @param player For player stats
   */
  public void playerHit(Player player){
    if (player.getStats().getHealthLeft() > 0) player.getStats().setHealthLeft(player.getStats().getHealthLeft() - Math.abs(this.stats.getDamage()-player.getStats().getDefense()/3));
    // if character has been killed
    if (player.getStats().getHealthLeft() <= 0){
      player.getStats().setHealthLeft(0);
      player.setDead(true);
      player.setMove(false);
    }
  }
  //Getters
  public boolean isAlive(){
    return !dead;
  }

  public int getRoom(){
    return this.room;
  }

  public boolean getMove(){
    return this.move;
  }
  // Character doesnt use mouse
  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    return false;
  }
}
