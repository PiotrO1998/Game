package game;

public class SnowGround extends Cell {
    /**
     * Abstract class Cell. Represents a space within the Map
     *
     * @param x          X location of the Cell
     * @param y          Y location of the Cell
     */
    SnowGround(int x, int y) {
        super(x, y, true);
    }
}
