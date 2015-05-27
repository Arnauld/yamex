package yamex.feature;

import org.junit.runner.RunWith;
import tzatziki.analysis.step.Features;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.analysis.tag.TagDictionaryLoader;
import tzatziki.junit.SanityTagChecker;
import yamex.TestSettings;

import java.io.File;
import java.io.IOException;

import static tzatziki.junit.SanityTagChecker.loadFeaturesFromSourceDirectory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(SanityTagChecker.class)
public class SanityTagUsageTest {
    @SanityTagChecker.TagDictionaryProvider
    public static TagDictionary tagDictionary() throws IOException {
        return new TagDictionaryLoader().fromUTF8PropertiesResource("/yamex/feature/tags.properties");
    }

    @SanityTagChecker.FeaturesProvider
    public static Features features() {
        String basedir = new TestSettings().getBaseDir();
        return loadFeaturesFromSourceDirectory(new File(basedir, "src/test/resources/yamex/feature"));
    }
}
