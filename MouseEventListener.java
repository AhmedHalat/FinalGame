

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Responsible for mouse events and movements
 */
public class MouseEventListener implements MouseListener, MouseMotionListener {
 private Game game;

 public MouseEventListener(Game game) {
	 this.game = game;
 }

/**
 * If the mouse is clicked call game.left click
 * @param event mouse
 */
 public void mousePressed(MouseEvent event){
	 if(event.getButton() == MouseEvent.BUTTON1)
		 game.leftClick(event.getX(), event.getY());

	 if(event.getButton() == MouseEvent.BUTTON3){}
 }

  //unused methods
  public void mouseClicked(MouseEvent event){}
  public void mouseDragged(MouseEvent event){}
  public void mouseEntered(MouseEvent event){}
  public void mouseExited(MouseEvent event){}
  public void mouseMoved(MouseEvent event){}
 public void mouseReleased(MouseEvent event){}
}
