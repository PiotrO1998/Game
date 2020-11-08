package game;

/**
 * Enemies that try to kill the Player.
 *
 * @author Pedro Caetano
 * @author Artur Zinnurov
 * @version 1.1
 */

abstract class Enemy extends Character {
    private int speed;
    private int rotation;

    /**
     * Enemy Move method, once called, will move the enemy using his own algorithm.
     *
     * @param gameManager GameManager providing information
     */
    public abstract void move(GameManager gameManager);

    /**
     * Sets the speed of the Enemy.
     *
     * @param speed Speed to set
     */
    private void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Super constructor for an Enemy.
     *
     * @param speed Speed of the enemy
     */
    Enemy(int speed) {
        setSpeed(speed);
    }

    /**
     * Gets the speed of the Enemy.
     *
     * @return Speed of Enemy
     */
    int getSpeed() {
        return speed;
    }

    public void killEnemy(){
        if (this.currentCell != null) {
            this.currentCell.setCharacter(null);
        }
        setCurrentCell(null);
    }

}
