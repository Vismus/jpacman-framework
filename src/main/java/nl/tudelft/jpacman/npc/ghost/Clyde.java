package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.*;

/**
 * <p>
 * An implementation of the classic Pac-Man ghost Clyde.
 * </p>
 * <p>
 * Pokey needs a new nickname because out of all the ghosts,
 * Clyde is the least likely to "C'lyde" with Pac-Man. Clyde is always the last
 * ghost out of the regenerator, and the loner of the gang, usually off doing
 * his own thing when not patrolling the bottom-left corner of the maze. His
 * behavior is very random, so while he's not likely to be following you in hot
 * pursuit with the other ghosts, he is a little less predictable, and still a
 * danger.
 * </p>
 * <p>
 * <b>AI:</b> Clyde has two basic AIs, one for when he's far from Pac-Man, and
 * one for when he is near to Pac-Man.
 * When Clyde is far away from Pac-Man (beyond eight grid spaces),
 * Clyde behaves very much like Blinky, trying to move to Pac-Man's exact
 * location. However, when Clyde gets within eight grid spaces of Pac-Man, he
 * automatically changes his behavior and runs away.
 * </p>
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 */
public class Clyde extends Ghost {

    /**
     * The amount of cells Clyde wants to stay away from Pac Man.
     */
    private static final int SHYNESS = Integer.parseInt(ConfigurationLoader.getProperty("ghost.clyde.shyness"));

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = Integer.parseInt(ConfigurationLoader.getProperty("ghost.clyde.interval.variation"));

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = Integer.parseInt(ConfigurationLoader.getProperty("ghost.clyde.move.interval"));

    /**
     * A map of opposite directions.
     */
    private static final Map<Direction, Direction> OPPOSITES = new EnumMap<>(Direction.class);

    static {
        OPPOSITES.put(Direction.NORTH, Direction.SOUTH);
        OPPOSITES.put(Direction.SOUTH, Direction.NORTH);
        OPPOSITES.put(Direction.WEST, Direction.EAST);
        OPPOSITES.put(Direction.EAST, Direction.WEST);
    }

    /**
     * Creates a new "Clyde", a.k.a. "Pokey".
     *
     * @param ghostSprites    An arraylist containing ghost sprites.
     * @param initialPosition The initial position of the ghost.
     */
    public Clyde(ArrayList<Map<Direction, Sprite>> ghostSprites, Square initialPosition) {
        super(ghostSprites, MOVE_INTERVAL, INTERVAL_VARIATION, initialPosition);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Clyde has two basic AIs, one for when he's far from Pac-Man, and one for
     * when he is near to Pac-Man.
     * When Clyde is far away from Pac-Man (beyond eight grid spaces),
     * Clyde behaves very much like Blinky, trying to move to Pac-Man's exact
     * location. However, when Clyde gets within eight grid spaces of Pac-Man,
     * he automatically changes his behavior and runs away
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();

        Unit nearest = Navigation.findNearest(Player.class, getSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        Square target = nearest.getSquare();

        List<Direction> path = Navigation.shortestPath(getSquare(), target, this);
        if (path != null && !path.isEmpty()) {
            Direction direction = path.get(0);
            if (path.size() <= SHYNESS) {
                return Optional.ofNullable(OPPOSITES.get(direction));
            }
            return Optional.of(direction);
        }
        return Optional.empty();
    }
}
