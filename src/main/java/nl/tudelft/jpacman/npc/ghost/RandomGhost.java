package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.Sprite;

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
     * @param ghostSprite The sprite for the ghost.
     */
    RandomGhost(Map<Direction, Sprite> ghostSprite) {
        super(ghostSprite, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    @Override
    public Optional<Direction> nextAiMove() {
        return Optional.empty();
    }
}
