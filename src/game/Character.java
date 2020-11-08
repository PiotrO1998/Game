package game;

import java.io.Serializable;

/**
 * A Character that can be placed on a Cell. It can move around the Map, be it through algorithms or User input
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @version 2.0
 */
abstract class Character implements Serializable {
    private static final long serialVersionUID = 1L;
    Cell currentCell;

    /**
     * Gets the X location of the Characters current Cell.
     *
     * @return X location of the Player
     */
    public int getX() {
        return currentCell.getX();
    }

    /**
     * Gets the Y location of the Characters current Cell.
     *
     * @return Y location of the Player
     */
    public int getY() {
        return currentCell.getY();
    }

    /**
     * Sets the current Cell the Character is on.
     *
     * @param cell Cell Player is standing on
     */
    void setCurrentCell(Cell cell) {
        this.currentCell = cell;
    }

    /**
     * Gets the Cell the Player is standing on.
     *
     * @return Cell that Player is on
     */
    public Cell getCell() {
        return this.currentCell;
    }
}
