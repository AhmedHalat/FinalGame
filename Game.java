import java.util.Arrays;
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
import java.io.*;
import java.awt.Font;

public class Game extends JFrame implements Runnable{

	public static int alpha = 0xFFFF00DC;
	Particle particle;

	private Canvas canvas = new Canvas();
	private RenderHandler renderer;

	private SpriteSheet sheet;
	private SpriteSheet playerSheet;

	private Player player;
	private Spawn spawner;

	private int selectedTileID = 2;
	private int selectedLayer = 0;

	private Tiles tiles;
	private Map map;

	private GameObject[] objects;
	private KeyBoardListener keyListener = new KeyBoardListener(this);
	private MouseEventListener mouseListener = new MouseEventListener(this);

	private int[][] randomMap;
	private int xZoom = 3;
	private int yZoom = 3;

	private Rectangle mouseRectangle;
	public int mapLevel = 1;
	public int room = 1;
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
		resetMap(new File ("Map.txt"));
		randomMap();
		map = new Map(new File("Map.txt"), tiles);

		//Load Map

		//Load SDK GUI
		GUIButton[] buttons = new GUIButton[tiles.size()];
		Sprite[] tileSprites = tiles.getSprites();


		for(int i = 0; i < buttons.length; i++){
			Rectangle tileRectangle = new Rectangle(0, i*(16*xZoom + 2), 16*xZoom, 16*yZoom);
			buttons[i] = new SDKButton(this, player, i, tileSprites[i], tileRectangle, false);
		}
		GUI gui = new GUI(null, 5, 5, true);

		BufferedImage chestSheetImage = loadImage("Chest.png");
		SpriteSheet chestSheet = new SpriteSheet(chestSheetImage);
		chestSheet.loadSprites(16, 16);
		AnimatedSprite chestAnimations = new AnimatedSprite(chestSheet, 25);
		Chest chest = new Chest(chestAnimations, 0, 0, 16, 16, 3, 3);

		//Load Objects
		objects = new GameObject[3];
		player = new Player(playerAnimations, xZoom, yZoom);
		spawner = new Spawn();
		objects[0] = player;
		objects[1] = gui;
		objects[2] = spawner;

		spawner.addCharacter(chest,1);


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

					canvas.requestFocus();
				}

				public void resetMap(File mapFile){
					try {
						FileWriter writer = new FileWriter(mapFile);
						writer.write("//Fill Tile\nFill:0\n//Layer,TileID,X,Y\n");
						writer.flush();
						writer.close();
					}catch (Exception e) {}
				}

				public void saveMap(String str){
					try{
						FileWriter fr = new FileWriter(new File("Map.txt"), true);
						fr.write(str+"\n");
						fr.close();
					}
					catch (java.io.IOException e){
						e.printStackTrace();
					}
				}

				public void update(){
					for(int i = 0; i < objects.length; i++)
					objects[i].update(this, player);
				}

				public static BufferedImage loadImage(String path){
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
					final int maxWidth = 22;
					final int minWidth = 16;
					final int maxHeight = 22;
					final int minHight = 16;
					selectedLayer = 1;
					int width, height, numberOfChambers = (int) (Math.random()*(5-3+1))+3;
					int layer = 0;
					randomMap = new int[numberOfChambers+1][4];
					height = (int) (Math.random()*(maxHeight-minHight+1))+minHight;

					for(int n = 0; n <= numberOfChambers; n++){
						width = (int) (Math.random()*(maxWidth-minWidth+1))+minWidth;
						height = (int) (Math.random()*(maxHeight-minHight+1))+minHight;
						randomMap[n][0] = -width/2;
						if (n == 0) randomMap[n][1] = height/2;
						else randomMap[n][1] = randomMap[n-1][1]-height/2-20;
						randomMap[n][2] = width/2;
						if (n == 0) randomMap[n][3] = -height/2;
						else randomMap[n][3] = randomMap[n-1][3]-height/2-20;
					}
					for (int i = 0; i < randomMap.length; i++) {
						for (int x = randomMap[i][0]; x <= randomMap[i][2]; x++){
							for (int y = randomMap[i][1]; y >= randomMap[i][3]; y--){
								if (i == 0 && y == randomMap[i][1] && (x == 1 || x == -1 || x == 0)) saveMap(0+","+6+","+x+","+y);
								else if (i == randomMap.length-1 && y == randomMap[i][3] && (x == 1 || x == -1 || x == 0)) saveMap(0+","+3+","+x+","+y);
								else if ((y == randomMap[i][1] || y == randomMap[i][3]) && (x == 1 || x == -1 || x == 0)) {}
								else if (x == randomMap[i][0] && y == randomMap[i][1]) saveMap(0+","+ 5+","+x+","+ y);
								else if (x == randomMap[i][2] && y == randomMap[i][1]) saveMap(0+","+ 7+","+x+","+ y);
								else if (x == randomMap[i][0]) saveMap(0+","+2+","+x+","+y);
								else if (x == randomMap[i][2]) saveMap(0+","+4+","+x+","+y);
								else if (y == randomMap[i][1]) saveMap(0+","+6+","+x+","+y);
								else if (y == randomMap[i][3]) saveMap(0+","+3+","+x+","+y);
								else saveMap(0+","+1+","+x+","+y);
							}
						}
					}
					for (int n =0; n < randomMap.length-1;n++)
					for(int i = randomMap[n][3]; i >= randomMap[n+1][1];i--){
						if (i != randomMap[n][3] && i != randomMap[n][1]) {
							saveMap(layer+","+2+","+-2+","+i);
							saveMap(layer+","+4+","+2+","+i);
						}
						saveMap(layer+","+1+","+1+","+i);
						saveMap(layer+","+1+","+0+","+i);
						saveMap(layer+","+1+","+-1+","+i);
						if (i == randomMap[n+1][1]){
							saveMap(layer+","+8+","+2+","+i);
							saveMap(layer+","+9+","+-2+","+i);
						}
					}
				}

				public void reset(){
					player.getRect().x = 0;
					player.getRect().y = 0;
					resetMap(new File ("Map.txt"));
					//Load Tiles
					tiles = new Tiles(new File("Tiles2.txt"),sheet);
					resetMap(new File ("Map.txt"));
					map = new Map(new File("Map.txt"), tiles);
					randomMap();
					render();
				}

				public void leftClick(int x, int y){
					 Rectangle mouseRectangle = new Rectangle(x, y, 1, 1);
					 boolean stoppedChecking = false;
					//
					for(int i = 0; i < objects.length; i++)
					if(!stoppedChecking) stoppedChecking = objects[i].handleMouseClick(mouseRectangle, renderer.getCamera(), xZoom, yZoom);
					/// if(!stoppedChecking){
					// 	x = (int) Math.floor(((x + renderer.getCamera().x)/(16.0 * xZoom)));
					// 	y = (int) Math.floor((y + renderer.getCamera().y)/(16.0 * yZoom));
					// 	map.setTile(selectedLayer, x, y, selectedTileID);
					// }
					//
					mapLevel++;
				}

				public void mapLocation() {
					for (int i = 0; i < randomMap.length; i++) {
						if (player.getRect().y < randomMap[i][1]*yZoom*16 && player.getRect().y > randomMap[i][3]*yZoom*16) room = i+1;
					}
				}


				public void render() {
					BufferStrategy bufferStrategy = canvas.getBufferStrategy();
					Graphics graphics = bufferStrategy.getDrawGraphics();
					super.paint(graphics);
					map.render(renderer, objects, xZoom, yZoom);
					mapLocation();
					// // player.renderParticles(renderer, 2, 2);
					renderer.render(graphics);
					graphics.setColor(new Color(255, 255, 255));
					renderer.renderString(graphics,mapLevel+"-"+room,getWidth() - 100, getHeight()- 50,50);
					graphics.dispose();
					bufferStrategy.show();
					renderer.clear();
				}

				//setters
				public void changeTile(int tileID){selectedTileID = tileID;}

				//getters
				public KeyBoardListener getKeyListener(){return keyListener;}
				public MouseEventListener getMouseListener(){return mouseListener;}
				public RenderHandler getRenderer(){return renderer;}
				public Map getMap() {return map;}
				public int getXZoom() {return xZoom;}
				public int getYZoom() {return yZoom;}


				public int getSelectedTile(){return selectedTileID;}

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
			}
