package Dungeon;

import java.awt.Point;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * The Enemy type representing a "StraightEnemy"
 * @author 985803	
 * @version 1.0
 * @since 04-12-2019
 */
class StraightEnemy extends Enemy {

    private int facing;

    
    /**
     * constructor for StraightEnemy object
     * @param x The X location for the StraightEnemy
     * @param y The Y location for the StraightEnemy
     * @param facing The direction that this StraightEnemy is facing
     */
    public StraightEnemy(int x, int y, int facing) {
        super(x, y);
        this.facing = facing;
    }

    
    /**
     * Plans and directs where the DumbEnemy is to move
     * @param walkable The walkable area of the DumbEnemy.
     * @param playerPos The player current position
     */    
    protected void planMove(Point playerPos, boolean[][] walkable) {

        //Variables to hold potential move
        int tryX = x;
        int tryY = y;

        //Attempts to move in the direction it is facing        
        switch (facing) {
            case 0:
                tryY--;
                break;
            case 180:
                tryY++;
                break;
            case 270:
                tryX--;
                break;
            case 90:
                tryX++;
                break;
            default:
                break;
        }

        //If this is possible then we do so
        //else, turn around and try move in the opposite direction.
        if (walkable[tryX][tryY] == true) {
            move(new Point(tryX, tryY));
        } else {
            tryX = x;
            tryY = y;
            switch (facing) {
                case 0:
                    facing = 180;
                    tryY++;
                    break;
                case 180:
                    facing = 0;
                    tryY--;
                    break;
                case 270:
                    facing = 90;
                    tryX++;
                    break;
                case 90:
                    facing = 270;
                    tryX--;
                    break;
                default:
                    break;
            }

            //If we are still unable to move, then the unit has no movement options and 
            //its turn is over.
            if (walkable[tryX][tryY] == true) {
                move(new Point(tryX, tryY));
            }
        }

    }
	@Override
	protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {
		gc.drawImage(images[1][5], x*cellWidth, y*cellWidth);
		
	}

    @Override
    protected String toFile() {
        return "EST" + "," + x + "," + y + "," + facing;
    }

}
