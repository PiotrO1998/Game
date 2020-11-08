package menu;

import game.MessageOfTheDay;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller of the PlayGame scene that contains the buttons and the methods to change the scene to the one where a menu can be
 * created or reloaded.
 *
 * @author Artur Zinnruov
 * @author Suad Mena Ahmadieh
 * @author Pedro Caetano
 * @version 1.0
 */
public class PlayGameController implements Initializable {
    private final MessageOfTheDay message = new MessageOfTheDay();
    private final PlayButton buttonSound = new PlayButton(60);

    /**
     * Initializable and the initialize method are used when we want to interact with stuff injected with @FXML.
     * During construction those variables aren't filled so we cannot interact with them so JavaFX will call the
     * Initialization interface after everything is set up.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * When the user clicks this button, it will directly change the scene to the one where the user can creates a new
     * profile. It will reload the CreateNewProfile FXML file and it will change the scene.
     *
     * @param event As a user clicks a button, presses a key, moves a mouse, or performs other actions, events
     *              are dispatched.
     * @throws IOException when the FXML CreateNewProfile is not loading or existing.
     */
    public void createNewProfile(ActionEvent event) throws IOException {
        buttonSound.playButton();
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("CreateNewProfile.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        // This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * When you click the Reload profile button, the scene will change to the one that will allow the user to enter
     * its details and reload his profile.
     *
     * @param event As a user clicks a button, presses a key, moves a mouse, or performs other actions, events
     *              are dispatched.
     * @throws IOException whenever the FXML file ReloadGame is not working or existing.
     */
    public void reloadProfile(ActionEvent event) throws IOException {
        buttonSound.playButton();
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("ReloadGame.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        // This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();

    }

    /**
     * When the user clicks the "Go back" button, the scene will change to the main menu where the corresponds buttons
     * and the message of the day will be displayed.
     *
     * @param event As a user clicks a button, presses a key, moves a mouse, or performs other actions, events
     *              are dispatched.
     * @throws IOException When the main menu FXML file is not reloading or existing.
     */

    public void goBack(ActionEvent event) throws IOException {
        buttonSound.playButton();
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Label lblData = (Label) tableViewParent.lookup("#messageofday");
        lblData.setText(message.getMessage());
        lblData.prefWidthProperty().bind(tableViewScene.widthProperty());
        // This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();

    }
}
