package nl.tudelft.jpacman.e2e.framework.stop;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Entry point for running the the Cucumber stop tests in JUnit.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    snippets = SnippetType.CAMELCASE,
    glue = {"nl.tudelft.jpacman.e2e.framework.stop"},
    features = "classpath:frameworkfeatures/stop")
public class StopTest {
}
