package game;

/**
 * This class represent IceCubes
 *
 * @author Piotr Obara
 * @version 1.0
 */
public class IceCubes extends Cell {
    /**
     * Abstract class Cell. Represents a space within the Map
     *
     * @param x          X location of the Cell
     * @param y          Y location of the Cell
     */
    IceCubes(int x, int y) {
        super(x, y, false);
    }
}
