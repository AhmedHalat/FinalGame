 

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import java.awt.image.BufferedImage;

public class Melee implements GameObject{
    private Rectangle proRectangle;
    private boolean render = true;
     
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;
    private int playerX, playerY;
    //Parameters: sprite animatoins, zooms
    //Creates rectangle and sets sprite 
    //Constructor
    public Melee(Sprite sprite, int xZoom, int yZoom)
    {
        this.sprite = sprite;
        if(sprite != null && sprite instanceof AnimatedSprite)
            animatedSprite = (AnimatedSprite) sprite;
        updateDirection(0);
        
        proRectangle = new Rectangle(0 - 90, 0, 5, 5);
        
    }
    //Parameters: the direction the player is facing
    //Updates the sprite based on the direction. Second value is set to 0 because sword doesnt have motion animations
    //returns Void
    private void updateDirection(int direction){
        if(animatedSprite != null){
            animatedSprite.setAnimationRange(direction, 0);
        }
    }
    //Parameters: None
    //makes sword visible or invisible depending on if player is using it
    //returns Void
    public void invisible(){
        if(render)render = false;
        else render = true;
    }
    //Call every time physically possible.
    //Parameters: renderer - used to call class renderhandler, zooms
    //renders sword GameObject Method
    //returns Void
    public void render(RenderHandler renderer, int xZoom, int yZoom){
        if(render){
            if(animatedSprite != null)
                renderer.renderSprite(animatedSprite, playerX, playerY, xZoom, yZoom, false);
            else if(sprite != null)
                renderer.renderSprite(sprite, playerX, playerY, xZoom, yZoom, false);
            else renderer.renderRectangle(proRectangle, xZoom, yZoom, false);
        }
    }

    //Call at 60 fps rate.
    //Parameters: GameObject that are updating
    //Updates the swords position and direction b
    //returns Void
    public void update(Game game, Player player, Spawn spawner, Projectile pro, Melee melee){
        playerX = player.getRectangle().x - 35;
        playerY = player.getRectangle().y - 35;
        proRectangle.x = playerX;
        proRectangle.y = playerY;
        int direction = player.direction;
        updateDirection(direction);
        
    }
    
    //Parameters: None
    //GameObject Method are necessairy since it's being implemented
    //returns Void
    public int getLayer() {
        return 0;
    }
    //Parameters: None
    //GameObject Method
    //returns x,y,w,h of sword hitbox
    public Rectangle getRectangle() {
        return proRectangle;
    }
    //Parameters: mouse, camera xywh and zooms
    //GameObject method
    //returns false since this class doesnt need mouse click
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
        return false; 
    }
    //Call whenever mouse is clicked on Canvas.
}