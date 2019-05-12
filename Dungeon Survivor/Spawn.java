 

import java.util.HashMap;
import java.util.ArrayList;
//GameObject Class
public class Spawn implements GameObject {
    private ArrayList<Bot> botList = new ArrayList<Bot>();
    private ArrayList<AnimatedSprite> animationList = new ArrayList<AnimatedSprite>();
    //private HashMap<Bot, Boolean> bots = new HashMap<Bot, Boolean>();
    
    private int layer = 0;
    private Game game;
    private Rectangle botRectangle;
    private SpriteSheet zombieSheet;
    
    Rectangle rect;
    
    private int xZoom;
    private int yZoom;
    
    private int wave = 1;
    private int health = 500;
    private int attackSpeed = 30;
    private int zombieAttack = -30;
    private int speed = 3;
    private int increase = 100; 
    
    private int respawnTimer = 0;
    private int zombieX = 0;
    private int zombieY = 0;
    private int numberOfBots = 4;
    //Parameters: Zombie spritesheet, Zoom - used to set zombie collisions and render size
    //Creates an array list of zombie objects and animations
    //constructor
    public Spawn(SpriteSheet zombieSheet, int xZoom, int yZoom){
        this.xZoom = xZoom;
        this.yZoom = yZoom;
        this.zombieSheet = zombieSheet;
        for(int i = 1; i <= numberOfBots; i++){
            AnimatedSprite zombieAnimations = zombieAnimations = new AnimatedSprite(zombieSheet, 10);
            int ran = (int) (Math.random() *(10) + 1);
            Bot bot = new Bot(zombieAnimations, xZoom, yZoom, randomPosX(ran), randomPosY(ran), health, zombieAttack, attackSpeed, speed - 1, randomZombieX(), randomZombieY(), 100);
            botList.add(bot);
            animationList.add(zombieAnimations);
        }
    }
    
    //Call every time physically possible.
    //Parameters: renderer - object that add pixels to pixel array to be added to canvas, Zooms
    // Renders every bot in botList arrayList
    //returns Void
    public void render(RenderHandler renderer, int xZoom, int yZoom){
        for(int i = 0; i < botList.size(); i++) botList.get(i).render(renderer, xZoom, yZoom);
    }
    //Parameters: None
    // Randomizes the x sprite coordinate of the zombie to get random zombies
    //returns x sprite coordinate
    public int randomZombieX(){
        int ran = (int) (Math.random() * 4);
        return ran * 3;
    }
    //Parameters: Spawnpoint - a random number between 1 - 5
    //Sets the x value of zombie based on what spawnpoint he will be at
    //returns the spawnpoint
    public int randomPosX(int spawnPoint){
        int pos = 0;
        if(spawnPoint == 1) pos = -445;
        else if(spawnPoint == 2) pos = -100;
        else if(spawnPoint == 3) pos = 850;
        else if(spawnPoint == 4) pos = 295;
        else if (spawnPoint == 5)pos = -230;
        else if (spawnPoint == 6)pos = 880;
        else if (spawnPoint == 7)pos = 845;
        else if (spawnPoint == 8)pos = 315;
        else if (spawnPoint == 9)pos = -60;
        else if (spawnPoint == 10)pos = 260;
        int ran = (int) (Math.random() *(2) + 1);
        if(ran == 1) ran = (int) (Math.random() *(50) + 1);
        else if(ran == 2) ran = -1* (int) (Math.random() *(50) + 1);
        return pos += ran;
    }
    //Parameters: Spawnpoint - a random number between 1 - 5
    //Sets the y value of zombie based on what spawnpoint he will be at
    //returns the spawnpoint
    public int randomPosY(int spawnPoint){
        int pos = 0;
        if(spawnPoint == 1) pos = -680;
        else if(spawnPoint == 2) pos = 300;
        else if(spawnPoint == 3) pos = -220;
        else if(spawnPoint == 4) pos = 250;
        else if (spawnPoint == 5) pos = -695;
        else if (spawnPoint == 6)pos = -425;
        else if (spawnPoint == 7)pos = 25;
        else if (spawnPoint == 8)pos = 215;
        else if (spawnPoint == 9)pos = -740;
        else if (spawnPoint == 10)pos = -725;
        int ran = (int) (Math.random() *(2) + 1);
        if(ran == 1) ran = (int) (Math.random() *(50) + 1);
        else if(ran == 2) ran = -1* (int) (Math.random() *(50) + 1);
        return pos += ran;
    }
    
    //Parameters: None
    // Randomizes the y sprite coordinate of the zombie to get random zombie sprites
    //returns y sprite coordinate
    public int randomZombieY(){
        int ran = (int) Math.round(Math.random());
        return ran * 4;
    }
    //Call at 60 fps rate.
    //Parameters: All gameobjects that need to be updated
    // If the zombie isn't dead, will tell zombie the coordinates of any projectiles. If all zombies are dead will start countdown timer for next round and update stats
    //returns Void
    public void update(Game game, Player player, Spawn spawner, Projectile pro, Melee melee){
        this.game = game;
        for(int i = 0; i < botList.size(); i++){
            Bot bot = botList.get(i);
            boolean dead = bot.getDead();
            if(dead)continue;
            bot.playerPos(player.getRectangle().x, player.getRectangle().y);
            int weaponID = game.selectedWeapon;
            if(weaponID == 5)
                bot.proPos(pro.getRectangle().x, pro.getRectangle().y, 10);
            else if(weaponID == 6) 
                bot.proPos(melee.getRectangle().x, melee.getRectangle().y, 60);
            bot.update(game, player, spawner, pro, melee);
        
        }
        boolean dead = true;
        for(int i = 0; i < botList.size(); i++){
            dead = botList.get(i).getDead();
            if(dead == false) break;
        }
        if(dead){
             if (respawnTimer == 0) {
                health = 500 + increase * 2;
                increase += 50;
                zombieAttack = -30 + wave * -5;
                game.newWave(health/100);
                wave = game.wave;
                speed = 3 + wave/5;
                if(speed > 15) speed = 15;
                numberOfBots = 4 + ((int) wave / 2);
                respawnTimer++;
                botList.clear();
                animationList.clear();
            }
            else respawnTimer++;
            if(respawnTimer > 600) {
                for(int i = 1; i <= numberOfBots; i++){
                    AnimatedSprite zombieAnimations = zombieAnimations = new AnimatedSprite(zombieSheet, 10);
                    animationList.add(zombieAnimations);
                    int ran = (int) (Math.random() *(5) + 1);
                    int rangex = (int) (Math.random() *(50) + 1);
                    int rangey = (int) (Math.random() *(50) + 1);
                    Bot bot = new Bot(zombieAnimations, xZoom, yZoom, randomPosX(ran)+rangex, randomPosY(ran)+rangey, health, zombieAttack, attackSpeed, speed - 1, randomZombieX(), randomZombieY(), 100);
                    botList.add(bot);
                }
                respawnTimer = 0;
            }
            game.waveCountDown(respawnTimer);
        }
    }
    
    //Call whenever mouse is clicked on Canvas.
    //Parameters: mouse
    // GameObject Method
    //returns false to continue checking other click
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
        return false;
    }
    //Parameters: None
    //GameObject method
    //returns the layer the player is on : layer 1
    public int getLayer(){
        return layer;
    }
    //Parameters: None
    //Used by other methods to get players rectangle. GameObject method
    //returns rect : null just there because its necessairy
    public Rectangle getRectangle(){
        return rect;
    }

}
