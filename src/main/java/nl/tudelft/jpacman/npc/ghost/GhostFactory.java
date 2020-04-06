package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Factory that creates ghosts.
 *
 * @author Jeroen Roosen
 */
public class GhostFactory {

    /**
     * The sprite store containing the ghost sprites.
     */
    private final PacManSprites sprites;

    /**
     * Creates a new ghost factory.
     *
     * @param spriteStore The sprite provider.
     */
    public GhostFactory(PacManSprites spriteStore) {
        this.sprites = spriteStore;
    }

    /**
     * Creates a new Blinky / Shadow, the red Ghost.
     *
     * @return A new Blinky.
     * @see Blinky
     */
    public Ghost createBlinky(Square initialPosition) {
        return new Blinky(this.getGhostSprites(GhostColor.RED), initialPosition);
    }

    /**
     * Creates a new Pinky / Speedy, the pink Ghost.
     *
     * @return A new Pinky.
     * @see Pinky
     */
    public Ghost createPinky(Square initialPosition) {
        return new Pinky(this.getGhostSprites(GhostColor.PINK), initialPosition);
    }

    /**
     * Creates a new Inky / Bashful, the cyan Ghost.
     *
     * @return A new Inky.
     * @see Inky
     */
    public Ghost createInky(Square initialPosition) {
        return new Inky(this.getGhostSprites(GhostColor.CYAN), initialPosition);
    }

    /**
     * Creates a new Clyde / Pokey, the orange Ghost.
     *
     * @return A new Clyde.
     * @see Clyde
     */
    public Ghost createClyde(Square initialPosition) {
        return new Clyde(this.getGhostSprites(GhostColor.ORANGE), initialPosition);
    }

    /**
     * Creates a new random ghost.
     *
     * @return A random ghost
     */
    public Ghost createRandomGhost(Square initialPosition) {
        return new RandomGhost(this.getGhostSprites(GhostColor.RED), initialPosition);
    }

    /**
     * Build an arraylist containing all the sprites for a ghost.
     *
     * @param color The color of the ghost
     * @return An arraylist containing the ghost sprites
     */
    private ArrayList<Map<Direction, Sprite>> getGhostSprites(GhostColor color) {
        ArrayList<Map<Direction, Sprite>> ghostSprites = new ArrayList<>();
        Collections.addAll(ghostSprites, sprites.getGhostSprite(color), sprites.getFleeGhostSprite(), sprites.getEndingFleeGhostSprite(), sprites.getDeadGhostSprites());
        return ghostSprites;
    }
}
