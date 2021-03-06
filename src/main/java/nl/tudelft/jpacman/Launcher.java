package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.exceptions.PacmanConfigurationException;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.strategies.PacManStrategy;
import nl.tudelft.jpacman.ui.Action;
import nl.tudelft.jpacman.ui.PacManUI;
import nl.tudelft.jpacman.ui.PacManUiBuilder;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

/**
 * Creates and launches the JPacMan UI.
 *
 * @author Jeroen Roosen
 */
@SuppressWarnings("PMD.TooManyMethods")
public class Launcher {

    private PacManSprites spriteStore;
    private String levelMap;
    private PacManUI pacManUI;
    private Game game;

    public Launcher(String configurationPath) {
        ConfigurationLoader.load(configurationPath);
        levelMap = ConfigurationLoader.getProperty("level.map");
        spriteStore = new PacManSprites();
    }

    /**
     * @return The game object this launcher will start when {@link #launch()}
     * is called.
     */
    public Game getGame() {
        return game;
    }

    /**
     * The map file used to populate the level.
     *
     * @return The name of the map file.
     */
    protected String getLevelMap() {
        return levelMap;
    }

    /**
     * Set the name of the file containing this level's map.
     *
     * @param fileName Map to be used.
     * @return Level corresponding to the given map.
     */
    public Launcher withMapFile(String fileName) {
        levelMap = fileName;
        return this;
    }

    /**
     * Creates a new game using the level from {@link #makeLevel()}.
     */
    public void makeGame() {
        GameFactory gf = getGameFactory();
        Level level = makeLevel();
        game = gf.createSinglePlayerGame(level);
    }

    /**
     * Creates a new level. By default this method will use the map parser to
     * parse the default board stored in the <code>board.txt</code> resource.
     *
     * @return A new level.
     */
    public Level makeLevel() {
        try {
            return getMapParser().parseMap(getLevelMap());
        } catch (IOException e) {
            throw new PacmanConfigurationException("Unable to create level, name = " + getLevelMap(), e);
        }
    }

    /**
     * @return A new map parser object using the factories from
     * {@link #getLevelFactory()} and {@link #getBoardFactory()}.
     */
    protected MapParser getMapParser() {
        return new MapParser(getLevelFactory(), getBoardFactory());
    }

    /**
     * @return A new board factory using the sprite store from
     * {@link #getSpriteStore()}.
     */
    protected BoardFactory getBoardFactory() {
        return new BoardFactory(getSpriteStore());
    }

    /**
     * @return The default {@link PacManSprites}.
     */
    protected PacManSprites getSpriteStore() {
        return spriteStore;
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}
     * and the ghosts from {@link #getGhostFactory()}.
     */
    protected LevelFactory getLevelFactory() {
        return new LevelFactory(getSpriteStore(), getGhostFactory(), getFruitFactory());
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    protected GhostFactory getGhostFactory() {
        return new GhostFactory(getSpriteStore());
    }

    /**
     * @return A new factory using the players from {@link #getPlayerFactory()}.
     */
    protected GameFactory getGameFactory() {
        return new GameFactory(getPlayerFactory());
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    protected PlayerFactory getPlayerFactory() {
        return new PlayerFactory(getSpriteStore());
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    protected FruitFactory getFruitFactory() {
        return new FruitFactory(getSpriteStore());
    }

    /**
     * Adds key events UP, DOWN, LEFT and RIGHT to a game.
     *
     * @param builder The {@link PacManUiBuilder} that will provide the UI.
     */
    protected void addSinglePlayerKeys(final PacManUiBuilder builder) {
        builder.addKey(KeyEvent.VK_UP, moveTowardsDirection(Direction.NORTH))
            .addKey(KeyEvent.VK_DOWN, moveTowardsDirection(Direction.SOUTH))
            .addKey(KeyEvent.VK_LEFT, moveTowardsDirection(Direction.WEST))
            .addKey(KeyEvent.VK_RIGHT, moveTowardsDirection(Direction.EAST));
    }

    /**
     * Changes the direction of movement of the unit.
     *
     * @param direction The new direction to adopt
     * @return An action that can be executed
     */
    private Action moveTowardsDirection(Direction direction) {
        return () -> {
            assert game != null;
            getGame().setPlayerDirection(getSinglePlayer(getGame()), direction);
        };
    }

    private Player getSinglePlayer(final Game game) {
        List<Player> players = game.getPlayers();
        if (players.isEmpty()) {
            throw new IllegalArgumentException("Game has 0 players.");
        }
        return players.get(0);
    }

    /**
     * Creates and starts a JPac-Man game.
     */
    public void launch() {
        if (getGame() == null) {
            makeGame();
        }
        PacManUiBuilder builder = new PacManUiBuilder().withDefaultButtons();
        addSinglePlayerKeys(builder);
        pacManUI = builder.build(getGame());
        pacManUI.start();
    }

    /**
     * Creates and starts a JPac-Man game.
     * This method is useful to facilitate the unit test
     *
     * @param clazz The class to generate the strategy for PacMan.
     */
    public void launch(Class<? extends PacManStrategy> clazz) {
        makeGame();
        try {
            game.selectStrategy(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch();
    }


    /**
     * Disposes of the UI. For more information see
     * {@link javax.swing.JFrame#dispose()}.
     * <p>
     * Precondition: The game was launched first.
     */
    public void dispose() {
        assert pacManUI != null;
        pacManUI.dispose();
    }

    /**
     * Main execution method for the Launcher.
     *
     * @param args The command line arguments - which are ignored.
     */
    public static void main(String[] args) {
        new Launcher("src/main/resources/configuration.properties").launch();
    }
}
