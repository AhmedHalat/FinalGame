


public class GUI implements GameObject{
	 private Sprite backgroundSprite;
	 private GUIButton[] buttons;
	 private Rectangle rect = new Rectangle();
	 private boolean fixed;
	 //Parameters: sprites for each button, an array of buttons, x,y position of the buttons, if the button is fixed on the screen
	 //Sets global variables
	 //Constructor
	 public GUI(Sprite backgroundSprite, GUIButton[] buttons, int x, int y, boolean fixed){
			 this.backgroundSprite = backgroundSprite;
			 this.buttons = buttons;
			 this.fixed = fixed;

			 rect.x = x;
			 rect.y = y;

			 if(backgroundSprite != null) {
					 rect.w = backgroundSprite.getWidth();
					 rect.h = backgroundSprite.getHeight();
			 }
	 }
	 //Parameters: Buttons, xy positions, fixed boolean
	 //Calls first constructor
	 //2nd Constructor
	 public GUI(GUIButton[] buttons, int x, int y, boolean fixed){
			 this(null, buttons, x, y, fixed);
	 }

	 public GUI(){

	 }


	 //Call every time physically possible.'
	 //Parameters: renderer object, zooms
	 //renders the GUI
	 //returns void
	 public void render(RenderHandler renderer, int xZoom, int yZoom){
			 if(backgroundSprite != null) renderer.renderSprite(backgroundSprite, rect.x, rect.y, xZoom/16, yZoom/16, fixed);

			 if(buttons != null)
					 for(int i = 0; i < buttons.length; i++) buttons[i].render(renderer, xZoom, yZoom, rect);

	 }

	 //Call at 60 fps rate.
	 //Parameters: GameObjects that need to be updated
	 //updates the buttons
	 //returns Void
	 public void update(Game game, Player player){
			 if(buttons != null)
					 for(int i = 0; i < buttons.length; i++) buttons[i].update(game);
	 }

	 //Call whenever mouse is clicked on Canvas.
	 //Parameters: mouserectangle ,camera, zooms
	 //checks every button to see if theyve been clicked
	 //returns boolean
	 public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
			 boolean stopChecking = false;
			 if(buttons == null) return stopChecking;
			 else if(!fixed)  mouseRectangle = new Rectangle(mouseRectangle.x + camera.x, mouseRectangle.y + camera.y, 1, 1);
			 else mouseRectangle = new Rectangle(mouseRectangle.x, mouseRectangle.y, 1, 1);

			 if(rect.w == 0 || rect.h == 0 || mouseRectangle.intersects(rect)) {
					 mouseRectangle.x -= rect.x;
					 mouseRectangle.y -= rect.y;
					 for(int i = 0; i < buttons.length; i++) {
							 boolean result = buttons[i].handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
							 if(stopChecking == false) stopChecking = result;
					 }
			 }

			 return stopChecking;
	 }
	 //Parameters: None
	 //returns top layer
	 public int getLayer() {
			 return Integer.MAX_VALUE;
	 }
	 //Parameters: None
	 //returns rect GameObject Method
	 public Rectangle getRectangle() {
			 return rect;
	 }


}
