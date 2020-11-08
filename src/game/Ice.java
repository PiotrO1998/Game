package game;

/**
 * Ground Cell that allows a Character to move onto it, and will replicate the move that was used to get on it.
 *
 * @author James Henry
 * @author Pedro Caetano
 * @version 1.0
 */
class Ice extends Cell {

    /**
     * Creates an Ice Cell.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    Ice(int x, int y) {
        super(x, y, true);
    }
}
