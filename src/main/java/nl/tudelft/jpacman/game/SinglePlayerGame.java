package nl.tudelft.jpacman.game;

import java.util.List;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.unit.Player;

import com.google.common.collect.ImmutableList;
import nl.tudelft.jpacman.strategies.PacManStrategy;

/**
 * A game with one player and a single level.
 *
 * @author Jeroen Roosen 
 */
public class SinglePlayerGame extends Game {

    /**
     * The player of this game.
     */
    private final Player player;

    /**
     * The level of this game.
     */
    private final Level level;

    /**
     * Create a new single player game for the provided level and player.
     *
     * @param player
     *            The player.
     * @param level
     *            The level.
     */
    protected SinglePlayerGame(Player player, Level level) {
        assert player != null;
        assert level != null;

        this.player = player;
        this.player.setStrategy(getStrategy());
        this.level = level;
        this.level.registerPlayer(player);
    }

    @Override
    public List<Player> getPlayers() {
        return ImmutableList.of(player);
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public PacManStrategy getStrategy() {
        return this.player.getStrategy();
    }

    @Override
    public void setStrategy(PacManStrategy strategy) {
        this.player.setStrategy(strategy);
    }

    @Override
    public void halfPelletsEaten() {
        this.level.placeFruit();
    }
}
