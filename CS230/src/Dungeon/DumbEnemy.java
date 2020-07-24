package Dungeon;

import java.awt.Point;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 *The DumbEnemy type representing the Enemy title
 * @author 985803
 * @version 1.0
 * @since 6/12/2019
 */
class DumbEnemy extends Enemy {

	/**
     * Constructor for the DumbEnemy
     * @param x DumbEnemy location x
     * @param y DumbEnemy location y
     */ 
    public DumbEnemy(int x, int y) {
        super(x, y);
    }

    /**
     * Plans and directs where the DumbEnemy to move
     * @param walkable The walkable area of the DumbEnemy.
     * @param playerPos The player current position
     */    
    protected void planMove(Point playerPos, boolean[][] walkable) {

        Point bestMove = new Point(x + 1, y);

        if (getDistance(x - 1, y, playerPos.x, playerPos.y) < getDistance(bestMove.x, bestMove.y, playerPos.x, playerPos.y)) {
            bestMove = new Point(x - 1, y);
        }
        if (getDistance(x, y + 1, playerPos.x, playerPos.y) < getDistance(bestMove.x, bestMove.y, playerPos.x, playerPos.y)) {
            bestMove = new Point(x, y + 1);
        }
        if (getDistance(x, y - 1, playerPos.x, playerPos.y) < getDistance(bestMove.x, bestMove.y, playerPos.x, playerPos.y)) {
            bestMove = new Point(x, y - 1);
        }

        if (walkable[bestMove.x][bestMove.y]) {
            move(bestMove);
        }

    }
/**
 * method that gets the distance of the enemy to the player 
 * @param x1 distance x1 of the player 
 * @param y1 distance y1 of the player
 * @param x2 player position x2
 * @param y2 player position y2
 * @return the distance between the enemy to the player
 */
    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    @Override
    protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[2][5], x * cellWidth, y * cellWidth);

    }

    @Override
    protected String toFile() {
        return "ED" + "," + x + "," + y;
    }
}
