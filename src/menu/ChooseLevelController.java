package menu;

import game.Game;
import game.Map;
import game.Profile;
import game.ProfileReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls the scene of choosing levels and contain a list of buttons with levels upto the highest level the profile has beat.
 * It will allow the ChooseLevel FXML file to change the scene whenever we click a button.
 *
 * @author Artur Zinnurov
 * @author Suad Mena Ahmadieh
 * @author Pedro Caetano
 * @version 1.6
 */
public class ChooseLevelController implements Initializable {
    private PlayButton buttonSound = new PlayButton(60);
    private FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("CreateNewProfile.fxml"));
    private FXMLLoader reloadLoader = new FXMLLoader(getClass().getResource("ReloadGame.fxml"));
    private FXMLLoader loginResultLoader = new FXMLLoader(getClass().getResource("CreateNewProfile.fxml"));
    private FXMLLoader reloadResultLoader = new FXMLLoader(getClass().getResource("ReloadGame.fxml"));
    @FXML
    private ImageView photo;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ComboBox<String> comboBoxDifficult;
    private int mapIndex;
    private String mapName;
    private int fov = 9;

    /**
     * Performs the initialisation the game.
     *
     * @param event button pushed
     * @throws IOException handled
     */
    public void playButton(ActionEvent event) throws IOException {
        Parent loginRoot = this.loginLoader.load();//Must not be deleted
        LoginProfileController loginProfileController = this.loginLoader.getController();

        Parent reloadController = this.reloadLoader.load();//Must not be deleted
        ReloadGameController reloadGameController = this.reloadLoader.getController();

        buttonSound.playButton();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Game game = new Game();
        GetFileList listMaps = new GetFileList();
        File[] fileList = listMaps.finderMap("src/maps");
        //     log.add((fileList[0].toString()),1);
        Map[] mapArray = new Map[fileList.length];
        for (int i = this.mapIndex; i < fileList.length; i++) {
            File file = fileList[i];
            Map newMap = new Map(file.toString());
            mapArray[i] = newMap;
        }
        Profile profile;
        if (reloadGameController.getPlayerName().equals("")) {
            profile = new Profile(loginProfileController.getProfileA(), 1, loginProfileController.getImageFile(), 0);

        } else {
            profile = new Profile(reloadGameController.getPlayerName(), 1, null, 0);
        }
        game.playGame(window, profile, mapArray, this.mapIndex, this.fov);
        window.show();
    }

    /**
     * Get the level from the user in order to put it in the combobox.
     *
     * @return Map index
     */
    private int getLevelIndex() {
        int index = 0;
        try {
            Parent loginRoot = this.loginResultLoader.load();//Must not be deleted
            LoginProfileController loginProfileController = this.loginResultLoader.getController();

            Parent reloadRoot = this.reloadResultLoader.load();//Must not be deleted
            ReloadGameController reloadGameController = this.reloadResultLoader.getController();

            GetFileList getFiles = new GetFileList();
            ProfileReader reader = new ProfileReader();
            File[] fileList = getFiles.finderResults("src/");
            for (File elem : fileList) {
                Profile currentProfile = reader.createResultsProfile(elem);
                if (reloadGameController.getPlayerName().equals(currentProfile.getName()) || loginProfileController.getProfileA().equals(currentProfile.getName())) {
                    index = currentProfile.getCurrentLevel();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return index;
    }

    /**
     * Initialise the canvas and start handling the Combo boxes.
     *
     * @param location  Location to init from
     * @param resources Resources to load
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int mapIndex = getLevelIndex();
        if (mapIndex == 0) {
            mapIndex += 1;
        }
        comboBoxDifficult.getItems().addAll("Easy", "Medium", "Hard");
        GetFileList listMaps = new GetFileList();
        File[] fileList = listMaps.finderMap("src/maps");

        for (int i = 0; i < mapIndex; i++) {
            comboBox.getItems().add(fileList[i].getName());
        }
        comboBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> setSelectedMap());
        comboBox.getSelectionModel().selectedIndexProperty().addListener((v, oldValue, newValue) -> setSelectedIndex());
        comboBoxDifficult.getSelectionModel().selectedIndexProperty().addListener((v, oldValue, newValue) -> setDifficulty());
    }

    /**
     * Set the name of the map from Combobox value event handler.
     */
    private void setSelectedMap() {
        this.mapName = comboBox.getValue();
    }

    /**
     * Set selected combobox value and store index of it.
     */
    private void setSelectedIndex() {
        for (int i = 0; i < comboBox.getItems().size(); i++) {
            if (comboBox.getItems().get(i).equals(this.mapName)) {
                this.mapIndex = i;
            }
        }
    }

    /**
     * Method which is changing the difficulty according to the event handler.
     */
    private void setDifficulty() {
        String difficulty = comboBoxDifficult.getValue();
        switch (difficulty) {
            case "Medium":
                this.fov = 6;
                break;
            case "Hard":
                this.fov = 3;
                break;
            default:
                this.fov = 9;
        }
    }
}


