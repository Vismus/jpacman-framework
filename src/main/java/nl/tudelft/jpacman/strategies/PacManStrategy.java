package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;

public abstract class PacManStrategy {
    /**
     * The reference of the game
     */
    private Game game;

    /**
     * Constructor PacManStrategy.
     *
     * @param game
     */
    public PacManStrategy(Game game) {
        this.game = game;
    }

    /**
     * Return the game.
     *
     * @return The game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Give the next direction the player will take for his next move.
     *
     * @return The next direction the player will take for his next move.
     */
    public abstract Direction nextMove();
}
