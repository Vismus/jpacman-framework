package nl.tudelft.jpacman.e2e.framework.startstop;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Entry point for running the the Cucumber start/stop tests in JUnit.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    snippets = SnippetType.CAMELCASE,
    glue = {"nl.tudelft.jpacman.e2e.framework.startstop"},
    features = "classpath:frameworkfeatures/startstop")
public class StartStopTest {

    /*
     * This class should be empty, step definitions should be in separate classes.
     */

}
