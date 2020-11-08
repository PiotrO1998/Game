package game;

/**
 * Allows teleportation of the Player between 2 Teleporters.
 *
 * @author Pedro Caetano
 * @version 1.0
 */
public class Teleporter extends Cell {
    private Teleporter pairedTeleporter;

    /**
     * Creates a Teleporter Cell.
     *
     * @param x X location of the Cell
     * @param y Y location of the Cell
     */
    public Teleporter(int x, int y) {
        super(x, y, true);
    }

    /**
     * Gets the paired Teleporter.
     *
     * @return Paired Teleporter
     */
    Teleporter getPairedTeleporter() {
        return pairedTeleporter;
    }

    /**
     * Sets the paired Teleporter.
     *
     * @param pairedTeleporter Teleporter to pair
     */
    void setPairedTeleporter(Teleporter pairedTeleporter) {
        this.pairedTeleporter = pairedTeleporter;
    }
}
