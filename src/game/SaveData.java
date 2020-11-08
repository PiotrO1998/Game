package game;

import java.io.*;

/**
 * This class creates methods for saving Object data into a file.
 *
 * @author Piotr Obara
 * @author Pedro Caetano
 * @version 1.0
 */
class SaveData {
    /**
     * Writes the Object to file.
     *
     * @param data     Object to be stored in file
     * @param fileName Name of the file
     */
    static void save(Serializable data, String fileName) {
        try (FileOutputStream f = new FileOutputStream(fileName);
             ObjectOutputStream o = new ObjectOutputStream(f)) {
            o.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the File back into an Object.
     *
     * @param fileName File name to be read
     * @return Object from a file
     */
    static Object load(String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
