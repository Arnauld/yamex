package yamex.feature;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        tags = {"~@wip", "~@notImplemented"},
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/yamex")
public class RunAllFeatures {
}
