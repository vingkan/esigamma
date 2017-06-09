package theHungerGames;

import java.awt.Graphics;



/**
 * This interface is for anything that is drawn by the Viewer class
 * 
 * @author Peter Dong
 *
 */
public interface Drawable {
	/**
	 * Draw the object
	 * 
	 * @param graph - the Graphics object that will draw it
	 * @param x - the position of the upper left corner of the object, in screen coordinates
	 * @param y - the position of the upper left corner of the object, in screen coordinates
	 */
	public void Draw(Graphics graph, int x, int y);
	
	/**
	 * @return the size of the object, in pixels
	 */
	public int getXSize();
	
	/**
	 * @return the size of the object, in pixels
	 */
	public int getYSize();
}
