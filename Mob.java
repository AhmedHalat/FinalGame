public class Mob extends Character{

  public Mob(Sprite sprite, int xZoom, int yZoom, int w, int h){ //add stats to parameters
    super(sprite, 3, 0, w, h);
    this.sprite = sprite;

    if(sprite != null && sprite instanceof AnimatedSprite) animatedSprite = (AnimatedSprite) sprite;
    updateDirection();
    Rectangle mobRectangle = new Rectangle(0, 0, w, h);
    Rectangle collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    super.setAnimatedSprite(animatedSprite);
    super.setRect(mobRectangle);
    super.setCollisionCheckRectangle(collisionCheckRectangle);
    dead = false;
    move = false;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    if(animatedSprite != null) renderer.renderSprite(animatedSprite, rect.x, rect.y, xZoom, yZoom, false);
    else if(sprite != null) renderer.renderSprite(sprite, rect.x, rect.y, xZoom, yZoom, false);
    else renderer.renderRectangle(rect, xZoom, yZoom, false);
  }

  public void playerPos(int x, int y){
    botDirecton(x, y);
  }

  public void proPos(int x, int y, int hitbox){
    // x y and hitbox size
    push = hitbox;
    Rectangle proRectangle = new Rectangle(x, y, hitbox, hitbox);
    if(!dead) if(rect.intersects(proRectangle)) zombieHit();
  }

  public void botDirecton(int playerX, int playerY){
    int x = rect.x - playerX;
    int y = rect.y - playerY;
    if(Math.abs(x) > 300 && Math.abs(y) > 300) move = true;

  }

  public void update(Game game, Player player){
    if(!dead)move(game, player);
  }

  public void move(Game game, Player player){
    int newDirection = direction;
    collisionCheckRectangle.x = rect.x;
    collisionCheckRectangle.y = rect.y;

    boolean didMove = false;

    if(newDirection != direction) updateDirection();
    if(didMove){
      didMove(game);
      animatedSprite.update(game, player);
    }
    else animatedSprite.reset();

  }

  public void didMove(Game game){

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

  public void zombieHit(){

  }

  public void updateStats(int [] stats){
    speed = stats[0];
  }

  public void updateDirection(){
    if(animatedSprite != null) animatedSprite.setAnimationRange(direction * 8, (direction * 8) + 7);
  }

  public void update(){

  }

  public int getLayer() {
    return layer;
  }

  public Rectangle getRectangle() {
    return rect;
  }

}
