import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.image.BufferedImage;
/**
 * The player class is a GameObject and a Character
 * This class is responsible for his motion, animations and collisions
 */
public class Player extends Character implements GameObject{
	private Particle particles;
	public Player(Sprite sprite, int xZoom, int yZoom){
		super((AnimatedSprite) sprite, 5, 15, 25);
		this.sprite = sprite;
		if(sprite != null && sprite instanceof AnimatedSprite) animatedSprite = (AnimatedSprite) sprite;
		updateDirection();
		Rectangle playerRectangle = new Rectangle(0, 100, 20, 26);
		Rectangle collisionCheckRectangle = new Rectangle(0, 0, 10*xZoom, 15*yZoom);
		particles = new Particle(30, 46, 50, 1);
		particles.fill(0xFFF7D80C);
		this.rect = playerRectangle;
		this.collisionCheckRectangle = collisionCheckRectangle;
	}

	/**
	 * When the player changes direction, load different layer on spritesheet
	 */
	public void updateDirection(){
		if(animatedSprite != null) animatedSprite.setAnimationRange(direction * 8, (direction * 8) + 7);
	}

	/**
	 * Ticks player with Game
	 * @param game    Give player access to map
	 * @param player  Not Used
	 * @param spawner Give player access to mobs and weaponS
	 */
	public void update(Game game, Player player, Spawn spawner){
		//end game if player is dead
		if(dead) game.endGame();
		//Get Key controlls
		KeyBoardListener keyListener = game.getKeyListener();
		boolean didMove = false;
		int newDirection = direction;
		layer = 0;
		//Create the collision rectangle to see if he can move
		collisionCheckRectangle.x = rect.x;
		collisionCheckRectangle.y = rect.y;
		//Move collisionCheckRectangle to position character is trying to move in
		if(keyListener.left()){
			newDirection = 1;
			didMove = true;
			collisionCheckRectangle.x -= speed;
		}
		else if(keyListener.right()){
			newDirection = 0;
			didMove = true;
			collisionCheckRectangle.x += speed;
		}
		else if(keyListener.up()){
			collisionCheckRectangle.y -= speed;
			didMove = true;
			newDirection = 2;
		}
		else if(keyListener.down()){
			newDirection = 3;
			didMove = true;
			collisionCheckRectangle.y += speed;
		}
		//Change character sprite direction
		if(newDirection != direction) {
			direction = newDirection;
			updateDirection();
		}
		//if he isnt moving, make his sprite standstill
		if(!didMove) {
			animatedSprite.reset();
		}
		else {
			//call didmove if he moved, which will check collisions and move character
			didMove(game, player, spawner);
			//animates his sprite
			animatedSprite.update(game, this, spawner);
		}
		//Moves camera to keep centered above him
		updateCamera(game.getRenderer().getCamera());
	}

	//Methods required from character
	public void action(Game game, Player player, Spawn spawner){
	}
	public void renderParticles(RenderHandler renderer, int xZoom, int yZoom){
	}

	public void updateStats(){
		speed = stats.getSpeed();
	}

	public void updateCamera(Rectangle camera) {
		camera.x = rect.x - (camera.w / 2);
		camera.y = rect.y - (camera.h / 2);
	}

	public boolean isAlive(){
		return dead;
	}

	public int getLayer() {
		return layer;
	}

	public Rectangle getRectangle() {
		return rect;
	}
	//Player doesnt use mouse
	public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) {
		return false;
	}

}
