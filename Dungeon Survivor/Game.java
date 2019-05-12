import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.Runnable;
import java.lang.Thread;

import javax.swing.JFrame;
import javax.swing.*;

import javax.imageio.ImageIO;

import java.applet.*;
import java.net.*;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
    /*
    * A zombie survival game. Upgradable stats with a stat point system. Get stat points by beating waves of zombies. Watch out, the zombies get stronger and faster.
    *
    * @Ahmed Halat
    * @January 23 2018
    *
    * Controls -- Arrow keys to move. Space to throw shurikens when selected. Q to change weapons or click icon in top right. Aim for the head
    * Try to live as long as you can and upgrade your stats after every wave

m
    */
public class Game extends JFrame implements Runnable{
    public static final int alpha = 0xFFFF00DC;

    private Canvas canvas = new Canvas();
    private RenderHandler renderer;
    private Rectangle mouseRectangle;


    private SpriteSheet sheet;
    private SpriteSheet playerSheet;
    private SpriteSheet zombieSheet;
    private SpriteSheet shurikenSheet;
    private SpriteSheet swordSheet;
    private SpriteSheet statSheet;
    private Sprite projectileSprite;
    private Sprite meleeSprite;

    private Tiles tiles;
    private Tiles statTiles;
    private Map map;
    StatMenu statMenu = new StatMenu();

    private GameObject[] objects;
    GUIButton[] statButtons;
    GUIButton[] weapons;
    Sprite[] tileSprites;

    private KeyBoardListener keyListener = new KeyBoardListener(this);
    private MouseEventListener mouseListener = new MouseEventListener(this);

    private Player player;
    private Spawn spawner;
    private Projectile pro;
    private Melee melee;
    Rectangle proRect;

    private int zoomX = 3;
    private int zoomY = 3;

    int selectedStatID = 0;
    int[] stats;
    int statPoints = 0;
    int wave = 1;
    String [] statNames;
    int spIncrement = 1;
    String speed = "5";
    int hpIncrement = 10;
    String health = "100";
    int atkIncrement = 15;
    int attackSpeed = 60;
    String attack = "100";
    int defIncrement = 1;
    String defence = "10";
    int statMultiplier = 1;
    int selectedWeapon = 5;
    int waveCount = 0;

    boolean upgrade = true;
    private boolean dead = false;
    private boolean open = true;
    int buttonX = 20;
    private int score = 0;
    private boolean pause = true;
    private boolean run = true;
    private boolean help = false;

    public Game() {
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
        BufferedImage sheetImage = LoadImage.loadImage("mapTiles.png");
        sheet = new SpriteSheet(sheetImage);
        sheet.loadSprites(16, 16);
        //load stat pictures
        BufferedImage statSheetImage = LoadImage.loadImage("StatPics.png");
        statSheet = new SpriteSheet(statSheetImage);
        statSheet.loadSprites(16, 16);
        //load player spritesheet
        BufferedImage playerSheetImage = LoadImage.loadImage("Player.png");
        playerSheet = new SpriteSheet(playerSheetImage);
        playerSheet.loadSprites(20, 26);

        //load zombie spritesheet
        BufferedImage zombieSheetImage = LoadImage.loadImage("zombies.png");
        zombieSheet = new SpriteSheet(zombieSheetImage);
        zombieSheet.loadSprites(26, 26);
        //load shuriken spritesheet
        BufferedImage shurikenSheetImage = LoadImage.loadImage("shuriken.png");
        shurikenSheet = new SpriteSheet(shurikenSheetImage);
        shurikenSheet.loadSprites(16, 16);
        projectileSprite = shurikenSheet.getSprite(0,0);
        //load sword spritesheet
        BufferedImage swordSheetImage = LoadImage.loadImage("sword.png");
        SpriteSheet swordSheet = new SpriteSheet(swordSheetImage);
        swordSheet.loadSprites(56, 56);
        meleeSprite = statSheet.getSprite(3,0);

        //Player Animated Sprites
        AnimatedSprite playerAnimations = new AnimatedSprite(playerSheet, 5);

        //Shuriken
        AnimatedSprite shurikenAnimations = new AnimatedSprite(shurikenSheet, 3);

        //Sword
        AnimatedSprite swordAnimations = new AnimatedSprite(swordSheet, 1);

        //Load Tiles
        tiles = new Tiles(new File("Tiles.txt"),sheet);
        statTiles = new Tiles(new File("StatPics.txt"),statSheet);

        //Load Map
        map = new Map(new File("Map.txt"), tiles);

        // //Load SDK GUI
        statButtons = new GUIButton[5];
        weapons = new GUIButton[2];
        tileSprites = statTiles.getSprites();
        //Load stat buttons
        for(int i = 0; i < statButtons.length; i++){
            Rectangle tileRectangle = new Rectangle(0, i*(16*zoomX + 2), buttonX*zoomX, 16*zoomY);
            statButtons[i] = new SDKButton(this,player, spawner, melee, i, tileSprites[0], tileRectangle, false);
        }
        //load weapons selector
        weapons[0] = new SDKButton(this,player,spawner, melee, 5, projectileSprite, new Rectangle(0, 0, buttonX*zoomX, 16*zoomY), false);
        weapons[1] = new SDKButton(this,player,spawner, melee, 6, meleeSprite, new Rectangle(0, (16*zoomX + 2), buttonX*zoomX, 16*zoomY), false);
        //set GUIS
        GUI gui = new GUI(statButtons, 5,  5, true);
        GUI weaponSelect = new GUI(weapons, 950,  5, true);
        //Load Objects
        player = new Player(playerAnimations, zoomX, zoomY);
        spawner = new Spawn(zombieSheet, 3, 3);
        pro = new Projectile(shurikenAnimations, 3, 3);
        melee = new Melee (swordAnimations, 3, 3);
        //set objects
        objects = new GameObject[7];
        objects[0] = melee;
        objects[1] = player;
        objects[2] = weaponSelect;
        objects[3] = statMenu;
        objects[4] = spawner;
        objects[5] = pro;
        objects[6] = gui;
         //Add Listeners
        weaponSelect(selectedWeapon);
        canvas.addKeyListener(keyListener);
        canvas.addFocusListener(keyListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
        // //resize canvas
        addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                int newWidth = canvas.getWidth();
                int newHeight = canvas.getHeight();

                if(newWidth > renderer.getMaxWidth())
                newWidth = renderer.getMaxWidth();

                if(newHeight > renderer.getMaxHeight())
                newHeight = renderer.getMaxHeight();

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
    }//game constructor
    //Parameters: None
    //Updates the game objects whenever called
    //returns Void
    public void update() {
        if(selectedWeapon == 6) objects[0].update(this, player, spawner, pro, melee);
        else objects[selectedWeapon].update(this, player, spawner, pro, melee);
        for(int i = 1; i < 5; i++)
            objects[i].update(this, player, spawner, pro, melee);

    }
    //Parameters: None
    //changes the value of pause
    //returns void
    public void pause(){
        if(pause) pause = false;
        else pause = true;
    }
    //Parameters: x and y coordinates of the mouse click
    //sends the coordinates of the mouse click to all gameobjects
    //returns Void
    public void leftClick(int x, int y){
        mouseRectangle = new Rectangle(x, y, 1, 1);
        boolean stoppedChecking = false;
        for(int i = 0; i < objects.length; i++)
            if(!stoppedChecking)
                stoppedChecking = objects[i].handleMouseClick(mouseRectangle, renderer.getCamera(), zoomX, zoomY);

        open = false;
        pause = false;
    }
    //Parameters: a timer that goes from 0 to 600. 60 is added to timer each second
    //sets the waveCount to timer
    //returns Void
    public void waveCountDown(int timer){
        waveCount = timer;
    }
    //Parameters: None
    //Handles the games rendering, creates bufferStrategies and graphics then sends them to renderhandler object
    //returns void
    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        super.paint(graphics);
        map.render(renderer, objects, zoomX, zoomY);
        if(open ){
            BufferedImage start = LoadImage.loadImage("start.png");
            Sprite startScreen = new Sprite(start);
            renderer.renderSprite(startScreen, getWidth()/2 - startScreen.getWidth() / 2, getHeight()/2 - startScreen.getHeight() / 2, 1, 1, true);

        }
        if(dead){
            //Renders the game one last time before closing it
            BufferedImage gameOver = LoadImage.loadImage("GameOver.png");
            Sprite gameOverScreen = new Sprite(gameOver);
            renderer.renderSprite(gameOverScreen, getWidth()/2 - gameOverScreen.getWidth() / 2, getHeight()/2 - gameOverScreen.getHeight() / 2, 1, 1, true);
            //quick fix for small bug of health bar not updating before I stop updating my game
            Rectangle statusBar = new Rectangle(0, 700, 115, 25);
            statusBar.generateGraphics(0xFF9F9F9F);
            renderer.renderRectangle(statusBar, zoomX, zoomY, true);
            renderer.render(graphics);
            renderer.render(graphics);
            graphics.dispose();
            bufferStrategy.show();
            renderer.clear();
            run = false;
        }
        Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();
        //g.drawImage(toDraw, 0, 0, null);
        renderer.render(graphics);

        renderer.render(graphics);
        if(!open){
            renderer.renderString(graphics, "Hp:" , 15, 712, 10);
            renderer.renderString(graphics, "Hp:" + health, 10, 80, 15);
            renderer.renderString(graphics, "Sp:" + speed , 10, 35, 15);
            renderer.renderString(graphics, "Atk:" + attack , 10, 180, 15);
            renderer.renderString(graphics, "Def:" + defence , 10, 135, 15);
            renderer.renderString(graphics, "Help" , 10, 230, 15);
            graphics.setColor(Color.white);
            renderer.renderString(graphics, "Wave: " + wave , 800, 50, 20);
            renderer.renderString(graphics, "Score: " + score , 800, 25, 20);
            if(waveCount != 0) renderer.renderString(graphics, "" + (10 - waveCount / 60) , getWidth()/2, getHeight()/2, 100);
            graphics.setColor(Color.black);
        }
        graphics.dispose();
        bufferStrategy.show();
        renderer.clear();
    }
    //Parameters: statID- ID of the stat that needs to be checked in the stat array. Upgrade - whether or not the stat is being upgraded
    // increases selected stats by their specific increment value. Then upgrade the stat GUI
    //returns Void
    public void changeStat(int statID, boolean upgrade) {
        selectedStatID = statID;

        if(statID == 5 || statID == 6) weaponSelect(statID);
        else if (statID == 4)  displayHelp();
        else if(statPoints > 0){
            if(statID == 0){
                stats[0] += spIncrement * statMultiplier;
                speed = (Integer.toString(stats[0]));
            }
            if(statID == 1){
                stats[1] += hpIncrement * statMultiplier;
                health = (Integer.toString(stats[1]));
            }
            if(statID == 2){
                stats[2] += defIncrement * statMultiplier;
                defence = (Integer.toString(stats[2]));
            }
            if(statID == 3){
                stats[3] += atkIncrement * statMultiplier;
                attackSpeed -= 10;
                attack = (Integer.toString(stats[3]));
            }

            statSelected();
        }

    }
    //Parameters: None
    //Displays help image on screen
    //returns Void
    public void displayHelp(){
        BufferedImage helpImage = LoadImage.loadImage("Help.png");
        Sprite helpScreen = new Sprite(helpImage);
        int counter = 0;
        pause = true;
        while(pause && counter < 1800){
            counter++;
            renderer.renderSprite(helpScreen, getWidth()/2 - helpScreen.getWidth() / 2, getHeight()/2 - helpScreen.getHeight() / 2, 1, 1, true);
        }
        pause = false;
    }
    //Parameters: None
    //Creates a loop that runs endlessly. It calls the upgrade class 60 times every second and the render class everytime possible
    //returns
    public void run() {

        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        int i = 0;
        int x = 0;
        int counter = 0;

        long lastTime = System.nanoTime(); //long 2^63
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
        double secondsChange = 0;

        while(run) {
            long now = System.nanoTime();

            secondsChange += (now - lastTime) / nanoSecondConversion;
            while(secondsChange >= 1) {
                if(!pause)update();
                secondsChange--;
            }

            render();
            lastTime = now;
        }
    }
    //Parameters: The value to add to the players score
    //Turns the stat GUI green to indicate to player that a stat point is availible. Also updates wave and score values
    //returns Void
    public void newWave(int scoreAdd){
        for(int i = 0; i < statButtons.length; i++){
            Rectangle tileRectangle = new Rectangle(0, i*(16*zoomX + 2), buttonX*zoomX, 16*zoomY);

            statButtons[i] = new SDKButton(this,player,spawner, melee, i, tileSprites[0], tileRectangle, true);
        }
        statPoints+= 2;
        wave++;
        score += scoreAdd;
    }
    //Parameters: the ID of the weapon that is selected. 5* for shuriken, 6* for sword.  (** their index on the stat array, see stat class)
    //Change weapon GUI to indicate to player which weapon is selected. Calls changeWeapon in projectile class
    //returns Void
    public void weaponSelect(int weaponID){
        sword();
        weapons[0] = new SDKButton(this,player,spawner, melee, 5, projectileSprite, new Rectangle(0, 0, buttonX*zoomX, 16*zoomY), false);
        weapons[1] = new SDKButton(this,player,spawner, melee, 6,  meleeSprite, new Rectangle(0, 1*(16*zoomX + 2), buttonX*zoomX, 16*zoomY), false);

        Rectangle tileRectangle = new Rectangle(0, (weaponID-5)*(16*zoomX + 2), buttonX*zoomX, 16*zoomY);
        if(weaponID == 5) weapons[0] = new SDKButton(this,player,spawner, melee, 5, projectileSprite, tileRectangle, true);
        else if(weaponID == 6)  weapons[1] = new SDKButton(this,player,spawner, melee, 6,  meleeSprite, tileRectangle, true);
        selectedWeapon = weaponID;
        pro.changeWeapon(selectedWeapon);
    }
    //Parameters:  None
    //Decrease stat points if stat to upgrade has been selected. Also if no more stat points availible turns stats yellow again
    //returns Void
    public void statSelected(){
        statPoints--;
        if(statPoints < 1)
            for(int i = 0; i < statButtons.length; i++){
                Rectangle tileRectangle = new Rectangle(0, i*(16*zoomX + 2), buttonX*zoomX, 16*zoomY);

                statButtons[i] = new SDKButton(this,player,spawner, melee, i, tileSprites[0], tileRectangle, false);
            }
    }
    //main method, stats game thread
    public static void main(String[] args) {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }
    //Parameters: None
    //Is called by other classes to inform the game to display gameover screen and stop updating
    //returns Void
    public void hpZero(){
        dead = true;
    }
    //Parameters: None
    //Makes the sword invisible if it is not currently selected
    //returns Void
    public void sword(){
        melee.invisible();
    }
    //Parameters: None
    //Gets direction player is facing and calls the update direction method in projectile. This method is called when space is clicked
    //returns
    public void attack(){
        int direction = player.direction;
        if(selectedWeapon == 5) pro.updateDirection(direction);
    }

    // --- Get and Set methods -- //

    //returns the keyboard listener for other classes to use
    public KeyBoardListener getKeyListener() {
        return keyListener;
    }
    //returns the renderer object for other classes to use. Since these objects are private in game class
    public RenderHandler getRenderer(){
        return renderer;
    }
    //returns map for other classes to use
    public Map getMap() {
        return map;
    }
    //Parameters: An array of the new stats
    //Sets new stats so they can be updated in the change stat method, also sets health to be displayed in GUI
    //returns
    public void setStats(int [] statList){
        stats = statList;
        health = (Integer.toString(stats[1]));
    }
}
