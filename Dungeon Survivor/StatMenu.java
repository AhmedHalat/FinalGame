 

import java.awt.image.BufferedImage;


import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class StatMenu implements GameObject {
    
    File statsFile;
    
    public int[] statList;
    public String [] statNames;
    public int hp = 100;
    
    private Rectangle healthBar;
    private Rectangle statusBar = new Rectangle(0, 700, 115, 25);
    private Rectangle experienceBar;
    private Player player;
    //Parameters: None
    //loads stat file
    //Constructor
    public StatMenu(){
        //Load character stats
        statsFile = new File("stats.txt");
        loadStatMenu(statsFile);
        
    }
    //Parameters: file
    //Loads all the stat values into an array
    //returns Void
    public void loadStatMenu(File statsFile){
         try 
        {
            Scanner scanner = new Scanner(statsFile);
            String line = scanner.nextLine();
            statList = new int[Integer.parseInt(line)];
            statNames = new String[Integer.parseInt(line)];
            int index = 0;
            while(scanner.hasNextLine()) 
            {
                line = scanner.nextLine();
                if(!line.startsWith("//"))
                {
                    String[] splitString = line.split("-");
                    String statName = splitString[0];
                    int statValue = Integer.parseInt(splitString[1]);
                    statList[index] = statValue;
                    statNames[index] = statName;
                    index++;
                }
            }
        } 
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    //Call every time physically possible.
    //Parameters: Renderer - gives acces to RenderHandler class, Zooms - pixel multipliers. GameObject method
    //renders healthbar and its background
    //returns 
    public void render(RenderHandler renderer, int xZoom, int yZoom){
            healthBar = new Rectangle(15, 715, hp, 10);
            
            statusBar.generateGraphics(0xFF9F9F9F);
            renderer.renderRectangle(statusBar, xZoom, yZoom, true);
            
            healthBar.generateGraphics(0xFFA73121);
            renderer.renderRectangle(healthBar, xZoom, yZoom, true);
    }
    

    //Call at 60 fps rate.
    //Parameters: GameObjects that are being updated
    //updates stats
    //returns Void
    public void update(Game game, Player player, Spawn spawner, Projectile pro, Melee melee){
        game.setStats(statList);
        player.updateStats(statList);
        hp = game.stats[1];
    }

    //Call whenever mouse is clicked on Canvas.
    //Parameters: mouse, camera, and zooms
    //Necessairy since this class implements gameobject
    //returns false since this class doesnt use mouse click
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
        return false;
    }
    //Parameters: None
    //sets the render layer to max so nothing renders above his healthbar. GameObject method
    //returns max value
    public int getLayer(){
        return Integer.MAX_VALUE;
    }
    //Parameters: None
    //GameObject Method
    //returns healthbar x,y,w,h
    public Rectangle getRectangle(){
        return healthBar;
    }
}
