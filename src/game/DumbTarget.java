package game;

/**
 * A type of Enemy that will move towards the Player to kill him.
 *
 * @author Pedro Caetano
 * @author Artur Zinnurov
 * @version 1.1
 */
public class DumbTarget extends Enemy {
    private final Log log = new Log(getClass().getSimpleName());

    /**
     * Dumb Enemy. A NPC Character that follows the general direction the Player is relative to him.
     *
     * @param speed Speed the Enemy moves at per Player move
     */
    DumbTarget(int speed) {
        super(speed);
    }

    /**
     * Overrides the Enemy move() method in order for the Enemy to move as described.
     *
     * @param gameManager GameManager providing information
     */
    @Override
    public void move(GameManager gameManager) {
        //loop that allows enemy speed to be different
        for (int i = 0; i < getSpeed(); i++) {
            Player p = gameManager.getCurrentPlayer();
            int playerX = p.getX();
            int playerY = p.getY();
            int cellX = getX();
            int cellY = getY();
            Direction dir = gameManager.getDirectionFromPlayerX(cellX, playerX, cellY, playerY);
            dir = gameManager.flipDirection(dir);
            log.add(dir.toString());
            //move in direction of player
            if (dir == Direction.UP) {
                cellY -= 1;
            } else if (dir == Direction.RIGHT) {
                cellX += 1;
            } else if (dir == Direction.DOWN) {
                cellY += 1;
            } else if (dir == Direction.LEFT) {
                cellX -= 1;
            }
            //if valid, update location
            if (gameManager.checkEnemyMove(this, cellX, cellY)) {
                gameManager.updateEnemyLocation(this, cellX, cellY);
            }
        }
    }
}
