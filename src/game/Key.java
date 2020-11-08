package game;

import java.io.Serializable;

/**
 * Coloured Key that can be used to unlock a KeyDoor of the same colour.
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @version 1.0
 */
class Key extends Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private ColorEnum colorEnum;

    /**
     * Creates a Key item of the respective Colour.
     *
     * @param colorEnum Color enum of the Key
     */
    Key(ColorEnum colorEnum) {
        super();
        setColorEnum(colorEnum);
    }

    /**
     * Gets the colour of the Key.
     *
     * @return Color of the Key
     */
    ColorEnum getColorEnum() {
        return this.colorEnum;
    }

    /**
     * Sets the colour of the Key.
     *
     * @param colorEnum Color enum too be set
     */
    private void setColorEnum(ColorEnum colorEnum) {
        this.colorEnum = colorEnum;
    }
}
