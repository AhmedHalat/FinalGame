import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite implements GameObject{

  private Sprite[] sprites;
  private int currentSprite = 0;
  private int speed;
  private int counter = 0;

  private int startSprite = 0;
  private int endSprite;
  private boolean looped = false;

  public AnimatedSprite(SpriteSheet sheet, Rectangle[] pos, int speed) {
    sprites = new Sprite[pos.length];
    this.speed = speed;
    this.endSprite = pos.length - 1;

    for(int i = 0; i < pos.length; i++)
    sprites[i] = new Sprite(sheet, pos[i].x, pos[i].y, pos[i].w, pos[i].h);
  }

  public AnimatedSprite(SpriteSheet sheet, int speed) {
    sprites = sheet.getLoadedSprites();
    this.speed = speed;
    this.endSprite = sprites.length - 1;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom) {}


    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) { return false; }

    public void update(Game game, Player player, Spawn spawner){
      counter++;
      if(counter >= speed) {
        counter = 0;
        incrementSprite();
      }
    }

    public void reset(){
      counter = 0;
      currentSprite = startSprite;
      looped = false;
    }

    public void setAnimationRange(int startSprite, int endSprite){
      this.startSprite = startSprite;
      this.endSprite = endSprite;
      reset();
    }

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

  }
