package menu;

import game.Profile;
import game.ProfileReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * This class will connect the buttons of the "CreateNewProfile" FXML file with the scene that will allow the user to
 * create a new Profile selecting a user Name and a profile Picture.
 *
 * @author Artur Zinnurov
 * @author Suad Mena Ahmadieh
 * @author Pedro Caetano
 * @version 1.0
 */
public class LoginProfileController {
    private static String profileA = ""; //DO NOT TOUCH THIS
    private final PlayButton buttonSound = new PlayButton(60);
    private String imageFile;//And this
    @FXML
    private Label lblStatus;
    @FXML
    private Label imageStatus;
    @FXML
    private TextField txtUserName;
    @FXML
    private ImageView photo;
    private File filePath;
    private String mapIndex;

    /**
     * This method will allow the user to change the image on the screen. It will allow him to connect to his own
     * image folder and select any of his own images.
     *
     * @param event Event of the user clicking the button
     * @throws MalformedURLException If URL is invalid
     */
    public void chooseImageButton(ActionEvent event) throws MalformedURLException {
        buttonSound.playButton();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(imageStatus.getScene().getWindow());

        if (selectedFile != null) {

            this.imageFile = selectedFile.toURI().toURL().toString();

            Image image = new Image(imageFile);
            photo.setImage(image);

        } else {
            imageStatus.setText("Image file selection cancelled.");
        }


    }

    /**
     * This method will check if the name of the user is matched with textfield class.
     *
     * @param nameOf textfield String
     * @return array of data
     */
    private String[] checkUser(String nameOf) {
        GetFileList getFiles = new GetFileList();
        ArrayList<Profile> profileList = new ArrayList<Profile>();
        File[] fileList = getFiles.finder("src/");
        ProfileReader profileReader = new ProfileReader();
        String[] allString = new String[3];

        for (File elem : fileList) {
            Profile currentProfile = profileReader.createProfile(elem);
            profileList.add(currentProfile);
        }

        String name = "";
        String photoPath = "";
        int mapIndex = 0;
        for (Profile elem : profileList) {
            if (elem.getName().equals(nameOf) && !nameOf.equals(" ")) {
                name = elem.getName();
                mapIndex = elem.getCurrentLevel();
                photoPath = elem.getPhotoPath();
            }
        }
        allString[0] = name;
        allString[1] = Integer.toString(mapIndex);
        allString[2] = photoPath;

        return allString;
    }


    /**
     * A method that will check if the profile already exists or not and will print in a label if the login is successful
     * or not.
     *
     * @param event As a user clicks a button, presses a key, moves a mouse, or performs other actions, events
     *              are dispatched.
     * @throws IOException When the next scene of Choosing levels is not displaying or reloading.
     */
    public void login(ActionEvent event) throws IOException {
        buttonSound.playButton();

        if (!(checkUser(txtUserName.getText())[0].equals("")) && checkUser(txtUserName.getText())[0].equals(txtUserName.getText())) {

            lblStatus.setText("Login Success" + txtUserName.getText());

            String maxIndex = checkUser(txtUserName.getText())[1];
            Profile profile = new Profile(txtUserName.getText(), Integer.parseInt(maxIndex), checkUser(txtUserName.getText())[2], 0);
            this.mapIndex = maxIndex;
            profileA = txtUserName.getText();
            this.imageFile = checkUser(txtUserName.getText())[2];


            buttonSound.playButton();
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("ChooseLevel.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);
            // This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();


        } else {

            if (txtUserName.getText().trim().isEmpty()) {
                lblStatus.setText("Failed! Your line is empty");
                txtUserName.clear();


            } else {
                lblStatus.setText("New profile was created ");
                profileA = txtUserName.getText();


                Profile profile = new Profile(profileA, 1, this.imageFile, 0);
                profile.saveProfile();
            }
            //for test in future we need to make it as Map1 and change  to the choosing map
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("ChooseLevel.fxml"));

            Scene tableViewScene = new Scene(tableViewParent);
            // This line gets the Stage information
            Stage window2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window2.setScene(tableViewScene);
            window2.show();

        }
    }

    /**
     * When the user clicks the "Go back" button, this method will display the PlayGame scene where the user will be
     * able to again choose between creating a new profile or reload it.
     *
     * @param event Event of the user clicking the button
     * @throws IOException When the PLayGame FXML file is not existing or loading.
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
     * Get the level of the map.
     *
     * @return Map index
     */
    public String getMapIndex() {
        return this.mapIndex;
    }

    /**
     * Get the name of the Profile.
     *
     * @return string of player name
     */
    public String getProfileA() {
        return profileA;
    }

    /**
     * Get the file of the image.
     *
     * @return imageFile
     */
    public String getImageFile() {
        return this.imageFile;
    }
}

