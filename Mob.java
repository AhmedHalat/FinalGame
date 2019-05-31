public class Mob extends Character{
  private int sheetSize;
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
    int newDirection = direction;
    collisionCheckRectangle.x = rect.x;
    collisionCheckRectangle.y = rect.y;

    boolean didMove = true;
    if(Math.abs(rect.x - player.getRectangle().x) > Math.abs(rect.y - player.getRectangle().y) ){
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

  public void updateStats(int [] stats){
    speed = stats[0];
  }

  public void updateDirection(){
    if(animatedSprite != null) animatedSprite.setAnimationRange(direction * sheetSize, (direction * sheetSize) + sheetSize-1);
  }

  public void update(){

  }

  public boolean isAlive(){
    return true;
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    return false;
  }

}
