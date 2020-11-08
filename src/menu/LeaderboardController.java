package menu;

import game.Profile;
import game.ProfileReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class will show and display the data of the 3 quickest times that each level has been completed in along with
 * the user profile that was used. The user will se the top 3 completion times for each level.
 *
 * @author Arthur
 * @author Suad Mena Ahmadieh
 * @author Pedro Caetano
 * @version 1.0
 */

public class LeaderboardController implements Initializable {

    final ObservableList<Profile> data = FXCollections.observableArrayList();
    @FXML
    private TableView<Profile> allData;
    @FXML
    private TableColumn<Profile, String> name;
    @FXML
    private TableColumn<Profile, Integer> steps;
    @FXML
    private TableColumn<Profile, Integer> currentLevel;

    /**
     * This method will create and associate the values of the cells and will add all the data of the getProfiles method
     * and will add it into the table matching each column with the correspondent data.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb  The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle rb) {
        //set up columns table
        name.setCellValueFactory(new PropertyValueFactory<Profile, String>("name"));
        currentLevel.setCellValueFactory(new PropertyValueFactory<Profile, Integer>("currentLevel"));
        steps.setCellValueFactory(new PropertyValueFactory<Profile, Integer>("steps"));
        getProfiles();
        allData.setItems(data);
    }

    /**
     * This method will scroll trough all the list of profiles and will add them to the list.
     *
     * @return ObservableList of Profile objects with the appropriate data.
     */
    private ObservableList<Profile> getProfiles() {
        GetFileList getFiles = new GetFileList();
        ProfileReader reader = new ProfileReader();
        File[] fileList = getFiles.finderResults("src/");

        for (File elem : fileList) {
            Profile currentProfile = reader.createResultsProfile(elem);
            data.add(currentProfile);
        }
        return FXCollections.observableArrayList();
    }
}

