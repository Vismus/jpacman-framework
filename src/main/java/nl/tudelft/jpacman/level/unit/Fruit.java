package nl.tudelft.jpacman.level.unit;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * A fruit, one of the fruits Pac-Man can eat to increase the score. A fruit can
 * be a cherry, a strawberry, an orange, an apple and a melon
 *
 * @author Xavier Bol
 */
public class Fruit extends Unit {

    /**
     * The sprite of this unit.
     */
    private final Sprite image;

    /**
     * The point value of this fruit.
     */
    private final int value;

    public Fruit(int points, Sprite sprite) {
        this.image = sprite;
        this.value = points;
    }

    /**
     * @return the point value of this fruit
     */
    public int getValue() {
        return value;
    }

    @Override
    public Sprite getSprite() {
        return image;
    }
}
