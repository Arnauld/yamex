package yamex.feature;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import yamex.util.JUnits;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SkipHooks {

    @Before(value = {"@notImplemented"})
    public void skip(Scenario scenario) {
        if(scenario.getSourceTagNames().contains("@notImplemented"))
            JUnits.skip("Not implemented");
        else if(scenario.getSourceTagNames().contains("@wip"))
            JUnits.skip("Work in progress");
    }
}
