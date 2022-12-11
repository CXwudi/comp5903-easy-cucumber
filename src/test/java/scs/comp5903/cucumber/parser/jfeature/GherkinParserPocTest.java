package scs.comp5903.cucumber.parser.jfeature;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.Source;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import scs.comp5903.cucumber.util.ResourceUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static io.cucumber.messages.types.SourceMediaType.TEXT_X_CUCUMBER_GHERKIN_PLAIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-12-09
 */
@Disabled("This is a POC test, not a real test")
class GherkinParserPocTest {

  private static final Logger log = getLogger(GherkinParserPocTest.class);

  @Test
  void defaultParser() throws URISyntaxException, IOException {
    var gherkinParser = GherkinParser.builder().build();
    var path = ResourceUtil.getResourcePath("sample/jfeature/multi-line-description-with-tags.jfeature");
    var envelopeList = gherkinParser.parse(path).collect(Collectors.toList());
    envelopeList.forEach(envelope -> log.info("{}", envelope));
    log.info("feature = {}", envelopeList.get(1).getGherkinDocument().orElseThrow().getFeature().orElseThrow());
  }

  @Test
  void regressionFromSwitchingFromOldImpl() throws URISyntaxException, IOException {
    var gherkinParser = GherkinParser.builder().build();
    var path = ResourceUtil.getResourcePath("sample/jfeature/multi-line-description-with-tags-old.jfeature");
    var envelopeList = gherkinParser.parse(path).collect(Collectors.toList());
    envelopeList.forEach(envelope -> log.info("{}", envelope));
  }

  @Test
  void parseOutlineTable() throws URISyntaxException, IOException {
    var gherkinParser = GherkinParser.builder().build();
    var path = ResourceUtil.getResourcePath("sample/jfeature/sample-tagged-scenarios.jfeature");
    var gherkinDocument = gherkinParser.parse(path).filter(envelope -> envelope.getGherkinDocument().isPresent())
        .findFirst().orElseThrow().getGherkinDocument().orElseThrow();
    log.info("gherkinDocument = {}", gherkinDocument);
  }

  @Test
  void onEmptyFile() throws URISyntaxException, IOException {
    var gherkinParser = GherkinParser.builder().build();
    var path = ResourceUtil.getResourcePath("sample/jfeature/empty-file.jfeature");
    var envelopeList = gherkinParser.parse(path).collect(Collectors.toList());
    log.info("envelopeList = {}", envelopeList);
  }

  @Test
  void onEmptyFile2() throws URISyntaxException, IOException {
    var gherkinParser = GherkinParser.builder().build();
    var path = ResourceUtil.getResourcePath("sample/jfeature/zero-scenarios.jfeature");
    var envelopeList = gherkinParser.parse(path).collect(Collectors.toList());
    log.info("envelopeList = {}", envelopeList);
  }

  @Test
  void parseWithError() throws URISyntaxException, IOException {
    var gherkinParser = GherkinParser.builder().build();
    var path = ResourceUtil.getResourcePath("sample/jfeature/with-error.jfeature");
    var envelopeList = gherkinParser.parse(path).collect(Collectors.toList());
    log.info("envelopeList = {}", envelopeList);
  }

  final String feature =
      "Feature: Minimal\n" +
          "\n" +
          "  Scenario: minimalistic\n" +
          "    Given the minimalism\n";
  final Envelope envelope = Envelope.of(new Source("minimal.feature", feature, TEXT_X_CUCUMBER_GHERKIN_PLAIN));

  @Test
  void use_this_in_readme() {
    GherkinParser parser = GherkinParser.builder().build();
    List<Envelope> pickles = parser.parse(envelope).collect(Collectors.toList());
    pickles.forEach(envelope -> log.info("{}", envelope));
    var envelope1 = pickles.get(0);
    envelope1.getPickle().ifPresent(pickle -> {
      assertEquals("minimalistic", pickle.getName());
      assertEquals("minimal.feature", pickle.getUri());
      assertEquals(1, pickle.getSteps().size());
      assertEquals("the minimalism", pickle.getSteps().get(0).getText());
    });
  }
}
