import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite implements GameObject{

  private Sprite[] sprites;
  private int currentSprite = 0;
  private int speed;
  private int counter = 0;

  private int startSprite = 0;
  private int endSprite;
  private boolean looped = false;
  //Parameters: Spritesheet, renctangle of sprite positions, speed of shuffle through animations
  //Sets variables and sprites array
  //Constructor
  public AnimatedSprite(SpriteSheet sheet, Rectangle[] pos, int speed) {
    sprites = new Sprite[pos.length];
    this.speed = speed;
    this.endSprite = pos.length - 1;

    for(int i = 0; i < pos.length; i++)
    sprites[i] = new Sprite(sheet, pos[i].x, pos[i].y, pos[i].w, pos[i].h);
  }
  //Parameters: spritesheet and speed to shuffle through them
  //sets variables
  //Second constructor
  public AnimatedSprite(SpriteSheet sheet, int speed) {
    sprites = sheet.getLoadedSprites();
    this.speed = speed;
    this.endSprite = sprites.length - 1;
  }

  //GameObject method not used here
  public void render(RenderHandler renderer, int xZoom, int yZoom) {}

    //GameObject method not used here
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) { return false; }

    //Call at 60 fps rate.
    //Parameters: GameObjects that are being updated
    //calls increment sprite method everytime the speed of this object has been reached
    //returns Void
    public void update(Game game, Player player){
      counter++;
      if(counter >= speed) {
        counter = 0;
        incrementSprite();
      }
    }
    //Parameters: None
    //reset the sprite that were on
    //returns Void
    public void reset(){
      counter = 0;
      currentSprite = startSprite;
    }
    //Parameters: Startsprite and endsprite
    //Sets the range of animations, used mostly for bot and player, see bot class for good example of why this class is used
    //returns Void
    public void setAnimationRange(int startSprite, int endSprite)
    {
      this.startSprite = startSprite;
      this.endSprite = endSprite;
      reset();
    }
    //Parameters: None
    //returns sprites size
    public int getWidth(){
      return sprites[currentSprite].getWidth();
    }
    //Parameters: None
    //returns sprites size
    public int getHeight(){
      return sprites[currentSprite].getHeight();
    }
    //Parameters: None
    //returns sprites pixel array to be rendered
    public int[] getPixels(){
      return sprites[currentSprite].getPixels();
    }
    //Parameters: None
    //Changes the sprite thats curently being rendered
    //returns
    public void incrementSprite() {
      currentSprite++;
      if(currentSprite >= endSprite){
        currentSprite = startSprite;
        looped = true;
      }
    }

    public void setStatic(){
      currentSprite = endSprite;
    }

    public boolean getLooped(){
      return looped;
    }

    //Parameters: None
    //Not being used here. GameObject Method
    public int getLayer() {
      return -1;
    }
    //Parameters:
    //Not being used here. GameObject method
    public Rectangle getRectangle() {
      return null;
    }

  }
