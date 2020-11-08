package game;

import javafx.scene.paint.Color;

/**
 * This class represent color using different enums.
 *
 * @author Piotr Obara
 * @author Pedro Caetano
 * @version 1.0
 */
public enum ColorEnum {
    BLUE,
    GREEN,
    RED;

    /**
     * Converts the enumerated type into the Color object it represents.
     *
     * @param colorEnum Enum color
     * @return Color javafx
     */
    public javafx.scene.paint.Color getColor(game.ColorEnum colorEnum) {
        if (colorEnum == BLUE) {
            return Color.BLUE;
        } else if (colorEnum == GREEN) {
            return Color.GREEN;
        } else if (colorEnum == RED) {
            return Color.RED;
        }
        return null;
    }
}

