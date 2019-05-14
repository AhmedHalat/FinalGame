import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.lang.Runnable;
import java.lang.Thread;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class Game extends JFrame implements Runnable{

	public static int alpha = 0xFFFF00DC;

	private Canvas canvas = new Canvas();
	private RenderHandler renderer;

	private SpriteSheet sheet;
	private SpriteSheet playerSheet;

	private Player player;
	Rectangle proRect;

	private int selectedTileID = 2;
	private int selectedLayer = 0;

	private Tiles tiles;
	private Map map;

	private GameObject[] objects;
	private KeyBoardListener keyListener = new KeyBoardListener(this);
	private MouseEventListener mouseListener = new MouseEventListener(this);

	private int xZoom = 3;
	private int yZoom = 3;

	public Game(){
		//Make our program shutdown when we exit out.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set the position and size of our frame.
		setBounds(0,0, 1000, 800);
		//Put our frame in the center of the screen.
		setLocationRelativeTo(null);
		//Add our graphics compoent
		add(canvas);
		//Make our frame visible.
		setVisible(true);
		//Create our object for buffer strategy.
		canvas.createBufferStrategy(3);
		renderer = new RenderHandler(getWidth(), getHeight());

		//Load Assets
		BufferedImage sheetImage = loadImage("DungeonTileset2.png");
		sheet = new SpriteSheet(sheetImage);
		sheet.loadSprites(16, 16);

		BufferedImage playerSheetImage = loadImage("Player.png");
		playerSheet = new SpriteSheet(playerSheetImage);
		playerSheet.loadSprites(20, 26);

		//Player Animated Sprites
		AnimatedSprite playerAnimations = new AnimatedSprite(playerSheet, 5);

		//Load Tiles
		tiles = new Tiles(new File("Tiles2.txt"),sheet);

		//Load Map
		map = new Map(new File("Map.txt"), tiles);

		//Load SDK GUI
		GUIButton[] buttons = new GUIButton[tiles.size()];
		Sprite[] tileSprites = tiles.getSprites();

		for(int i = 0; i < buttons.length; i++){
			Rectangle tileRectangle = new Rectangle(0, i*(16*xZoom + 2), 16*xZoom, 16*yZoom);
			buttons[i] = new SDKButton(this, player, i, tileSprites[i], tileRectangle, false);
		}
		GUI gui = new GUI(buttons, 5, 5, true);

		//Load Objects
		objects = new GameObject[2];
		player = new Player(playerAnimations, xZoom, yZoom);
		objects[0] = player;
		objects[1] = gui;

		//Add Listeners
		canvas.addKeyListener(keyListener);
		canvas.addFocusListener(keyListener);
		canvas.addMouseListener(mouseListener);
		canvas.addMouseMotionListener(mouseListener);

		addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				int newWidth = canvas.getWidth();
				int newHeight = canvas.getHeight();
				if(newWidth > renderer.getMaxWidth()) newWidth = renderer.getMaxWidth();
				if(newHeight > renderer.getMaxHeight()) newHeight = renderer.getMaxHeight();
				renderer.getCamera().w = newWidth;
				renderer.getCamera().h = newHeight;
				canvas.setSize(newWidth, newHeight);
				pack();
			}
			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
		});
		randomMap();
		canvas.requestFocus();
	}

	public void update(){
		for(int i = 0; i < objects.length; i++)
			objects[i].update(this, player);
	}

	public BufferedImage loadImage(String path){
		try{
			BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
			BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

			return formattedImage;
		}
		catch(IOException exception){
			exception.printStackTrace();
			return null;
		}
	}

	public void saveMap(){
			map.saveMap();
	}


	public void randomMap(){
		final int maxWidth = 20;
		final int minWidth = 8;
		final int maxHeight = 20;
		final int minHight = 8;
		selectedLayer = 1;
		int width, height, numberOfChambers = (int) (Math.random()*(5-3+1))+3;
		int layer = 0;
		width = (int) (Math.random()*(maxWidth-minWidth+1))+minWidth;
		height = (int) (Math.random()*(maxHeight-minHight+1))+minHight;

	for(int n = 0; n <= numberOfChambers; n++){
		width = (int) (Math.random()*(maxWidth-minWidth+1))+minWidth;
		height = (int) (Math.random()*(maxHeight-minHight+1))+minHight;

		leftClick(-10, -10);
		for (int x = 0; x <= width; x++)
			for (int y = n*30+height;y <= n*30+2*height;y++)
				if (x == 0 && y == n*30+2*height) map.setTile(layer,x,y,5);
				else if (x == 0 ) map.setTile(layer,x,y,2);
				else if (x == width && y == n*30+2*height) map.setTile(layer,x,y,7);
				else if (x == width) map.setTile(layer,x,y,4);
				else if (y == n*30+height) map.setTile(layer,x,y,3);
				else if (y == n*30+2*height) map.setTile(layer,x,y,6);
				else map.setTile(layer,x,y,1);
	}
	for(int i =height; i < numberOfChambers*30+2*height;i++){
		map.setTile(layer,4,i,1);
		map.setTile(layer,5,i,1);
		map.setTile(layer,6,i,1);
	}
	}

	public void leftClick(int x, int y){
		Rectangle mouseRectangle = new Rectangle(x, y, 1, 1);
		boolean stoppedChecking = false;

		for(int i = 0; i < objects.length; i++) if(!stoppedChecking) stoppedChecking = objects[i].handleMouseClick(mouseRectangle, renderer.getCamera(), xZoom, yZoom);
		if(!stoppedChecking){
			x = (int) Math.floor(((x + renderer.getCamera().x)/(16.0 * xZoom)));
			y = (int) Math.floor((y + renderer.getCamera().y)/(16.0 * yZoom));
			map.setTile(selectedLayer, x, y, selectedTileID);

		}
	}

	public void rightClick(int x, int y) {
		x = (int) Math.floor((x + renderer.getCamera().x)/(16.0 * xZoom));
		y = (int) Math.floor((y + renderer.getCamera().y)/(16.0 * yZoom));
		map.removeTile(selectedLayer, x, y);
	}


	public void render() {
			BufferStrategy bufferStrategy = canvas.getBufferStrategy();
			Graphics graphics = bufferStrategy.getDrawGraphics();
			super.paint(graphics);

			map.render(renderer, objects, xZoom, yZoom);

			renderer.render(graphics);

			graphics.dispose();
			bufferStrategy.show();
			renderer.clear();
	}

	public void changeTile(int tileID){
		selectedTileID = tileID;
	}

	public int getSelectedTile(){
		return selectedTileID;
	}

	public void run(){
		long lastTime = System.nanoTime(); //long 2^63
		double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
		double changeInSeconds = 0;

		while(true){
			long now = System.nanoTime();

			changeInSeconds += (now - lastTime) / nanoSecondConversion;
			while(changeInSeconds >= 1) {
				update();
				changeInSeconds--;
			}
			render();
			lastTime = now;
		}
	}

	public static void main(String[] args){
		Game game = new Game();
		Thread gameThread = new Thread(game);
		gameThread.start();
	}

	public KeyBoardListener getKeyListener(){return keyListener;}

	public MouseEventListener getMouseListener(){return mouseListener;}

	public RenderHandler getRenderer(){return renderer;}

	public Map getMap() {return map;}

	public int getXZoom() {return xZoom;}

	public int getYZoom() {return yZoom;}
}
