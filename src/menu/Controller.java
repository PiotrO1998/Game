package menu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main Controller class that controls the first scene of the menu. It contains 4 buttons that will go to different
 * scenes depending on what the user wants to do. It also includes in the FXML an option to display the message of the day.
 *
 * @author Artur Zinnurov
 * @author Pedro Caetano
 * @author Suad Mena Ahmadieh
 * @version 1.0
 */
public class Controller {
    private final PlayButton buttonSound = new PlayButton(60);

    /**
     * Method to change scenes from play game to start creating or reloading profiles when you click the Play Button.
     *
     * @param event Event of the user clicking the button
     * @throws IOException when the PlayGame FXML is not loading or does not exist.
     */
    @FXML
    public void playGameButton(ActionEvent event) throws IOException {
        buttonSound.playButton();
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("PlayGame.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        // This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    /**
     * When you click this button, you will directly go to the LeaderBoard scene where results are going.
     *
     * @param event As a user clicks a button, presses a key, moves a mouse, or performs other actions, eventsare dispatched.
     * @throws IOException when the LeaderBoard FXML is not loading or does not exist.
     */
    public void leaderBoard(ActionEvent event) throws IOException {
        buttonSound.playButton();
        Parent tableViewParent2 = FXMLLoader.load(getClass().getResource("Leaderboard.fxml"));
        // This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setScene(new Scene(tableViewParent2, 500, 400));
        window.show();
        stage.show();
    }

    /**
     * When you click this button, you will directly go to the Help Instructions scene where Instructions about
     * how to play the Game are displayed.
     *
     * @param event Event of the user clicking the button
     * @throws IOException when the Help FXML is not loading or does not exist.
     **/
    public void helpInstructions(ActionEvent event) throws IOException {
        buttonSound.playButton();
        Parent tableViewParent2 = FXMLLoader.load(getClass().getResource("HelpModified.fxml"));
        // This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setScene(new Scene(tableViewParent2, 600, 400));
        window.show();
        stage.show();
    }

    /**
     * When you click this button, you will directly exit the Game.
     *
     * @param event Event of the user clicking the button
     */
    public void quitButton(ActionEvent event) {
        buttonSound.playButton();
        Platform.exit();
    }
}