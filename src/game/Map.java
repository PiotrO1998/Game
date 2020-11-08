package game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a Map that can be loaded and played, can consist of many types of Cells.
 *
 * @author Pedro Caetano
 * @author Artur Zinnurov
 * @author Piotr Obara
 * @version 1.0
 */
public class Map implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Log log;
    private Cell[][] map;
    private Map originalMap;
    private Cell spawnCell;
    private String mapLocation;
    private int tokenCount;

    /**
     * Initialises a Map.
     * From a file location, creates the map and finds any key attributes.
     *
     * @param fileLocation File location of map to read
     */
    public Map(String fileLocation) {
        log = new Log(this.getClass().getSimpleName());
        MapReader file = new MapReader();
        this.map = file.readFile(fileLocation);
        this.spawnCell = findSpawn();
        log.setImportance(1);
        mapLocation = fileLocation;
        tokenCount = countTokens();
    }

    /**
     * Basic Map constructor.
     *
     * @param map Cell[][] array from which to create map
     */
    public Map(Cell[][] map) {
        log = new Log(this.getClass().getSimpleName());
        this.map = map;
    }

    /**
     * Finds the Player in the Map.
     *
     * @return reference to Player.
     */
    public Player getPlayer() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].getOccupier() != null) {
                    if (map[i][j].getOccupier() instanceof Player) {
                        return (Player) map[i][j].getOccupier();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Overridden toString method in order to facilitate testing.
     *
     * @return A string representation of the map
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int y = 0; y < this.getSizeY(); y++) {
            for (int x = 0; x < this.getSizeX(); x++) {
                result.append(" ").append(x).append(",").append(y).append(": ");
                result.append(this.getCell(x, y));
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Finds the Player within the Map.
     *
     * @return Cell Player is on
     */
    public Cell findPlayer() {
        return getPlayer().getCell();
    }

    /**
     * Gets the current Cell[][] array.
     *
     * @return Map cell[][] array
     */
    public Cell[][] getMap() {
        return this.map;
    }

    /**
     * Set Cell[][] array.
     *
     * @param map Cell[][]
     */
    public void setMap(Cell[][] map) {
        this.map = map;
    }

    /**
     * Finds the cell at X,Y and returns it.
     *
     * @param x X location of the cell
     * @param y Y location of the cell
     * @return Cell at X,Y
     */
    Cell getCell(int x, int y) {
        if (x < this.getSizeX() && y < this.getSizeY()) {
            return map[x][y];
        } else {
            log.add("Invalid getCell() call: " + x + "," + y, 1);
            return null;
        }
    }

    /**
     * Get the length of the map in the X direction.
     *
     * @return Max X of the Map
     */
    int getSizeX() {
        return map.length;
    }

    /**
     * Get the length of the map in the Y direction.
     *
     * @return Max Y of the map
     */
    int getSizeY() {
        return map[0].length;
    }

    /**
     * Gets the Player Spawn  cell.
     *
     * @return Cell where the Player is spawned
     */
    Cell getSpawnCell() {
        return this.spawnCell;
    }

    /**
     * Finds all Enemy's in the map and returns an ArrayList of them.
     *
     * @return ArrayList of Enemy's in the Map
     */
    ArrayList<Enemy> getEnemies() {
        ArrayList<Enemy> enemyList = new ArrayList<>();
        for (int y = 0; y < this.getSizeY(); y++) {
            for (int x = 0; x < this.getSizeX(); x++) {
                Cell currentCell = this.getCell(x, y);
                if (currentCell.getOccupier() != null) {
                    Character currentOccupier = currentCell.getOccupier();
                    log.add(currentOccupier.getClass().getSimpleName());
                    if (currentOccupier instanceof Enemy) {
                        enemyList.add((Enemy) currentOccupier);
                    }
                }
            }
        }
        return enemyList;
    }

    /**
     * Gets the number of Tokens on the Map.
     *
     * @return Number of Tokens
     */
    int getTokenCount() {
        return this.tokenCount;
    }

    /**
     * gets the original Map before any moves were played.
     *
     * @return Original Map.
     */
    Map getOriginalMap() {
        return new Map(mapLocation);
    }

    /**
     * Counts the number of Tokens within the map.
     *
     * @return Number of Tokens
     */
    private int countTokens() {
        int count = 0;
        for (int y = 0; y < this.getSizeY(); y++) {
            for (int x = 0; x < this.getSizeX(); x++) {
                Cell currentCell = this.getCell(x, y);
                if (currentCell.getItem() != null) {
                    if (currentCell.getItem() instanceof Token) {
                        count += 1;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Finds the Cell where a Player should be spawned.
     *
     * @return Spawn cell
     */
    private Cell findSpawn() {
        for (int y = 0; y < getSizeY(); y++) {
            for (int x = 0; x < getSizeX(); x++) {
                if (getCell(x, y).isSpawn()) {
                    log.add("Spawn cell = " + x + "," + y);
                    return getCell(x, y);
                }
            }
        }
        log.add("Spawn cell not found", 1);
        return null;
    }

    /**
     * Method to set all IceCubes passable as true
     *
     */
    public void setIceCubesPassable() {
        for (int y = 0; y < this.getSizeY(); y++) {
            for (int x = 0; x < this.getSizeX(); x++) {
                Cell currentCell = this.getCell(x, y);
                if (currentCell instanceof IceCubes) {
                    currentCell.setPassable(true);
                }
            }
        }
    }

}

