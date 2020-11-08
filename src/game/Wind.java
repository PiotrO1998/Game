package game;

/**
 * Wind Cell that can kill a Player if he is not equipped properly.
 *
 * @author Pedro Caetano
 * @version 1.0
 */
class Wind extends Cell {

    /**
     * Creates a Wind Cell.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    Wind(int x, int y) {
        super(x, y, true);
    }
}
