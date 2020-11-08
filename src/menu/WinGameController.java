package menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller that contains all the buttons and actions of the WinGame FXML file that pops a window each time that the
 * user reaches the goal of the last level.
 *
 * @author Suad Mena Ahmadieh
 * @author Artur Zinnurov
 * @author Pedro Caetano
 * @version 1.0
 */
public class WinGameController {
    private final PlayButton buttonSound = new PlayButton(60);

    /**
     * When the user click the Continue button once the game has been won, he will return back to the Play Game scene,
     * to choose whether to reload a profile or create a new one.
     *
     * @param event As a user clicks a button, presses a key, moves a mouse, or performs other actions, events
     *              are dispatched.
     * @throws IOException when the PlayGame FXML file is not existing, or loading.
     */
    public void goBack(ActionEvent event) throws IOException {
        buttonSound.playButton();
        Parent tableViewParent4 = FXMLLoader.load(getClass().getResource("PlayGame.fxml"));
        Scene tableViewScene = new Scene(tableViewParent4);
        // This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
}
