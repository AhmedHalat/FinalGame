 

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Bot {
    private Rectangle botRectangle;
    private Rectangle collisionCheckRectangle;
    
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;
    Game game;
    
    
    //0 = Right, 1 = Left, 2 = Up, 3 = Down
    private int direction = 0;
    private int directionChange = 0;
    private boolean directionStuck = false;
    private boolean goUp = false;
    private boolean goDown = false;
    private boolean goLeft = false;
    private boolean goRight = false;
    private boolean dontMove = false;
    private boolean attack = false;
    
    private double timer = 0.0;
    private double hpTimer = 0.0;
    private int respawnTimer = 0;
    
    private int health = 500;
    private int start = 500;
    private int zombieAttack = -40;
    private int attackSpeed = 25;
    private int speed = 3;
    private int damage = 0;
    private int proX;
    private int proY;
    
    private int zombieX = 0;
    private int zombieY = 0;
    private int push = 10;
    private int newDirection = 0;
    private boolean dead = false;
    private final int xCollisionOffset = 15;
    private final int yCollisionOffset = 25;
    //Parameters: Sprite - animations, zooms - collision box multiplier, pos - spawn positions, health - starting health, attack - attack stat, attackSpeed - how long a zombie has to be near you to attack, speed - zombie speed stat, zombieXY - zombie sprite location on sprite sheet, damage - damage to player
    //Creates object and rectangles
    //Constructor
    public Bot(Sprite sprite, int xZoom, int yZoom, int xPos, int yPos, int health, int attack, int attackSpeed, int speed, int zombieX, int zombieY, int damage)
    {
        this.sprite = sprite;
        this.health = health;
        this.attackSpeed = attackSpeed;
        this.speed = speed;
        this.zombieX = zombieX;
        this.zombieY = zombieY;
        this.damage = damage;
        start = health;
        zombieAttack = attack;
        
        if(sprite != null && sprite instanceof AnimatedSprite)
            animatedSprite = (AnimatedSprite) sprite;
        updateDirection();
        botRectangle = new Rectangle(xPos - 90, yPos, 40, 5);
        botRectangle.generateGraphics( 0xFF00FF90);
        //collision multiplier on the x and y values         !         !
        collisionCheckRectangle = new Rectangle(xPos, yPos, 10*xZoom, 15*yZoom);
    }
    //Parameters: players x and y coordinates
    //sends coordinate to botDirection
    //returns Void
    public void playerPos(int x, int y){
        botDirecton(x, y);
    }
    //Parameters: x y coordinate of players weapon, size of weapon hitbox
    //checks if players weapon is hitting the bot, if yes call zombiehit method
    //returns Void
    public void proPos(int x, int y, int hitbox){
        // x y and hitbox size
        push = hitbox;
        Rectangle proRectangle = new Rectangle(x, y, hitbox, hitbox);
        if(!dead) if(botRectangle.intersects(proRectangle)) zombieHit();
    }
    //Parameters: players x and y position
    //Sets the direction the bot will walk in based on bots location relative to the player
    //returns Void
    public void botDirecton(int playerX, int playerY){
        int x = botRectangle.x - playerX;
        int y = botRectangle.y - playerY;
        if(Math.abs(x) > 300 && Math.abs(y) > 300)
            dontMove = true;
        
        else if(directionChange > 60 || directionStuck){
            if(x > 0){
                if(y < 0) goDown = true;
                else if(y > 0) goUp = true;
                directionStuck = true;
            }
            else if(x < 0){
                if(y > 0) goUp = true;
                else if(y < 0) goDown = true;
                directionStuck = true;
            }
            else if (x == 0){
                if(y > 0) goRight = true;
                else if(y < 0)  goLeft = true;
                directionStuck = true;
                }
        }
        
        else if (!directionStuck && x + y < 1000){
            if(Math.abs(x) < 60 && Math.abs(y) < 20 || Math.abs(y) < 60 && Math.abs(x) < 20)
                attack = true;
            else if(Math.abs(x) > 5){
                if(x > 0) goLeft = true; //left
                else goRight = true; //right
                attack = false;
            }
            else  {
                if(y > 0) goUp = true; //up
                else goDown = true; //down
                attack = false;
            }
        }
    }
    //Parameters: Game class - to send stats to main game
    //is called if zombie hits player, changes players health based on zombie attack stat and players health and defence
    //returns Void
    private void attack(Game game){
        int [] stats = game.stats;
        int damage = 0;
        if(stats[2] > (zombieAttack * -1)) damage = -1;
        else damage = zombieAttack + stats[2];
        if(stats[1] + damage > 0)stats[1] += damage;
        else {
            stats[1] = 0;
            game.setStats(stats);
            game.hpZero();
        }
        game.setStats(stats);
    }
    //Parameters: None
    //sets the zombies sprite animation range based on the zombieXY to chose a zombie from the sheet
    //returns Void
    private void updateDirection()
    {
        if(animatedSprite != null)
        {
            animatedSprite.setAnimationRange((direction + zombieY) *12 + zombieX, ((direction+zombieY)*12) + 3 + zombieX );
        }
    }

    //Call every time physically possible.
    //Parameters: Renderer - object to call RenderHandler class, zooms - pixel multiplier
    //Set the zombies health bar and sends his sprite and health to be rendered GameObject Method
    //returns Void
    public void render(RenderHandler renderer, int xZoom, int yZoom){
        if(!dead){
            Rectangle bar = new Rectangle(botRectangle.x - 10, botRectangle.y - 10, start / 15, 5);
            bar.generateGraphics(0xFF5D5D5E);
            renderer.renderRectangle(bar, xZoom, yZoom, false);
            
            Rectangle healthBar = new Rectangle(botRectangle.x - 10, botRectangle.y - 10, health/15, 5);
            healthBar.generateGraphics(0xFFA73121);
            renderer.renderRectangle(healthBar, xZoom, yZoom, false);
            if(animatedSprite != null)
                renderer.renderSprite(animatedSprite, botRectangle.x, botRectangle.y, xZoom, yZoom, false);
            else if(sprite != null)
                renderer.renderSprite(sprite, botRectangle.x, botRectangle.y, xZoom, yZoom, false);
            else
                renderer.renderRectangle(botRectangle, xZoom, yZoom, false);
        }
    }

    //Call at 60 fps rate.
    //Parameters: GameObject that are being updated by update method
    //If player isnt dead call move method
    //returns Void
    public void update(Game game, Player player, Spawn spawner, Projectile pro, Melee melee){
        this.game = game;
        if(!dead)move(game, player, this, spawner, pro, melee);
    }
    //Parameters: None
    //If zombie is hit call direction method with switch parameters to make zombie go backwards if hit. Also decrease zombies health
    //returns Void
    public void zombieHit(){
        direction(goDown, goUp, goLeft, goRight, false, (int)(damage/2));
        didMove();
        if(health - damage > 0) health -= damage;
        else dead = true;
    }
    //Parameters: GameObject that are being updated by update method (called in because necessairy to call animatedSprite method)
    //decides whether or not to attack player and updates player collisions
    //returns Void
    public void move(Game game, Player player, Bot bot, Spawn spawner, Projectile pro, Melee melee){
        newDirection = direction;
        if(directionStuck) timer++;
        collisionCheckRectangle.x = botRectangle.x;
        collisionCheckRectangle.y = botRectangle.y;
        
        boolean didMove = direction(goUp, goDown, goRight, goLeft, true, speed);
        
        if(newDirection != direction) {
           direction = newDirection;
           directionChange = 0;
           hpTimer = 0;
           attack = false;
           updateDirection();
        }
        else if(directionStuck)
            if(timer > 15){
               directionChange = 0;
               //if(goRight|| goLeft)  goUp = true;
               goUp = false;
               goDown = false; 
               goLeft = false;
               goRight = false;
               dontMove = false;
               attack = false;
               directionStuck = false;
               timer = 0;
            }

        if(!didMove) {
            animatedSprite.reset();
        }
        if(didMove){
            didMove();
            animatedSprite.update(game, player, spawner, pro, melee);
        }
        if(hpTimer > attackSpeed){ 
            attack(game);
            hpTimer = 0;
        }
        
    }
    //Parameters: bot direction booleans, whether or not to update the sprite if his direction changes, amount - amount he's moving (speed or knockback)
    //updates players location
    //returns didMove - whether or not the bot moved
    public boolean direction( boolean up, boolean down, boolean right, boolean left, boolean updateDirection, int amount){
        newDirection = direction;
        boolean didMove = false;
        if(up) {
            //up
            collisionCheckRectangle.y -= amount;
            directionChange ++;
            didMove = true;
            if(updateDirection)newDirection = 3;   
        }
        else if( down) {
            //down
            if(updateDirection)newDirection = 0;
            directionChange ++;
            didMove = true;
            collisionCheckRectangle.y += amount;
        }
        else if(left){
            //left
            if(updateDirection)newDirection = 1;
            didMove = true;
            directionChange ++;
            collisionCheckRectangle.x -= amount;
        }
        else if(right){
            //right
            if(updateDirection)newDirection = 2;
            directionChange ++;
            didMove = true;
            collisionCheckRectangle.x += amount;
        }
        if(dontMove){
            didMove = false;
        }
        if(attack){
            //not moving
            didMove = false;
            hpTimer ++;
        }
        
        return didMove;
    }
    //Parameters: None
    //Checks bots collisions if he did move
    //returns Void
    public void didMove(){
    
        collisionCheckRectangle.x += xCollisionOffset;
        collisionCheckRectangle.y += yCollisionOffset;

        Rectangle axisCheck = new Rectangle(collisionCheckRectangle.x, botRectangle.y + yCollisionOffset, collisionCheckRectangle.w, collisionCheckRectangle.h);

        //Check the X axis
        if(!game.getMap().checkCollision(axisCheck, 0, 3, 3) && 
            !game.getMap().checkCollision(axisCheck, 0 + 1, 3, 3)) {
            botRectangle.x = collisionCheckRectangle.x - xCollisionOffset;
        }

        axisCheck.x = botRectangle.x + xCollisionOffset;
        axisCheck.y = collisionCheckRectangle.y;
        axisCheck.w = collisionCheckRectangle.w;
        axisCheck.h = collisionCheckRectangle.h;
        //axisCheck = new Rectangle(botRectangle.x, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);

        //Check the Y axis
        if(!game.getMap().checkCollision(axisCheck, 0, 3, 3) && 
            !game.getMap().checkCollision(axisCheck, 0 + 1, 3, 3)) {
            botRectangle.y = collisionCheckRectangle.y - yCollisionOffset;
        }
        
    }
    //Parameters: none
    //returns true if zombie is dead
    public boolean getDead(){
        return dead;
    }
    //Parameters: None
    //rectangle cotaining bot x,y,w,h
    //returns rectangle ^^
    public Rectangle getRectangle() {
        return botRectangle;
    }
}