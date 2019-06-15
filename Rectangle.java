/**
 * Rectangle that sets collisions and size/position of every character
 */
public class Rectangle{
	public int x,y,w,h;
	private int[] pixels; //stores the color of every pixel
	/**
	 * default Constructor
	 * @param x Pos
	 * @param y Pos
	 * @param w width
	 * @param h height
	 */
	Rectangle(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	//just sets everything to 0
	Rectangle(){
		this(0,0,0,0);
	}

/**
 * generates the graphics for the Rectangle
 * @param color color used to visiualise collision rectangles
 */
	public void generateGraphics(int color){
		pixels = new int[w*h];
		for(int y = 0; y < h; y++) for(int x = 0; x < w; x++) pixels[x + y * w] = color;
	}

	//clears all pixels
	public void clear(){
		this.pixels = new int[w*h];
	}

/**
 * determins if two rectangles intersect
 * @param  otherRectangle other rectangle
 * @return                Boolean, true if collision
 */
	public boolean intersects(Rectangle otherRectangle){
			if(x > otherRectangle.x + otherRectangle.w || otherRectangle.x > x + w) return false;
			if(y > otherRectangle.y + otherRectangle.h || otherRectangle.y > y + h) return false;
			return true;
	}

	/**
	 * sets the pixel of this rectangle
	 * @param pixel stores the color of every pixel
	 */
	public void setPixel(int[] pixel){
		this.pixels = pixel;
	}

	//getter for pixels
	public int[] getPixels(){
		if(pixels != null) return pixels;
		else System.out.println("Attempted to retrive pixels from a Rectangle without generated graphics.");
		return null;
	}

	/**
	* Create string representation of Rectangle for printing
	* @return
	*/
	@Override
	public String toString() {
		return "Rectangle [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h +"]";
	}
}
