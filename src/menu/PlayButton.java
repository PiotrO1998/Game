package menu;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * This class it is entirely for the volume and the noise that the buttons will produce when they are clicked.
 *
 * @author Artur Zinnurov
 * @author Suad Mena Ahmadieh
 * @author Pedro Caetano
 * @version 1.0
 */
class PlayButton {
    private final Double volume;

    /**
     * Constructs the noise that the buttons produce when they are clicked..
     *
     * @param volume Volume to play sound at
     */
    public PlayButton(double volume) {
        this.volume = volume;
    }

    /**
     * Method to set the specific noise that the buttons will produce.
     */
    public void playButton() {
        String musicFile = "src/audio/button.wav";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(this.volume);
        mediaPlayer.play();
    }

}
