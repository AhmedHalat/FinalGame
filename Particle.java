public class Particle{
  private int width;
  private int height;
  private int timer;
  private int speed;
  private int[] pixels;
  //private int density;
  private Rectangle rect;

  public Particle(int width, int height, int density, int speed){
    this.width = width;
    this.height = height;
    this.speed = speed;
    //this.density = density;
    rect = new Rectangle(0, 0, width, height);
    rect.generateGraphics(0xFFFF00DC);
    pixels = rect.getPixels();

    rect.generateGraphics(0xFFFF00DC);
    pixels = rect.getPixels();
    rect.clear();
    for (int i = 0; i < pixels.length; i++) {
      if(Math.random() < 0.001 * density) pixels[i] = 0xFFE7DF25;
    }
    rect.setPixel(pixels);

    System.out.println(pixels.length);
  }

  public void fill(int color){
  }

  public void fill(){
    timer = 0;
		int mod = (int) (Math.random() * (-6) + 3);
    for (int i = 0; i < pixels.length; i++)
      if(pixels[i] != 0xFFFF00DC){
        try{
        pixels[i] = 0xFFFF00DC;
        if(Math.random() < 0.6){
          if(i - width > 0 && i - width < pixels.length )pixels[i - width] = 0xFFE7DF25;
          else pixels[pixels.length - i] = 0xFFE7DF25;
        }
        else{
          if(i + mod < pixels.length && i + mod > 0) pixels[i+mod] = 0xFFE7DF25;
          else pixels[i] = 0xFFE7DF25;
        }
      }
      catch(Exception e){
        System.out.println("I: "+ i+ " Mod: "+ mod);
      }

    rect.setPixel(pixels);
  }

  }

  public void update(int x, int y){
    rect.x = x ;
    rect.y = y - height/4;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    if(timer > speed) fill();
    else{
      timer++;
      rect.setPixel(pixels);
    }
    renderer.renderRectangle(rect, xZoom, yZoom, false);
  }
}
