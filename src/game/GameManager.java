package game;

import java.util.ArrayList;

/**
 * Manages the running of the game, orchestrates any actions that happen because of Player movement, or enemy movement.
 *
 * @author Pedro Caetano
 * @author Artur Zinnurov
 * @author James Henry
 * @version 5.0
 */
class GameManager {
    private final Log log = new Log(this.getClass().getSimpleName());
    private final GameSounds sound = new GameSounds();
    private Player currentPlayer;
    private Map currentMap;
    private boolean killEnemy = false;
    Enemy enemy;



    /**
     * Constructor. Creates a GameManager Object.
     */
    GameManager() {
        log.setImportance(1);
    }

    /**
     * Checks if a Player move is valid.
     *
     * @param requestedX X location that the player wishes to move to
     * @param requestedY Y location that player wishes to move to
     * @return True if move is valid
     */
    boolean checkPlayerMove(int requestedX, int requestedY) {
        Map map = getCurrentMap();
        Cell requestedCell = map.getCell(requestedX, requestedY);
        Cell currentCell = getCurrentPlayer().getCell();

        if (checkDistance(currentCell, requestedCell)) {
            // if diff between requested x/y and current x/y is 1 or less
            // in both x/y, move can be valid (diagonals not possible)
            log.add("Difference okay, continue checks", 0);
            if (requestedCell.isPassable() || (requestedCell instanceof IceCubes && currentPlayer.checkIfcontainPicaxe())) { //if unoccupied and passable
                log.add("Unoccupied and Passable, can move", 0);
                return true; //can move
            } else {
                log.add("Occupied or unPassable, continue checks", 0);
                if ((requestedCell instanceof Door)
                        && requestedCell.getOccupier() == null) { //if it is a door
                    log.add("isDoor, attempt to open", 0);
                    ((Door) requestedCell).open(currentPlayer); //try to open door
                    if (requestedCell.isPassable()) { //if it is NOW passable, move is valid
                        sound.playDoor();
                        log.add("Unlocked, can move", 0);
                        return true; //can move
                    } else {
                        log.add("Still locked, cannot move", 0);
                        return false; //door still not open, invalid move
                    }
                } else {
                    log.add("Not a door, Can't move", 0);
                    return false; //not a door, invalid move
                }
            }
        } else {
            log.add("Too far away, cannot move", 0);
        }
        return false; //too far away, cant be a valid move
    }

    /**
     * Checks if an Enemy move is valid.
     * Very similar in logic to checkPlayerMove, hence the lack of comments
     *
     * @param enemy      Enemy that wants to move
     * @param requestedX X location he wants to move to
     * @param requestedY Y location he wants to move to
     * @return True if move is valid
     */
    boolean checkEnemyMove(Enemy enemy, int requestedX, int requestedY) {
        Cell currentCell = currentMap.getCell(enemy.getX(), enemy.getY());
        Cell requestedCell = currentMap.getCell(requestedX, requestedY);
        if (checkDistance(currentCell, requestedCell)) {
            log.add("Difference okay, continue checks", 0);
            if (requestedCell instanceof Ground || requestedCell instanceof SnowGround) {
                log.add("Ground, Enemy can move", 0);
                boolean hasItem = (requestedCell.getItem() == null);
                boolean hasEnemy = (requestedCell.getOccupier() instanceof Enemy);
                if (hasItem || hasEnemy) {
                    log.add("No Item on the cell, no Enemy on the cell, Enemy can move", 0);
                    return true;
                } else {
                    log.add("Item or Enemy on the cell, Enemy cannot move", 0);
                    return false; //cannot move
                }
            } else {
                log.add("Not Ground, Enemy cannot move", 0);
                return false;
            }
        } else {
            log.add("Too far away, Enemy cannot move", 0);
            return false;
        }
    }

    /**
     * Any actions that have to occur after a player is moved, but before a screen refresh
     * Current actions include checking if player has survived the current move, and if not, killing him
     * As well ass allowing pickup of items from the cells. Triggers movement of Enemies.
     */
    void postMovement() {
        log.add("Moves: " + currentPlayer.getCurrentSteps(), 2);
        if (currentPlayer.isAlive()) {
            int playerX = currentPlayer.getX();
            int playerY = currentPlayer.getY();
            Cell currentCell = currentMap.getCell(playerX, playerY);
            moveEnemies();
            if(killEnemy){
                enemy.killEnemy();
                currentPlayer.deteletSword();
                killEnemy = false;
            }
            itemPickup(currentCell);
            if (!survivesMove(currentCell) || !currentPlayer.isAlive()) {
                log.add("Player does not survive move", 1);
                currentPlayer.kill();
            }
        } else {
            log.add("Post Movement invalid, Player dead", 1);
        }
    }

    /**
     * Gets relative direction from two points on the map.
     *
     * @param x  Origin X
     * @param xx Destination X
     * @param y  Origin Y
     * @param yy Destination Y
     * @return Direction relative to Origin
     */
    Direction getDirectionFromPlayerX(int x, int xx, int y, int yy) {
        if (x < xx) {
            return Direction.LEFT;
        } else if (x > xx) {
            return Direction.RIGHT;
        } else if (y < yy) {
            return Direction.UP;
        } else if (y > yy) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }

    /**
     * Updates Enemy location.
     *
     * @param enemy Enemy to move
     * @param x     New X location
     * @param y     New Y location
     */
    void updateEnemyLocation(Enemy enemy, int x, int y) {
        Cell nextCell = currentMap.getCell(x, y);
        Cell currentCell = currentMap.getCell(enemy.getX(), enemy.getY());
        if (nextCell.getOccupier() instanceof Player && !currentPlayer.checkIfContainSword()) {
            currentPlayer.kill();
            currentCell.setCharacter(null);
            enemy.setCurrentCell(nextCell);
            nextCell.setCharacter(enemy);
        } else if (nextCell.getOccupier() instanceof Player && currentPlayer.checkIfContainSword()) {
            killEnemy = true;
            this.enemy = enemy;
        } else if (!(nextCell.getOccupier() instanceof Player)) {
            currentCell.setCharacter(null);
            enemy.setCurrentCell(nextCell);
            nextCell.setCharacter(enemy);
        }
    }

    /**
     * Gets the opposite direction to the one supplied.
     *
     * @param direction Initial direction
     * @return Opposite direction
     */
    Direction flipDirection(Direction direction) {
        if (direction == Direction.UP) {
            return Direction.DOWN;
        } else if (direction == Direction.RIGHT) {
            return Direction.LEFT;
        } else if (direction == Direction.DOWN) {
            return Direction.UP;
        } else if (direction == Direction.LEFT) {
            return Direction.RIGHT;
        }
        log.add("Invalid direction supplied", 1);
        return null;
    }

    /**
     * Gets the current Player.
     *
     * @return Current Player
     */
    Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Sets the current Player.
     *
     * @param player Player in the map
     */
    void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Gets the current map.
     *
     * @return Map being played
     */
    Map getCurrentMap() {
        return this.currentMap;
    }

    /**
     * Sets the current map.
     *
     * @param map Map being played
     */
    void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    /**
     * Updates the player location to the supplied X,Y.
     * Will kill player if an Enemy is on the cell.
     * Handles teleportation.
     *
     * @param x New X location of the Player
     * @param y New Y location of the Player
     */
    void updatePlayerLocation(int x, int y) {
        int currentMoves = currentPlayer.getCurrentSteps();
        boolean teleported = false;

        int direction = 0;

        Cell newLocation = null;

        if (currentMap.getCell(x, y) instanceof Teleporter) {
            Teleporter t1 = (Teleporter) currentMap.getCell(x, y);
            Teleporter t2 = t1.getPairedTeleporter();
            Direction relativeDirection = getDirectionFromPlayerX(currentPlayer.getX(),
                    x, currentPlayer.getY(), y);


            if (relativeDirection == Direction.LEFT) {
                newLocation = currentMap.getCell(t2.getX() + 1, t2.getY());
                direction = 1;
            } else if (relativeDirection == Direction.RIGHT) {
                newLocation = currentMap.getCell(t2.getX() - 1, t2.getY());
                direction = 2;
            } else if (relativeDirection == Direction.UP) {
                newLocation = currentMap.getCell(t2.getX(), t2.getY() + 1);
                direction = 3;
            } else if (relativeDirection == Direction.DOWN) {
                newLocation = currentMap.getCell(t2.getX(), t2.getY() - 1);
                direction = 4;
            } else {
                newLocation = currentMap.getCell(currentPlayer.getX(), currentPlayer.getY());
            }
            if (newLocation.isPassable()) {
                updatePlayerLocation(newLocation.getX(), newLocation.getY());
                teleported = true;
                log.add("You are here");
            } else {
                teleported = failedTeleport(direction, t2, newLocation, 0);
                log.add("Teleport Succeded? " + teleported);
            }
        }
        if (!teleported) {
            Cell currentCell = currentMap.getCell(x, y);

            if (!currentCell.isOccupied()) {
                if (currentCell.getOccupier() instanceof Enemy && !currentPlayer.checkIfContainSword()) {
                    log.add("Player has been killed by an enemy", 1);
                    currentPlayer.kill();
                } else {
                    Enemy enemy = (Enemy)currentCell.getOccupier();
                    enemy.killEnemy();
                    currentPlayer.setSteps(currentMoves + 1);
                    currentPlayer.setCurrentCell(getCurrentMap().getCell(x, y));
                    getCurrentMap().getCell(x, y).setCharacter(currentPlayer);
                    currentPlayer.deteletSword();
                }
            } else {
                currentPlayer.setSteps(currentMoves + 1);
                currentPlayer.setCurrentCell(getCurrentMap().getCell(x, y));
                getCurrentMap().getCell(x, y).setCharacter(currentPlayer);
            }
        } else {
            sound.playTeleportStart();
        }
    }


    /**
     * Gets a list of Enemies and calls initiates their move.
     */
    private void moveEnemies() {
        ArrayList<Enemy> enemyList = currentMap.getEnemies();
        for (Enemy elem : enemyList) {
            if (elem != null) {
                elem.move(this);
            }
        }
    }

    /**
     * Removes any items from current cell and adds it to player inventory.
     *
     * @param currentCell The cell to pickup from
     */
    private void itemPickup(Cell currentCell) {
        if (currentCell.getItem() != null) {
            log.add("There is an item on this cell", 0);
            //Add to player
            Item currentItem = currentCell.getItem();
            if (currentItem instanceof RainUmbrella) {
                sound.playItemPickup();
                currentPlayer.addWearable((RainUmbrella) currentItem);
            } else if (currentItem instanceof WindJacket) {
                sound.playItemPickup();
                currentPlayer.addWearable((WindJacket) currentItem);
            } else if (currentItem instanceof Sword){
                sound.playItemPickup();
                currentPlayer.addWearable((Sword) currentItem);
            } else if (currentItem instanceof Pickaxe) {
                sound.playItemPickup();
                currentPlayer.addWearable((Pickaxe) currentItem);
            }
                else {
                sound.playTokenPickup();
                currentPlayer.addItem(currentItem);
            }
            log.add("Added " + currentItem.getClass().getSimpleName() + " to Player", 1);
            //remove cell from item
            currentItem.setCurrentCell(null);
            //remove item from cell and thus map
            currentCell.setItem(null);
        }
    }

    /**
     * Checks if a player has the appropriate wearables to survive the cell they are on.
     * WindJacket required for Wind
     * RainUmbrella required for Rain
     *
     * @param currentCell The cell to check
     * @return True if player survives
     */
    private boolean survivesMove(Cell currentCell) {
        ArrayList<Wearable> wearables = currentPlayer.getWearables();
        boolean survives = false;
        String requiredWearable = "";
        if (currentCell instanceof Wind) {
            sound.playWind();
            requiredWearable = "WindJacket";
        } else if (currentCell instanceof Rain) {
            sound.playWater();
            requiredWearable = "RainUmbrella";
        } else if (currentCell instanceof IceCubes) {
                requiredWearable = "Pickaxe";
        } else {
            survives = true;
        }

        for (Wearable wearable : wearables) {
            if (wearable.getClass().getSimpleName().equals(requiredWearable)) {
                survives = true;
                if (wearable instanceof Pickaxe){
                    System.out.println("dzi kdskkdkdk");
                    currentMap.setIceCubesPassable();
                }
            }
        }
        return survives;
    }

    /**
     * Compares 2 integers and returns the difference between them.
     *
     * @param a First int
     * @param b Second int
     * @return Difference between 'a' and 'b'
     */
    private int checkDifference(int a, int b) {
        if (a > b) {
            return a - b;
        } else {
            return b - a;
        }
    }

    /**
     * In the event that the player cannot exit the teleporter whilst maintaining their direction, this method will cycle through
     * the remaining three cardinal directions around the teleporter until it finds a free space.
     *
     * @param direction   The previous direction attempted.
     * @param t2          The teleporter being travelled to.
     * @param newLocation The previous location that we attempt to move the player to.
     * @param count       Number of times that failedTeleport has run so far'
     * @return true when it finds a location the player can move to. If no such move exists (i.e teleporter is surrounded on all sides with
     * walls), returns false.
     */
    private boolean failedTeleport(int direction, Teleporter t2, Cell newLocation, int count) {
        int counter = count;

        if (direction < 4) {
            direction += 1;
        } else {
            direction = 1;
        }

        log.add("Teleport check count: " + counter);

        if (direction == 1) {
            newLocation = currentMap.getCell(t2.getX() - 1, t2.getY());
        } else if (direction == 2) {
            newLocation = currentMap.getCell(t2.getX() + 1, t2.getY());
        } else if (direction == 3) {
            newLocation = currentMap.getCell(t2.getX(), t2.getY() + 1);
        } else if (direction == 4) {
            newLocation = currentMap.getCell(t2.getX(), t2.getY() - 1);
        }

        if (newLocation.isPassable()) {
            updatePlayerLocation(newLocation.getX(), newLocation.getY());
            log.add("Successful teleport");
            return true;
        } else {
            log.add("Unsuccessful teleport");
            if (counter < 3) {
                counter += 1;
                failedTeleport(direction, t2, newLocation, counter);
            } else {
                log.add("Resetting back");
                //updatePlayerLocation(currentPlayer.getX(), currentPlayer.getY());
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the distance between 2 cells is less than 1.
     *
     * @param previous Cell from
     * @param next     Cell to
     * @return True if distance is a valid move
     */
    private boolean checkDistance(Cell previous, Cell next) {
        int requestedX = next.getX();
        int requestedY = next.getY();
        int previousX = previous.getX();
        int previousY = previous.getY();
        int distX = checkDifference(requestedX, previousX);
        int distY = checkDifference(requestedY, previousY);
        return distX <= 1 && distY <= 1;
    }

}
