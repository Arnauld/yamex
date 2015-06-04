package yamex.fixprotocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FixProtocol implements FixContext {
    private char delimiter = '|';

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public FixMessage parse(String msgAsString) {
        FixMessage.Builder builder = new FixMessage.Builder();

        Pattern p = Pattern.compile("(\\d+)=([^" + delimiter + "]+)(?:\\Q" + delimiter + "\\E)?");
        Matcher matcher = p.matcher(msgAsString);
        while (matcher.find()) {
            String tag = matcher.group(1);
            String val = matcher.group(2);
            builder.define(Integer.parseInt(tag), val);
        }
        return builder.build(this);
    }
}
