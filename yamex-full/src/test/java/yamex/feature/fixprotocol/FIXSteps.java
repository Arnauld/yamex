package yamex.feature.fixprotocol;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import yamex.feature.Context;
import yamex.fixprotocol.FieldDictionary;
import yamex.fixprotocol.FixMessage;
import yamex.fixprotocol.FixProtocol;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FIXSteps {

    private static final String FIX_MESSAGE_STRING = "fix-message-string";
    private static final String FIX_MESSAGE = "fix-message";

    private final Context context;

    public FIXSteps(Context context) {
        this.context = context;
    }

    @Given("^the FIX message delimiter has been set to \"(.{1})\"$")
    public void the_FIX_separator_has_been_set_to(char delimiter) throws Throwable {
        FixProtocol fixProtocol = context.getFixProtocol();
        fixProtocol.setDelimiter(delimiter);
    }

    @Given("^the following FIX message:$")
    public void the_following_FIX_message(String messageAsString) throws Throwable {
        context.put(FIX_MESSAGE_STRING, messageAsString);
    }


    @When("^the FIX message is parsed$")
    public void the_FIX_message_is_parsed() throws Throwable {
        String msg = context.get(String.class, FIX_MESSAGE_STRING);

        FixProtocol fixProtocol = context.getFixProtocol();
        FixMessage message = fixProtocol.parse(msg);
        context.put(FIX_MESSAGE, message);
    }

    @Then("^the FIX message should match:$")
    public void the_FIX_message_should_match(DataTable expectedFields) throws Throwable {
        FixMessage fixMessage = context.get(FixMessage.class, FIX_MESSAGE);
        FieldDictionary fieldDictionary = context.getFieldDictionary();

        List<FixMessageFieldRow> actualRows =
                expectedFields.asList(FixMessageFieldRow.class)
                        .stream()
                        .map(fr -> {
                                    FieldDictionary.Field f = fieldDictionary.get(fr.tag);
                                    Object value = fixMessage.getValue(f);
                                    return new FixMessageFieldRow(f, value);
                                }
                        ).collect(toList());

        expectedFields.unorderedDiff(actualRows);
    }
}
