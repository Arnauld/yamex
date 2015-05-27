package yamex.feature;

import com.google.common.base.Predicate;
import gutenberg.itext.FontModifier;
import gutenberg.itext.Styles;
import gutenberg.itext.model.Markdown;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.support.TagView;
import tzatziki.analysis.exec.tag.TagFilter;
import tzatziki.analysis.tag.Tag;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.analysis.tag.TagDictionaryLoader;
import tzatziki.pdf.support.Configuration;
import tzatziki.pdf.support.DefaultPdfReportBuilder;
import tzatziki.pdf.support.TagViewsFromDictionaryBuilder;
import yamex.TestSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static tzatziki.pdf.support.DefaultPdfReportBuilder.Overview.FeatureSummary;
import static tzatziki.pdf.support.DefaultPdfReportBuilder.Overview.TagViews;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({RunAllFeatures.class})
public class RunAllFeatureAndGenerateReportTest {

    @AfterClass
    public static void generateExecutionReport() throws Exception {
        List<FeatureExec> execs = loadExec(executionResultFile());
        TagDictionary tagDictionary = new TagDictionaryLoader().fromUTF8PropertiesResource("/yamex/feature/tags.properties");

        File fileOut = new File(buildDir(), "yamex/report.pdf");

        new DefaultPdfReportBuilder()
                .using(new Configuration()
                                .displayFeatureTags(true)
                                .displayScenarioTags(true)
                                .declareProperty("imageDir",
                                        new File(baseDir(), "/src/test/resources/yamex/feature/images").toURI().toString())
                                .adjustFont(Styles.TABLE_HEADER_FONT, new FontModifier().size(10.0f))
                )
                .title("Yamex")
                .subTitle("Technical & Functional specifications")
                .markup(Markdown.fromUTF8Resource("/yamex/feature/preamble.md"))
                .overview(TagViews)
                .features(execs)
                .tagDictionary(tagDictionary)
                .tagViewsFromDictionary(new TagViewsFromDictionaryBuilder().tagFilter(excludeWip()))
                .tagViews(
                        new TagView("Orders (non wip)", TagFilter.from("~@wip", "@limitOrder,@marketOrder,@stopOrder")),
                        new TagView("Matching Principles", TagFilter.from("~@wip", "@matchingPrinciple"))
                )
                .sampleSteps()
                .generate(fileOut);
    }

    private static Predicate<Tag> excludeWip() {
        return tag -> !tag.getTag().startsWith("@wip");
    }

    private static File buildDir() {
        String baseDir = new TestSettings().getBuildDir();
        return new File(baseDir);
    }

    private static File baseDir() {
        String baseDir = new TestSettings().getBaseDir();
        return new File(baseDir);
    }

    private static File executionResultFile() {
        return new File(buildDir(), "yamex/exec.json");
    }

    private static File featuresSourceTree() {
        return new File(baseDir(), "src/main/resources/yamex/feature");
    }

    private static List<FeatureExec> loadExec(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return new JsonIO().load(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
