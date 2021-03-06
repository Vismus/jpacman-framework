package nl.tudelft.jpacman.game;

import java.util.List;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.strategies.PacManStrategy;

/**
 * A basic implementation of a Pac-Man game.
 *
 * @author Jeroen Roosen 
 */
public abstract class Game implements LevelObserver {

    /**
     * <code>true</code> if the game is in progress.
     */
    private boolean inProgress;

    /**
     * Object that locks the start and stop methods.
     */
    private final Object progressLock = new Object();

    /**
     * Creates a new game.
     */
    protected Game() {
        inProgress = false;
    }

    /**
     * Starts or resumes the game.
     */
    public void start() {
        synchronized (progressLock) {
            if (isInProgress()) {
                return;
            }
            if (getLevel().isAnyPlayerAlive() && getLevel().remainingPellets(false) > 0) {
                inProgress = true;
                getLevel().addObserver(this);
                getLevel().start();
            }
        }
    }

    /**
     * Pauses the game.
     */
    public void stop() {
        synchronized (progressLock) {
            if (!isInProgress()) {
                return;
            }
            inProgress = false;
            getLevel().stop();
        }
    }

    /**
     * @return <code>true</code> iff the game is started and in progress.
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * @return An immutable list of the participants of this game.
     */
    public abstract List<Player> getPlayers();

    /**
     * @return The level currently being played.
     */
    public abstract Level getLevel();

    /**
     * @return The strategy for the game.
     */
    public abstract PacManStrategy getStrategy();

    /**
     * @param clazz the selected strategy
     */
    public abstract void selectStrategy(Class<? extends PacManStrategy> clazz);

    /**
     * Change the direction of the player in the game.
     * @param player The player
     * @param direction The new direction to adopt
     */
    public void setPlayerDirection(Player player, Direction direction) {
        if (isInProgress()) {
            player.setDirection(direction);
        }
    }

    @Override
    public void levelWon() {
        stop();
    }

    @Override
    public void levelLost() {
        stop();
    }
}
