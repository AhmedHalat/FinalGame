

public abstract class GUIButton implements GameObject{
    protected Sprite sprite;
    protected Rectangle rect;
    protected boolean fixed;
    //Parameters: Sprite to be placed inside button, Rectangle - size and pos of button, fixed - if the button moves or stays on the camera
    //Sets variables
    //Constructor
    public GUIButton(Sprite sprite, Rectangle rect, Boolean fixed){
        this.sprite = sprite;
        this.rect = rect;
        this.fixed = fixed;
    }

    //Call every time physically possible.
    //Not used. gameobject method
    public void render(RenderHandler renderer, int xZoom, int yZoom) {}
    //Parameters: interfaceRect - changes position of rectangle so it stays on the camera
    //Renders the sprites onto the buttons
    //returns Void
    public void render(RenderHandler renderer, int xZoom, int yZoom, Rectangle interfaceRect){
        renderer.renderSprite(sprite, rect.x + interfaceRect.x, rect.y + interfaceRect.y, xZoom, yZoom, fixed);
    }

    //Call at 60 fps rate.
    //Not used gameObject method
    public void update(Game game) {}

    //Call whenever mouse is clicked on Canvas.
    //Parameters: Mouse x,y,w,h camera's x,y,w,h and zooms
    //Calls activate and returns true if the mouse is clicking on a GUI so game can stop checking
    //returns True is mouse is on gui else returns false
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
        if(mouseRectangle.intersects(rect)) {
            activate();
            return true;
        }

        return false;
    }
    //Abstract gameObject method only used in SDK button class
    public abstract void activate();
    //Parameters: None
    //layer is max since GUI is always ontop
    //returns max
    public int getLayer() {
        return Integer.MAX_VALUE;
    }
    //Parameters: None
    //GameObject method
    //returns gui position
    public Rectangle getRectangle() {
        return rect;
    }

}