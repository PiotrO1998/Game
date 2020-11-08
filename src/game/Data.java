package game;

import java.io.Serializable;

/**
 * This class represent data that will be stored in file, for saving and loading profile.
 *
 * @author Piotr Obara
 * @author Pedro Caetano
 * @version 1.0
 */
class Data implements Serializable {
    private Cell[][] map;
    private int fov;
    private int gridCellWidth;
    private int gridCellHeight;
    private Map[] mapsArray;
    private int levelNumber;

    /**
     * Gets map of Data.
     *
     * @return map of Data.
     */
    public Cell[][] getMap() {
        return this.map;
    }

    /**
     * Sets map to Data.
     *
     * @param map Cell[][] array to be set.
     */
    public void setMap(Cell[][] map) {
        this.map = map;
    }

    /**
     * Gets FOV of Data.
     *
     * @return FOV.
     */
    int getFov() {
        return this.fov;
    }

    /**
     * Sets FOV of Data.
     *
     * @param fov to be set.
     */
    void setFov(int fov) {
        this.fov = fov;
    }

    /**
     * Gets GRID_CELL_WIDTH of Data.
     *
     * @return GRID_CELL_WIDTH of Data.
     */
    int getGridCellWidth() {
        return this.gridCellWidth;
    }

    /**
     * Sets GRID_CELL_WIDTH of Data.
     *
     * @param gridCellWidth value to be set.
     */
    void setGridCellWidth(int gridCellWidth) {
        this.gridCellWidth = gridCellWidth;
    }

    /**
     * Gets GRID_CELL_HEIGHT of Data.
     *
     * @return GRID_CELL_HEIGHT of Data.
     */
    int getGridCellHeight() {
        return this.gridCellHeight;
    }

    /**
     * Sets GRID_CELL_HEIGHT of Data.
     *
     * @param gridCellHeight value to be set.
     */
    void setGridCellHeight(int gridCellHeight) {
        this.gridCellHeight = gridCellHeight;
    }

    /**
     * Gets mapsArray of Data.
     *
     * @return Map[] mapsArray of Data.
     */
    Map[] getMapsArray() {
        return this.mapsArray;
    }

    /**
     * Sets mapsArray of Data.
     *
     * @param mapsArray Map[] array to be set.
     */
    void setMapsArray(Map[] mapsArray) {
        this.mapsArray = mapsArray;
    }

    /**
     * Gets levelNumber of Data.
     *
     * @return levelNumber of Data.
     */
    int getLevelNumber() {
        return this.levelNumber;
    }

    /**
     * Sets levelNumber of Data.
     *
     * @param levelNumber Level Number to be set.
     */
    void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }
}
