package Dungeon;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The player entity that is drawn on screen. does not handle player related events
 * @author 985803
 */
public class Player extends Entity {

    private ArrayList<String> inventory = new ArrayList<>();
    /**
     * Creates an instance of a player
     * @param x The X location of the player
     * @param y The Y location of the player
     */
    public Player(int x, int y) {
        super(x, y);
    }
    
    /**
     * Sets the X location of the player
     * @param x The X location for the player to be set to
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the Y location of the player
     * @param y The Y location for the player to be set to
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Adds a collectible as a string to the players inventory.
     * @param item The item to be added to the players inventory
     */
    public void addItem(String item) {
        inventory.add(item);
    }

    /**
     * Removes a collectible as a string from the players inventory
     * @param item The item to be removed from the players inventory
     */
    public void removeItem(String item) {
        inventory.remove(item);
    }
    
    /**
     * Takes an collectible as a string and checks if the players inventory is holding it
     * @param item the collectible to be checked for in the players inventory
     * @return true if the player has that Collectible in their inventory
     */
    public boolean hasItem(String item) {
        if (inventory.contains(item)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[4][5], x * cellWidth, y * cellWidth);

    }

    @Override
    protected String toFile() {
        return "P" + "," + x + "," + y;
    }

}
