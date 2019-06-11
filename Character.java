import java.util.Map;
import java.util.HashMap;

public abstract class Character implements Comparable <Character>{
  protected Rectangle rect;
  protected Rectangle collisionCheckRectangle;
  //protected abstract Item drop;

  //0 = Right, 1 = Left, 2 = Up, 3 = Down
  protected int direction;
  protected int speed;
	protected int layer;
	protected Sprite sprite;
  protected AnimatedSprite animatedSprite;

  protected boolean dead;
  protected boolean move;
  //Collision offset right and left of the blocks
  protected final int xCollisionOffset;
  protected final int yCollisionOffset;

  protected Particle particles;
  protected boolean particle;
  // protected static Map <String,Integer> stats = new HashMap <String,Integer> ();
  protected Stats stats;
  protected int color = 0;
  protected Rectangle hitbox;
  protected int[] line = new int[4];

   Character[] drops = new Character[2];

  public Character(AnimatedSprite animatedSprite, Rectangle rect, Rectangle collisionCheckRectangle, int speed, int direction, int layer, int xCollisionOffset, int yCollisionOffset) {
    this.rect = rect;
    this.collisionCheckRectangle = collisionCheckRectangle;
    this.direction = direction;
    this.speed = speed;
    this.layer = layer;
    this.animatedSprite = animatedSprite;
    this.xCollisionOffset = xCollisionOffset;
    this.yCollisionOffset = yCollisionOffset;
    this.stats = new Stats(1,10,1,100,speed);
  }

  public Character(AnimatedSprite sprite, int speed, int xCollisionOffset, int yCollisionOffset){
    this.animatedSprite = sprite;
    this.speed = speed;
    this.layer = 0;
    this.xCollisionOffset = xCollisionOffset;
    this.yCollisionOffset = yCollisionOffset;
    this.stats = new Stats(1,10,1,100,speed);
  }


  public Character(int speed, int xCollisionOffset, int yCollisionOffset){
    this.speed = speed;
    this.layer = 0;
    this.xCollisionOffset = xCollisionOffset;
    this.yCollisionOffset = yCollisionOffset;
    this.stats = new Stats(1,10,1,100,speed);
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    if(animatedSprite != null) renderer.renderSprite(animatedSprite, rect.x, rect.y, xZoom, yZoom, false);
    else if(sprite != null) renderer.renderSprite(sprite, rect.x, rect.y, xZoom, yZoom, false);
    else renderer.renderRectangle(rect, xZoom, yZoom, false);
  }

  public void renderParticles(RenderHandler renderer, int xZoom, int yZoom){
    if(color != 0)particles.fill(color);
    particles.update(rect.x + 6, rect.y - 6);
    particles.render(renderer, 2, 2);
  }

  public void interact(Game game, Player player, Spawn spawner){
    if(true) action(game, player, spawner);
  }


  public void didMove(Game game ,Player player, Spawn spawner){
    collisionCheckRectangle.x += xCollisionOffset;
    collisionCheckRectangle.y += yCollisionOffset;

    Rectangle axisCheck = new Rectangle(collisionCheckRectangle.x, rect.y + yCollisionOffset, collisionCheckRectangle.w, collisionCheckRectangle.h);

    //Check the X axis
    if(!game.getMap().checkCollision(axisCheck, 0, 3, 3) && !game.getMap().checkCollision(axisCheck, 0 + 1, 3, 3)) rect.x = collisionCheckRectangle.x - xCollisionOffset;

    axisCheck.x = rect.x + xCollisionOffset;
    axisCheck.y = collisionCheckRectangle.y;
    axisCheck.w = collisionCheckRectangle.w;
    axisCheck.h = collisionCheckRectangle.h;
    //axisCheck = new Rectangle(botRectangle.x, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);
    //Check the Y axis
    if(!game.getMap().checkCollision(axisCheck, 0, 3, 3) && !game.getMap().checkCollision(axisCheck, 0 + 1, 3, 3)) rect.y = collisionCheckRectangle.y - yCollisionOffset;
  }

  public abstract void updateStats();
  public abstract void updateDirection();
  public abstract void action(Game game, Player player, Spawn spawner);
  public abstract boolean isAlive();
  public abstract boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom);


  //Getters and Setters
  public int getRoom(){
    try {
      if (this instanceof Player) return -1;
      return this.getRoom();
    } catch(Exception e) {
      System.out.println(this);
    }
    return -1;
  }
	public Rectangle getRect() {return rect;}

	public void setRect(Rectangle rect) {this.rect = rect;}

	public Rectangle getCollisionCheckRectangle() {return collisionCheckRectangle;}

  public int getCollisionCheckRectangleX() {return collisionCheckRectangle.x;}

  public int getCollisionCheckRectangleY() {return collisionCheckRectangle.y;}

	public void setCollisionCheckRectangle(Rectangle collisionCheckRectangle) {this.collisionCheckRectangle = collisionCheckRectangle;}

  public void setCollisionCheckRectangleX(int x) {this.collisionCheckRectangle.x = x;}

  public void setCollisionCheckRectangleY(int y) {this.collisionCheckRectangle.y = y;}

	public int getDirection() {return direction;}

	public void setDirection(int direction) {this.direction = direction;}

	public int getSpeed() {return speed;}

	public void setSpeed(int speed) {this.speed = speed;}

	public int getLayer() {return layer;}

  public boolean particles(){return particle;}

	public void setLayer(int layer) {this.layer = layer;}

	public Sprite getSprite() {return sprite;}

	public void setSprite(Sprite sprite) {this.sprite = sprite;}

	public AnimatedSprite getAnimatedSprite() {return animatedSprite;}

	public void setAnimatedSprite(AnimatedSprite animatedSprite) {this.animatedSprite = animatedSprite;}

	public int getXCollisionOffset() {return xCollisionOffset;}

	public int getYCollisionOffset() {return yCollisionOffset;}

  public Stats getStats() {return this.stats;}

  public void setStats(Stats stats) {this.stats = stats;}

  public void setStats(int luck, int defense, int damage, int health,int healthLeft, int speed) {
		this.stats.setLuck(luck);
		this.stats.setDefense(defense);
		this.stats.setDamage(damage);
		this.stats.setHealth(health);
		this.stats.setHealthLeft(healthLeft);
		this.stats.setSpeed(speed);
	}

  public Rectangle getHitBox(){
    return hitbox;
  }

  public int[] getHitLine(){
    return line;
  }

  public boolean hitbox(){
    if(hitbox != null) return true;
    return false;
  }

  public boolean hitLine(){
    if(line != null) return true;
    return false;
  }


  @Override
    public int compareTo(Character mob2){
      if(this.rect.intersects(mob2.rect)) return 0;
      return 1;
    }

    public boolean equals(Object o){
      Mob mob2 = (Mob) o;
      return this.rect.intersects(mob2.rect);
    }

}
