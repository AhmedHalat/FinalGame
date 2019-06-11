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
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.HashSet;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Game extends JFrame implements Runnable, ActionListener{

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

	private int mX;
	private int mY;
	private int mW;
	private int mH;
	private boolean showMouseLine = false;
	private int mX2;
	private int mY2;
	private int mW2;
	private int mH2;
	private boolean showMouseLine2 = false;
		private boolean line2 = false;
			private boolean line3 = false;

	private Tiles tiles;
	private Map map;

	private GameObject[] objects;
	private KeyBoardListener keyListener = new KeyBoardListener(this);
	private MouseEventListener mouseListener = new MouseEventListener(this);

	private int[][] randomMap;
	private int xZoom = 3;
	private int yZoom = 3;

	private Graphics graphics;

	private Rectangle mouseRectangle;
	public int mapLevel = 1;
	public int room = 1;
	public int levelUp = 0;
	public static JMenuBar mainMenu = new JMenuBar ();
	public static Set mobSet = new HashSet <Character> ();

	public Game(){
		//Make our program shutdown when we exit out.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set the position and size of our frame.
		setBounds(0,0, 1000, 800);
		//Put our frame in the center of the screen.
		setLocationRelativeTo(null);

		BufferedImage playerSheetImage = loadImage("Player.png");
		playerSheet = new SpriteSheet(playerSheetImage);
		playerSheet.loadSprites(20, 26);

		//Player Animated Sprites
		AnimatedSprite playerAnimations = new AnimatedSprite(playerSheet, 5);
		player = new Player(playerAnimations, xZoom, yZoom);
		//Menu Bar
		jMenu();

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

		//Load Tiles and Map
		tiles = new Tiles(new File("Tiles2.txt"),sheet);
		resetMap(new File ("Map.txt"));
		randomMap();
		map = new Map(new File("Map.txt"), tiles);


		Sprite[] tileSprites = tiles.getSprites();
		BufferedImage proImg1 = loadImage("book1.png");
		SpriteSheet proSheet1 = new SpriteSheet(proImg1);
		proSheet1.loadSprites(62, 54);
		AnimatedSprite pro1 = new AnimatedSprite(proSheet1, 25);

		Character projectile = new Projectile(pro1, 0, 0, 16, 16, 1, 1,1);

		//Load Objects
		objects = new GameObject[2];
		spawner = new Spawn();
		objects[0] = player;
		objects[1] = spawner;

		spawner.addWeapon(projectile);
		randomMobs();

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


				public void jMenu(){
					mainMenu.removeAll();
					JMenuItem weapon = new JMenuItem ("Weapon");
					JMenuItem weapon2 = new JMenuItem ("Weapon 2");
					JMenuItem attack = new JMenuItem ("Attack: " + (player.getStats().getDamage()));
					JMenuItem defense = new JMenuItem ("Defense: " + (player.getStats().getDefense()));
					JMenuItem health = new JMenuItem ("Health: " + (player.getStats().getHealth()));
					JMenuItem speed = new JMenuItem ("Speed: " + (player.getStats().getSpeed()));
					JMenuItem luck = new JMenuItem ("Luck: " + (player.getStats().getLuck()));
					JMenuItem expInfo = new JMenuItem ("You can now level up "+levelUp+" times!\nClick on a stat to upgrade it!");
					if (levelUp == 1)  expInfo = new JMenuItem ("You can now level up "+levelUp+" time!\nClick on a stat to upgrade it!");
					// JMenuItem weaponAttack = new JMenuItem ("Attack: " + (player.getStats().getDamage()));
					// JMenuItem weaponDefense = new JMenuItem ("Defense: " + (player.getStats().getDefense()));
					// JMenuItem weaponSpeed = new JMenuItem ("Speed: " + (player.getStats().getSpeed()));
					// JMenuItem weaponLuck = new JMenuItem ("Luck: " + (player.getStats().getLuck()));
					JMenu statMenu = new JMenu ("Stats");
					JMenu expMenu = new JMenu ("Level Up!");
					JMenu weaponMenu = new JMenu ("Weapons");
					expMenu.add(expInfo);
					weaponMenu.add(weapon);
					weaponMenu.add(weapon2);
					statMenu.add(attack);
					statMenu.add(defense);
					statMenu.add(health);
					statMenu.add(speed);
					statMenu.add(luck);
					mainMenu.add (weaponMenu);
					mainMenu.add (statMenu);
					mainMenu.add(expMenu);
					setJMenuBar(mainMenu);
					attack.setActionCommand ("Attack");
					attack.addActionListener (this);
					defense.setActionCommand ("Defense");
					defense.addActionListener (this);
					health.setActionCommand ("Health");
					health.addActionListener (this);
					speed.setActionCommand ("Speed");
					speed.addActionListener (this);
					luck.setActionCommand ("Luck");
					luck.addActionListener (this);
					weapon.setActionCommand ("weapon");
					weapon.addActionListener (this);
					weapon2.setActionCommand ("weapon2");
					weapon2.addActionListener (this);
					if (levelUp == 0) expMenu.hide();
					expInfo.disable();
				}

				public void actionPerformed(ActionEvent event) {
					// for (Character c: spawner.getCharacters()) System.out.println(c.isAlive() + ":	" + c.getRoom());
					// System.out.println("All Dead:" + spawner.allDead(0));

					if (keyListener.levelUp()) levelUp++;
					if (levelUp > 0){
					String e = event.getActionCommand ();
					if (e.equals("Attack")) player.getStats().setDamage(player.getStats().getDamage()+1);
					else if (e.equals("Defense")) player.getStats().setDefense(player.getStats().getDefense()+1);
					else if (e.equals("Speed")) player.getStats().setSpeed(player.getStats().getSpeed()+1);
					else if (e.equals("Health")) player.getStats().setHealth(player.getStats().getHealth()+10);
					else if (e.equals("Luck")) player.getStats().setLuck(player.getStats().getLuck()+1);
					levelUp--;
					player.updateStats();
					jMenu();
				}
				}

				public void resetMap(File mapFile){
					try {
						FileWriter writer = new FileWriter(mapFile);
						writer.write("//Fill Tile\nFill:0\n//Layer,TileID,X,Y\n");
						writer.flush();
						writer.close();
					}catch (Exception e) {e.printStackTrace();}
				}

				public void saveMap(String str){
					try{
						FileWriter fr = new FileWriter(new File("Map.txt"), true);
						fr.write(str+"\n");
						fr.close();
					}
					catch (java.io.IOException e){e.printStackTrace();}
				}

				public void update(){
					for(int i = 0; i < objects.length; i++)
					objects[i].update(this, player, spawner);
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
					int width, height, numberOfChambers = 2;//(int) (Math.random()*(5-3+1))+3;
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
								if (i == 0 && y == randomMap[i][1] && (x == 1 || x == -1 || x == 0)) saveMap("0,6,"+x+","+y);
								else if (i == randomMap.length-1 && y == randomMap[i][3] && (x == 1 || x == -1 || x == 0)) saveMap(0+","+3+","+x+","+y);
								else if (i == randomMap.length-1 && y == randomMap[i][3]+3 && x == 0) saveMap("0,10,"+x+","+y);
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
						if (i == randomMap[n][3] || i == randomMap[n][1]) {
							saveMap(layer+","+3+","+1+","+i);
							saveMap(layer+","+3+","+0+","+i);
							saveMap(layer+","+3+","+-1+","+i);
						}
						else{
							saveMap(layer+","+1+","+1+","+i);
							saveMap(layer+","+1+","+0+","+i);
							saveMap(layer+","+1+","+-1+","+i);
						}
						if (i == randomMap[n+1][1]){
							saveMap(layer+","+8+","+2+","+i);
							saveMap(layer+","+9+","+-2+","+i);
						}
					}
				}

				public void randomMobs(){
					mobSet.removeAll(mobSet);
					BufferedImage enemySheetImage = loadImage("EnemySpriteSheet.png");
					SpriteSheet enemySheet = new SpriteSheet(enemySheetImage);
					enemySheet.loadSprites(27, 28);
					AnimatedSprite enemyAnimations = new AnimatedSprite(enemySheet, 25);

					mobSet.add(new Mob(enemyAnimations, 0, -360, 16, 26, 16, 16,12, 0));
					System.out.println("create");
					BufferedImage chestSheetImage = loadImage("Chest.png");
					SpriteSheet chestSheet = new SpriteSheet(chestSheetImage);
					chestSheet.loadSprites(16, 16);
					AnimatedSprite chestAnimations = new AnimatedSprite(chestSheet, 25);
					Character chest = new Chest(chestAnimations, 0, 0, 16, 16, 6, 6);
					spawner.removeAll();
					spawner.addCharacter(mobSet);
					spawner.addItem(chest, 1);
				}


				public void reset(){
					randomMobs();
					player.getRect().x = 0;
					player.getRect().y = 0;
					//Load Tiles
					resetMap(new File ("Map.txt"));
					randomMap();
					map = new Map(new File("Map.txt"), tiles);
					render();
				}

				public void leftClick(int x, int y){
					if (keyListener.levelUp()) player.getStats().setExp(player.getStats().getExp()+50);
					 Rectangle mouseRectangle = new Rectangle(x, y, 1, 1);
					 boolean stoppedChecking = false;
					for(int i = 0; i < objects.length; i++)
					if(!stoppedChecking) stoppedChecking = objects[i].handleMouseClick(mouseRectangle, renderer.getCamera(), xZoom, yZoom);
					/// if(!stoppedChecking){
					// 	x = (int) Math.floor(((x + renderer.getCamera().x)/(16.0 * xZoom)));
					// 	y = (int) Math.floor((y + renderer.getCamera().y)/(16.0 * yZoom));
					// 	map.setTile(selectedLayer, x, y, selectedTileID);
					// }
				}

				public void mapUpdater() {
					for (int i = 0; i < randomMap.length; i++) {
						if (player.getRect().y < randomMap[i][1]*yZoom*16-32*yZoom && player.getRect().y > randomMap[i][3]*yZoom*16) room = i+1;
					}
					int i = room-1;
					if (i > 0 && i < randomMap.length-1 && player.getRect().y < randomMap[i][1]*yZoom*16-32*yZoom ){
						map.setTile(0,-1,randomMap[i][1],3);
						map.setTile(0,0,randomMap[i][1],3);
						map.setTile(0,1,randomMap[i][1],3);
					}
					if ((i < randomMap.length-1 && player.getRect().x < 3*16*yZoom && player.getRect().x > -3*16*yZoom && player.getRect().y-32*yZoom < randomMap[i][3]*yZoom*16 && player.getRect().y > randomMap[i+1][1]*yZoom*16 && spawner.allDead(i)) ||
					(player.getRect().y+32*yZoom > randomMap[i][3]*yZoom*16 && spawner.allDead(i))) {
						if (i < randomMap.length-1){
						map.setTile(0,-1,randomMap[i][3],1);
						map.setTile(0,0,randomMap[i][3],1);
						map.setTile(0,1,randomMap[i][3],1);}
						if (i > 0){
							map.setTile(0,-1,randomMap[i][1],1);
							map.setTile(0,0,randomMap[i][1],1);
							map.setTile(0,1,randomMap[i][1],1);
						}
					}

					if (player.getRect().y <= randomMap[randomMap.length-1][3]*yZoom*16+3*16*yZoom && player.getRect().y > randomMap[randomMap.length-1][3]*yZoom*16+2*16*yZoom
							&& player.getRect().x < 1*16*yZoom && player.getRect().x > -1*16*yZoom) {
						mapLevel++;
						player.getStats().setExp(player.getStats().getExp()+1);
						player.getStats().setHealthLeft((int) Math.round(player.getStats().getHealthLeft()+player.getStats().getHealth()*.1));
						if (player.getStats().getHealthLeft() > player.getStats().getHealth())player.getStats().setHealthLeft(player.getStats().getHealth());
						jMenu();
						reset();
					}
				}


				public void render() {
					BufferStrategy bufferStrategy = canvas.getBufferStrategy();
				 graphics = bufferStrategy.getDrawGraphics();
					super.paint(graphics);
					map.render(renderer, objects, xZoom, yZoom);
					mapUpdater();
					if (player.getStats().getExp() >= 10){
						player.getStats().setExp(player.getStats().getExp()-10);
						levelUp++;
						jMenu();
					}
					Rectangle r = new Rectangle(20, getHeight()-100, (int) Math.round(player.getStats().getHealthLeft()*1.0/player.getStats().getHealth()*100), 5);
					r.generateGraphics(0xFFff000F);
					renderer.renderRectangle(r, xZoom, yZoom, true);
					// // player.renderParticles(renderer, 2, 2);
					renderer.render(graphics);
					graphics.setColor(new Color(255, 255, 255));
					renderer.renderString(graphics,mapLevel+"-"+room,getWidth() - 100, getHeight()- 50,50);
					renderer.renderString(graphics, "Health:" + player.getStats().getHealthLeft() +"/" +player.getStats().getHealth(), 20, getHeight()- 50, 20);
					renderer.renderString(graphics, "XP:" + player.getStats().getExp() +"/"+10, 200, getHeight()- 50, 20);

					Graphics2D g2 = (Graphics2D) graphics;

					graphics.setColor(new Color(199, 14, 14));
					g2.setStroke(new BasicStroke(30));
					if(line3)g2.drawLine(mX, mY, mW, mH);

					g2.setStroke(new BasicStroke(15));
					graphics.setColor(new Color(213, 99, 16));
					if(line2)g2.drawLine(mX, mY, mW, mH);

					graphics.setColor(new Color(255, 223, 6));
					g2.setStroke(new BasicStroke(2));
					if(showMouseLine) g2.drawLine(mX, mY, mW, mH);
					if(showMouseLine2) g2.drawLine(mX2, mY2, mW2, mH2);
					graphics.dispose();
					bufferStrategy.show();
					renderer.clear();
				}


				public void drawLine(int color, int w, int h, int x, int y, int thickness){
					mX = x;
					mY = y;
					mW = w;
					mH = h;
					showMouseLine = true;
				}
				public void drawLine2(int color, int w, int h, int x, int y, int thickness){
					mX2 = x;
					mY2 = y;
					mW2 = w;
					mH2 = h;
					showMouseLine2 = true;
				}

				public void line2(){
					line2 = true;
				}

				public void line3(){
					line3 = true;
				}

				public void hideLine(){
					showMouseLine = false;
					line2 = false;
					line3 = false;
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

				public Canvas getCanvas(){
					return canvas;
				}

				public static void main(String[] args){
					Game game = new Game();
					Thread gameThread = new Thread(game);
					gameThread.start();
				}
			}
