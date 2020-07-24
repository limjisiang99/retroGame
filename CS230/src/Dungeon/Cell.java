package Dungeon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
/**
 * The abstract Cell class that represents a tile on the level
 * @author 967652
 * @version 1.0
 * @since 02-12-2019
 **/
abstract class Cell {

    private boolean isPlayerAllowed;
    private boolean isEnemyAllowed;
    /**
     * Constructor for a Cell object
     * @param isPlayerAllowed The boolean value determining whether the player can enter the cell
     * @param isEnemyAllowed The boolean value determining whether an enemy can enter the cell
     */
    public Cell(boolean isPlayerAllowed, boolean isEnemyAllowed) {
        this.isPlayerAllowed = isPlayerAllowed;
        this.isEnemyAllowed = isEnemyAllowed;
    }
    /**
     * Gets the boolean value determining whether the player can enter the Cell
     * @return The boolean value determining whether the player can enter the Cell
     */
    public boolean isPlayerAllowed() {
        return isPlayerAllowed;
    }
    /**
     * Gets the boolean value determining whether an enemy can enter the Cell
     * @return The boolean value determining whether an enemy can enter the Cell
     */
    public boolean isEnemyAllowed() {
        return isEnemyAllowed;
    }
    
    /**
     * Method that gives a return value that represents the Cell type as a string
     * @return The value of that Cell type if converted to a String
     */
    protected abstract String toFile();

    /**
     * Gets the name of the required item as a String
     * @return The name of the item required to use the Cell (if applicable)
     */
    protected abstract String getRequiredItem();
    
    /**
     * Draws the cell onto a canvas given a specified place and size
     * @param x The X coordinate of the Cell
     * @param y The Y coordinate of the Cell
     * @param cellWidth The width of the Cell
     * @param gc The game canvas to put the image on
     * @param images The two dimensional array of every different image (tilemap)
     */
    protected abstract void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images);

}

/**
 * The Cell type representing a "Coloured Door" tile
 * @author 967652,985803	
 * @version 1.0
 * @since 04-12-2019
 */
class ColouredDoor extends Cell {

    private final String itemRequired;
    private final String colour;
    
    /**
     * Constructor for a "Coloured Door" tile.
     * @param colour The colour of the ColouredDoor
     */
    public ColouredDoor(String colour) {
        super(true, false);
        itemRequired = "KEY";
        this.colour = colour;
    }

    @Override
    protected String getRequiredItem() {
        return itemRequired;
    }
    /**
     * Gets the colour of the ColouredDoor as a String
     * @return The colour of the ColouredDoor as a String
     */
    public String getRequiredColour() {
        return colour;
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        switch (colour) { //A case statement to draw the right coloured door image
            case "YELLOWKEY":
                gc.drawImage(images[2][1], x * cellWidth, y * cellWidth);
                break;
            case "GREENKEY":
                gc.drawImage(images[3][1], x * cellWidth, y * cellWidth);
                break;
            case "BLUEKEY":
                gc.drawImage(images[4][1], x * cellWidth, y * cellWidth);
                break;
            case "REDKEY":
                gc.drawImage(images[5][1], x * cellWidth, y * cellWidth);
                break;
            default:
                gc.drawImage(images[0][1], x * cellWidth, y * cellWidth);
        }
    }

    @Override
    protected String toFile() {
        switch (colour) {//a case statement to output the correct coloured door for use in the file
            case "REDKEY":
                return "dr";
            case "GREENKEY":
                return "dg";
            case "BLUEKEY":
                return "db";
            case "YELLOWKEY":
                return "dy";
            default:
                return null;
        }
    }

}

/**
 * The Cell type representing a "Fire" tile
 * @author 967652,985803	
 * @version 1.0
 * @since 04-12-2019
 */
class Fire extends Cell {

    private final String itemRequired;
    /**
     * Constructor for a "Fire" tile.
     */
    public Fire() {
        super(true, false);
        itemRequired = "FIREBOOTS";
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[3][0], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String getRequiredItem() {
        return itemRequired;
    }

    @Override
    protected String toFile() {
        return "f";
    }

}

/**
 * The Cell type representing a "Hole" tile
 * @author 967652,985803	
 * @version 1.0
 * @since 04-12-2019
 */
class Hole extends Cell {

    private final String itemRequired;
    /**
     * Constructor for a "Hole" tile.
     */
    public Hole() {
        super(true, false);
        itemRequired = "FLYBOOTS";
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[1][0], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String getRequiredItem() {
        return itemRequired;
    }

    @Override
    protected String toFile() {
        return "h";
    }

}

/**
 * The Cell type representing a "Water" tile
 * @author 967652,985803	
 * @version 1.0
 * @since 05-12-2019
 */
class Water extends Cell {

    private final String itemRequired;
    /**
     * Constructor for a "Water" tile.
     */
    public Water() {
        super(true, false);
        itemRequired = "WATERBOOTS";
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[2][0], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String getRequiredItem() {
        return itemRequired;
    }

    @Override
    protected String toFile() {
        return "w";
    }

}

/**
 * The Cell type representing a "Goal" tile
 * @author 967652,985803	
 * @version 1.0
 * @since 07-12-2019
 */
class Goal extends Cell {
    /**
     * Constructor for a "Goal" tile.
     */
    public Goal() {
        super(true, false);
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[5][0], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String getRequiredItem() {
        return null;
    }

    @Override
    protected String toFile() {
        return "g";
    }

}


/**
 * The Cell type representing a "Ground" tile
 * @author 967652	
 * @version 1.0
 * @since 02-12-2019
 */
class Ground extends Cell {
    /**
     * Constructor for a "Ground" tile.
     */
    public Ground() {
        super(true, true);
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[0][0], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String getRequiredItem() {
        return null;
    }

    @Override
    protected String toFile() {
        return "-";
    }

}

/**
 * The Cell type representing a "Token Door" tile
 * @author 967652,985803	
 * @version 1.0
 * @since 04-12-2019
 */
class TokenDoor extends Cell {

    private int tokensRequired;
    private String itemRequired;
    /**
     * Constructor for a "Token Door" tile.
     * @param tokensRequired The number of tokens required to open the door as an integer
     */
    public TokenDoor(int tokensRequired) {
        super(true, false);
        this.tokensRequired = tokensRequired;
        this.itemRequired = "TOKENS";
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[0][2], x * cellWidth, y * cellWidth);
        gc.setFont(new Font(16));
        gc.fillText(String.valueOf(this.tokensRequired), x*32+12, y*32+25);/*adds text to the
        door to represent the number of tokens required to enter it*/
    }

    @Override
    protected String getRequiredItem() {
        return itemRequired;
    }
    /**
     * Gets the number of tokens required to open the TokenDoor
     * @return The number of tokens required to open the TokenDoor
     */
    public int getTokensRequired() {
        return tokensRequired;
    }

    @Override
    protected String toFile() {
        return "dt" + tokensRequired;
    }

}

/**
 * The Cell type representing a "Wall" tile
 * @author 967652
 * @version 1.0
 * @since 02-12-2019
 */
class Wall extends Cell {
    /**
     * Constructor for a "Wall" tile.
     */
    public Wall() {
        super(false, false);
    }
    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[0][1], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String getRequiredItem() {
        return null;
    }

    @Override
    protected String toFile() {
        return "#";
    }

}

/**
 * The Cell type representing a "Void" tile
 * @author 967652	
 * @version 1.0
 * @since 04-12-2019
 */
class Void extends Cell {
    /**
     * Constructor for a "Void" tile.
     */
    public Void() {
        super(true, true);
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
    }

    @Override
    protected String getRequiredItem() {
        return null;
    }

    @Override
    protected String toFile() {
        return "v";
    }

}

/**
 * The Cell type representing a "Torch" tile
 * @author 975359
 * @version 1.0
 * @since 07-12-2019
 */
class Torch extends Cell {
    /**
     * Constructor for a "Torch" tile.
     */
    public Torch() {
        super(false, false);
    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[1][1], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String getRequiredItem() {
        return null;
    }

    @Override
    protected String toFile() {
        return "l";
    }

}

/**
 * The Cell type representing a "Teleporter" tile
 * @author 967652,985803	
 * @version 1.0
 * @since 06-12-2019
 */
class Teleporter extends Cell {

    private int destinationX;
    private int destinationY;
    /**
     * The constructor for the "Teleporter" tile
     * @param destinationX the X location of the teleporters destination
     * @param destinationY the Y location of the teleporters destination
     */
    public Teleporter(int destinationX, int destinationY) {
        super(true, false);
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }

    @Override
    protected String getRequiredItem() {
        return null;
    }
    /**
     * Gets the X location of the Teleporter destination
     * @return The X location of the Teleporter destination
     */
    public int getDestinationX() {
        return destinationX;

    }

    /**
     * Gets the Y location of the Teleporter destination
     * @return The Y location of the Teleporter destination
     */
    public int getDestinationY() {
        return destinationY;

    }

    @Override
    protected void draw(int x, int y, int cellWidth, GraphicsContext gc, Image[][] images) {
        gc.drawImage(images[4][0], x * cellWidth, y * cellWidth);
    }

    @Override
    protected String toFile() {
        return "t/" + destinationX + "/" + destinationY; // 't' followed by the destination x and y coordinates
    }

}
