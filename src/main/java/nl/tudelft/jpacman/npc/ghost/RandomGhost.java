package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;


/**
 * Implementation of an NPC that wanders around randomly.
 *
 * @author Jeroen Roosen
 */
public class RandomGhost extends Ghost {

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = Integer.parseInt(ConfigurationLoader.getProperty("ghost.random.ghost.interval.variation"));

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = Integer.parseInt(ConfigurationLoader.getProperty("ghost.random.ghost.move.interval"));

    /**
     * Creates a new random ghost.
     *
     * @param ghostSprites    An arraylist containing ghost sprites.
     * @param initialPosition The initial position of the ghost.
     */
    public RandomGhost(ArrayList<Map<Direction, Sprite>> ghostSprites, Square initialPosition) {
        super(ghostSprites, MOVE_INTERVAL, INTERVAL_VARIATION, initialPosition);
    }

    @Override
    public Optional<Direction> nextAiMove() {
        return Optional.empty();
    }
}
