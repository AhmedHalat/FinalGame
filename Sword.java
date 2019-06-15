class Sword extends Character{

  public Sword(AnimatedSprite sprite, int x, int y, int w, int h, int xZoom, int yZoom, int type){
    super(sprite, 0, w, h);
    this.sprite = sprite;
    
  }

  public void updateStats(){

  }

  public void updateDirection(){

  }

  public void action(Game game, Player player, Spawn spawner){

  }

  public boolean isAlive(){
    return false;
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
    return false;
  }

}
