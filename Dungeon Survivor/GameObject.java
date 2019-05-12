 

public interface GameObject 
{

    //Call every time physically possible.
    //Parameters: Renderer - object that does all renders, Zooms - pixel multiplier.
    //Interface method
    //returns Void
    public void render(RenderHandler renderer, int xZoom, int yZoom);

    //Call at 60 fps rate.
    //Parameters: GameObject that are being updated
    //Interface method
    //returns Void
    public void update(Game game, Player player, Spawn spawner, Projectile pro, Melee melee);
    
    //Parameters: None
    //Call whenever mouse is clicked on Canvas.
    //Return true to stop checking other clicks.
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom);
    //Parameters: None
    //Interface method used to get layer of any gameobject
    //returns Void
    public int getLayer();
    //Parameters: None
    //Interface method. Used to get rectangle of any gameobject
    //returns Void
    public Rectangle getRectangle();
}