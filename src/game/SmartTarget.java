package game;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * A type of Enemy that will move along the shortest path to kill the Player.
 *
 * @author Pedro Caetano
 * @version 1.1
 */
public class SmartTarget extends Enemy {
    private final Log log = new Log(getClass().getSimpleName());
    private final Hashtable<Cell, Integer> hashCells = new Hashtable<>();
    private final Hashtable<Integer, Cell> hashKeys = new Hashtable<>();
    private int maxX;
    private int maxY;
    private Graph graph;
    private boolean[][] alreadyAdded;
    private Cell lastPlayer;

    /**
     * Smart Enemy. A NPC Character that follows that calculates the shortest possible path to the Player and follows it.
     *
     * @param speed Speed the Enemy moves at per Player move
     */
    SmartTarget(int speed) {
        super(speed);
    }

    /**
     * Overrides the Enemy move() method in order for the Enemy to move as described.
     *
     * @param gameManager GameManager providing information
     */
    @Override
    public void move(GameManager gameManager) {
        for (int i = 0; i < getSpeed(); i++) {
            //init variables required
            maxX = gameManager.getCurrentMap().getSizeX();
            maxY = gameManager.getCurrentMap().getSizeY();
            alreadyAdded = new boolean[maxX][maxY];
            hashCells(gameManager.getCurrentMap());
            graph = new Graph(countCells(gameManager.getCurrentMap()));
            addNeighbours(gameManager.getCurrentMap(), getX(), getY());
            Cell playerCell = gameManager.getCurrentPlayer().getCell();

            int hashedPlayer = hashCells.get(playerCell);
            int hashedCurrent = hashCells.get(getCell());

            //execute path finding
            ArrayList<Integer> pathTo = graph.findPath(hashedCurrent, hashedPlayer);
            if (pathTo.size() == 0 && lastPlayer != null) {
                //no path found
                log.add("Trying last known Cell", 1);
                hashedPlayer = hashCells.get(lastPlayer);
                pathTo = graph.findPath(hashedCurrent, hashedPlayer);
            } else {
                //save cell as it is a good Cell
                lastPlayer = playerCell;
            }
            //output for log
            StringBuilder output = new StringBuilder();
            for (Integer cell : pathTo) {
                Cell currentCell = hashKeys.get(cell);
                String currentMove = "[" + currentCell.getX() + "][" + currentCell.getY() + "]";
                output.append(currentMove).append(" ");
            }
            log.add("Shortest Path = " + output, 1);
            //attempt the next move as per path finding
            try {
                Cell currentCell = hashKeys.get(pathTo.get(1));
                gameManager.updateEnemyLocation(this, currentCell.getX(), currentCell.getY());
            } catch (IndexOutOfBoundsException e) {
                log.add("No Move Available");
            }
        }
    }

    /**
     * Counts number of Cells on the Map.
     *
     * @param map Map to count Cells in
     * @return Number of Cells
     */
    private int countCells(Map map) {
        return map.getSizeY() * map.getSizeX();
    }

    /**
     * Hashes the Cell and Key into hashTables for lookup.
     *
     * @param map Map to read Cells from
     */
    private void hashCells(Map map) {
        int count = 0;
        for (int y = 0; y < map.getSizeY(); y++) {
            for (int x = 0; x < map.getSizeX(); x++) {
                hashCells.put(map.getCell(x, y), count);
                hashKeys.put(count, map.getCell(x, y));
                count += 1;
            }
        }
    }

    /**
     * Adds any neighbours that the current Cell is adjacent to, if they're a valid move.
     * Called recursively to ensure Graph is generated correctly.
     *
     * @param map Map to query
     * @param x   X location of the Cell
     * @param y   Y location of the Cell
     */
    private void addNeighbours(Map map, int x, int y) {
        int newX = x;
        int newY = y;
        Cell currentCell = map.getCell(x, y);
        log.add("[" + newX + "][" + newY + "] " + currentCell.getClass().getSimpleName());
        if (!alreadyAdded(currentCell)) {
            alreadyAdded[currentCell.getX()][currentCell.getY()] = true;
            if (isValid(x + 1, y)) {
                x += 1;
                Cell nextCell = map.getCell(x, y);
                checkThenAddCell(currentCell, nextCell, map);
            }
            x = newX;
            y = newY;
            if (isValid(x - 1, y)) {
                x -= 1;
                Cell nextCell = map.getCell(x, y);
                checkThenAddCell(currentCell, nextCell, map);
            }
            x = newX;
            y = newY;
            if (isValid(x, y + 1)) {
                y += 1;
                Cell nextCell = map.getCell(x, y);
                checkThenAddCell(currentCell, nextCell, map);
            }
            x = newX;
            y = newY;
            if (isValid(x, y - 1)) {
                y -= 1;
                Cell nextCell = map.getCell(x, y);
                checkThenAddCell(currentCell, nextCell, map);
            }
        }
    }

    /**
     * If a Cell is a valid move for the enemy, add it to the Graph as an Edge from the previous Cell.
     *
     * @param currentCell Parent Cell
     * @param nextCell    Next Cell
     * @param map         Map to query
     */
    private void checkThenAddCell(Cell currentCell, Cell nextCell, Map map) {
        int x = nextCell.getX();
        int y = nextCell.getY();
        if (isPassable(nextCell)) {
            graph.addEdge(hashCells.get(currentCell), hashCells.get(nextCell));
            addNeighbours(map, x, y);
        }
    }

    /**
     * Checks if a Cell has already been added to the Graph.
     *
     * @param cell Cell to check
     * @return True if Cell has already been added
     */
    private boolean alreadyAdded(Cell cell) {
        return alreadyAdded[cell.getX()][cell.getY()];
    }

    /**
     * Checks if a location would result in a valid Cell on the map.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     * @return True if it is valid
     */
    private boolean isValid(int x, int y) {
        if (x > maxX - 1) {
            return false;
        } else if (y > maxY - 1) {
            return false;
        } else if (x < 0) {
            return false;
        } else {
            return y >= 0;
        }
    }

    /**
     * Checks if a cell can be passed by the SmartEnemy.
     *
     * @param cell Cell to be checked
     * @return True if it can be passed and is a valid move
     */
    private boolean isPassable(Cell cell) {
        if (cell instanceof Ground && cell.getItem() == null) {
            if (cell.getOccupier() == null) {
                return true;
            } else {
                if (cell.getOccupier() instanceof Player) {
                    return true;
                } else {
                    return cell == currentCell;
                }
            }
        } else {
            return false;
        }
    }
}


