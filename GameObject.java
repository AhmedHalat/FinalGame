
public interface GameObject {

   public void render(RenderHandler renderer, int xZoom, int yZoom);

   public void update(Game game, Player player);

   public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom);

   public int getLayer();

   public Rectangle getRectangle();
}
