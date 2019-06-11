public class Item{
  private Rectangle itemRectangle;
  private Rectangle collisionCheckRectangle;
  private static int layer = 1;
  private Sprite sprite;
  private AnimatedSprite animatedSprite = null;
	//Collision offset right and left of the blocks
	private final int xCollisionOffset = 15;
	private final int yCollisionOffset = 25;


  public Item(Sprite sprite, int xZoom, int yZoom, Rectangle rect){
		this.sprite = sprite;
		if(sprite != null && sprite instanceof AnimatedSprite) animatedSprite = (AnimatedSprite) sprite;
		// updateDirection();
		itemRectangle = rect;
		collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
	}

  public void render(RenderHandler renderer, int xZoom, int yZoom){
		if(animatedSprite != null) renderer.renderSprite(animatedSprite, Player.getPlayerRectangle().x, Player.getPlayerRectangle().y, xZoom, yZoom, false);
		else if(sprite != null) renderer.renderSprite(sprite, Player.getPlayerRectangle().x, Player.getPlayerRectangle().y, xZoom, yZoom, false);
		// else
		// renderer.renderRectangle(playerRectangle, xZoom, yZoom, false);
	}


}
