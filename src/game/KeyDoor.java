package game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Door Cell that can be unlocked by a Key of the same colour.
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @version 2.0
 */
public class KeyDoor extends Door implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Log log;
    private ColorEnum colorEnum;

    /**
     * Creates a KeyDoor of the provided colour.
     *
     * @param x         X location of the Door
     * @param y         Y location of the Door
     * @param colorEnum Colour enum of the Door
     */
    KeyDoor(int x, int y, ColorEnum colorEnum) {
        super(x, y);
        setColorEnum(colorEnum);
        log = new Log(this.getClass().getSimpleName());
        log.setImportance(1);
    }

    /**
     * Checks Player inventory to check if it's Inventory contains the correct colour Key to open the door.
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
            for (int i = 0; i < inventory.size(); i++) {
                Item item = inventory.get(i);
                if (item instanceof Key) {
                    Key currentKey = (Key) item;
                    if (currentKey.getColorEnum() == this.getColorEnum()) {
                        log.add("Key found, unlocking door", 1);
                        player.removeItem(item);
                        this.setPassable(true);
                    }
                }
            }
        }
    }

    /**
     * Returns the colour of the door.
     *
     * @return colorEnum Color enum of the door
     */
    ColorEnum getColorEnum() {
        return this.colorEnum;
    }

    /**
     * Sets the colour of the Key Door.
     *
     * @param colorEnum Color to set
     */
    private void setColorEnum(ColorEnum colorEnum) {
        this.colorEnum = colorEnum;
    }
}