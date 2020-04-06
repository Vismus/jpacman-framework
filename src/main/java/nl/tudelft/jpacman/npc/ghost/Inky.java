package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * An implementation of the classic Pac-Man ghost Inky.
 * </p>
 * <b>AI:</b> Inky has the most complicated AI of all. Inky considers two things: Blinky's
 * location, and the location two grid spaces ahead of Pac-Man. Inky draws a
 * line from Blinky to the spot that is two squares in front of Pac-Man and
 * extends that line twice as far. Therefore, if Inky is alongside Blinky
 * when they are behind Pac-Man, Inky will usually follow Blinky the whole
 * time. But if Inky is in front of Pac-Man when Blinky is far behind him,
 * Inky tends to want to move away from Pac-Man (in reality, to a point very
 * far ahead of Pac-Man). Inky is affected by a similar targeting bug that
 * affects Speedy. When Pac-Man is moving or facing up, the spot Inky uses to
 * draw the line is two squares above and left of Pac-Man.
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 */
public class Inky extends Ghost {

    private static final int SQUARES_AHEAD = Integer.parseInt(ConfigurationLoader.getProperty("ghost.inky.squares.ahead"));

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = Integer.parseInt(ConfigurationLoader.getProperty("ghost.inky.interval.variation"));

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = Integer.parseInt(ConfigurationLoader.getProperty("ghost.inky.move.interval"));

    /**
     * Creates a new "Inky".
     *
     * @param ghostSprites    An arraylist containing ghost sprites.
     * @param initialPosition The initial position of the ghost.
     */
    public Inky(ArrayList<Map<Direction, Sprite>> ghostSprites, Square initialPosition) {
        super(ghostSprites, MOVE_INTERVAL, INTERVAL_VARIATION, initialPosition);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Inky has the most complicated AI of all. Inky considers two things: Blinky's
     * location, and the location two grid spaces ahead of Pac-Man. Inky
     * draws a line from Blinky to the spot that is two squares in front of
     * Pac-Man and extends that line twice as far. Therefore, if Inky is
     * alongside Blinky when they are behind Pac-Man, Inky will usually
     * follow Blinky the whole time. But if Inky is in front of Pac-Man when
     * Blinky is far behind him, Inky tends to want to move away from Pac-Man
     * (in reality, to a point very far ahead of Pac-Man). Inky is affected
     * by a similar targeting bug that affects Speedy. When Pac-Man is moving or
     * facing up, the spot Inky uses to draw the line is two squares above
     * and left of Pac-Man.
     * </p>
     *
     * <p>
     * <b>Implementation:</b>
     * To actually implement this in jpacman we have the following approximation:
     * first determine the square of Blinky (A) and the square 2
     * squares away from Pac-Man (B). Then determine the shortest path from A to
     * B regardless of terrain and walk that same path from B. This is the
     * destination.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();
        Unit blinky = Navigation.findNearest(Blinky.class, getSquare());
        Unit player = Navigation.findNearest(Player.class, getSquare());

        if (blinky == null || player == null) {
            return Optional.empty();
        }

        assert player.hasSquare();
        Square playerDestination = player.squaresAheadOf(SQUARES_AHEAD);

        List<Direction> firstHalf = Navigation.shortestPath(blinky.getSquare(),
            playerDestination, null);

        if (firstHalf == null) {
            return Optional.empty();
        }

        Square destination = followPath(firstHalf, playerDestination);

        return Navigation.getNextDirection(getSquare(), destination, this);
    }


    private Square followPath(List<Direction> directions, Square start) {
        Square destination = start;

        for (Direction d : directions) {
            destination = destination.getSquareAt(d);
        }

        return destination;
    }
}
