package Dungeon;
import java.awt.Point;

/**
 * The Entity type representing an Enemy
 * @author 985803
 * @version 1.0
 * @since 6/12/2019
 */
abstract class Enemy extends Entity {

	/**
     * The Constructor for an Enemy
     * @param x The Enemy X location 
     * @param y The Enemy Y location
     */ 
    public Enemy(int x, int y) {
        super(x, y);
    }

    /**
     * Plans and directs where the Enemy is to move
     * @param walkable The walkable area of the map as a two dimensional boolean array.
     * @param playerPos The players current position as a Point
     */    
    protected abstract void planMove(Point playerPos, boolean[][] walkable);

    /**
     * Sets the X and Y location of the Enemy
     * @param p The location that the Enemy is to be set to
     */
    protected void move(Point p) {
        x = p.x;
        y = p.y;
    }

}
