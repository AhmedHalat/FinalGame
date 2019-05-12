 

public class SDKButton extends GUIButton{
    private Game game;
    private Player player;
    private Projectile pro;
    private int statID;
    private boolean upgrade = false;
    //Parameters: Required objects to call update method from gameObject, StatID - the id of the button thats clicked, rect - position and size of button, upgrade - boolean; true if player has availible stat points
    //Sets global variables and calls update
    //Constructor
    public SDKButton(Game game, Player player, Spawn spawner, Melee melee, int statID, Sprite tileSprite, Rectangle rect, boolean upgrade) 
    {
        super(tileSprite, rect, true);
        this.game = game;
        this.player = player;
        this.statID = statID;
        this.upgrade = upgrade;
        //0xFFFB3D
        rect.generateGraphics(0xFFFB3D);
        update(game, player, spawner, pro, melee);
    }
    
    //Parameters: GameObjects that are being updated
    //Changes the color of the button
    //returns 
    public void update(Game game, Player player, Spawn spawner, Projectile pro, Melee melee) {
        if(upgrade)  rect.generateGraphics(0xFF67FF3D);
        else rect.generateGraphics(0xFFDB3D);
    }
    @Override
    //Parameters: interfaceRect - changes rect position based on camera motion so the rect stays still
    //Overides the basic render method
    //returns Void
    public void render(RenderHandler renderer, int xZoom, int yZoom, Rectangle interfaceRect){
        renderer.renderRectangle(rect, interfaceRect, 1, 1, fixed);
        renderer.renderSprite(sprite, 
                              rect.x + interfaceRect.x + (xZoom - (xZoom - 1))*rect.w/2/xZoom, 
                              rect.y + interfaceRect.y + (yZoom - (yZoom - 1))*rect.h/2/yZoom, 
                              xZoom - 1, 
                              yZoom - 1, 
                              fixed);
    }
    //changes the players stats
    public void activate(){
        game.changeStat(statID, upgrade);
    }

}