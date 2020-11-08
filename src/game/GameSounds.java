package game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * Contains and initialises any sound media files.
 *
 * @author Pedro Caetano
 * @version 1.0
 */
class GameSounds {
    private final File coinPickupFile = new File("src/audio/coinPickup.mp3");
    private final Media coinPickup = new Media(coinPickupFile.toURI().toString());

    private final File doorFile = new File("src/audio/door.mp3");
    private final Media door = new Media(doorFile.toURI().toString());

    private final File itemPickupFile = new File("src/audio/itemPickup.mp3");
    private final Media itemPickup = new Media(itemPickupFile.toURI().toString());

    private final File waterFile = new File("src/audio/water.mp3");
    private final Media waterCell = new Media(waterFile.toURI().toString());

    private final File windFile = new File("src/audio/wind.mp3");
    private final Media windCell = new Media(windFile.toURI().toString());

    private final File teleportStartFile = new File("src/audio/teleportStart.mp3");
    private final Media teleportStart = new Media(teleportStartFile.toURI().toString());

    private final File teleportEndFile = new File("src/audio/teleportEnd.mp3");
    private final Media teleportEnd = new Media(teleportEndFile.toURI().toString());

    private final File gameMusicFile = new File("src/audio/music.mp3");
    private final Media gameMusic = new Media(gameMusicFile.toURI().toString());

    private final File thumpFile = new File("src/audio/thump.mp3");
    private final Media thump = new Media(thumpFile.toURI().toString());

    private final File deathFile = new File("src/audio/death.mp3");
    private final Media death = new Media(deathFile.toURI().toString());

    private final File winFile = new File("src/audio/win.mp3");
    private final Media win = new Media(winFile.toURI().toString());

    /**
     * Plays a Token pickup sound.
     */
    void playTokenPickup() {
        playSound(coinPickup);
    }

    /**
     * Plays a Door opening sound.
     */
    void playDoor() {
        playSound(door);
    }

    /**
     * Plays an Item pickup sound.
     */
    void playItemPickup() {
        playSound(itemPickup);
    }

    /**
     * Plays a Rain sound.
     */
    void playWater() {
        playSound(waterCell);
    }

    /**
     * Plays a Wind sound.
     */
    void playWind() {
        playSound(windCell);
    }

    /**
     * Plays a Teleport start sound.
     */
    void playTeleportStart() {
        playSound(teleportStart);
    }

    /**
     * Plays a Teleport end sound.
     */
    void playTeleportEnd() {
        playSound(teleportEnd);
    }

    /**
     * Plays the game music.
     */
    void playGameMusic() {
        playSound(gameMusic);
    }

    /**
     * Plays a thump sound.
     */
    void playThump() {
        playSound(thump);
    }

    /**
     * Plays a death sound.
     */
    void playDeath() {
        playSound(death);
    }

    /**
     * Plays a win sound.
     */
    void playWin() {
        playSound(win);
    }


    /**
     * Plays any game interaction sounds.
     *
     * @param sound Sound to be played
     */
    private void playSound(Media sound) {
        MediaPlayer player = new MediaPlayer(sound);
        player.setCycleCount(1);
        player.play();
    }
}
