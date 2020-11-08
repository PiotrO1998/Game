package game;

import java.io.Serializable;

/**
 * Allows time to be measured, between the start and current time.
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @version 1.0
 */
class Timer implements Serializable {
    private static final long serialVersionUID = 1L;
    private long startTime;

    /**
     * Stores the start time for the Timer.
     */
    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Gets the time that has elapsed since the start of the Timer.
     *
     * @return Seconds since start of Timer.
     */
    public long secondsElapsed() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
