package nl.tudelft.jpacman.sprite;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.exceptions.PacmanConfigurationException;
import nl.tudelft.jpacman.level.FruitType;
import nl.tudelft.jpacman.npc.ghost.GhostColor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Sprite Store containing the classic Pac-Man sprites.
 *
 * @author Jeroen Roosen
 */
public class PacManSprites extends SpriteStore {

    /**
     * The sprite files are vertically stacked series for each direction, this
     * array denotes the order.
     */
    private static final Direction[] DIRECTIONS = {
        Direction.NORTH,
        Direction.EAST,
        Direction.SOUTH,
        Direction.WEST
    };

    /**
     * The image size in pixels.
     */
    private static final int SPRITE_SIZE = Integer.parseInt(ConfigurationLoader.getProperty("sprite.pacmansprite.size"));

    /**
     * The amount of frames in the pacman animation.
     */
    private static final int PACMAN_ANIMATION_FRAMES = Integer.parseInt(ConfigurationLoader.getProperty("sprite.pacmansprite.pacman.animation.frames"));

    /**
     * The amount of frames in the pacman dying animation.
     */
    private static final int PACMAN_DEATH_FRAMES = Integer.parseInt(ConfigurationLoader.getProperty("sprite.pacmansprite.pacman.death.frames"));

    /**
     * The amount of frames in the ghost animation.
     */
    private static final int GHOST_ANIMATION_FRAMES = Integer.parseInt(ConfigurationLoader.getProperty("sprite.pacmansprite.ghost.animation.frames"));

    /**
     * The amount of frames in the dead ghost animation.
     */
    private static final int GHOST_DEAD_ANIMATION_FRAMES = Integer.parseInt(ConfigurationLoader.getProperty("sprite.pacmansprite.ghost.dead.animation.frames"));

    /**
     * The delay between frames.
     */
    private static final int ANIMATION_DELAY = Integer.parseInt(ConfigurationLoader.getProperty("sprite.pacmansprite.animation.delay"));

    /**
     * @return A map of animated Pac-Man sprites for all directions.
     */
    public Map<Direction, Sprite> getPacmanSprites() {
        return directionSprite("/sprite/pacman.png", PACMAN_ANIMATION_FRAMES);
    }

    /**
     * @return The animation of a dying Pac-Man.
     */
    public AnimatedSprite getPacManDeathAnimation() {
        String resource = "/sprite/dead.png";

        Sprite baseImage = loadSprite(resource);

        return createAnimatedSprite(baseImage, PACMAN_DEATH_FRAMES, ANIMATION_DELAY, false);
    }

    /**
     * Returns a new map with animations for all directions.
     *
     * @param resource The resource name of the sprite.
     * @param frames   The number of frames in this sprite.
     * @return The animated sprite facing the given direction.
     */
    private Map<Direction, Sprite> directionSprite(String resource, int frames) {
        Map<Direction, Sprite> sprite = new HashMap<>();

        Sprite baseImage = loadSprite(resource);
        for (int i = 0; i < DIRECTIONS.length; i++) {
            Sprite directionSprite = baseImage.split(0, i * SPRITE_SIZE, frames * SPRITE_SIZE, SPRITE_SIZE);
            AnimatedSprite animation = createAnimatedSprite(directionSprite,
                frames, ANIMATION_DELAY, true);
            animation.setAnimating(true);
            sprite.put(DIRECTIONS[i], animation);
        }

        return sprite;
    }

    /**
     * Returns a map of animated ghost sprites for all directions.
     *
     * @param color The colour of the ghost.
     * @return The Sprite for the ghost.
     */
    public Map<Direction, Sprite> getGhostSprite(GhostColor color) {
        assert color != null;

        String resource = "/sprite/ghost_" + color.name().toLowerCase()
            + ".png";
        return directionSprite(resource, GHOST_ANIMATION_FRAMES);
    }

    /**
     * Returns a map of animated fleeing ghost sprites for all directions.
     *
     * @return The Sprite for the ghost.
     */
    public Map<Direction, Sprite> getFleeGhostSprite() {
        return directionSprite("/sprite/ghost_vul_blue.png", GHOST_ANIMATION_FRAMES);
    }

    /**
     * Returns a map of animated sprites for the ghost at the end of flee mode in all directions.
     *
     * @return The Sprite for the ghost.
     */
    public Map<Direction, Sprite> getEndingFleeGhostSprite() {
        return directionSprite("/sprite/ghost_vul_end.png", GHOST_ANIMATION_FRAMES);
    }

    /**
     * Returns a map of animated sprites for the dead ghost in all directions.
     *
     * @return The Sprite for the ghost.
     */
    public Map<Direction, Sprite> getDeadGhostSprites() {
        return directionSprite("/sprite/ghost_eyes.png", GHOST_DEAD_ANIMATION_FRAMES);
    }

    /**
     * @return The sprite for the wall.
     */
    public Sprite getWallSprite() {
        return loadSprite("/sprite/wall.png");
    }

    /**
     * @return The sprite for the ground.
     */
    public Sprite getGroundSprite() {
        return loadSprite("/sprite/floor.png");
    }

    /**
     * @return The sprite for the pellet
     */
    public Sprite getPelletSprite() {
        return loadSprite("/sprite/pellet.png");
    }

    /**
     * @return The sprite for the power pellet
     */
    public Sprite getPowerPelletSprite() {
        Sprite baseImage = loadSprite("/sprite/powerpellet.png");
        Sprite powerPellet = baseImage.split(0, 0, 2 * SPRITE_SIZE, SPRITE_SIZE);
        AnimatedSprite animation = createAnimatedSprite(powerPellet, 2, ANIMATION_DELAY, true);
        animation.setAnimating(true);
        return animation;
    }

    public Sprite getFruit(FruitType type) {
        assert type != null;

        String resource = "/sprite/" + type.name().toLowerCase() + ".png";

        return loadSprite(resource);
    }

    /**
     * Overloads the default sprite loading, ignoring the exception. This class
     * assumes all sprites are provided, hence the exception will be thrown as a
     * {@link RuntimeException}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Sprite loadSprite(String resource) {
        try {
            return super.loadSprite(resource);
        } catch (IOException e) {
            throw new PacmanConfigurationException("Unable to load sprite: " + resource, e);
        }
    }
}
