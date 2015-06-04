package yamex.feature.fixprotocol;

import yamex.fixprotocol.FieldDictionary;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FixMessageFieldRow {
    public Integer tag;
    public String value;
    public String tagName;

    public FixMessageFieldRow(FieldDictionary.Field f, Object value) {
        this.tag = f.tag();
        this.tagName = f.field();
        this.value = f.format(value);
    }
}
