package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;

public final class HumanStrategy extends PacManStrategy {
    /**
     * The title of this strategy which will be displayed in the UI
     * to select the strategy for the player for the new game.
     */
    public static String TITLE = "Key Controlled";

    /**
     * Create the human strategy for a player.
     *
     * @param game the current game.
     */
    public HumanStrategy(Game game) {
        super(game);
    }

    @Override
    public Direction nextMove() {
        return null;
    }
}
