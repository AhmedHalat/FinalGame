

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.image.BufferedImage;
//GameObject method
public class Player implements GameObject{
	private Rectangle playerRectangle;
	private Rectangle collisionCheckRectangle;
	int newSpeed = 5;
	//0 = Right, 1 = Left, 2 = Up, 3 = Down
	int direction = 0;
	private int layer;
	private Sprite sprite;
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
		playerRectangle = new Rectangle(0, 0, 20, 26);
		collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
	}
	//Parameters: array that contains players current stats
	//sets the players new motion speed if the stats have been upgraded
	//returns Void
	public void updateStats(int [] stats){
		newSpeed = stats[0];
	}


	private void updateDirection(){
		if(animatedSprite != null)
		{
			animatedSprite.setAnimationRange(direction * 8, (direction * 8) + 7);
		}
	}

	public void render(RenderHandler renderer, int xZoom, int yZoom){
		if(animatedSprite != null)
		renderer.renderSprite(animatedSprite, playerRectangle.x, playerRectangle.y, xZoom, yZoom, false);
		else if(sprite != null)
		renderer.renderSprite(sprite, playerRectangle.x, playerRectangle.y, xZoom, yZoom, false);
		else
		renderer.renderRectangle(playerRectangle, xZoom, yZoom, false);
	}

	public void update(Game game, Player player){

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


			animatedSprite.update(game, this);
		}

		updateCamera(game.getRenderer().getCamera());
	}

	public void updateCamera(Rectangle camera) {
		camera.x = playerRectangle.x - (camera.w / 2);
		camera.y = playerRectangle.y - (camera.h / 2);
	}

	public int getLayer() {
		return layer;
	}

	public Rectangle getRectangle() {
		return playerRectangle;
	}

	public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
		return false;
	}

}
