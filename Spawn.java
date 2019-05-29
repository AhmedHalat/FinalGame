import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class Spawn implements GameObject{
  private ArrayList<Character> characters;
  private Rectangle rect;
  private int layer;
  private static int room;
  private static int wave;

  public Spawn(int roomWidth, int roomHeight, int roomType){
    rect = new Rectangle(0,0,0,0);
  }

  public Spawn(){
    BufferedImage chestSheetImage = Game.loadImage("Chest.png");
    SpriteSheet chestSheet = new SpriteSheet(chestSheetImage);
		chestSheet.loadSprites(16, 16);
    AnimatedSprite chestAnimations = new AnimatedSprite(chestSheet, 5);
    characters.add(new Chest(chestAnimations, 0, 0, 16, 16, 3, 3));
  }

  public void addCharacter(Character character, int multiple){
    for(int i = 0; i < multiple; i++) characters.add(character);
  }

  public void render(RenderHandler renderer, int xZoom, int yZoom){
  }

  public void update(Game game, Player player){
    for(Character character: characters){
      if(character.isAlive()) character.interact(game, player);
    }
  }

  public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
    boolean stoppedChecking = false;
    for(Character character: characters){
      if(character.isAlive()) stoppedChecking = character.handleMouseClick(mouseRectangle, camera, xZoom, yZoom);
      if(stoppedChecking) break;
    }
    return stoppedChecking;
  }

  public int getLayer(){
    return layer;
  }

  public Rectangle getRectangle(){
    return rect;
  }
}
