package Dungeon;

import java.util.ArrayList;

/**
 * The class which manages the players inventory
 * @author 985803
 */
public class Inventory {

    private boolean fireBoots;
    private boolean waterBoots;
    private boolean flyBoots;
    private boolean lantern;
    private int tokens;
    private ArrayList<String> keys;

    /**
     * Constructor for a players Inventory
     */
    public Inventory() {
        flyBoots = false;
        fireBoots = false;
        waterBoots = false;
        lantern = false;
        tokens = 0;
        keys = new ArrayList<>();

    }

    /**
     * Gets a boolean determining whether the player has fire boots 
     * @return True if the player has fire boots, false otherwise
     */
    public boolean hasFireBoots() {
        return fireBoots;
    }
    
    /**
     * Gets a boolean determining whether the player has fly boots 
     * @return True if the player has fly boots, false otherwise
     */
    public boolean hasFlyBoots() {
        return flyBoots;
    }
    
    /**
     * Gets a boolean determining whether the player has water boots 
     * @return True if the player has water boots, false otherwise
     */
    public boolean hasWaterBoots() {
        return waterBoots;
    }
    
    /**
     * Gets the number of tokens in the inventory
     * @return The number of tokens in the inventory as an integer
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * Gets a list of the keys that the player is in possession of
     * @return An ArrayList of strings with every key the player is holding in it
     */
    public ArrayList<String> getKeys() {
        return keys;
    }
    
    /**
     * Removes the key from a String from the players inventory
     * @param key The key as a string to remove
     */
    public void removeKey(String key){
        keys.remove(key);
    }
    
    /**
     * Adds fire boots to the players inventory
     */
    public void addFireBoots() {
        fireBoots = true;
    }
    
    /**
     * Adds fly boots to the players inventory
     */
    public void addFlyBoots() {
        flyBoots = true;
    }

    /**
     * Adds water boots to the players inventory
     */
    public void addWaterBoots() {
        waterBoots = true;
    }

    /**
     * Adds tokens to the players inventory
     * @param tokens The number of tokens to add as an integer
     */
    public void addTokens(int tokens) {
        this.tokens = this.tokens + tokens;
    }
    
    /**
     * Adds a key from a String to the players Inventory
     * @param key The key to be added as a String
     */
    public void addKey(String key) {
        keys.add(key);
    }
    
    /**
     * Gets a boolean determining whether the player has a lantern 
     * @return True if the player has a lantern, false otherwise
     */
     public boolean hasLantern() {
        return lantern;
    }
     /**
      * Adds a Lantern to the players inventory
      */
    public void addLantern() {
        lantern = true;
    }

}
