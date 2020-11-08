package game;

/**
 * Ground Cell that allows a Character to move onto it, and can hold Items to be collected.
 *
 * @author Pedro Caetano
 * @version 1.0
 */
class Ground extends Cell {
    /**
     * Creates a Ground cell.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    Ground(int x, int y) {
        super(x, y, true);
    }
}
