package Dungeon;


/**
 * The Enemy type representing a "WallEnemy"
 * @author 985803	
 * @version 1.0
 * @since 04-12-2019
 */
import java.awt.Point;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

class WallEnemy extends Enemy {

    private boolean rightBearing;
    private int facing;
    /**
     * The constructor for a Wall Enemy
     * @param x The x location of the enemy
     * @param y The y location of the enemy
     * @param rightBearing Whether or not the wall enemy sticks to the right hand side of the wall
     * @param facing The direction in which the wall enemy is facing when created
     */
    public WallEnemy(int x, int y, boolean rightBearing, int facing) {
        super(x, y);
        this.rightBearing = rightBearing;
        this.facing = facing;
    }
    
    /**
     * Plans the next move of the enemy.
     * @param playerPos the position of the player represented as a Point
     * @param walkable The walkable areas of the map from the enemies perspective
     * stored in a two dimensional boolean array
     */
    protected void planMove(Point playerPos, boolean[][] walkable) {

        if (rightBearing) {
            turn(true);

            if (walkable[moveForward(new Point(x, y), facing).x][moveForward(new Point(x, y), facing).y]) {
               //if the enemy is facing a wall
            	move(moveForward(new Point(x, y), facing));
            } else {
                turn(false);
                if (walkable[moveForward(new Point(x, y), facing).x][moveForward(new Point(x, y), facing).y]) {
                	move(moveForward(new Point(x, y), facing));
                } else {
                    turn(false);
                    if (walkable[moveForward(new Point(x, y), facing).x][moveForward(new Point(x, y), facing).y]) {
                        move(moveForward(new Point(x, y), facing));
                    } else {
                        turn(false);
                        if (walkable[moveForward(new Point(x, y), facing).x][moveForward(new Point(x, y), facing).y]) {
                            move(moveForward(new Point(x, y), facing));
                        } else {
                            move(new Point(x, y));
                        }
                    }
                }
            }

        } else {
        	//if there is not a wall in front of the enemy
            turn(false);

            if (walkable[moveForward(new Point(x, y), facing).x][moveForward(new Point(x, y), facing).y]) {
                move(moveForward(new Point(x, y), facing));
            } else {
                turn(true);
                if (walkable[moveForward(new Point(x, y), facing).x][moveForward(new Point(x, y), facing).y]) {
                    move(moveForward(new Point(x, y), facing));
                } else {
                    turn(true);
                    if (walkable[moveForward(new Point(x, y), facing).x][moveForward(new Point(x, y), facing).y]) {
                        move(moveForward(new Point(x, y), facing));
                    } else {
                        turn(true);
                        if (walkable[moveForward(new Point(x, y), facing).x][moveForward(new Point(x, y), facing).y]) {
                            move(moveForward(new Point(x, y), facing));
                        } else {
                            move(new Point(x, y));
                        }
                    }
                }
            }
        }

    }
/**
 * Returns the location of the enemy if moved forward
 * @param pos The current position of the enemy
 * @param degree The angle at which the enemy is facing
 * @return The new location of the enemy once moved forward
 */
    private Point moveForward(Point pos, int degree) {

        Point newPos = pos;

        switch (degree) {
            case 0:
                newPos.y--;
                break;
            case 90:
                newPos.x++;
                break;
            case 180:
                newPos.y++;
                break;
            case 270:
                newPos.x--;
                break;
            default:
                break;
        }

        return newPos;
    }

    /**
     * Turns the enemy either clockwise or anticlockwise
     * @param clockwise A boolean that determains which way the enemy rotates
     */
    private void turn(boolean clockwise) {
        if (clockwise) {
            if (facing < 270) {
                facing = facing + 90;
            } else {
                facing = 0;
            }
        } else {
            if (facing == 0) {
                facing = 270;
            } else {
                facing = facing - 90;
            }
        }
    }
	@Override
	protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {
		gc.drawImage(images[0][5], x*cellWidth, y*cellWidth);
		
	}

    @Override
    protected String toFile() {
        return "EW" + "," + x + "," + y + "," + rightBearing + "," + facing;
    }
}
