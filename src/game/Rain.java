package game;

/**
 * Rain Cell that can kill a Player if he is not equipped properly.
 *
 * @author Pedro Caetano
 * @version 1.0
 */
class Rain extends Cell {

    /**
     * Creates a Rain Cell.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    Rain(int x, int y) {
        super(x, y, true);
    }
}
