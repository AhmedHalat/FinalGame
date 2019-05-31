public abstract class Character{
  protected Rectangle rect;
  protected Rectangle collisionCheckRectangle;
  //protected abstract Item drop;

  //0 = Right, 1 = Left, 2 = Up, 3 = Down
  protected int direction;
  protected int speed;
	protected int layer;
	protected Sprite sprite;
  protected AnimatedSprite animatedSprite;

  protected boolean dead;
  protected boolean move;
  //Collision offset right and left of the blocks
  protected final int xCollisionOffset;
  protected final int yCollisionOffset;

  protected Particle particles;
  protected boolean particle;

   Character[] drops = new Character[2];

  public Character(AnimatedSprite animatedSprite, Rectangle rect, Rectangle collisionCheckRectangle, int speed, int direction, int layer, int xCollisionOffset, int yCollisionOffset) {
    this.rect = rect;
    this.collisionCheckRectangle = collisionCheckRectangle;
    this.direction = direction;
    this.speed = speed;
    this.layer = layer;
    this.animatedSprite = animatedSprite;
    this.xCollisionOffset = xCollisionOffset;
    this.yCollisionOffset = yCollisionOffset;
  }

  public Character(AnimatedSprite sprite, int speed, int xCollisionOffset, int yCollisionOffset){
    this.animatedSprite = sprite;
    this.speed = speed;
    this.layer = 0;
    this.xCollisionOffset = xCollisionOffset;
    this.yCollisionOffset = yCollisionOffset;
  }

  public Character(int speed, int xCollisionOffset, int yCollisionOffset){
    this.speed = speed;
    this.layer = 0;
    this.xCollisionOffset = xCollisionOffset;
    this.yCollisionOffset = yCollisionOffset;
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
    if(animatedSprite != null) renderer.renderSprite(animatedSprite, rect.x, rect.y, xZoom, yZoom, false);
    else if(sprite != null) renderer.renderSprite(sprite, rect.x, rect.y, xZoom, yZoom, false);
    else renderer.renderRectangle(rect, xZoom, yZoom, false);
  }

  public void renderParticles(RenderHandler renderer, int xZoom, int yZoom){
    particles.update(rect.x + 6, rect.y - 6);
    particles.render(renderer, 2, 2);
  }

  public void interact(Game game, Player player, Spawn spawner){
    if(true) action(game, player, spawner);
  }


  public abstract void updateStats(int [] stats);
  public abstract void updateDirection();
  public abstract void action(Game game, Player player, Spawn spawner);
  public abstract boolean isAlive();
  public abstract boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom);


	/**
	* Returns value of rect
	* @return
	*/
	public Rectangle getRect() {
		return rect;
	}

	/**
	* Sets new value of rect
	* @param
	*/
	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	/**
	* Returns value of collisionCheckRectangle
	* @return
	*/
	public Rectangle getCollisionCheckRectangle() {
		return collisionCheckRectangle;
	}

  public int getCollisionCheckRectangleX() {
    return collisionCheckRectangle.x;
  }

  public int getCollisionCheckRectangleY() {
    return collisionCheckRectangle.y;
  }

	/**
	* Sets new value of collisionCheckRectangle
	* @param
	*/
	public void setCollisionCheckRectangle(Rectangle collisionCheckRectangle) {
		this.collisionCheckRectangle = collisionCheckRectangle;
	}

  public void setCollisionCheckRectangleX(int x) {
    this.collisionCheckRectangle.x = x;
  }

  public void setCollisionCheckRectangleY(int y) {
    this.collisionCheckRectangle.y = y;
  }

	/**
	* Returns value of direction
	* @return
	*/
	public int getDirection() {
		return direction;
	}

	/**
	* Sets new value of direction
	* @param
	*/
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	* Returns value of speed
	* @return
	*/
	public int getSpeed() {
		return speed;
	}

	/**
	* Sets new value of speed
	* @param
	*/
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	* Returns value of layer
	* @return
	*/
	public int getLayer() {
		return layer;
	}

  public boolean particles(){
    return particle;
  }

	/**
	* Sets new value of layer
	* @param
	*/
	public void setLayer(int layer) {
		this.layer = layer;
	}

	/**
	* Returns value of sprite
	* @return
	*/
	public Sprite getSprite() {
		return sprite;
	}

	/**
	* Sets new value of sprite
	* @param
	*/
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	/**
	* Returns value of animatedSprite
	* @return
	*/
	public AnimatedSprite getAnimatedSprite() {
		return animatedSprite;
	}

	/**
	* Sets new value of animatedSprite
	* @param
	*/
	public void setAnimatedSprite(AnimatedSprite animatedSprite) {
		this.animatedSprite = animatedSprite;
	}

	/**
	* Returns value of xCollisionOffset
	* @return
	*/
	public int getXCollisionOffset() {
		return xCollisionOffset;
	}

	/**
	* Returns value of yCollisionOffset
	* @return
	*/
	public int getYCollisionOffset() {
		return yCollisionOffset;
	}

}
