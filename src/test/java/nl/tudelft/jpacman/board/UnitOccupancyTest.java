package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.common.BasicGround;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
 *
 * @author Jeroen Roosen 
 *
 */
class UnitOccupancyTest {

    /**
     * The unit under test.
     */
    private Unit unit;

    /**
     * Resets the unit under test.
     */
    @BeforeEach
    void setUp() {
        unit = new BasicUnit();
    }

    /**
     * Asserts that a unit has no square to start with.
     */
    @Test
    void noStartSquare() {
        assertThat(unit.hasSquare()).isFalse();
    }

    /**
     * Tests that the unit indeed has the target square as its base after
     * occupation.
     */
    @Test
    void testOccupy() {
        Square target = new BasicGround();
        unit.occupy(target);
        assertThat(unit.getSquare()).isEqualTo(target);
        assertThat(target.getOccupants()).contains(unit);
    }

    /**
     * Test that the unit indeed has the target square as its base after
     * double occupation.
     */
    @Test
    void testReoccupy() {
        Square target = new BasicGround();
        unit.occupy(target);
        unit.occupy(target);
        assertThat(unit.getSquare()).isEqualTo(target);
        assertThat(target.getOccupants()).contains(unit);
    }

    /**
     * Test that the unit leaves the square.
     */
    @Test
    void testLeaveSquare() {
        Square target = new BasicGround();
        unit.occupy(target);
        unit.leaveSquare();
        assertThat(target.getOccupants()).doesNotContain(unit);
    }
}
