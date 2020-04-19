package nl.tudelft.jpacman.e2e.framework.startstop;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.strategies.HumanStrategy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for start/stop Cucumber tests.
 * <p>
 * The steps also support setting up a {@link Game} object
 * which other tests can use for further testing the game.
 *
 * @author Jan-Willem Gmelig Meyling, Arie van Deursen
 */
public class StartStopSteps {

    private Launcher launcher;

    private Game getGame() {
        return launcher.getGame();
    }


    /**
     * Launch the game. This makes the game available via
     * the {@link #getGame()} method.
     */
    @Given("^the user has launched the JPacman GUI$")
    public void theUserHasLaunchedTheJPacmanGUI() {
        launcher = new Launcher("src/test/resources/configuration.properties");
        launcher.launch(HumanStrategy.class);
    }

    /**
     * Start playing the game.
     */
    @When("^the user presses the \"Start\" button$")
    public void theUserPressesStart() {
        getGame().start();
    }

    /**
     * Verify that the play is actually running.
     */
    @Then("^the game is running$")
    public void theGameShouldStart() {
        assertThat(getGame().isInProgress()).isTrue();
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
