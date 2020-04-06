package nl.tudelft.jpacman.level.unit;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * A pellet, one of the little dots Pac-Man has to collect.
 * A pellet can be a simple pellet or a power pellet.
 *
 * @author Jeroen Roosen 
 */
public class Pellet extends Unit {

    /**
     * The sprite of this unit.
     */
    private final Sprite image;

    /**
     * The point value of this pellet.
     */
    private final int value;

    /**
     * A boolean indicating whether the pellet is a power pellet.
     */
    private final boolean powerPellet;

    /**
     * Creates a new pellet.
     * @param powerPellet True if the pellet is a power pellet
     * @param points The point value of this pellet.
     * @param sprite The sprite of this pellet.
     */
    public Pellet(boolean powerPellet, int points, Sprite sprite) {
        this.powerPellet = powerPellet;
        this.image = sprite;
        this.value = points;
    }

    /**
     * Returns the point value of this pellet.
     * @return The point value of this pellet.
     */
    public int getValue() {
        return value;
    }

    /**
     * Pellet type accessor
     * @return true if the pellet is a power pellet, false otherwise.
     */
    public boolean isPowerPellet() {
        return powerPellet;
    }

    @Override
    public Sprite getSprite() {
        return image;
    }
}
