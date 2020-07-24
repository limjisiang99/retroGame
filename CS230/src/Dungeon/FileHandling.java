package Dungeon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * The class responsible for loading and saving any file related to the Game
 * @author 985803,1902626,975359	
 */
class FileHandling {
	/**
	 * Loads the tilemap and splits it into a 2 dimensional array of images
	 * @param tilemapWidth The width of the map
	 * @param tilemapHeight The height of the map
	 * @param tileWidth The width of each tile on the map
	 * @param filepath The path of the tilemap file
	 * @return A two dimensional array of images
	 */
    static Image[][] loadImages(int tilemapWidth, int tilemapHeight, int tileWidth, String filepath) {
        Image[][] images = new Image[tilemapWidth][tilemapHeight];
        Image imageToLoad = new Image(filepath);
        for (int i = 0; i < tilemapWidth; i++) {//cycles through every 32x32 area in the image
            for (int j = 0; j < tilemapHeight; j++) {
                images[i][j] = new WritableImage(imageToLoad.getPixelReader(),
                		i * tileWidth, j * tileWidth, tileWidth, tileWidth);/*Creates a smaller image
                		 using for the array at the specified location*/
            }
        }
        return images;
    }

    
    /**
     * Loads the level from a save file
     * @param profile The username of the player
     * @param level The level number to be loaded
     * @return The level that has been opened
     */
    static Level loadLevel(String profile, int level) {

        try {
            Cell[][] cells;
            ArrayList<Enemy> enemies = new ArrayList<>();
            ArrayList<Collectable> collectables = new ArrayList<>();
            Player player;

            FileReader reader = new FileReader("src/Dungeon/profileLevels/"
                    + profile.toUpperCase() + "_level" + level + ".txt");
            BufferedReader buffer = new BufferedReader(reader);

            String line = buffer.readLine();

            Double timer = Double.valueOf(line);

            Inventory inventory = new Inventory();

            line = buffer.readLine();

            String lineArray[] = line.split(",");

            if (Boolean.valueOf(lineArray[0])) {
                inventory.addFireBoots();
            }
            if (Boolean.valueOf(lineArray[1])) {
                inventory.addWaterBoots();
            }
            if (Boolean.valueOf(lineArray[2])) {
                inventory.addFlyBoots();
            }
            if (Boolean.valueOf(lineArray[3])) {
                inventory.addLantern();
            }

            inventory.addTokens(Integer.valueOf(lineArray[4]));

            line = buffer.readLine();

            if (line != null) {
                lineArray = line.split(",");

                for (int i = 0; i < lineArray.length; i++) {
                    inventory.addKey(lineArray[i]);
                }
            }
            line = buffer.readLine();
            int mapWidth = Integer.valueOf(line.split(",")[0]);
            int mapHeight = Integer.valueOf(line.split(",")[1]);
            cells = new Cell[mapWidth][mapHeight];
            int rowNumber = 0;
            while (rowNumber < mapHeight) {//reads each tile within the map
                line = buffer.readLine();
                String[] row = line.split(",");
                for (int i = 0; i < row.length; i++) {
                    if (row[i].equals("#")) {
                        cells[i][rowNumber] = new Wall();
                    } else if (row[i].equals("g")) {
                        cells[i][rowNumber] = new Goal();
                    } else if (row[i].equals("-")) {
                        cells[i][rowNumber] = new Ground();
                    } else if (row[i].equals("v")) {
                        cells[i][rowNumber] = new Void();
                    } else if (row[i].equals("w")) {
                        cells[i][rowNumber] = new Water();
                    } else if (row[i].equals("f")) {
                        cells[i][rowNumber] = new Fire();
                    } else if (row[i].equals("h")) {
                        cells[i][rowNumber] = new Hole();
                    } else if (row[i].equals("l")) {
                        cells[i][rowNumber] = new Torch();
                    } else if (row[i].equals("dr")) {
                        cells[i][rowNumber] = new ColouredDoor("REDKEY");
                    } else if (row[i].equals("dg")) {
                        cells[i][rowNumber] = new ColouredDoor("GREENKEY");
                    } else if (row[i].equals("db")) {
                        cells[i][rowNumber] = new ColouredDoor("BLUEKEY");
                    } else if (row[i].equals("dy")) {
                        cells[i][rowNumber] = new ColouredDoor("YELLOWKEY");
                    } else if (row[i].contains("dt")) {
                        int tokens = Integer.valueOf(row[i].substring(2, row[i].length()));
                        cells[i][rowNumber] = new TokenDoor(tokens);
                    } else if (row[i].contains("t")) {
                        String[] temp = row[i].split("/");
                        cells[i][rowNumber] = new Teleporter(Integer.valueOf(temp[1]), Integer.valueOf(temp[2]));
                    }
                }
                rowNumber++;
            }

            line = buffer.readLine();
            player = new Player(Integer.valueOf(line.split(",")[1]), Integer.valueOf(line.split(",")[2]));

            while ((line = buffer.readLine()) != null) {//reads in the entities from the file
                lineArray = line.split(",");
                String entity = lineArray[0];
                int x = Integer.valueOf(lineArray[1]);
                int y = Integer.valueOf(lineArray[2]);
                if (entity.equals("BF")) {
                    collectables.add(new Boots(x, y, "FIREBOOTS"));
                } else if (entity.equals("BW")) {
                    collectables.add(new Boots(x, y, "WATERBOOTS"));
                } else if (entity.equals("BH")) {
                    collectables.add(new Boots(x, y, "FLYBOOTS"));
                } else if (entity.equals("KR")) {
                    collectables.add(new Key(x, y, "REDKEY"));
                } else if (entity.equals("KG")) {
                    collectables.add(new Key(x, y, "GREENKEY"));
                } else if (entity.equals("KB")) {
                    collectables.add(new Key(x, y, "BLUEKEY"));
                } else if (entity.equals("KY")) {
                    collectables.add(new Key(x, y, "YELLOWKEY"));
                } else if (entity.equals("T")) {
                    collectables.add(new Token(x, y));
                } else if (entity.equals("L")) {
                    collectables.add(new Lantern(x, y));
                } else if (entity.equals("ESM")) {
                    enemies.add(new SmartEnemy(x, y));
                } else if (entity.equals("EST")) {
                    enemies.add(new StraightEnemy(x, y, Integer.valueOf(lineArray[3])));
                } else if (entity.equals("ED")) {
                    enemies.add(new DumbEnemy(x, y));
                } else if (entity.equals("EW")) {
                    enemies.add(new WallEnemy(x, y, Boolean.valueOf(lineArray[3]), Integer.valueOf(lineArray[4])));
                }
            }
            buffer.close();
            return new Level(player, enemies, collectables, cells, inventory, timer);
        } catch (IOException ex) {
            System.exit(0);
            return null;
        
        }
       
    }
    /**
     * Loads in the profile from their username and maximum level allowed.
     * @param profilename The username of the player
     * @param maxAllowed The maximum level the user is allowed to play
     * @return True if the profile loads successfully
     */
    static void loadProfile(String profilename, int maxAllowed) {
        try {
            FileReader reader = new FileReader("src/Dungeon/profiles.txt");
            BufferedReader buffer = new BufferedReader(reader);
            boolean exists = false;
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] split = line.split(",");
                if (split[0].equals(profilename.toUpperCase())) {
                    exists = true;
                    Game.maxLevelAllowed = Integer.valueOf(split[1]);
                }
            }
            buffer.close();
            if (!exists)  {
                FileWriter writer = new FileWriter("src/Dungeon/profiles.txt", true);
                writer.append(profilename.toUpperCase() + "," + maxAllowed + "\n");
                writer.flush();
                writer.close();
                copyFile("src/Dungeon/defaultLevels/level1.txt",
                        "src/Dungeon/profileLevels/" + profilename.toUpperCase() + "_level1.txt");
                copyFile("src/Dungeon/defaultLevels/level2.txt",
                        "src/Dungeon/profileLevels/" + profilename.toUpperCase() + "_level2.txt");
                copyFile("src/Dungeon/defaultLevels/level3.txt",
                        "src/Dungeon/profileLevels/" + profilename.toUpperCase() + "_level3.txt");
                copyFile("src/Dungeon/defaultLevels/level4.txt",
                        "src/Dungeon/profileLevels/" + profilename.toUpperCase() + "_level4.txt");
            }

        } catch (IOException ex) {

        }
    }

    /**
     * Removes the profile belonging to the user with the username profileName
     * @param profileName The name of the profile to be deleted
     */
    static void removeProfile(String profileName) {

        try {
            FileReader reader = new FileReader("src/Dungeon/profiles.txt");
            BufferedReader buffer = new BufferedReader(reader);
            boolean exists = false;
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.equals(profileName.toUpperCase()+","+Game.maxLevelAllowed)) {
                    exists = true;//Checks if the profile exists within the profiles file
                }
            }
            buffer.close();
            reader.close();
            if (exists) {
            	String lineToRemove = profileName.toUpperCase() +"," +Game.maxLevelAllowed;
            	File inputFile = new File("src/Dungeon/profiles.txt");
            	File tempFile = new File("src/Dungeon/pTemp.txt");
            	BufferedReader r2 = new BufferedReader(new FileReader(inputFile));
            	BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            	String currentLine;
            	while((currentLine = r2.readLine()) != null) {
            	    // trim newline when comparing with lineToRemove
            	    String trimmedLine = currentLine.trim();
            	    if(trimmedLine.equals(lineToRemove)) continue;
            	    writer.write(currentLine + System.getProperty("line.separator"));
            	}
            	writer.close(); 
            	r2.close(); 
            	inputFile.delete();//Swaps the new file with the old file
            	tempFile.renameTo(new File("src/Dungeon/profiles.txt"));
            	File levelFile = new File("src/Dungeon/profileLevels/"+ profileName.toUpperCase()+"_level1.txt");//Removes all saves by that profile
            	levelFile.delete();
            	levelFile = new File("src/Dungeon/profileLevels/"+ profileName.toUpperCase()+"_level2.txt");
            	levelFile.delete();
            	levelFile = new File("src/Dungeon/profileLevels/"+ profileName.toUpperCase()+"_level3.txt");
            	levelFile.delete();
            	levelFile = new File("src/Dungeon/profileLevels/"+ profileName.toUpperCase()+"_level4.txt");
            	levelFile.delete();
            	
            }
            

        } catch (IOException ex) {
        	
        }

    }
    /**
     * Copies a file from a source to a destination
     * @param source The source file to copy
     * @param output The destination of the copied file
     */
    static void copyFile(String source, String output) {
        try (FileReader fr = new FileReader(source);
                FileWriter fw = new FileWriter(output)) {
            int c = fr.read();
            while (c != -1) {
                fw.write(c);
                c = fr.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves and closes the level
     * @param profile The profile currently running the level
     * @param levelNumber The number of the current level
     * @param cells All of the cells contained within the level
     * @param inventory The players inventory
     * @param enemies The enemies currently in the level
     * @param collectables The collectables currently in the level
     * @param player The player (Entity) who is currently in the level
     * @param timer The amount of time in seconds that this level has been played for
     */
    static void saveClose(String profile, int levelNumber, Cell[][] cells, Inventory inventory, ArrayList<Enemy> enemies, ArrayList<Collectable> collectables, Player player, double timer) {
        try {
            FileWriter writer = new FileWriter("src/Dungeon/profileLevels/"
                    + profile.toUpperCase() + "_level" + levelNumber + ".txt", false);

            writer.append(timer + "\n");
            //Outputs all of the collectibles that aren't keys in the players inventory
            writer.append(String.valueOf(inventory.hasFireBoots()) + ","
                    + String.valueOf(inventory.hasWaterBoots()) + ","
                    + String.valueOf(inventory.hasFlyBoots()) + ","
                    + String.valueOf(inventory.hasLantern()) + ","
                    + inventory.getTokens() + "\n"
            );
            String temp = "";
            //Outputs all the keys in the players inventory
            if (!inventory.getKeys().isEmpty()) {

                for (String key : inventory.getKeys()) {
                    temp = temp + key;
                }
                temp = temp.substring(0, temp.length());
            }

            writer.append(temp + "\n");

            writer.append(cells.length + "," + cells[0].length + "\n");
            for (int x = 0; x < cells[0].length; x++) {/*Cycles through all
            of the cells in the level and outputs them to the file*/
                String row = "";
                for (int y = 0; y < cells.length; y++) {
                    row = row + cells[y][x].toFile() + ",";
                }
                row = row.substring(0, row.length() - 1);
                writer.append(row + "\n");
            }
            writer.append(player.toFile() + "\n");
            //Outputs the properties of any enemies that are currently in the game
            if (!enemies.isEmpty()) {
                for (Enemy enemy : enemies) {
                    writer.append(enemy.toFile() + "\n");
                }
            }
            
            /*Outputs the properties of any Collectables that are not currently
            In the players inventory*/
            if (!collectables.isEmpty()) {
                for (Collectable collectable : collectables) {
                    writer.append(collectable.toFile() + "\n");
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException ex) {

        }

    }
    /**
     * The function responsible for loading and managing the leaderboard
     */
    static void manageLeaderboard(Record record, int level) {
        ArrayList<Record> leaderboard = new ArrayList<>();
        try {

            FileReader reader = new FileReader("src/Dungeon/leaderboard" + level + ".txt");
            BufferedReader buffer = new BufferedReader(reader);
            String line;
            while ((line = buffer.readLine()) != null) {
                leaderboard.add(new Record(line.split(",")[0], Double.valueOf(line.split(",")[1])));
            }

            leaderboard.add(record);
            //checks if the record has broken any previous records
            leaderboard.sort((o1, o2) -> Double.compare(o1.getTime(), o2.getTime()));
            

            FileWriter writer = new FileWriter("src/Dungeon/leaderboard" + level + ".txt", false);
            //rewrites the leaderboard
            for (int i = 0; i < leaderboard.size() && i < 3; i++) {
                writer.append(leaderboard.get(i).getName() + "," + leaderboard.get(i).getTime() + "\n");
            }
            buffer.close();
            writer.flush();
            writer.close();
        } catch (IOException ex) {

        }
    }
/**
 * Method to get the current entries on the leaderboard
 * @param level The level for which the leaderboard should be displayed for
 * @return The arraylist of Strings that represent
 */
    static String[] getLeaderboard(int level) {
        String[] leaderboard = new String[3];
        try {
        	//reads in the leaderboard
            FileReader reader = new FileReader("src/Dungeon/leaderboard" + level + ".txt");
            BufferedReader buffer = new BufferedReader(reader);
            leaderboard[0] = buffer.readLine();
            leaderboard[1] = buffer.readLine();
            leaderboard[2] = buffer.readLine();
            buffer.close();
        } catch (IOException ex) {

        }
        
        return leaderboard;
    }

}

/**
 * A class created to store individual records for a leaderboard entry
 * @author 985803	
 */
class Record {
    private String name;
    private double time;
    /**
     * The constructor for a record
     * @param name The name of the profile who this record belongs to
     * @param time The time in which this record was done 
     */
    public Record(String name, double time) {
        this.time = time;
        this.name = name;
    }
    /**
     * Gets the name from the record
     * @return The name from the record
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the amount of time that the record took
     * @return The amount of time that the record took
     */
    public double getTime() {
        return time;
    }

}

/**
 * A class that is used to store a level in
 * @author 985803
 */
class Level {

    ArrayList<Enemy> enemies;
    ArrayList<Collectable> collectables;
    Cell[][] cells;
    Player player;
    Inventory inventory;
    double timer;
    /**
     * The constructor for a level
     * @param player The player in the level
     * @param enemies A list of all of the enemies in the level
     * @param collectables A list of all of the collectables in the level
     * @param cells The two dimensional array that stores every Cell in the level
     * @param inventory The players inventory
     * @param timer The time in seconds that the level was paused on
     */
    public Level(Player player, ArrayList<Enemy> enemies, ArrayList<Collectable> collectables, Cell[][] cells, Inventory inventory, double timer) {
        this.player = player;
        this.enemies = enemies;
        this.collectables = collectables;
        this.cells = cells;
        this.inventory = inventory;
        this.timer = timer;
    }
    /**
     * Gets all of the enemies in the level
     * @return an ArrayList of enemies representing all of the enemies in the level
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
    /**
     * Gets the time that the level was paused on
     * @return A double representing the number of seconds that the level was paused on
     */
    public double getTimer() {
        return timer;
    }
    /**
     * Gets the players inventory
     * @return The players inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
    /**
     * Gets all of the collectables in the level
     * @return an ArrayList of collectables representing all of the collectables in the level
     */
    public ArrayList<Collectable> getCollectables() {
        return collectables;
    }

    /**
     * Gets all of the Cells contained within the level
     * @return a two dimensonal array of all of the cells in the level
     */
    public Cell[][] getCells() {
        return cells;
    }
    
    /**
     * Gets the player in the level.
     * @return The player in the level
     */
    Player getPlayer() {
        return player;
    }
}
