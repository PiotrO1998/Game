package game;

/**
 * Target Cell that must be reached in order to advanced to the next level or win the game.
 *
 * @author Pedro Caetano
 * @version 1.0
 */
class Target extends Cell {

    /**
     * Creates a Target Cell.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    Target(int x, int y) {
        super(x, y, true);
    }
}
