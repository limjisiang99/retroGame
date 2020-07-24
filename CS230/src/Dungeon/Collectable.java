/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dungeon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The Entity type representing a "Collectable"
 * @author 985803 983146
 * @version 1.0
 * @since 4/12/2019
 */
abstract class Collectable extends Entity {

	/**
	 * Constructor for Collectable
	 * @param x The X location of the Collectable
	 * @param y The Y location of the Collectable
	 */
    public Collectable(int x, int y) {
        super(x, y);
    }

    abstract String getType();
}

/**
 * The Collectable type representing the Boots title
 * @author 985803 983146
 * @version 1.0
 * @since 6/12/2019
 */
class Boots extends Collectable {

    String bootType;

/**
 * constructor for Boots
 * @param x The X location of the Boots
 * @param y The Y location of the Boots
 * @param bootType The type of the Boots as a String
 */
    public Boots(int x, int y, String bootType) {
        super(x, y);
        this.bootType = bootType;
    }

    @Override
    String getType() {
        return bootType;
    }

    @Override
    protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {
        switch (bootType) {//A case statement to get the right boot types' image
            case "FIREBOOTS":
                gc.drawImage(images[0][4], x * cellWidth, y * cellWidth);
                break;
            case "WATERBOOTS":
                gc.drawImage(images[1][4], x * cellWidth, y * cellWidth);
                break;
            case "FLYBOOTS":
                gc.drawImage(images[2][4], x * cellWidth, y * cellWidth);
                break;
        }
    }

    @Override
    protected String toFile() {
        switch (bootType) {//a case statement which outputs the correct 
        //boots string for use in a file
            case "FIREBOOTS":
                return "BF" + "," + x + "," + y;
            case "WATERBOOTS":
                return "BW" + "," + x + "," + y;
            case "FLYBOOTS":
                return "BH" + "," + x + "," + y;
            default:
                return null;
        }
    }

}
/**
 *The collectable type representing a "Key"
 * @author 983146
 * @version 1.0
 * @since 6/12/2019
 */
class Key extends Collectable {

    String keyColour;
    
 /**
  * The constructor for a Key
  * @param x The X location of the Key
  * @param y The Y location of the Key
  * @param keyColour The colour of the key
  */  
    public Key(int x, int y, String keyColour) {
        super(x, y);
        this.keyColour = keyColour;
    }

    @Override
    String getType() {
        return keyColour;
    }

    @Override
    protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {

        switch (keyColour) {// a case statement which gets the right image
        //for a key of that colour
            case "YELLOWKEY":
                gc.drawImage(images[0][3], x * cellWidth, y * cellWidth);
                break;
            case "GREENKEY":
                gc.drawImage(images[1][3], x * cellWidth, y * cellWidth);
                break;
            case "BLUEKEY":
                gc.drawImage(images[2][3], x * cellWidth, y * cellWidth);
                break;
            case "REDKEY":
                gc.drawImage(images[3][3], x * cellWidth, y * cellWidth);
                break;

        }
    }

    @Override
    protected String toFile() {//a case statement which outputs the correct 
    	//String for that key for use in the file
        switch (keyColour) {
            case "YELLOWKEY":
                return "KY" + "," + x + "," + y;
            case "GREENKEY":
                return "KG" + "," + x + "," + y;
            case "BLUEKEY":
                return "KB" + "," + x + "," + y;
            case "REDKEY":
                return "KR" + "," + x + "," + y;
            default:
                return null;
        }
    }

}
/**
 *The collectable type representing a "Token"
 * @author 985803 983146
 * @version 1.0
 * @since 6/12/2019
 */
class Token extends Collectable {
/**
 * The constructor for a Token
 * @param x The X location of the token
 * @param y The Y location of the token
 */
    public Token(int x, int y) {
        super(x, y);
    }
    
    @Override
    String getType() {
        return "TOKEN";
    }

    @Override
    protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[5][3], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String toFile() {
        return "T" + "," + x + "," + y;
    }

}

/**
 *The Collectable type representing a "Lantern"
 * @author 975359
 * @version 1.0
 * @since 7/12/2019
 */
class Lantern extends Collectable {

	/**
	 * The Constructor for a Lantern
	 * @param x The X location of the Lantern
	 * @param y The Y location of the Lantern
	 */
    public Lantern(int x, int y) {
        super(x, y);
    }

    @Override
    String getType() {
        return "LANTERN";
    }

    @Override
    protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[4][3], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String toFile() {
        return "L" + "," + x + "," + y;
    }

}
