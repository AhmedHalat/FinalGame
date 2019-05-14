public abstract class Character{
  protected Rectangle rect;
  protected Rectangle collisionCheckRectangle;
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

  public Character(Sprite sprite, AnimatedSprite animatedSprite, Rectangle rect, Rectangle collisionCheckRectangle, int speed, int direction, int layer, int xCollisionOffset, int yCollisionOffset) {
    this.rect = rect;
    this.collisionCheckRectangle = collisionCheckRectangle;
    this.direction = direction;
    this.speed = speed;
    this.layer = layer;
    this.sprite = sprite;
    this.animatedSprite = animatedSprite;
    this.xCollisionOffset = xCollisionOffset;
    this.yCollisionOffset = yCollisionOffset;
  }

  public Character(Sprite sprite, int speed, int layer, int xCollisionOffset, int yCollisionOffset){
    this.sprite = sprite;
    this.speed = speed;
    this.layer = layer;
    this.xCollisionOffset = xCollisionOffset;
    this.yCollisionOffset = yCollisionOffset;
  }

  public abstract void update();
  public abstract void updateStats(int [] stats);
  public abstract void updateDirection();


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
