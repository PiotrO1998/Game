package menu;

import game.Log;
import game.Map;
import game.Profile;
import game.ProfileReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * When the user gets to the ReloadGame FXML file, he will be able to introduce his login details,
 * the user name in this case, and if they are correct and the profile is already existing,he will go to a scene where
 * he can chooses the level of the game.
 *
 * @author Suad Mena Ahmadieh
 * @author Artur Zinnurov
 * @author Pedro Caetano
 * @version 1.0
 */
public class ReloadGameController implements Initializable {
    private static String playerName = ""; //AND DO NOT TOUCH THIS as well
    private final Log log = new Log(getClass().getSimpleName());
    private final PlayButton buttonSound = new PlayButton(60);
    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtUserName;

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
     * If the user clicks the the goBack Button it will return back to the PlayGame scene using the Play Game FXML file.
     *
     * @param event As a user clicks a button, presses a key, moves a mouse, or performs other actions, events
     *              are dispatched.
     * @throws IOException when you cannot access the PlayGame FXML file.
     */
    public void goBack(ActionEvent event) throws IOException {
        buttonSound.playButton();
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("PlayGame.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        // This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * If the user clicks the continue button, then it will take the profileList to check if the profile is already there.
     * If it is not there, it will throw a message in a label saying Login Failed, if it is there then it will be
     * successful and it will go to a new scene where the user can choose whether to go to a previous level or a more
     * difficult level.
     *
     * @param event As a user clicks a button, presses a key, moves a mouse, or performs other actions, events
     *              are dispatched.
     * @throws IOException When you cannot access the next scene of Choosing which level.
     */
    public void continueButton(ActionEvent event) throws IOException {
        buttonSound.playButton();
        ArrayList<Profile> profileList = new ArrayList<Profile>();
        ArrayList<Map> mapArrayList = new ArrayList<>();
        ProfileReader profileReader = new ProfileReader();

        ArrayList<File> files = new ArrayList<>();
        GetFileList getFiles = new GetFileList();
        File[] fileList = getFiles.finder("src/");

        for (File elem : fileList) {
            log.add(elem.getAbsolutePath(), 1);
            Profile currentProfile = profileReader.createProfile(elem);
            profileList.add(currentProfile);
        }
        boolean profileFound = false;
        String name = "";
        int mapIndex = 0;
        for (Profile elem : profileList) {
            if (elem.getName().equals(txtUserName.getText())) {
                profileFound = true;
                name = elem.getName();
                mapIndex = elem.getCurrentLevel();
            }
        }
        if (profileFound) {
            lblStatus.setText("Login Success");
            playerName = txtUserName.getText();

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("ChooseLevel.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            Stage window2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window2.setScene(tableViewScene);
            window2.show();

        } else {
            lblStatus.setText("Login Failed");
        }
    }

    /**
     * Name of the player to pass to the next scene.
     *
     * @return String name
     */
    public String getPlayerName() {
        return playerName;
    }
}
