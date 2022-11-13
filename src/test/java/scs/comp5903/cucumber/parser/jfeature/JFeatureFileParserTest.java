package scs.comp5903.cucumber.parser.jfeature;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.jfeature.jstep.AndStep;
import scs.comp5903.cucumber.model.jfeature.jstep.GivenStep;
import scs.comp5903.cucumber.model.jfeature.jstep.ThenStep;
import scs.comp5903.cucumber.model.jfeature.jstep.WhenStep;
import scs.comp5903.cucumber.util.ResourceUtil;

import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
class JFeatureFileParserTest {

  private final DetailBuilder detailBuilder = new DetailBuilder();
  private final JFeatureFileLineByLineParser lineByLineParser = new JFeatureFileLineByLineParser(detailBuilder);

  @Test
  void canParseScenario() throws URISyntaxException {
    var jFeatureDetail = new JFeatureFileParser(lineByLineParser).parse(ResourceUtil.getResourcePath("sample/jfeature/rummikub/declaring_winner.jfeature"));
    assertEquals("Declaring Winner", jFeatureDetail.getTitle());
    assertEquals(1, jFeatureDetail.getScenarios().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlines().size());
    assertEquals(1, jFeatureDetail.getScenarioOrders().size());
    assertEquals(0, jFeatureDetail.getScenarioOrders().get(0));
    assertEquals(0, jFeatureDetail.getScenarioOutlineOrders().size());
    var scenario = jFeatureDetail.getScenarios().get(0);
    assertEquals("Test Player Winning and Ending Game", scenario.getTitle());
    assertEquals("Test Server is started", scenario.getSteps().get(0).getStepString());
    assertEquals("Player 3 score is -38", scenario.getSteps().get(12).getStepString());
    assertEquals(GivenStep.class, scenario.getSteps().get(0).getClass());
    assertEquals(ThenStep.class, scenario.getSteps().get(9).getClass());
  }

  @Test
  void canParseScenarioOutline() throws URISyntaxException {
    var jFeatureDetail = new JFeatureFileParser(lineByLineParser).parse(ResourceUtil.getResourcePath("sample/jfeature/rummikub/initial_points.jfeature"));
    assertEquals("Test if first play meets initial point threshold", jFeatureDetail.getTitle());
    assertEquals(1, jFeatureDetail.getScenarios().size());
    assertEquals(1, jFeatureDetail.getScenarioOrders().size());
    assertEquals(3, jFeatureDetail.getScenarioOrders().get(0));
    assertEquals(4, jFeatureDetail.getScenarioOutlines().size());
    assertEquals(4, jFeatureDetail.getScenarioOutlineOrders().size());
    assertFalse(jFeatureDetail.getScenarioOutlineOrders().contains(3));
    assertEquals(17, jFeatureDetail.getScenarioOutlines().stream().mapToLong(jScenarioOutlineDetail -> jScenarioOutlineDetail.getScenarios().size()).sum());
    // the second scenario outline, and the second example in that scenario outline
    var oneExtractedScenario = jFeatureDetail.getScenarioOutlines().get(1).getScenarios().get(1);
    assertEquals("Test Initial Point Threshold with Jokers - Example 2", oneExtractedScenario.getTitle());
    assertEquals("Player 1 hand starts with \"R8 R9 R10 R11 B9 B11 O9 O11 *\"", oneExtractedScenario.getSteps().get(1).getStepString());
    assertEquals(AndStep.class, oneExtractedScenario.getSteps().get(1).getClass());
  }

  @Test
  void shouldThrowOnEmptyOrNoTitleFeatureFile() {
    var exp = assertThrows(EasyCucumberException.class, () -> new JFeatureFileParser(lineByLineParser).parse(ResourceUtil.getResourcePath("sample/jfeature/empty-file.jfeature")));
    assertTrue(exp.getMessage().contains("A feature must have a valid title"));
  }

  @Test
  void shouldNotThrowOnFeatureFileWithZeroScenarios() throws URISyntaxException {
    var jFeatureDetail = new JFeatureFileParser(lineByLineParser).parse(ResourceUtil.getResourcePath("sample/jfeature/zero-scenarios.jfeature"));
    assertEquals(0, jFeatureDetail.getScenarios().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlines().size());
    assertEquals(0, jFeatureDetail.getScenarioOrders().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlineOrders().size());
  }

  @Test
  void canIgnoreCommentsAndDescription() throws URISyntaxException {
    var jFeatureDetail = new JFeatureFileParser(lineByLineParser).parse(ResourceUtil.getResourcePath("sample/jfeature/multi-line-description.jfeature"));
    assertEquals("This is a feature with description and comments", jFeatureDetail.getTitle());
    assertEquals(1, jFeatureDetail.getScenarios().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlines().size());
    assertEquals(1, jFeatureDetail.getScenarioOrders().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlineOrders().size());
    var scenario = jFeatureDetail.getScenarios().get(0);
    assertEquals("This is a scenario with description and comments", scenario.getTitle());
    assertEquals("I have an apple", scenario.getSteps().get(0).getStepString());
    assertEquals(GivenStep.class, scenario.getSteps().get(0).getClass());
    assertEquals("I eat it", scenario.getSteps().get(1).getStepString());
    assertEquals(WhenStep.class, scenario.getSteps().get(1).getClass());
    assertEquals("I am full", scenario.getSteps().get(2).getStepString());
    assertEquals(ThenStep.class, scenario.getSteps().get(2).getClass());
  }

  @Test
  void canParseTags() throws URISyntaxException {
    var jFeatureDetail = new JFeatureFileParser(lineByLineParser).parse(ResourceUtil.getResourcePath("sample/jfeature/multi-line-description-with-tags.jfeature"));
    assertEquals("This is a feature with description and comments", jFeatureDetail.getTitle());
    assertIterableEquals(Arrays.asList("tag1", "tag2", "tag3"), jFeatureDetail.getTags());
    assertEquals(1, jFeatureDetail.getScenarios().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlines().size());
    assertEquals(1, jFeatureDetail.getScenarioOrders().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlineOrders().size());
    var scenario = jFeatureDetail.getScenarios().get(0);
    assertIterableEquals(Arrays.asList("tag4", "tag5"), scenario.getTags());
    assertEquals("This is a scenario with description and comments", scenario.getTitle());
    assertEquals("I have an apple", scenario.getSteps().get(0).getStepString());
    assertEquals(GivenStep.class, scenario.getSteps().get(0).getClass());
    assertEquals("I eat it", scenario.getSteps().get(1).getStepString());
    assertEquals(WhenStep.class, scenario.getSteps().get(1).getClass());
    assertEquals("I am full", scenario.getSteps().get(2).getStepString());
    assertEquals(ThenStep.class, scenario.getSteps().get(2).getClass());

  }
}