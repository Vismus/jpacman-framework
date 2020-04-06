package nl.tudelft.jpacman.level;

import com.google.common.collect.Lists;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.Blinky;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests various aspects of level.
 *
 * @author Jeroen Roosen
 */
// The four suppress warnings ignore the same rule, which results in 4 same string literals
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.TooManyStaticImports"})
class LevelTest {

    /**
     * The level under test.
     */
    private Level level;

    /**
     * An NPC on this level.
     */
    private Ghost ghost;

    /**
     * Starting position 1.
     */
    private Square square1;

    /**
     * Starting position 2.
     */
    private Square square2;

    private MapParser parser;

    /**
     * The board for this level.
     */
    private Board board;

    /**
     * Sets up the level with the default board, a single NPC and a starting
     * square.
     */
    @BeforeEach
    void setUp() {
        PacManSprites sprites = new PacManSprites();
        parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
        board = parser
            .parseMap(Lists.newArrayList("#####", "# o #", "#####"))
            .getBoard();
        square1 = board.squareAt(1, 1);
        square2 = board.squareAt(3, 1);
        ghost = new Blinky(mock(ArrayList.class), square2);
        ghost.occupy(square2);

        level = new Level(board, Lists.newArrayList(ghost), Lists.newArrayList(square1, square2));
    }

    /**
     * Validates the state of the level when it isn't started yet.
     */
    @Test
    void noStart() {
        assertThat(level.isInProgress()).isFalse();
    }

    /**
     * Validates the state of the level when it is stopped without starting.
     */
    @Test
    void stop() {
        level.stop();
        assertThat(level.isInProgress()).isFalse();
    }

    /**
     * Validates the state of the level when it is started.
     */
    @Test
    void start() {
        level.start();
        assertThat(level.isInProgress()).isTrue();
    }

    /**
     * Validates the state of the level when it is started then stopped.
     */
    @Test
    void startStop() {
        level.start();
        level.stop();
        assertThat(level.isInProgress()).isFalse();
    }

    /**
     * Verifies registering a player puts the player on the correct starting
     * square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void registerPlayer() {
        Player p = mock(Player.class);
        level.registerPlayer(p);
        verify(p).occupy(square1);
    }

    /**
     * Verifies registering a player twice does not do anything.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void registerPlayerTwice() {
        Player p = mock(Player.class);
        level.registerPlayer(p);
        level.registerPlayer(p);
        verify(p, times(1)).occupy(square1);
    }

    /**
     * Verifies registering a second player puts that player on the correct
     * starting square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void registerSecondPlayer() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        level.registerPlayer(p1);
        level.registerPlayer(p2);
        verify(p2).occupy(square2);
    }

    /**
     * Verifies registering a third player puts the player on the correct
     * starting square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void registerThirdPlayer() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Player p3 = mock(Player.class);
        level.registerPlayer(p1);
        level.registerPlayer(p2);
        level.registerPlayer(p3);
        verify(p3).occupy(square1);
    }

    /**
     * Verifies that the function counts the number of pellets.
     */
    @Test
    void testRemainingPellet() {
        assertThat(level.remainingPellets(true)).isEqualTo(1);
        assertThat(level.remainingPellets(false)).isEqualTo(0);
    }

    /**
     * Verifies that pacman is eating a powerPellet.
     */
    @Test
    void eatPowerPellet() {
        Player p = new Player(mock(Map.class), mock(AnimatedSprite.class));
        level.registerPlayer(p);
        level.start();
        level.move(p, Direction.EAST);
        assertThat(level.getGameMode()).isEqualTo((byte) 1);
        assertThat(level.remainingPellets(true)).isEqualTo(0);
        assertThat(ghost.getGameMode()).isEqualTo((byte) 1);
    }

    /**
     * Verifies that pacman can eat a ghost in hunting mode
     */
    @Test
    void chaseGhost() {
        Player p = new Player(mock(Map.class), mock(AnimatedSprite.class));
        level.registerPlayer(p);
        level.start();
        level.move(p, Direction.EAST);
        level.move(p, Direction.EAST);
        assertThat(p.getSquare()).isEqualTo(ghost.getSquare());
        assertThat(ghost.isAlive()).isFalse();
        assertThat(p.isAlive()).isTrue();
    }
}
