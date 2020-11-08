package game;

/**
 * Wall Cell that does not allow movement onto it.
 *
 * @author Pedro Caetano
 * @version 1.0
 */
class Wall extends Cell {

    /**
     * Creates a Wall Cell.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    Wall(int x, int y) {
        super(x, y, false);
    }

}
