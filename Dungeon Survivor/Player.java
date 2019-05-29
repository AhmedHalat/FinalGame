

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.image.BufferedImage;
//GameObject method
public class Player implements GameObject
{
    private Rectangle playerRectangle;
    private Rectangle collisionCheckRectangle;
    int newSpeed = 5;
    //0 = Right, 1 = Left, 2 = Up, 3 = Down
    int direction = 0;
    private int layer;
    private Sprite sprite;
    private Bot bot;
    private AnimatedSprite animatedSprite = null;
    //Collision offset right and left of the blocks
    private final int xCollisionOffset = 15;
    private final int yCollisionOffset = 25;
    //Parameters: Sprite - the players animated sprite, zoom - the pixel zoom used to set player collision rectangle
    //Player object constructor, creates player rectangle and collision
    //Contructor
    public Player(Sprite sprite, int xZoom, int yZoom){
        this.sprite = sprite;

        if(sprite != null && sprite instanceof AnimatedSprite)
            animatedSprite = (AnimatedSprite) sprite;

        updateDirection();
        playerRectangle = new Rectangle(100, -300, 20, 26);
        collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
    }
    //Parameters: array that contains players current stats
    //sets the players new motion speed if the stats have been upgraded
    //returns Void
    public void updateStats(int [] stats){
       newSpeed = stats[0];
    }

    //Parameters: None
    //Updates the players sprite range to the ones facing in the correct direction. Range to be shuffled through every few frames to create walking effect
    //returns Void
    private void updateDirection(){
        if(animatedSprite != null)
        {
            animatedSprite.setAnimationRange(direction * 8, (direction * 8) + 7);
        }
    }

    //Parameters: renderer - object used to call render class and render sprites to screen, xZoom yZoom.
    //renders player sprite. This method is called every time possible. GameObject method
    //returns Void
    public void render(RenderHandler renderer, int xZoom, int yZoom)
    {
        if(animatedSprite != null)
            renderer.renderSprite(animatedSprite, playerRectangle.x, playerRectangle.y, xZoom, yZoom, false);
        else if(sprite != null)
            renderer.renderSprite(sprite, playerRectangle.x, playerRectangle.y, xZoom, yZoom, false);
        else
            renderer.renderRectangle(playerRectangle, xZoom, yZoom, false);
    }

    //Call at 60 fps rate.
    //Parameters: All GameObjects that need to constantly be updated
    //Updates the players direction and collision boxe and updates the camera rectangle to match player position. GameObject method
    //returns Void
    public void update(Game game, Player player, Spawn spawner, Projectile pro, Melee melee){

        KeyBoardListener keyListener = game.getKeyListener();
        boolean didMove = false;
        int newDirection = direction;
        layer = 0;

        collisionCheckRectangle.x = playerRectangle.x;
        collisionCheckRectangle.y = playerRectangle.y;
        int speed = newSpeed;
        if(keyListener.left())
        {
            newDirection = 1;
            didMove = true;
            collisionCheckRectangle.x -= speed;
        }
        else if(keyListener.right())
        {
            newDirection = 0;
            didMove = true;
            collisionCheckRectangle.x += speed;
        }
        else if(keyListener.up())
        {
            collisionCheckRectangle.y -= speed;
            didMove = true;
            newDirection = 2;
        }
        else if(keyListener.down())
        {
            newDirection = 3;
            didMove = true;
            collisionCheckRectangle.y += speed;
        }

        if(newDirection != direction) {
            direction = newDirection;
            updateDirection();
        }


        if(!didMove) {
            animatedSprite.reset();
        }

        if(didMove) {


            collisionCheckRectangle.x += xCollisionOffset;
            collisionCheckRectangle.y += yCollisionOffset;

            Rectangle axisCheck = new Rectangle(collisionCheckRectangle.x, playerRectangle.y + yCollisionOffset, collisionCheckRectangle.w, collisionCheckRectangle.h);

            //Check the X axis
            if(!game.getMap().checkCollision(axisCheck, layer, 3, 3) &&
                !game.getMap().checkCollision(axisCheck, layer + 1, 3, 3)) {
                playerRectangle.x = collisionCheckRectangle.x - xCollisionOffset;
            }

            axisCheck.x = playerRectangle.x + xCollisionOffset;
            axisCheck.y = collisionCheckRectangle.y;
            axisCheck.w = collisionCheckRectangle.w;
            axisCheck.h = collisionCheckRectangle.h;
            //axisCheck = new Rectangle(playerRectangle.x, collisionCheckRectangle.y, collisionCheckRectangle.w, collisionCheckRectangle.h);

            //Check the Y axis
            if(!game.getMap().checkCollision(axisCheck, layer, 3, 3) &&
                !game.getMap().checkCollision(axisCheck, layer + 1, 3, 3)) {
                playerRectangle.y = collisionCheckRectangle.y - yCollisionOffset;
            }


            animatedSprite.update(game, player, spawner, pro, melee);
        }

        updateCamera(game.getRenderer().getCamera());
    }
    //Parameters: Camera Rectangle - rectangle objects contain x,y,w,h
    //Updates the camera's x,y to match player position
    //returns
    public void updateCamera(Rectangle camera) {
        camera.x = playerRectangle.x - (camera.w / 2);
        camera.y = playerRectangle.y - (camera.h / 2);
    }
    //Parameters: None
    //GameObject method
    //returns the layer the player is on : layer 1
    public int getLayer() {
        return layer;
    }

    public void setX(int x){
      playerRectangle.x = x;
    }
    public void setY(int y){
      playerRectangle.y = y;
    }

    //Parameters: None
    //Used by other methods to get players rectangle. GameObject method
    //returns player Rectangle
    public Rectangle getRectangle() {
        return playerRectangle;
    }
    //Parameters: Mouse rectangle - x,y,w,h. Camera rectangle - x,y,w,h. Xzoom yZoom
    //True if this method wants to use the mouse click. GameObject method
    //returns false since this method does not use the mouse
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        return false;
    }
    //Call whenever mouse is clicked on Canvas.
}
