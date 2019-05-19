public class Rectangle{
	public int x,y,w,h;
	private int[] pixels;

	Rectangle(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	Rectangle(){
		this(0,0,0,0);
	}

	public void generateGraphics(int color){
		pixels = new int[w*h];
		for(int y = 0; y < h; y++) for(int x = 0; x < w; x++) pixels[x + y * w] = color;
	}

	public void setPixel(int[] pixel){
		this.pixels = pixel;
	}

	public void clear(){
		this.pixels = new int[w*h];
	}


	public void generateSelectedGraphics(int xA, int yA, int color){
		int xStart = (w-xA)/2;
		int yStart = (h-yA)/2;
		pixels = new int[w*h];
		for(int y = yStart; y < h-yStart; y++) for(int x = xStart; x < w-xStart; x++) pixels[x + y * w] = color;
	}

	public int[] getPixels(){
		if(pixels != null) return pixels;
		else System.out.println("Attempted to retrive pixels from a Rectangle without generated graphics.");
		return null;
	}

	public boolean intersects(Rectangle otherRectangle){
			if(x > otherRectangle.x + otherRectangle.w || otherRectangle.x > x + w) return false;
			if(y > otherRectangle.y + otherRectangle.h || otherRectangle.y > y + h) return false;
			return true;
	}

}
