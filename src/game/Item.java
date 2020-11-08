package game;

import java.io.Serializable;

/**
 * Items that can be interacted with throughout the game.
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @version 1.0
 */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private Cell currentCell;

    /**
     * Get X location.
     *
     * @return X location of the cell
     */
    public int getX() {
        return this.currentCell.getX();
    }


    /**
     * Get Y location.
     *
     * @return Y location of the cell
     */
    public int getY() {
        return this.currentCell.getY();
    }

    /**
     * Sets the Cell the Item is on.
     *
     * @param currentCell Cell Item is on
     */
    void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }
}


