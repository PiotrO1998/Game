package game;

/**
 * A Door Cell that can be unlocked to progress through the level.
 *
 * @author Pedro Caetano
 * @version 1.0
 */
abstract class Door extends Cell {

    /**
     * Abstract class Cell. Represents a Door within the Map.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    Door(int x, int y) {
        super(x, y, false);
    }

    /**
     * Abstract method for opening/unlocking a Door.
     *
     * @param player Player trying to unlock the Door
     */
    public abstract void open(Player player);
}
