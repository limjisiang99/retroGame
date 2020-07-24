package Dungeon;

import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
/**
 * Manages the menu system
 * @author 985803,987854
 *
 */
public class Controller extends Game {

    @FXML
    private TextField nameTextField;
    @FXML
    private Button levelOne;
    @FXML
    private Button levelTwo;
    @FXML
    private Button levelThree;
    @FXML
    private Button levelFour;

    /**
     * Loads the scene for the title screen.
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    public void launchTitleScreen() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("TitlePage.fxml"));
        stage.setScene(new Scene(parent, 790, 500));
        stage.setResizable(false);
        stage.setTitle(MOTD.getMOTD());
        stage.show();
    }

    /**
     * Loads the scene for the login screen.
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    private void launchLogin(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(parent, 500, 450));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * When the login button is pressed.
     * @param event The Event driven by a button press
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    private void login(ActionEvent event) throws IOException {
        profile = nameTextField.getText();
        profile = profile.replaceAll("[^a-zA-Z]+", "");
        if (!profile.equals("")) {
            FileHandling.loadProfile(profile, maxLevelAllowed);
            launchMenu(event);
        }
    }

    /**
     * Loads the scene for the menu screen.
     * @param event The Event driven by a button press
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    public void launchMenu(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(parent, 500, 450));
        stage.setResizable(false);
        stage.show();

    }
    /**
     * Quits the game.
     * @param event The Event driven by a button press
     */
    @FXML
    private void quitGame(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Loads the scene for the level select screen.
     * @throws IOException If the fxml file cannot be loaded
     */
    
    @FXML
    private void launchLevelScreen(ActionEvent event) throws IOException {

        if (event.getSource().equals(levelOne)) {
            levelNumber = 1;
        } else if (event.getSource().equals(levelTwo)) {
            levelNumber = 2;
        } else if (event.getSource().equals(levelThree)) {
            levelNumber = 3;
        } else if (event.getSource().equals(levelFour)) {
            levelNumber = 4;
        }

        if (levelNumber <= maxLevelAllowed) {
            Parent parent = FXMLLoader.load(getClass().getResource("Level.fxml"));
            stage.setTitle("Level " + levelNumber);
            stage.setScene(new Scene(parent, 500, 450));
            stage.setResizable(false);
            stage.show();
        }

    }
  /**
 * Deletes the current profile.
 * @param event The Event driven by a button press
 * @throws IOException If the fxml file cannot be loaded
 */
    @FXML
    public void deleteProfile(ActionEvent event) throws IOException {
        FileHandling.removeProfile(profile);
        launchLogin(event);
    }

    /**
     * Loads the scene for the level 1 leaderboard screen.
     * @param event The Event driven by a button press
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    public void launchLeaderboard1View(ActionEvent event) throws IOException {

        ListView<String> listView = new ListView<>();
        Button button = new Button("Back");
        String[] leaderboard = FileHandling.getLeaderboard(1);//loads in the leaderboard

        for (String string : leaderboard) {
            if (string != null) {

                String tempString[] = string.split(",");//formats the leaderboard entries 
                String formattedString = tempString[0] + "  -  " + tempString[1] + "s";

                listView.getItems().add(formattedString);
            }
        }

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        button.setOnAction(e -> {
            try {
                launchLeaderboard(event);
            } catch (IOException ex) {

            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.getChildren().addAll(listView, button);
        stage.setScene(new Scene(vbox, 500, 500));
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Loads the scene for the level 2 leaderboard screen.
      * @param event The Event driven by a button press
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    public void launchLeaderboard2View(ActionEvent event) throws IOException {

        ListView<String> listView = new ListView<>();
        Button button = new Button("Back");
        String[] leaderboard = FileHandling.getLeaderboard(2);

        for (String string : leaderboard) {
            if (string != null) {
                listView.getItems().add(string);
            }
        }

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        button.setOnAction(e -> {
            try {
                launchLeaderboard(event);
            } catch (IOException ex) {

            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.getChildren().addAll(listView, button);
        stage.setScene(new Scene(vbox, 500, 500));
        stage.setResizable(false);
        stage.show();

    }
    /**
     * Loads the scene for the level 3 leaderboard screen.
      * @param event The Event driven by a button press
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    public void launchLeaderboard3View(ActionEvent event) throws IOException {

        ListView<String> listView = new ListView<>();
        Button button = new Button("Back");
        String[] leaderboard = FileHandling.getLeaderboard(3);

        for (String string : leaderboard) {
            if (string != null) {
                listView.getItems().add(string);
            }
        }

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        button.setOnAction(e -> {
            try {
                launchLeaderboard(event);
            } catch (IOException ex) {

            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.getChildren().addAll(listView, button);
        stage.setScene(new Scene(vbox, 500, 500));
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Loads the scene for the level 4 leaderboard screen.
     * @param event The Event driven by a button press
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    public void launchLeaderboard4View(ActionEvent event) throws IOException {

        ListView<String> listView = new ListView<>();
        Button button = new Button("Back");
        String[] leaderboard = FileHandling.getLeaderboard(4);

        for (String string : leaderboard) {
            if (string != null) {
                listView.getItems().add(string);
            }
        }

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        button.setOnAction(e -> {
            try {
                launchLeaderboard(event);
            } catch (IOException ex) {

            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.getChildren().addAll(listView, button);
        stage.setScene(new Scene(vbox, 500, 500));
        stage.setResizable(false);
        stage.show();

    }


    /**
     * Loads the scene for the leaderboard screen
     * @param event The Event driven by a button press
     * @throws IOException If the fxml file cannot be loaded
     */
    @FXML
    public void launchLeaderboard(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("Leaderboard.fxml"));
        stage.setScene(new Scene(parent, 500, 450));
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Loads the game when a new game is launched
     * @param event The Event driven by a button press
     */
    @FXML
    public void launchGameNew(ActionEvent event) {
        startGame(stage, false);

    }
    /**
     * Loads the game when a game from a save file is launched
     * @param event The Event driven by a button press
     */
    @FXML
    public void launchGameSave(ActionEvent event) {
        startGame(stage, true);
    }

}
