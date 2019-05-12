 

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class KeyBoardListener implements KeyListener, FocusListener {
    //120 keys that im checking for. 
    public boolean[] keys = new boolean[120];

    private Game game;
    StatMenu statMenu = new StatMenu();
    public KeyBoardListener(Game game){
        this.game = game;
    }
    //Parameters: Key event
    //gets what key is currently being clicked and performs the appropriate action
    //returns Void
     public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        
        if(keyCode < keys.length) keys[keyCode] = true;
        
        if(keys[KeyEvent.VK_P])game.pause();
        if(keys[KeyEvent.VK_SPACE])game.attack();
        if(keys[KeyEvent.VK_Q]){
            int weaponID = game.selectedWeapon;
            
            if(weaponID == 5) game.weaponSelect(6);
            else if (weaponID == 6) game.weaponSelect(5);
        }
        if(keys[KeyEvent.VK_CONTROL] && keys[KeyEvent.VK_U]) game.newWave(0);
    }
    //Parameters: event
    //if key is released, sets the key to false
    //returns Void
    public void keyReleased(KeyEvent event){
        int keyCode = event.getKeyCode();

        if(keyCode < keys.length)  keys[keyCode] = false;
     }
    //Parameters: Focus event
    //if player clicks off screen, all keys set to false
    //returns 
    public void focusLost(FocusEvent event){
        for(int i = 0; i < keys.length; i++)  keys[i] = false;
    }

    public void keyTyped(KeyEvent event) {}

    public void focusGained(FocusEvent event) {}
    //Player directions to be used in player class
    public boolean up(){
        return keys[KeyEvent.VK_UP];
    }
    public boolean down(){
        return  keys[KeyEvent.VK_DOWN];
    }
    public boolean left(){
        return keys[KeyEvent.VK_LEFT] ;
    }
    public boolean right(){
        return keys[KeyEvent.VK_RIGHT];
    }
    

}