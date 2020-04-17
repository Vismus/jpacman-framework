package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;

public class HumanStrategy extends PacManStrategy {
    public static String TITLE = "Key Controlled";

    public HumanStrategy(Game game) {
        super(game);
    }

    @Override
    public Direction nextMove() {
        return null;
    }
}
