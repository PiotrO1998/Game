package game;

/**
 * A type of Enemy that will follow the Wall along and attempt to kill a Player.
 *
 * @author Artur Zinnurov
 * @author Pedro Caetano
 * @version 3.0
 */
public class WallFollow extends Enemy {
    private final Log log = new Log(this.getClass().getSimpleName());
    private final Direction directionTo;
    private Direction direction;

    /**
     * Wall Enemy. An NPC Character that follows the Wall nearest to it in order to kill the Player.
     *
     * @param speed     Speed the Enemy moves at per Player move
     * @param direction Direction it is facing
     */
    WallFollow(int speed, Direction direction) {
        super(speed);
        this.direction = direction;
        this.directionTo = direction;
    }

    /**
     * Overrides the Enemy move() method in order for the Enemy to move as described.
     * Covers all possible movement scenarios for Wall Enemy
     *
     * @param g GameManager providing information
     */
    public void move(GameManager g) {
        int x = this.getX();
        int y = this.getY();
        log.add(x + " " + y);

        for (int i = 0; i < getSpeed(); i++) {
            if (this.directionTo == Direction.RIGHT) {
                right(g, x, y);
            } else {
                if (this.direction == Direction.DOWN) {
                    down(g, x, y);
                } else if (this.direction == Direction.UP) {
                    up(g, x, y);
                } else if (this.direction == Direction.LEFT) {
                    left(g, x, y);
                } else if (this.direction == Direction.RIGHT) {
                    otherRight(g, x, y);
                } else {
                    log.add("no direction", 1);
                }
            }
        }
    }

    /**
     * This method check all possible possible situation if enemy moves only right direction
     * through map which covers UP,DOWN,LEFT,RIGHT corner of the square.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void right(GameManager g, int x, int y) {
        if (this.direction == Direction.DOWN) {
            rightDown(g, x, y);
            /*
				In case if up direction it checks all possible movement where wall could be right to him left to him or down
				if for example wall down to him -enemy will go to left or right where checks any available cells
				if no any directions it reverse to the down wayz
			*/
        } else if (this.direction == Direction.UP) {
            rightUp(g, x, y);
        } else if (this.direction == Direction.LEFT) {
            rightLeft(g, x, y);
        } else if (this.direction == Direction.RIGHT) {
            rightRight(g, x, y);
        } else {
            log.add("no direction", 1);
        }
    }

    /**
     * Move enemy down from the TOP of the square.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void rightDown(GameManager g, int x, int y) {
        if ((checkWall(g, x - 1, y + 1) || checkWall(g, x + 1, y + 1)) && g.checkEnemyMove(this, x, y + 1)) {
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x + 1, y + 1) && checkWall(g, x + 1, y)) {
            x += 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y + 1) && checkWall(g, x, y + 1)) {
            x -= 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y + 1) && checkWall(g, x - 1, y)) {
            x -= 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (checkWall(g, x - 1, y) && g.checkEnemyMove(this, x - 1, y)) {
            this.direction = Direction.RIGHT;
        } else {
            checkWallOne(g, x, y);
        }
    }

    /**
     * Method to move change Enemy direction.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void checkWallOne(GameManager g, int x, int y) {
        if ((checkWall(g, x - 1, y + 1) || checkWall(g, x - 1, y - 1)
                && g.checkEnemyMove(this, x - 1, y))) {
            this.direction = Direction.LEFT;
        } else if ((checkWall(g, x - 1, y + 1) || checkWall(g, x - 1, y - 1))
                && g.checkEnemyMove(this, x + 1, y)) {
            this.direction = Direction.RIGHT;
        } else {
            this.direction = g.flipDirection(this.direction);
        }
    }

    /**
     * Method to move enemy to the UP from the BOTTOM of the square.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void rightUp(GameManager g, int x, int y) {
        if ((checkWall(g, x - 1, y - 1) || checkWall(g, x + 1, y - 1))
                && g.checkEnemyMove(this, x, y - 1)) {
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x + 1, y - 1)
                && checkWall(g, x, y - 1)) {
            x += 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y - 1)
                && checkWall(g, x - 1, y)) {
            x -= 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if ((checkWall(g, x - 1, y - 1) || checkWall(g, x - 1, y))
                && g.checkEnemyMove(this, x + 1, y)) {
            this.direction = Direction.RIGHT;
        } else {
            checkWallOne(g, x, y);
        }
    }

    /**
     * Method to move from the TOP of the square to the LEFT corner or to the RIGHT corner.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void rightLeft(GameManager g, int x, int y) {
        if ((checkWall(g, x - 1, y + 1) || checkWall(g, x - 1, y - 1)) && g.checkEnemyMove(this, x - 1, y)) {
            x -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y + 1) && checkWall(g, x, y + 1)) {
            x -= 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y - 1) && checkWall(g, x - 1, y)) {
            x -= 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y - 1)) {
            if (checkWall(g, x, y - 1)) {
                x -= 1;
                y -= 1;
                g.updateEnemyLocation(this, x, y);
            } else {
                checkWallTwo(g, x, y);
            }
        } else {
            checkWallTwo(g, x, y);
        }
    }

    /**
     * Move enemy UP or DOWN if no any directions is available.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void checkWallTwo(GameManager g, int x, int y) {
        if ((checkWall(g, x - 1, y - 1) || checkWall(g, x + 1, y - 1))
                && g.checkEnemyMove(this, x, y - 1)) {
            this.direction = Direction.UP;
        } else if ((checkWall(g, x - 1, y + 1) || checkWall(g, x + 1, y + 1))
                && g.checkEnemyMove(this, x, y + 1)) {
            this.direction = Direction.DOWN;
        } else {
            this.direction = g.flipDirection(this.direction);
        }
    }

    /**
     * Move enemy from the TOP of the square to the RIGHT UP position or RIGHT DOWN.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void rightRight(GameManager g, int x, int y) {
        if ((checkWall(g, x + 1, y - 1) || checkWall(g, x + 1, y + 1))
                && g.checkEnemyMove(this, x + 1, y)) {
            x += 1;
            g.updateEnemyLocation(this, x, y);
        } else if ((checkWall(g, x - 1, y - 1) || checkWall(g, x + 1, y - 1))
                && g.checkEnemyMove(this, x, y - 1)) {
            this.direction = Direction.UP;
        } else if ((checkWall(g, x - 1, y + 1) || checkWall(g, x + 1, y + 1))
                && g.checkEnemyMove(this, x, y + 1)) {
            this.direction = Direction.DOWN;
        } else if (g.checkEnemyMove(this, x + 1, y + 1)
                && checkWall(g, x + 1, y)) {
            x += 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x + 1, y - 1)
                && checkWall(g, x + 1, y)) {
            x += 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x + 1, y + 1) && checkWall(g, x, y + 1)) {
            x += 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x + 1, y - 1) && checkWall(g, x, y - 1)) {
            x += 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else {
            this.direction = g.flipDirection(this.direction);
        }
    }

    /**
     * Move enemy down and check ALL possible LEFT or RIGHT directions.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void down(GameManager g, int x, int y) {
        if ((checkWall(g, x - 1, y + 1) || checkWall(g, x + 1, y + 1)) && g.checkEnemyMove(this, x, y + 1)) {
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y - 1) && checkWall(g, x, y - 1)) {
            x -= 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x + 1, y + 1) && checkWall(g, x, y + 1)) {
            x += 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y + 1) && checkWall(g, x - 1, y)) {
            x -= 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if ((checkWall(g, x - 1, y) || checkWall(g, x + 1, y + 1)
                && g.checkEnemyMove(this, x + 1, y))) {
            this.direction = Direction.RIGHT;
        } else {
            this.direction = g.flipDirection(this.direction);
        }
    }

    /**
     * Move enemy up and check if any LEFT or RIGHT directions  cases is occurred.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void up(GameManager g, int x, int y) {
        if ((checkWall(g, x - 1, y - 1) || checkWall(g, x + 1, y - 1))
                && g.checkEnemyMove(this, x, y - 1)) {
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x + 1, y - 1) && checkWall(g, x + 1, y)) {
            x += 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if ((checkWall(g, x - 1, y - 1) || checkWall(g, x - 1, y))
                && g.checkEnemyMove(this, x + 1, y)) {
            this.direction = Direction.RIGHT;
        } else {
            checkWallOne(g, x, y);
        }
    }

    /**
     * Move enemy LEFT direction or move LEFT UP or LEFT DOWN direction.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void left(GameManager g, int x, int y) {
        if ((checkWall(g, x - 1, y + 1) || checkWall(g, x - 1, y - 1)) && g.checkEnemyMove(this, x - 1, y)) {
            x -= 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y + 1) && checkWall(g, x - 1, y)) {
            x -= 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);
        } else if (g.checkEnemyMove(this, x - 1, y - 1) && checkWall(g, x, y - 1)) {
            x -= 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);
        } else {
            checkWallTwo(g, x, y);
        }
    }

    /**
     * Move enemy to the Left if enemy uses RIGHT hand direction.
     *
     * @param g GameManager providing information
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    private void otherRight(GameManager g, int x, int y) {
        if ((checkWall(g, x + 1, y - 1) || checkWall(g, x + 1, y + 1)) && g.checkEnemyMove(this, x + 1, y)) {
            x += 1;
            g.updateEnemyLocation(this, x, y);

        } else if (g.checkEnemyMove(this, x + 1, y - 1) && checkWall(g, x + 1, y)) {
            x += 1;
            y -= 1;
            g.updateEnemyLocation(this, x, y);

        } else if (g.checkEnemyMove(this, x - 1, y + 1) && checkWall(g, x - 1, y)) {
            x -= 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);

        } else if (g.checkEnemyMove(this, x + 1, y + 1) && checkWall(g, x + 1, y)) {
            x += 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);

        } else if (g.checkEnemyMove(this, x - 1, y + 1) && checkWall(g, x - 1, y)) {
            x -= 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);

        } else if (g.checkEnemyMove(this, x + 1, y + 1) && checkWall(g, x, y + 1)) {
            x += 1;
            y += 1;
            g.updateEnemyLocation(this, x, y);

        } else {
            checkWallTwo(g, x, y);
        }
    }

    /**
     * Method to checks actual wall near enemy and obstacle.
     *
     * @param g GameManager
     * @param x X location of the Cell
     * @param y Y location of the Cell
     * @return True if there is a wall
     */
    private boolean checkWall(GameManager g, int x, int y) {
        Cell requestedCell = g.getCurrentMap().getCell(x, y);
        if (requestedCell != null) {
            log.add("Difference okay, continue checks", 0);
            if (!requestedCell.isPassable() || requestedCell.getItem() != null
                    || requestedCell.getClass().getSimpleName().equals("Rain")
                    || requestedCell.getClass().getSimpleName().equals("Wind")
                    || requestedCell.getClass().getSimpleName().equals("Teleporter")) {
                log.add("wall is near, Enemy can move", 0);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}