package nl.tudelft.jpacman.npc;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.strategies.Navigation;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.*;

/**
 * A non-player unit.
 *
 * @author Jeroen Roosen
 */
public abstract class Ghost extends Unit {
    /**
     * The default sprite map, one sprite for each direction.
     */
    private final ArrayList<Map<Direction, Sprite>> ghostSprites;

    /**
     * The base move interval of the ghost.
     */
    private final int moveInterval;

    /**
     * The random variation added to the {@link #moveInterval}.
     */
    private final int intervalVariation;

    /**
     * <code>true</code> iff this ghost is alive.
     */
    private boolean alive;

    /**
     * Initial square of the ghost.
     */
    private final Square initialPosition;

    /**
     * The ghost's current game mode. It only serves to assign sprites.
     */
    private byte gameMode;


    /**
     * Calculates the next move for this unit and returns the direction to move
     * in.
     * <p>
     * Precondition: The NPC occupies a square (hasSquare() holds).
     *
     * @return The direction to move in, or <code>null</code> if no move could
     * be devised.
     */
    public Direction nextMove() {
        if (!this.isAlive()) {
            return Navigation.getNextDirection(this.getSquare(), this.getInitialPosition(), this).orElseGet(this::randomMove);
        } else if (gameMode != 0) {
            return this.randomMove();
        } else {
            return nextAiMove().orElseGet(this::randomMove);
        }
    }

    /**
     * Tries to calculate a move based on the behaviour of the npc.
     *
     * @return an optional containing the move or empty if the current state of the game
     * makes the ai move impossible
     */
    public abstract Optional<Direction> nextAiMove();

    /**
     * Creates a new ghost.
     *
     * @param ghostSprites      An arraylist containing ghost sprites.
     * @param moveInterval      The base interval of movement.
     * @param intervalVariation The variation of the interval.
     * @param initialPosition   The initial square of the ghost.
     */
    protected Ghost(ArrayList<Map<Direction, Sprite>> ghostSprites, int moveInterval, int intervalVariation, Square initialPosition) {
        this.ghostSprites = ghostSprites;
        this.intervalVariation = intervalVariation;
        this.moveInterval = moveInterval;
        this.initialPosition = initialPosition;
        this.gameMode = 0;
        this.alive = true;
    }

    /**
     * Indicates if the ghost is in flee mode.
     *
     * @return the gameMode
     */
    public byte getGameMode() {
        return gameMode;
    }

    /**
     * Set the gamemode of the ghost.
     *
     * @param gameMode 0: ghosts chase pacman
     *                 1: ghosts are vulnerable
     *                 2: ghosts will soon be hunting again
     */
    public void setGameMode(byte gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Returns the ghost's initial position.
     *
     * @return The ghost's initial position.
     */
    public Square getInitialPosition() {
        return this.initialPosition;
    }

    /**
     * Returns whether this ghost is alive or not.
     *
     * @return <code>true</code> iff the ghost is alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets whether this ghost is alive or not.
     *
     * @param isAlive <code>true</code> iff this ghost is alive.
     */
    public void setAlive(boolean isAlive) {
        this.alive = isAlive;
    }

    @Override
    public Sprite getSprite() {
        if (this.isAlive()) {
            return this.ghostSprites.get(this.gameMode).get(this.getDirection());
        } else {
            return this.ghostSprites.get(3).get(this.getDirection());
        }
    }

    /**
     * The time that should be taken between moves.
     *
     * @return The suggested delay between moves in milliseconds.
     */
    public long getInterval() {
        int interval = this.moveInterval;
        if (this.gameMode != 0) {
            interval *= 2;
        }

        if (this.intervalVariation == 0) {
            return interval;
        } else {
            return interval + new Random().nextInt(this.intervalVariation);
        }
    }

    /**
     * Determines a possible move in a random direction.
     *
     * @return A direction in which the ghost can move, or <code>null</code> if
     * the ghost is shut in by inaccessible squares.
     */
    protected Direction randomMove() {
        Square square = getSquare();
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (square.getSquareAt(direction).isAccessibleTo(this)) {
                directions.add(direction);
            }
        }
        if (directions.isEmpty()) {
            return null;
        }
        int i = new Random().nextInt(directions.size());
        return directions.get(i);
    }
}
