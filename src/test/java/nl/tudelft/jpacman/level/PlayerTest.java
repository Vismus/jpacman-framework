package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.level.unit.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {
    private static Player player;
    private static Launcher launcher;

    @BeforeEach
    public void setUp() {
        launcher = new Launcher("src/test/resources/configuration.properties");
        launcher.launch();
        player = launcher.getGame().getPlayers().get(0);
    }

    @Test
    public void testAddPoint() {
        player.addPoints(50);
        assertThat(player.getScore()).isEqualTo(50);
    }

    @Test
    public void testIsAlive() {
        assertThat(player.isAlive()).isTrue();
    }

    @Test
    public void testSetAlive() {
        player.setAlive(false);
        assertThat(player.isAlive()).isFalse();
    }

    @Test
    public void testPlayerLosesLife() {
        assertThat(player.getRemainingLifes()).isEqualTo(3);
        player.loseLife();
        assertThat(player.getRemainingLifes()).isEqualTo(2);
    }

    @Test
    public void testPlayerGainsExtraLife() {
        assertThat(player.getRemainingLifes()).isEqualTo(3);
        player.addPoints(10000);
        assertThat(player.getRemainingLifes()).isEqualTo(4);
    }
}
