class Weapon extends Character{
  private String name;
  public Weapon(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom){ //add stats to parameters
    super(sprite, 0, w, h);
    this.sprite = sprite;

    rect = new Rectangle(x, y, w, h);
    collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    animatedSprite.setAnimationRange(0, 9);
    dead = false;
    move = false;

    particles = new Particle(rect.w, rect.h, 50, 1);
    particles.fill(0xFFF7D80C);
  }

  public void action(Game game, Player player, Spawn spawner){
    if(animatedSprite.getLooped()){
      animatedSprite.setStatic();
      dead = true;
      particle = true;
    }
    else if(move)animatedSprite.update(game, player, spawner);
  }

  public boolean isAlive(){
    return !false;
  }

  public void updateStats(){

  }

  public void updateDirection(){

  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
    return false;
  }

  public void setName(String name){this.name=name;}

  public String getName(){return this.name;}
}
