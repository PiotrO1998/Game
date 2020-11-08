package menu;

import game.MessageOfTheDay;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * This is the main class where one of the 2 stages of the game is going to be created with its own title, icon, size,etc.
 *
 * @author Artur Zinnurov
 * @author Suad Mena Ahmadieh
 * @author Pedro Caetano
 * @version 3.0
 */
public class Main extends Application {
    private static MediaPlayer musicPlayer;

    /**
     * Main method to start and display the whole menu.
     *
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Label lblData = (Label) root.lookup("#MOTD");
        lblData.setWrapText(true);
        lblData.setMaxWidth(550);
        lblData.setTextAlignment(TextAlignment.CENTER);
        //MessageOfTheDay testMessage = new MessageOfTheDay();
        //lblData.setText(testMessage.toString());
        Scene scene = new Scene(root);
        lblData.prefWidthProperty().bind(scene.widthProperty());

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpg")));

        primaryStage.setTitle("SWANGAME");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
