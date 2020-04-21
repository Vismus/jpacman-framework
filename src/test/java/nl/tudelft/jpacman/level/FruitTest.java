package nl.tudelft.jpacman.level;

import com.google.common.collect.Lists;
import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.unit.Fruit;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * Tests various aspects of fruit in a level.
 *
 * @author Xavier Bol
 */
public class FruitTest {
    /**
     * the game to launch to test if a fruit appears
     * when Pacman eats the half of pellets on the board.
     */
    private Game game;

    /**
     * The level to set for the tests
     */
    private Level level;

    /**
     * The square which can contain the fruit.
     */
    private Square fruitSquare;

    /**
     * The fruit factory to easily create one fruit for the tests.
     */
    private FruitFactory fruitCreator;

    /**
     * A Pacman for the tests
     */
    private Player p;

    /**
     * Sets up the game with a default board, a starting square contains a player.
     */
    @BeforeEach
    void setUp() {
        ConfigurationLoader.load("src/test/resources/configuration.properties");
        PacManSprites sprites = new PacManSprites();
        GameFactory gf = new GameFactory(new PlayerFactory(sprites));
        p = new Player(mock(Map.class), mock(AnimatedSprite.class));

        fruitCreator = new FruitFactory(sprites);
        MapParser parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites), fruitCreator), new BoardFactory(sprites));
        Board board = parser
            .parseMap(Lists.newArrayList("# ..#", "# F.#", "#...#"))
            .getBoard();
        Square playerSquare = board.squareAt(1, 0);
        fruitSquare = board.squareAt(2, 1);
        level = new Level(board, Lists.newArrayList(), Lists.newArrayList(playerSquare), Lists.newArrayList(fruitSquare), fruitCreator);
        level.registerPlayer(p);
        game = gf.createSinglePlayerGame(level);
    }

    /**
     * Test when Pacman eats the half of pellets on the board game,
     * a fruit appears on the fruit square.
     */
    @Test
    void testFruitAppears() {
        game.start();
        level.move(p, Direction.EAST);
        level.move(p, Direction.EAST);
        level.move(p, Direction.SOUTH);
        assert (p.getScore() == 3 * Integer.parseInt(ConfigurationLoader.getProperty("level.factory.pellet.value")));
        assert (level.getNbPellets() / 2 == level.remainingPellets(false));
        level.move(p, Direction.SOUTH);
        assert (fruitSquare.getOccupants().get(0) instanceof Fruit);
    }

    /**
     * Test the creation of a cherry and validate when Pacman eats the fruit
     * the score increase by 100 points.
     */
    @Test
    void testEatsCherry() {
        fruitCreator.createCherry().occupy(fruitSquare);
        assert (fruitSquare.getOccupants().get(0) instanceof Fruit);

        level.start();
        level.move(p, Direction.SOUTH);
        assert (p.getScore() == 0);
        level.move(p, Direction.EAST);
        assert fruitSquare.getOccupants().size() == 1 && fruitSquare.getOccupants().get(0) instanceof Player;
        assert p.getScore() == Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.cherry.value"));
    }

    /**
     * Test the creation of a strawberry and validate when Pacman eats the fruit
     * the score increase by 300 points.
     */
    @Test
    void testEatsStrawberry() {
        fruitCreator.createStrawberry().occupy(fruitSquare);
        assert (fruitSquare.getOccupants().get(0) instanceof Fruit);

        level.start();
        level.move(p, Direction.SOUTH);
        assert p.getScore() == 0;
        level.move(p, Direction.EAST);
        assert fruitSquare.getOccupants().size() == 1 && fruitSquare.getOccupants().get(0) instanceof Player;
        assert p.getScore() == Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.strawberry.value"));
    }

    /**
     * Test the creation of an orange and validate when Pacman eats the fruit
     * the score increase by 500 points.
     */
    @Test
    void testEatsOrange() {
        fruitCreator.createOrange().occupy(fruitSquare);
        assert fruitSquare.getOccupants().get(0) instanceof Fruit;

        level.start();
        level.move(p, Direction.SOUTH);
        assert p.getScore() == 0;
        level.move(p, Direction.EAST);
        assert fruitSquare.getOccupants().size() == 1 && fruitSquare.getOccupants().get(0) instanceof Player;
        assert p.getScore() == Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.orange.value"));
    }

    /**
     * Test the creation of an apple and validate when Pacman eats the fruit
     * the score increase by 700 points.
     */
    @Test
    void testApple() {
        fruitCreator.createApple().occupy(fruitSquare);
        assert fruitSquare.getOccupants().get(0) instanceof Fruit;

        level.start();
        level.move(p, Direction.SOUTH);
        assert p.getScore() == 0;
        level.move(p, Direction.EAST);
        assert fruitSquare.getOccupants().size() == 1 && fruitSquare.getOccupants().get(0) instanceof Player;
        assert p.getScore() == Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.apple.value"));
    }

    /**
     * Test the creation of a melon and validate when Pacman eats the fruit
     * the score increase by 1000 points.
     */
    @Test
    void testMelon() {
        fruitCreator.createMelon().occupy(fruitSquare);
        assert fruitSquare.getOccupants().get(0) instanceof Fruit;

        level.start();
        level.move(p, Direction.SOUTH);
        assert p.getScore() == 0;
        level.move(p, Direction.EAST);
        assert fruitSquare.getOccupants().size() == 1 && fruitSquare.getOccupants().get(0) instanceof Player;
        assert p.getScore() == Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.melon.value"));
    }
}
