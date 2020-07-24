package Dungeon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The abstract class that represents the Entity on the level
 * @author 983146	
 * @version 1.0
 * @since 06-12-2019
 */
abstract class Entity {

	protected int x, y;

	/**
	 * constructor of the Entity
	 * @param x the X location of the Entity
	 * @param y the Y location of the Entity
	 */
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	   * Gets the X location of the entity
	   * @return the X location of entity as an Integer
	   */
	public int getX() {
		return x;
	}

		/**
	   * Gets the X location of the entity
	   * @return the Y location of entity as an Integer
	   */
	public int getY() {
		return y;
	}
        /**
         * Method that gives a return value that represents the Entity as a String
         * @return The value of the Entity when converted to a String for the save file
         */
        protected abstract String toFile();
        
        /**
         * Draw cell into canvas with given width and size
         * @param cellWidth, The width of the Cell
         * @param gc, the game canvas to put image on
         * @param images The two dimensional array of every different image (tilemap)
         */ 
	protected abstract void draw(int cellWidth, GraphicsContext gc,Image[][] images);

}
