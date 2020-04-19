package nl.tudelft.jpacman.level.unit;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite;
import nl.tudelft.jpacman.strategies.HumanStrategy;
import nl.tudelft.jpacman.strategies.PacManStrategy;

import java.util.ArrayList;
import java.util.List;
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
     * The initial position of the player in the level
     */
    private Square initialPostion;

    /**
     * The list of player observers which will be notify when the player is killed by a ghost
     */
    public final List<PlayerObserver> observers;

    /**
     * The strategy of the player (either it's a human or a IA which move the PacMan.
     */
    private PacManStrategy strategy;

    /**
     * The remaining lifes of the player.
     * By default, the player has the number of lifes defined into the configuration files.
     */
    public int remainingLifes = Integer.parseInt(ConfigurationLoader.getProperty("player.default.number.lifes"));

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = Integer.parseInt(ConfigurationLoader.getProperty("player.move.interval"));

    /**
     * The score that the player must reach to gain an extra life
     */
    private static final int SCORE_GAIN_EXTRA_LIFE = Integer.parseInt(ConfigurationLoader.getProperty("player.score.extra.life"));

    /**
     * Check the player has already gained his extra life
     */
    private boolean receiveExtraLife;

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
        this.observers = new ArrayList<>();
        this.receiveExtraLife = false;
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
     * Returns the remaining lifes of the player
     *
     * @return The remaining lifes of the player
     */
    public int getRemainingLifes() {
        return remainingLifes;
    }

    /**
     * Returns the initial position of the player
     *
     * @return The initial position of the player
     */
    public Square getInitialPostion() {
        return initialPostion;
    }

    /**
     * Sets the initial position for the player
     *
     * @param initialPostion the initial to set for the player
     */
    public void setInitialPostion(Square initialPostion) {
        this.initialPostion = initialPostion;
    }

    /**
     * Return the strategy of the player.
     *
     * @return The strategy of the player.
     */
    public PacManStrategy getStrategy() {
        return strategy;
    }

    /**
     * Set the strategy of the player.
     *
     * @param strategy The strategy to set for the player.
     */
    public void setStrategy(PacManStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Adds points to the score of this player.
     *
     * @param points The amount of points to add to the points this player already
     *               has.
     */
    public void addPoints(int points) {
        score += points;

        if (!receiveExtraLife && score >= SCORE_GAIN_EXTRA_LIFE) {
            gainExtraLife();
        }
    }

    /**
     * Add an extra life, when the player reaches the limit of points to gain an extra life.
     */
    public void gainExtraLife() {
        this.remainingLifes += 1;
        receiveExtraLife = true;
    }

    /**
     * Removes a life to the player
     */
    public void loseLife() {
        this.remainingLifes -= 1;
        if (remainingLifes > 0) {
            notifyObservers();
        } else {
            setAlive(false);
        }
    }

    /**
     * Select the next move for the player based on the strategy of the player.
     *
     * @return the next direction the player must do for the next move.
     */
    public Direction nextMove() {
        if (strategy instanceof HumanStrategy) {
            return getDirection();
        } else {
            return strategy.nextMove();
        }
    }

    /**
     * When the player is registered into the level, then the level is added into the list of observers
     *
     * @param level is the player observer for this player
     */
    public void register(Level level) {
        observers.add(level);
    }

    /**
     * Remove the level to the player observer when the player is unresgisterec to this level.
     *
     * @param level: The level to remove to the list of player observer
     */
    public void unregister(Level level) {
        observers.remove(level);
    }

    /**
     * Notifies to all player observers that the player is killed by a ghost
     */
    public void notifyObservers() {
        observers.forEach(playerObserver -> playerObserver.playerKilled(this));
    }

    /**
     * An observer that will be notified when the player is killed by a ghost.
     */
    public interface PlayerObserver {
        /**
         * When the player is killed by a ghost, we schedule the animation to show the death of the player
         * and then the game restart with the NPCs and the player in their initial position.
         *
         * @param player The player killed by a ghost
         */
        void playerKilled(Player player);
    }
}
