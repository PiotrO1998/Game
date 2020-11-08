package menu;

import java.io.File;

/**
 * This class contains the necessary information for the leaderboard in order to return the final results of the users.
 *
 * @author Artur Zinnurov
 * @author Suad Mena Ahmadieh
 * @author Pedro Caetano
 * @version 1.0
 */
public class GetFileList {
    /**
     * Find the directory of .profile file extension in order to return all possible file paths.
     *
     * @param dirName String path to directory
     * @return array of .profile files
     */
    public File[] finder(String dirName) {
        File dir = new File(dirName);

        return dir.listFiles((dir1, filename) -> filename.endsWith(".profile"));
    }

    /**
     * FInd the directory of .map extension
     *
     * @param dirName String path to directory
     * @return array of .map files
     */
    public File[] finderMap(String dirName) {
        File dir = new File(dirName);

        return dir.listFiles((dir1, filename) -> filename.endsWith(".map"));
    }

    /**
     * *FInd the directory of .results extension
     *
     * @param dirName String path to directory
     * @return array of .results files
     */
    public File[] finderResults(String dirName) {
        File dir = new File(dirName);
        return dir.listFiles((dir1, filename) -> filename.endsWith(".results"));
    }
}