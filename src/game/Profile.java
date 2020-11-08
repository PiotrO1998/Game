package game;

import menu.GetFileList;

import java.io.*;
import java.util.Hashtable;

/**
 * Profile that stores user data and level progress.
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @author Artur Zinnurov
 * @author James Henry
 * @version 3.0
 */
public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Log log = new Log(getClass().getSimpleName());
    private String name; //The name of the profile.
    private int currentLevel; //The next unlocked but uncompleted level.
    private String pictureLocation;
    private Hashtable<String, Integer> mapMoves = new Hashtable<>();
    private int steps;
    private int bestLevel = 0;

    /**
     * Creates a Profile that can be used to store user data/progress.
     *
     * @param name            Name of the user.
     * @param currentLevel    Current level being played.
     * @param pictureLocation Location Profile picture is located
     * @param steps           Steps taken by the Player
     */
    public Profile(String name, int currentLevel, String pictureLocation, int steps) {
        setName(name);
        setLevel(currentLevel);
        setPictureLocation(pictureLocation);
        setSteps(steps);
    }

    /**
     * Gets the Profiles picture path.
     *
     * @return Picture path
     */
    public String getPhotoPath() {
        return this.pictureLocation;
    }

    /**
     * Gets the name of the Profile.
     *
     * @return Profile name
     */
    public String getName() {
        return name;
    }

    /**
     * This method writes the information stored within profile to a file with the same name as the profile's name,
     * overwriting any previously existing file with the same name.
     */
    public void saveProfile() {
        GetFileList getFileList = new GetFileList();
        File[] fileList = getFileList.finder("src/");

        try {
            File file = new File("src/profile" + ((fileList.length) + 1) + ".profile");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(getName() + ",");
            out.write(getCurrentLevel() + ",");
            if (getPhotoPath() != null) {
                out.write(getPhotoPath());
            } else {
                out.write("NoPIC");
            }

            out.close();
        } catch (java.io.IOException e) {
            log.add("Error writing profile to file", 1);
        }
        /*
            Writer fileWriter = new FileWriter("/src/profile.profile" ); //Test location, remember to override this with the actual location.
            fileWriter.write(getName() + "\n");
            fileWriter.write(levelToString());
            fileWriter.close();
        */
    }

    /**
     * Gets the current level.
     *
     * @return Current level
     */
    public int getCurrentLevel() {
        return currentLevel;
    }


    /**
     * Gets number of steps profile has made.
     *
     * @return Number of steps
     */
    public int getSteps() {
        return this.steps;
    }

    /**
     * Sets the Profile name.
     *
     * @param name Name
     */
    private void setName(String name) {
        this.name = name;
    }

    /**
     * Sets number of steps
     *
     * @param steps Number of steps
     */
    private void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * Save results method.
     *
     * @param level Level being written
     * @param steps Steps for the Level
     */
    void save(int level, int steps) {
        try {
            GetFileList getFileList = new GetFileList();
            // File[] fileList = getFileList.finderResults("src/");
            File[] profileList = getFileList.finder("src/");
            File file = new File("src/results-" + (profileList.length) + "--" + (level) + ".results");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(getName() + ",");
            out.write(level + ",");
            out.write(steps + ",");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.add("Error writing profile to file", 1);
        }
    }

    /**
     * Sets the Picture path for the Profile.
     *
     * @param pictureLocation Path to picture
     */
    private void setPictureLocation(String pictureLocation) {
        this.pictureLocation = pictureLocation;
    }

    /**
     * Sets the Profile's current level.
     *
     * @param level Level being played
     */
    private void setLevel(int level) {
        this.currentLevel = level;
    }
}
