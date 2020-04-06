package nl.tudelft.jpacman.level.unit;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.Map;

/**
 * A player operated unit in our game.
 *
 * @author Jeroen Roosen
 */
public class Player extends Unit {

    /**
     * The amount of points accumulated by this player.
     */
    private int score;

    /**
     * The animations for every direction.
     */
    private final Map<Direction, Sprite> sprites;

    /**
     * The animation that is to be played when Pac-Man dies.
     */
    private final AnimatedSprite deathSprite;

    /**
     * <code>true</code> iff this player is alive.
     */
    private boolean alive;

    /**
     * Integer indicating the number of successive kills during hunting mode.
     */
    private int consecutiveKills;

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = Integer.parseInt(ConfigurationLoader.getProperty("player.move.interval"));

    /**
     * Creates a new player with a score of 0 points.
     *
     * @param spriteMap      A map containing a sprite for this player for every direction.
     * @param deathAnimation The sprite to be shown when this player dies.
     */
    public Player(Map<Direction, Sprite> spriteMap, AnimatedSprite deathAnimation) {
        this.score = 0;
        this.alive = true;
        this.sprites = spriteMap;
        this.deathSprite = deathAnimation;
        this.consecutiveKills = 0;
        deathSprite.setAnimating(false);
    }

    /**
     * Returns whether this player is alive or not.
     *
     * @return <code>true</code> iff the player is alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Accessor for the number of consecutive kills during hunting mode.
     *
     * @return The number of consecutive kills
     */
    public int getConsecutiveKills() {
        return this.consecutiveKills;
    }

    /**
     * Mutator for the number of consecutive kills during hunting mode.
     *
     * @param consecutiveKills The new number of consecutive kills
     */
    public void setConsecutiveKills(int consecutiveKills) {
        this.consecutiveKills = consecutiveKills;
    }

    /**
     * Sets whether this player is alive or not.
     *
     * @param isAlive <code>true</code> iff this player is alive.
     */
    public void setAlive(boolean isAlive) {
        if (isAlive) {
            deathSprite.setAnimating(false);
        }
        if (!isAlive) {
            deathSprite.restart();
        }
        this.alive = isAlive;
    }

    /**
     * Returns the amount of points accumulated by this player.
     *
     * @return The amount of points accumulated by this player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the player's movement interval
     *
     * @return The player's movement interval
     */
    public int getInterval() {
        return MOVE_INTERVAL;
    }

    @Override
    public Sprite getSprite() {
        if (isAlive()) {
            return sprites.get(getDirection());
        }
        return deathSprite;
    }

    /**
     * Adds points to the score of this player.
     *
     * @param points The amount of points to add to the points this player already
     *               has.
     */
    public void addPoints(int points) {
        score += points;
    }
}
