package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Reads and builds a new Profile from a file.
 *
 * @author Artur Zinnurov
 * @author Piotr Obara
 * @author James Henry
 * @version 1.0
 */
public class ProfileReader {
    private final Log log = new Log(getClass().getSimpleName());
    private File profile; //The file to be read

    /**
     * Method that creates a new instance of the Profile class using information from a given file.
     *
     * @param file File to extract Profile information from
     * @return A new profile with the details specified in the passed file.
     */
    public Profile createProfile(File file) {
        //File inputFile = new File(fileName);
        Scanner in = null;
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            log.add("Failed to open file", 1);
            System.exit(-1);
        }
        Scanner readline = new Scanner(in.nextLine());
        readline.useDelimiter(",");
        String profileName = readline.next();
        int nextLevel = readline.nextInt();
        String pictureLocation = readline.next();
        in.close();
        return new Profile(profileName, nextLevel, pictureLocation, 0);
    }

    /**
     * Creates a Profile in preparation to be displayed on the Results/Leaderboard.
     *
     * @param file File to read profile from
     * @return Completed Profile
     */
    public Profile createResultsProfile(File file) {
        Scanner in = null;
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert in != null;
        Scanner readline = new Scanner(in.nextLine());
        readline.useDelimiter(",");
        String profileName = readline.next();
        int level = readline.nextInt();
        int steps = readline.nextInt();
        in.close();
        return new Profile(profileName, level, null, steps);
    }
}
