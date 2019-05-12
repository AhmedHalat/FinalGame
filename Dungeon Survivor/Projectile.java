 

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import java.awt.image.BufferedImage;
//GameObject class
public class Projectile implements GameObject{
    private Rectangle proRectangle;
    private GameObject[] objects;
    private Rectangle playerRectangle;
    Player player;
    private Rectangle collisionCheckRectangle;
    private int xSpeed = 10;
    private int ySpeed = 10;
    //0 = Right, 1 = Left, 2 = Up, 3 = Down
    private int direction = 0;
    private int proDirection = 0;
    private boolean directionStuck = false;
    private boolean goUp = false;
    private boolean goDown = false;
    private boolean goLeft = false;
    private boolean goRight = false;
    private boolean fly = false;
    private int attackSpeed = 60;
    int timer = 120;
    int lastHitTimer = 0;
    int weaponID = 5;
    private int layer = 0;
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;
    //Rectangle [] pros = new Rectangle[5];
    //Collision offset right 
    private final int xCollisionOffset = 10;
    private final int yCollisionOffset = 15;
    private int playerX, playerY;
    
    int mouseX, mouseY;
    //Parameters: Sprite - current sprite, Zoom - pixel multiplier for when rendering and creating collisions
    // Projectile object constructor
    //returns
    public Projectile(Sprite sprite, int xZoom, int yZoom){
        this.sprite = sprite;
        if(sprite != null && sprite instanceof AnimatedSprite)
            animatedSprite = (AnimatedSprite) sprite;
        
        proRectangle = new Rectangle(0 - 90, 0, 5, 5);
        
        //collision multiplier on the x and y values    !         !
        collisionCheckRectangle = new Rectangle(0, 0, 8*xZoom, 8*yZoom);
    }
    //Parameters: direction - the direction the player is currently facing
    //Updates the direction to travel of the player. Attack speed is determined by player attack stat
    //returns Void
    public void updateDirection(int direction){
        if(timer >= attackSpeed ){
            if(direction == 0) goRight = true;
            else if (direction == 1) goLeft = true;
            else if(direction == 2) goUp = true;
            else if(direction == 3) goDown = true;
            proRectangle.x = playerX;
            proRectangle.y = playerY;
            fly = true;
            timer = 0;
            lastHitTimer = 0;
       }
    }
    //Parameters: ID of the current weapon
    //is called by other classes when the weapon is changed
    //returns Void
    public void changeWeapon(int weaponID){
        this.weaponID = weaponID;
    }

    //Call every time physically possible.
    //Parameters: renderer - object to render sprites to canvas, xZoom,yZoom
    //If the shuriken is currently flying, renders the shuriken to the screen if the animations have been loaded. GameObject Method
    //returns Void
    public void render(RenderHandler renderer, int xZoom, int yZoom){
        if(fly){
            if(animatedSprite != null)
                renderer.renderSprite(animatedSprite, proRectangle.x, proRectangle.y, xZoom, yZoom, false);
            else if(sprite != null)
                renderer.renderSprite(sprite, proRectangle.x, proRectangle.y, xZoom, yZoom, false);
            else renderer.renderRectangle(proRectangle, xZoom, yZoom, false);
        }
    }

    //Call at 60 fps rate.
    //Parameters: GameObjects that are continuously being updated
    // Updates the shuriken position and if shuriken is currently flying calls move method
    //returns Void
    public void update(Game game, Player player, Spawn spawner, Projectile pro, Melee melee){
        playerX = player.getRectangle().x;
        playerY = player.getRectangle().y;
        timer ++;
        attackSpeed = game.attackSpeed;
        if(fly)  move(game, player, spawner, pro, melee);
        
    }
    //Parameters: GameObjects that are continuously being updated (Only called in to be used to call another method in a different class with same parameters)
    //Moves the shuriken in a straight line and updates collisions
    //returns Void
    public void move(Game game, Player player, Spawn spawner, Projectile pro, Melee melee){
        boolean didMove = false;
        int preX = proRectangle.x;
        int preY = proRectangle.y;
        int newDirection = direction;
        collisionCheckRectangle.x = proRectangle.x;
        collisionCheckRectangle.y = proRectangle.y;
        if(goUp) {
            //up
            collisionCheckRectangle.y -= ySpeed;
            didMove = true;
            newDirection = 2;   
        }
        else if(goDown) {
            //down
            newDirection = 3;
            didMove = true;
            collisionCheckRectangle.y += ySpeed;
        }
        else if(goLeft){
            //left
            newDirection = 1;
            didMove = true;
            collisionCheckRectangle.x -= xSpeed;
        }
        else if(goRight){
            //right
            newDirection = 0;
            didMove = true;
            collisionCheckRectangle.x += xSpeed;
        }
        if(newDirection != direction) 
        {
           direction = newDirection;
        }
        if(!didMove) {
            animatedSprite.reset();
        }

        if(didMove) {


            collisionCheckRectangle.x += xCollisionOffset;
            collisionCheckRectangle.y += yCollisionOffset;

            Rectangle axisCheck = new Rectangle(collisionCheckRectangle.x, proRectangle.y + yCollisionOffset, collisionCheckRectangle.w, collisionCheckRectangle.h);

            //Check the X axis
            if(!game.getMap().checkCollision(axisCheck, layer, 3, 3) && 
                !game.getMap().checkCollision(axisCheck, layer + 1, 3, 3)) {
                proRectangle.x = collisionCheckRectangle.x - xCollisionOffset;
            }

            axisCheck.x = proRectangle.x + xCollisionOffset;
            axisCheck.y = collisionCheckRectangle.y;
            axisCheck.w = collisionCheckRectangle.w;
            axisCheck.h = collisionCheckRectangle.h;
            axisCheck = new Rectangle(proRectangle.x, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);

            //Check the Y axis
            if(!game.getMap().checkCollision(axisCheck, layer, 3, 3) && 
                !game.getMap().checkCollision(axisCheck, layer + 1, 3, 3)) {
                proRectangle.y = collisionCheckRectangle.y - yCollisionOffset;
            }

            animatedSprite.update(game, player, spawner, pro, melee);
        }
        if(preX == proRectangle.x && preY == proRectangle.y){
           goUp = false;
           goDown = false;
           goLeft = false;
           goRight = false;
        }
        
    }
    //Parameters: None
    // GameObject method
    //returns the layer of the shuriken
    public int getLayer() {
        return layer;
    }
    //Parameters: None
    // Rectangle contains x,y,w,h. GameObject method
    //returns projectile Rectangle
    public Rectangle getRectangle() {
        return proRectangle;
    }
    //Parameters: mouse - x,y,w,h. Camera - x,y,w,h. zooms
    // GameObject Method
    //returns false since this class does use mouse
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        mouseX = mouseRectangle.x;
        mouseY = mouseRectangle.y;
        return true; 
    }
    //Call whenever mouse is clicked on Canvas.
}