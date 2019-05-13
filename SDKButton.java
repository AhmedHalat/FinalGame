public class SDKButton extends GUIButton{
	private Game game;
	private int tileID;
	private boolean upgrade = false;

	public SDKButton(Game game, Player player, int tileID, Sprite tileSprite, Rectangle rect, boolean upgrade){
		super(tileSprite, rect, true);
		this.game = game;
		this.tileID = tileID;
		this.upgrade = upgrade;
		update(game, player);
	}

	@Override
	public void update(Game game, Player player) {
		if(upgrade) rect.generateGraphics(0xFF67FF3D);
		else rect.generateGraphics(0xFFDB3D);
	}

	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom, Rectangle interfaceRect){
		renderer.renderRectangle(rect, interfaceRect, 1, 1, fixed);
		renderer.renderSprite(sprite, rect.x + interfaceRect.x + (xZoom - (xZoom - 1))*rect.w/2/xZoom, rect.y + interfaceRect.y + (yZoom - (yZoom - 1))*rect.h/2/yZoom, xZoom - 1, yZoom - 1, fixed);
	}

	public void activate(){
		game.changeTile(tileID);
	}

}
