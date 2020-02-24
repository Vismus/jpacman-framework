package nl.tudelft.jpacman.e2e.framework.stop;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for stop cucumber tests.
 */
public class StopSteps {
    private Launcher launcher;

    private Game getGame() {
        return launcher.getGame();
    }

    /**
     * Launch and start the game. This makes the game available via the {@link #getGame()} method.
     */
    @Given("^the user has launched the JPacman GUI and has pressed the start button$")
    public void theUserHasLaunchedAndStartTheJPacmanGUI() {
        launcher = new Launcher();
        launcher.launch();
        getGame().start();
    }

    /**
     * Stop the game.
     */
    @When("^the user presses the \"Stop\" button$")
    public void theUserPressesStop() {
        getGame().stop();
    }

    /**
     * Verify that the game is not running anymore.
     */
    @Then("^the game is not running anymore$")
    public void theGameShouldStop() {
        assertThat(getGame().isInProgress()).isFalse();
    }

    /**
     * Close the UI after all tests are finished.
     */
    @After("@framework")
    public void tearDownUI() {
        launcher.dispose();
    }
}
