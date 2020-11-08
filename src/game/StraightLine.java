package game;

/**
 * A type of Enemy that will move along an Axis/Direction in order to kill the Player.
 *
 * @author Pedro Caetano
 * @author Suad Mena Ahmadieh
 * @version 1.1
 */
public class StraightLine extends Enemy {
    private final Log log = new Log(getClass().getSimpleName());
    private Direction direction;

    /**
     * Creates a StraightLine Enemy.
     *
     * @param speed     Speed at which the Enemy moves
     * @param direction Direction the Enemy is currently facing
     */
    StraightLine(int speed, Direction direction) {
        super(speed);
        this.direction = direction;
        log.setImportance(0);
    }

    /**
     * Calculates and performs the next movement.
     * Moves the enemy in the direction he is meant to move in,
     * after movement is attempted, if next move can't be done,
     * flip direction
     *
     * @param gameManager GameManager to check against
     */
    @Override
    public void move(GameManager gameManager) {
        for (int i = 0; i < getSpeed(); i++) {
            switch (this.direction) {
                case UP:
                    if (gameManager.checkEnemyMove(this, this.getX(), this.getY() + 1)) {
                        log.add("Enemy move allowed", 0);
                        gameManager.updateEnemyLocation(this, this.getX(), this.getY() + 1);
                    }
                    if (this == null){
                        System.out.println("its nulllll");
                        break;
                    }

                    if (!gameManager.checkEnemyMove(this, this.getX(), this.getY() + 1)) {
                        log.add("Next move not allowed, flipping direction", 0);
                        this.direction = gameManager.flipDirection(this.direction);
                    }
                    break;
                case RIGHT:
                    if (gameManager.checkEnemyMove(this, this.getX() + 1, this.getY())) {
                        log.add("Enemy move allowed", 0);
                        gameManager.updateEnemyLocation(this, this.getX() + 1, this.getY());
                    }
                    if (this == null){
                        break;
                    }
                    if (!gameManager.checkEnemyMove(this, this.getX() + 1, this.getY())) {
                        log.add("Next move not allowed, flipping direction", 0);
                        this.direction = gameManager.flipDirection(this.direction);
                    }
                    break;
                case DOWN:
                    if (gameManager.checkEnemyMove(this, this.getX(), this.getY() - 1)) {
                        log.add("Enemy move allowed", 0);
                        gameManager.updateEnemyLocation(this, this.getX(), this.getY() - 1);
                    }
                    if (this == null){
                        break;
                    }
                    if (!gameManager.checkEnemyMove(this, this.getX(), this.getY() - 1)) {
                        log.add("Next move not allowed, flipping direction", 0);
                        this.direction = gameManager.flipDirection(this.direction);
                    }
                    break;
                case LEFT:
                    if (gameManager.checkEnemyMove(this, this.getX() - 1, this.getY())) {
                        log.add("Enemy move allowed", 0);
                        gameManager.updateEnemyLocation(this, this.getX() - 1, this.getY());
                    }
                    if (this == null){
                        break;
                    }
                    if (!gameManager.checkEnemyMove(this, this.getX() - 1, this.getY())) {
                        log.add("Next move not allowed, flipping direction", 0);
                        this.direction = gameManager.flipDirection(this.direction);
                    }
                    break;
                default:
                    log.add("No direction", 1);
            }
        }
    }

}
