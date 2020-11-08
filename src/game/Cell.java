package game;

import java.io.Serializable;

/**
 * Cell that combines with others to make a Map.
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @version 1.3
 */
public abstract class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int locationX;
    private final int locationY;
    private boolean isPassable;
    private Character character;
    private Item item;
    private boolean isSpawn = false;

    /**
     * Abstract class Cell. Represents a space within the Map
     *
     * @param x          X location of the Cell
     * @param y          Y location of the Cell
     * @param isPassable Whether the cell is passable by the Player
     */
    Cell(int x, int y, boolean isPassable) {
        this.locationX = x;
        this.locationY = y;
        this.isPassable = isPassable;
    }

    /**
     * Gets the current Item that is on the cell.
     *
     * @return Item on the cell
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * Set current cell Item that is on the cell.
     *
     * @param item Item on the cell
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Get X location.
     *
     * @return X location of the cell
     */
    public int getX() {
        return this.locationX;
    }

    /**
     * Get Y location.
     *
     * @return Y location of the cell
     */
    public int getY() {
        return this.locationY;
    }

    /**
     * Sets the occupier of the cell.
     *
     * @param character Character that will be occupying the cell
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Checks if the Cell is occupied.
     *
     * @return True if Cell has an occupier
     */
    boolean isOccupied() {
        return (this.getOccupier() == null);
    }

    /**
     * Checks if Cell is passable.
     *
     * @return True if cell is passable
     */
    boolean isPassable() {
        return this.isPassable;
    }

    /**
     * Sets the current Passable state to whatever is provided.
     *
     * @param passable True if the Cell can be passed
     */
    void setPassable(Boolean passable) {
        this.isPassable = passable;
    }

    /**
     * Checks if current Cell is the player spawn Cell.
     *
     * @return True if current Cell is the spawn Cell
     */
    boolean isSpawn() {
        return this.isSpawn;
    }

    /**
     * Sets the current Cell to be a spawn Cell.
     *
     * @param spawn True if Cell is the spawn
     */
    void setSpawn(Boolean spawn) {
        this.isSpawn = spawn;
    }

    /**
     * Get current occupier of the cell.
     *
     * @return character Character currently occupying cell. Null if empty
     */
    Character getOccupier() {
        return character;
    }
}