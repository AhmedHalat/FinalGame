import java.awt.image.BufferedImage;

/**
 * Creates an animation using spites to be used by all Character
 */
public class AnimatedSprite extends Sprite implements GameObject{
  //global variables
  private Sprite[] sprites;
  private int currentSprite = 0;
  private int speed;
  private int counter = 0;
  private int startSprite = 0;
  private int endSprite;
  private boolean looped = false;

  /**
   * main Constructor that creates the Animation
   * @param sheet the sprite Sheet
   * @param pos   the position the character exists on the SpriteSheet
   * @param speed the speed to cycle trough
   */
  public AnimatedSprite(SpriteSheet sheet, Rectangle[] pos, int speed) {
    sprites = new Sprite[pos.length];
    this.speed = speed;
    this.endSprite = pos.length - 1;
    for(int i = 0; i < pos.length; i++)sprites[i] = new Sprite(sheet, pos[i].x, pos[i].y, pos[i].w, pos[i].h);
  }

  /**
  * Constructor that creates the Animation
  * @param sheet the sprite Sheet
  * @param speed the speed to cycle trough
   */
  public AnimatedSprite(SpriteSheet sheet, int speed) {
    sprites = sheet.getLoadedSprites();
    this.speed = speed;
    this.endSprite = sprites.length - 1;
  }


  /**
   * updates the animation of the sprites
    interfaced vars not used
   */
    public void update(Game game, Player player, Spawn spawner){
      counter++;
      if(counter >= speed) {
        counter = 0;
        incrementSprite();
      }
    }

    //reset animation time
    public void reset(){
      counter = 0;
      currentSprite = startSprite;
      looped = false;
    }

    /**
     * creates an animation Range
     * @param startSprite starting Pos
     * @param endSprite   ending Pos
     */
    public void setAnimationRange(int startSprite, int endSprite){
      this.startSprite = startSprite;
      this.endSprite = endSprite;
      reset();
    }

    //animate sprite in  a loop
    public void incrementSprite() {
      currentSprite++;
      if(currentSprite >= endSprite){
        currentSprite = startSprite;
        looped = true;
      }
    }

    //getters and setters
    public void setStatic(){
      currentSprite = endSprite;
    }

    public boolean getLooped(){
      return looped;
    }

    public int getHeight(){
      return sprites[currentSprite].getHeight();
    }

    public int getWidth(){
      return sprites[currentSprite].getWidth();
    }

    public int[] getPixels(){
      return sprites[currentSprite].getPixels();
    }

    public int getLayer() {
      return -1;
    }

    public Rectangle getRectangle() {
      return null;
    }

    //unused Methods
    public void render(RenderHandler renderer, int xZoom, int yZoom) {}
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) { return false; }
  }
