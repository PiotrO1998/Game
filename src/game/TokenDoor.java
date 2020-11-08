package game;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A Door Cell that can be unlocked by having sufficient Tokens.
 *
 * @author Pedro Caetano
 * @version 2.1
 */
public class TokenDoor extends Door {
    private final Log log;
    private int requirement;

    /**
     * Creates a TokenDoor with the provided Token Requirement.
     *
     * @param x           X location of the Cell
     * @param y           Y location of the Cell
     * @param requirement Tokens required to open door
     */
    TokenDoor(int x, int y, int requirement) {
        super(x, y);
        setRequirement(requirement);
        log = new Log(this.getClass().getSimpleName());
        log.setImportance(0);
    }

    /**
     * Checks Player inventory to check if it's Inventory contains enough Tokens to open the door
     * If so, remove them from the Inventory and open the door.
     *
     * @param player Player who's Inventory to check
     */
    @Override
    public void open(Player player) {
        ArrayList<Item> inventory = player.getInventory();
        if (this.isPassable()) {
            log.add("Door already open", 1);
        } else {
            log.add("Going through Inventory", 0);
            int tokenCount = player.getTokenCount();
            if (tokenCount >= requirement) {
                log.add("Enough tokens (" + requirement + "), unlocking door", 1);
                int toRemove = requirement;
                Collection<Item> items = new ArrayList<>();
                for (int i = 0; i < inventory.size(); i++) {
                    Item item = inventory.get(i);
                    if (item instanceof Token) {
                        if (toRemove > 0) {
                            toRemove = toRemove - 1;
                            log.add("Removing a token, " + toRemove + " remaining.", 0);
                            items.add(item);
                        }
                    }
                }
                for (Item item : items) {
                    player.removeItem(item);
                }
                this.setPassable(true);
            }
        }
    }

    /**
     * Gets number of Tokens required to open the Door.
     *
     * @return Number of Tokens required
     */
    public int getRequirement() {
        return requirement;
    }

    /**
     * Set the amount of Tokens required to open the door.
     *
     * @param requirement Tokens required to open door
     */
    private void setRequirement(int requirement) {
        this.requirement = requirement;
    }
}
