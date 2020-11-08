package game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

/**
 * Logging class, created to allow granular control over what gets outputted to the console.
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @version 3.1
 */
public class Log implements Serializable {
    private int importance = 1;
    private String module;
    private String completeLog = "";
    private boolean timeStamps = false;

    /**
     * Initialises a basic Log object.
     *
     * @param module Name of Class/Module calling the log
     */
    public Log(String module) {
        setModule(module);
    }

    /**
     * Initialises a Log object with time stamping enabled.
     *
     * @param module     Name of Class/Module calling the log
     * @param timeStamps Whether timestamps are enabled
     */
    public Log(String module, boolean timeStamps) {
        setModule(module);
        setTimeStamps(timeStamps);
    }

    /**
     * Sets whether the log will output with timestamps.
     *
     * @param timeEnabled Whether timestamps are enabled
     */
    private void setTimeStamps(Boolean timeEnabled) {
        this.timeStamps = timeEnabled;
    }

    /**
     * Adds a log message to the console if it is important enough.
     *
     * @param message Message to be added to the console
     * @param level   Importance level of the message
     */
    public void add(String message, int level) {
        String output = "";
        if (level >= this.importance) {
            if (timeStamps) {
                output += System.currentTimeMillis() + " : ";
            }
            output += module + ": " + message;
            completeLog += output + "\n";
            System.out.println(output);
        }
    }

    /**
     * Adds a log message to the console if it is important enough.
     * Default = 0
     *
     * @param message Message to be added to the console
     */
    public void add(String message) {
        int level = 0;
        if (level >= this.importance) {
            System.out.println(module + ": " + message);
        }
    }

    /**
     * Sets the minimum importance for messages to be displayed.
     *
     * @param level Minimum importance of the message
     */
    public void setImportance(int level) {
        this.importance = level;
    }

    /**
     * Outputs the accumulated log to file.
     */
    public void saveLog() {
        String fileName = module + ".log";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(completeLog);

            writer.close();
        } catch (IOException e) {
            this.add("IO Exception when writing to file", 1);
        }
    }

    /**
     * Sets the Class/Module name for the log.
     *
     * @param module Class/Module name
     */
    private void setModule(String module) {
        this.module = module;
    }
}