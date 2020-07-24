package Dungeon;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
/**
 * The main game class which runs on startup.
 * @author 985803,975359	
 */
public class Game extends Application {

    private static final int CANVAS_WIDTH = 576;
    private static final int CANVAS_HEIGHT = 384;
    private static final int CELL_WIDTH = 32;

    public static String profile;
    public static Stage stage;
    public static int levelNumber;
    public static int maxLevelAllowed = 1;

    private static Group root;
    private static Canvas canvas;
    private static Canvas overlay;
    private static Canvas hud;
    private static Image[][] tilemap;

    private static Level level;
    private static Cell[][] cells;
    private static Player player;
    private static ArrayList<Enemy> enemies;
    private static ArrayList<Collectable> collectables;
    private static Inventory inventory;

    /**
     * Launches an application.
     * @param args holds any command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Opens title screen.
     */
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        Controller controller = new Controller();
        controller.launchTitleScreen();
    }
    /**
     * Starts the game.
     * @param stage The window to draw the game to
     * @param loadProgress If the game should start a new save or continue on an old save
     */
    public void startGame(Stage stage, boolean loadProgress) {

        FileHandling.loadProfile(profile, maxLevelAllowed);
        if (!loadProgress) {//loads the progress of a profile
            FileHandling.copyFile("src/Dungeon/defaultLevels/level" + levelNumber + ".txt",
                    "src/Dungeon/profileLevels/" + profile.toUpperCase()
                    + "_level" + levelNumber + ".txt");
            FileHandling.loadProfile(profile, maxLevelAllowed);
        }
        //adjusts canvas settings
        stage.setTitle("Dungeon Getaway");
        stage.setResizable(false);
        stage.setWidth(CANVAS_WIDTH + 6);
        stage.setHeight(CANVAS_HEIGHT + 28);
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        overlay = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        overlay.setBlendMode(BlendMode.MULTIPLY);
        hud = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        root = new Group();
        root.getChildren().add(canvas);
        root.getChildren().add(overlay);
        root.getChildren().add(hud);
        Scene scene = new Scene(root);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> processKeyEvent(event));
        prepareLevel();
        LevelTimer.addSavedTime(level.getTimer());
        LevelTimer.startTimer();

        render();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Sets up the level to be played
     */
    private void prepareLevel() {

        tilemap = FileHandling.loadImages(6, 6,CELL_WIDTH, "file:src/Dungeon/tilemap.png");
        level = FileHandling.loadLevel(profile, levelNumber);
        player = level.getPlayer();
        enemies = level.getEnemies();
        cells = level.getCells();
        collectables = level.getCollectables();
        inventory = level.getInventory();
    }
    /**
     * Handles the key events to control the player.
     * @param event The keyEvent to handle
     */
    private void processKeyEvent(KeyEvent event) {
        switch (event.getCode()) {
            case D:
                if (canMove(player.getX() + 1, player.getY())) {
                    player.setX(player.getX() + 1);
                    update();
                }
                break;
            case A:
                if (canMove(player.getX() - 1, player.getY())) {
                    player.setX(player.getX() - 1);
                    update();
                }
                break;
            case W:
                if (canMove(player.getX(), player.getY() - 1)) {
                    player.setY(player.getY() - 1);
                    update();
                }
                break;
            case S:
                if (canMove(player.getX(), player.getY() + 1)) {
                    player.setY(player.getY() + 1);
                    update();
                }
                break;
            case P:// pauses the game
                FileHandling.saveClose(profile, levelNumber, cells, inventory,
                        enemies, collectables, player, LevelTimer.getCurrentTime());
                stage.close();//closes the window
                break;
            default:
                break;
        }
        render();
        event.consume();
    }
    /**
     * Renders the game
     */
    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.setFill((Paint) Color.rgb(37, 19, 26));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        Affine old = new Affine(gc.getTransform());
        gc.translate(-(((player.getX() * CELL_WIDTH) - CANVAS_WIDTH / 2) + CELL_WIDTH / 2),
                -(((player.getY() * CELL_WIDTH) - CANVAS_HEIGHT / 2) + CELL_WIDTH / 2));
        /*Layers canvases on top of eachother, going from cells, collectables, player and enemies 
         then the lighting and finally the hud*/
        renderCells(gc);
        renderCollectables(gc);
        renderPlayer(gc);
        renderEnemies(gc);

        renderLighting(gc.getTransform(), 256);
        renderHud();
        gc.setTransform(old);

    }
    /**
     * Is used to render the lighting over the level
     * @param transform Applies the same transformation to the lighting layer so it
     * aligns with the cells
     * @param lightRadius The radius of the light emitting from the sources
     */
    public static void renderLighting(Affine transform, int lightRadius) {//added in lighting fix

        GraphicsContext gc = overlay.getGraphicsContext2D();

        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        ArrayList<Point2D> lights = getLightLocations();

        Affine old = new Affine(gc.getTransform());

        for (Point2D l : lights) {//creates a new radial gradient for every light source
            RadialGradient rg = new RadialGradient(0,
                    .1,
                    (l.getX() * 32) + 16,
                    (l.getY() * 32) + 16,
                    lightRadius / 2,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(255, 220, 180)),
                    new Stop(1, Color.gray(0, 0)));//smoothens out the lighting

            gc.setTransform(transform);
            gc.setFill(rg);
            gc.fillOval(l.getX() * CELL_WIDTH - lightRadius / 2 + 16,
                    l.getY() * CELL_WIDTH - lightRadius / 2 + 16, lightRadius, lightRadius);

        }
        gc.setTransform(old);
    }
    /**
     * Gets a list of all of the points on the level that emit light.
     * @return An ArrayList of points where the light is emitting from
     */
    private static ArrayList<Point2D> getLightLocations() {
        ArrayList<Point2D> lightSources = new ArrayList<Point2D>();

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j] instanceof Torch) {/*gets all of the torch cells
                 and adds them to the list of light sources*/
                    lightSources.add(new Point2D(i, j));
                }
            }
        }
        for (Collectable c : collectables) {
            if (c instanceof Lantern) {/*gets all of the lantern
            collectables and adds them to the list of light sources*/
                lightSources.add(new Point2D(c.x, c.y));
            }
        }
        if (inventory.hasLantern()) {/*If the player is holding a lantern, add
        them to the list of light sources*/
            lightSources.add(new Point2D(player.getX(), player.getY()));
        }

        return lightSources;
    }

    /**
     * Renders the player onto the screen.
     * @param gc the GraphicsContext on which to draw the player
     */
    private void renderPlayer(GraphicsContext gc) {
        player.draw(CELL_WIDTH, gc, tilemap);
    }
    
    /**
     * Renders the enemies onto the screen.
     * @param gc the GraphicsContext on which to draw the enemies
     */

    private void renderEnemies(GraphicsContext gc) {
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                enemy.draw(CELL_WIDTH, gc, tilemap);
            }
        }
    }
    /**
     * Renders the collectibles onto the screen.
     * @param gc the GraphicsContext on which to draw the Collectables
     */
    private void renderCollectables(GraphicsContext gc) {
        if (!collectables.isEmpty()) {
            for (Collectable collectable : collectables) {
                collectable.draw(CELL_WIDTH, gc, tilemap);
            }
        }
    }
    /**
     * Renders the Cells onto the screen.
     * @param gc the GraphicsContext on which to draw the Cells
     */
    private void renderCells(GraphicsContext gc) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].draw(i, j, CELL_WIDTH, gc, tilemap);
            }
        }
    }
    /**
     * Renders the hud onto the screen.
     */
    private void renderHud() {
        GraphicsContext gc = hud.getGraphicsContext2D();
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.setFill(Color.GOLD);
        gc.setFont(new Font(20));//displays the number of tokens
        if (inventory.getTokens() > 0) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[5][3], 0, 64);
        gc.fillText(String.valueOf(inventory.getTokens()), 32, 86);
        //displays the keys in the inventory
        if (inventory.getKeys().contains("YELLOWKEY")) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[0][3], 0, 0);
        if (inventory.getKeys().contains("GREENKEY")) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[1][3], 32, 0);
        if (inventory.getKeys().contains("BLUEKEY")) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[2][3], 64, 0);
        if (inventory.getKeys().contains("REDKEY")) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[3][3], 96, 0);
        //displays the boots
        if (inventory.hasFireBoots()) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[0][4], 0, 32);
        if (inventory.hasWaterBoots()) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[1][4], 32, 32);
        if (inventory.hasFlyBoots()) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[2][4], 64, 32);

        if (inventory.hasLantern()) {
            gc.setGlobalAlpha(1);
        } else {
            gc.setGlobalAlpha(0.2);
        }
        gc.drawImage(tilemap[4][3], 0, 96);
        /*displays the amount of time that the level has taken so far, as well as the
         name of the profile*/
        gc.setGlobalAlpha(1);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(20));
        gc.fillText(profile.toUpperCase(), 0, 360);
        gc.fillText(String.valueOf(LevelTimer.getCurrentTime()) + "s", 0, 380);
    }
    /**
     * Updates the game, This method is called any time the player inputs a valid button press
     */
    private void update() {
        if (!enemies.isEmpty()) {
            boolean[][] walkableCells = enemyWalkableMap();
            for (Enemy enemy : enemies) {//if the player has been killed on the turn
                if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                    FileHandling.copyFile("src/Dungeon/defaultLevels/level" + levelNumber + ".txt",
                            "src/Dungeon/profileLevels/" + profile.toUpperCase()
                            + "_level" + levelNumber + ".txt");
                    FileHandling.loadProfile(profile, maxLevelAllowed);
                    prepareLevel();
                }
                enemy.planMove(new Point(player.getX(), player.getY()), walkableCells);
                if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                    FileHandling.copyFile("src/Dungeon/defaultLevels/level" + levelNumber + ".txt",
                            "src/Dungeon/profileLevels/" + profile.toUpperCase()
                            + "_level" + levelNumber + ".txt");
                    FileHandling.loadProfile(profile, maxLevelAllowed);
                    prepareLevel();
                }
            }
        }
        if (!collectables.isEmpty()) {
        	//removes any collectables that are picked up on this move from the level and adds them to the players inventory
            ArrayList<Collectable> toBeRemoved = new ArrayList<>();
            for (Collectable collectable : collectables) {
                if (collectable.getX() == player.getX() && collectable.getY() == player.getY()) {
                    if (collectable.getType().equals("TOKEN")) {
                        inventory.addTokens(1);
                        toBeRemoved.add(collectable);
                    } else if (collectable.getType().equals("FIREBOOTS")) {
                        inventory.addFireBoots();
                        toBeRemoved.add(collectable);
                    } else if (collectable.getType().equals("WATERBOOTS")) {
                        inventory.addWaterBoots();
                        toBeRemoved.add(collectable);
                    } else if (collectable.getType().equals("FLYBOOTS")) {
                        inventory.addFlyBoots();
                        toBeRemoved.add(collectable);
                    } else if (collectable.getType().equals("REDKEY")) {
                        if (!inventory.getKeys().contains("REDKEY")) {
                            inventory.addKey("REDKEY");
                            toBeRemoved.add(collectable);
                        }
                    } else if (collectable.getType().equals("GREENKEY")) {
                        if (!inventory.getKeys().contains("GREENKEY")) {
                            inventory.addKey("GREENKEY");
                            toBeRemoved.add(collectable);
                        }
                    } else if (collectable.getType().equals("BLUEKEY")) {
                        if (!inventory.getKeys().contains("BLUEKEY")) {
                            inventory.addKey("BLUEKEY");
                            toBeRemoved.add(collectable);
                        }
                    } else if (collectable.getType().equals("YELLOWKEY")) {
                        if (!inventory.getKeys().contains("YELLOWKEY")) {
                            inventory.addKey("YELLOWKEY");
                            toBeRemoved.add(collectable);
                        }
                    } else if (collectable.getType().equals("LANTERN")) {
                        inventory.addLantern();
                        toBeRemoved.add(collectable);
                    }

                }
            }
            collectables.removeAll(toBeRemoved);
        }

    }
    /**
     * A function that determains whether or not a player can move to a location
     * @param x the X location to validate the move to
     * @param y the Y location to validate the move to
     * @return True if the player can move onto the location, False otherwise
     */
    private boolean canMove(int x, int y) {
    	//if the player has reached a goal
        if (cells[x][y] instanceof Goal) {
            if(maxLevelAllowed== levelNumber) {//increases the players progression through the levels
            	FileHandling.removeProfile(profile);
            	 FileHandling.loadProfile(profile, maxLevelAllowed+1);
            }
           //adds the player to the leaderboard if necessary 
            FileHandling.manageLeaderboard(new Record(profile,LevelTimer.getCurrentTime()), levelNumber );
            FileHandling.copyFile("src/Dungeon/defaultLevels/level" + levelNumber + ".txt",
                    "src/Dungeon/profileLevels/" + profile.toUpperCase()
                    + "_level" + levelNumber + ".txt");
            stage.close();
        }
        
        //If the player is allowed to walk onto the Cell
        if (cells[x][y].isPlayerAllowed()) {
        	//if the player requires an item to walk on the cell
            if (cells[x][y].getRequiredItem() != null) {
            	//if the player has water boots, d
                if (cells[x][y].getRequiredItem().equals("WATERBOOTS")) {
                    if (inventory.hasWaterBoots()) {
                        return true;
                    } else {//"kills" the player
                        FileHandling.copyFile("src/Dungeon/defaultLevels/level" + levelNumber + ".txt",
                                "src/Dungeon/profileLevels/" + profile.toUpperCase()
                                + "_level" + levelNumber + ".txt");
                        FileHandling.loadProfile(profile, maxLevelAllowed);
                        prepareLevel();
                        return false;
                    }

                } else if (cells[x][y].getRequiredItem().equals("FIREBOOTS")) {
                    if (inventory.hasFireBoots()) {
                        return true;
                    } else {
                        FileHandling.copyFile("src/Dungeon/defaultLevels/level" + levelNumber + ".txt",
                                "src/Dungeon/profileLevels/" + profile.toUpperCase()
                                + "_level" + levelNumber + ".txt");
                        FileHandling.loadProfile(profile, maxLevelAllowed);
                        prepareLevel();
                        return false;
                    }

                } else if (cells[x][y].getRequiredItem().equals("FLYBOOTS")) {
                    if (inventory.hasFlyBoots()) {
                        return true;
                    } else {
                        FileHandling.copyFile("src/Dungeon/defaultLevels/level" + levelNumber + ".txt",
                                "src/Dungeon/profileLevels/" + profile.toUpperCase()
                                + "_level" + levelNumber + ".txt");
                        FileHandling.loadProfile(profile, maxLevelAllowed);
                        prepareLevel();
                        return false;
                    }
                    //checks if the player has the correct number of tokens to access a door
                } else if (cells[x][y].getRequiredItem().equals("TOKENS")) {
                    TokenDoor temp = (TokenDoor) cells[x][y];
                    if (inventory.getTokens() >= temp.getTokensRequired()) {
                        cells[x][y] = new Ground();//"opens" the door by setting the tile to ground
                        return true;
                    } else {
                        return false;
                    }
                    
                    //checks if the player has the correct key to open a door
                    
                } else if (cells[x][y].getRequiredItem().equals("KEY")) {
                    ColouredDoor temp = (ColouredDoor) cells[x][y];
                    if (inventory.getKeys().contains(temp.getRequiredColour())) {
                        inventory.removeKey(temp.getRequiredColour());
                        cells[x][y] = new Ground();//"opens" the door by setting the tile to ground
                        return true;
                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
            } else {
            	//if the player has entered a teleporter
                if (cells[x][y] instanceof Teleporter) {
                    Teleporter teleporter = (Teleporter) cells[x][y];
                    //teleport the player to the destination location
                    player.setX(teleporter.getDestinationX());
                    player.setY(teleporter.getDestinationY());
                    update();
                    return false;
                } else {
                   return true;
                }
            }
        } else {
            return false;
        }
    }
/**
 * Creates an enemy walkable map using the properties of the Cell class
 * @return A two dimensional array of booleans determining if an enemy can walk on
 * each cell
 */
    private boolean[][] enemyWalkableMap() {
        boolean walkable[][] = new boolean[cells.length][cells[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j].isEnemyAllowed()) {
                    walkable[i][j] = true;
                } else {
                    walkable[i][j] = false;
                }
            }
        }
        return walkable;
    }

}
