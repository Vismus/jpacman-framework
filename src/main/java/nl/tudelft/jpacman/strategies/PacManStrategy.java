package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;

public abstract class PacManStrategy {
    private Game game;

    public PacManStrategy(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract Direction nextMove();
}
