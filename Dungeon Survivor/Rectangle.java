

public class Rectangle 
{
    public int x,y,w,h;
    private int[] pixels;
    //Parameters: x,y positions ,w,h size
    //Sets variables
    //Constructor
    Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    //Parameters: None
    //Creates a empty rectangle for gameObject classes
    //2nd constructor
    Rectangle() {
        this(0,0,0,0);
    }
    //Parameters: a color
    //creates a rectangle and sets all its pixels to one color
    //returns Void
    public void generateGraphics(int color) {
        pixels = new int[w*h];
        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++) pixels[x + y * w] = color;
    }
    //Parameters: another rectangler
    //checks if one rectangle intersects with another
    //returns false if they don't, true if they do
    public boolean intersects(Rectangle otherRectangle){
        if(x > otherRectangle.x + otherRectangle.w || otherRectangle.x > x + w) return false;

        if(y > otherRectangle.y + otherRectangle.h || otherRectangle.y > y + h) return false;

        return true;
    }
    //Parameters: None
    //returns the pixels for rendering
    public int[] getPixels() {
        return pixels;
    }
}
