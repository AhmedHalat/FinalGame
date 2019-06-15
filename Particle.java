/**
*Simple Particle effects
*/
public class Particle{
  private int timer;
  private int speed;
  private int width;
  private int height;
  private int[] pixels;
  private Rectangle rect;

  public Particle(int width, int height, int density, int speed){
    this.width = width;
    this.height = height;
    this.speed = speed;
    //Makes a transparent rectangle  of specified size
    rect = new Rectangle(0, 0, width, height);
    rect.generateGraphics(0xFFFF00DC);
    pixels = rect.getPixels();

    rect.generateGraphics(0xFFFF00DC);
    pixels = rect.getPixels();
    rect.clear();
    //fills the rectangle with particles, based on density
    for (int i = 0; i < pixels.length; i++) {
      if(Math.random() < 0.001 * density) pixels[i] = 0xFFE7DF25;
    }
    rect.setPixel(pixels);
  }
  //fills particle with default color if none are called
  public void fill(){
    fill(0xFFE7DF25);
  }
  /**
  * Change the color of the particles, move each particle
  * @param color the new color
  */
  public void fill(int color){
    timer = 0;
    //How fast and far to move the particle
    //Mod is big on purpose so some particles are lost,
    //The index would be too big and caught exception will cause particle to disappear
    //Adds the cool effect of making item seem older
    int mod = (int) (Math.random() * (-6) + 3);
    for (int i = 0; i < pixels.length; i++)
    //If pixel isnt transparent ie there is a particle there
    if(pixels[i] != 0xFFFF00DC){
      try{
        pixels[i] = 0xFFFF00DC;
        //Randomly moves particle up or LR
        //UP
        if(Math.random() < 0.6){
          if(i - width > 0 && i - width < pixels.length )pixels[i - width] = color;
          else pixels[pixels.length - i] = color;
        }
        //LR
        else{
          if(i + mod < pixels.length && i + mod > 0) pixels[i+mod] = color;
          else pixels[i] = color;
        }
      }
      catch(Exception e){
        //System.out.println("I: "+ i+ " Mod: "+ mod);
      }

      rect.setPixel(pixels);
    }

  }
  /**
   * Updates the particles position
   * @param x New x
   * @param y new Y
   */
  public void update(int x, int y){
    rect.x = x ;
    rect.y = y - height/4;
  }
  /**
   * Renders the particles and ticks them
   * @param renderer [description]
   * @param xZoom    [description]
   * @param yZoom    [description]
   */
  public void render(RenderHandler renderer, int xZoom, int yZoom){
    //moves particle with default color
    if(timer > speed) fill();
    else{
      //how often particles are moved
      timer++;
      rect.setPixel(pixels);
    }
    renderer.renderRectangle(rect, xZoom, yZoom, false);
  }
}
