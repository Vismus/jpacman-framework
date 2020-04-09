package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.unit.Fruit;
import nl.tudelft.jpacman.level.unit.Pellet;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;

import java.util.List;

/**
 * Factory that creates levels and units.
 *
 * @author Jeroen Roosen
 */
public class LevelFactory {

    private static final int GHOSTS = Integer.parseInt(ConfigurationLoader.getProperty("level.factory.ghost.types"));
    private static final int BLINKY = 0;
    private static final int INKY = 1;
    private static final int PINKY = 2;
    private static final int CLYDE = 3;

    /**
     * The default value of a pellet.
     */
    private static final int PELLET_VALUE = Integer.parseInt(ConfigurationLoader.getProperty("level.factory.pellet.value"));

    /**
     * The default value of a power pellet.
     */
    private static final int POWER_PELLET_VALUE = Integer.parseInt(ConfigurationLoader.getProperty("level.factory.power.pellet.value"));

    /**
     * The sprite store that provides sprites for units.
     */
    private final PacManSprites sprites;

    /**
     * Used to cycle through the various ghost types.
     */
    private int ghostIndex;

    /**
     * The factory providing ghosts.
     */
    private final GhostFactory ghostFact;

    /**
     * The factory providing the fruits.
     */
    private final FruitFactory fruitFactory;

    /**
     * Creates a new level factory.
     *
     * @param spriteStore  The sprite store providing the sprites for units.
     * @param ghostFactory The factory providing ghosts.
     */
    public LevelFactory(PacManSprites spriteStore, GhostFactory ghostFactory, FruitFactory fruitFactory) {
        this.sprites = spriteStore;
        this.ghostIndex = -1;
        this.ghostFact = ghostFactory;
        this.fruitFactory = fruitFactory;
    }

    /**
     * Creates a new level from the provided data.
     *
     * @param board          The board with all ghosts and pellets occupying their squares.
     * @param ghosts         A list of all ghosts on the board.
     * @param startPositions A list of squares from which players may start the game.
     * @return A new level for the board.
     */
    public Level createLevel(Board board, List<Ghost> ghosts, List<Square> startPositions, List<Square> fruitPositions) {
        return new Level(board, ghosts, startPositions, fruitPositions, fruitFactory);
    }

    /**
     * Creates a new ghost.
     *
     * @param position The initial position of the ghost
     * @return The new ghost.
     */
    Ghost createGhost(Square position) {
        ghostIndex++;
        ghostIndex %= GHOSTS;
        switch (ghostIndex) {
            case BLINKY:
                return ghostFact.createBlinky(position);
            case INKY:
                return ghostFact.createInky(position);
            case PINKY:
                return ghostFact.createPinky(position);
            case CLYDE:
                return ghostFact.createClyde(position);
            default:
                return ghostFact.createRandomGhost(position);
        }
    }

    /**
     * Creates a new pellet.
     *
     * @return The new pellet.
     */
    public Pellet createPellet() {
        return new Pellet(false, PELLET_VALUE, sprites.getPelletSprite());
    }

    /**
     * Creates a new power pellet.
     *
     * @return The new power pellet.
     */
    public Pellet createPowerPellet() {
        return new Pellet(true, POWER_PELLET_VALUE, sprites.getPowerPelletSprite());
    }
}
