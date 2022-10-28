package scs.comp5903.cucumber.parser;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.jfeature.jstep.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-29
 */
class DetailBuilderTest {

  @Test
  void shouldThrowOnUnknownKeyword() {
    var detailBuilder = new DetailBuilder();
    var exp = assertThrows(EasyCucumberException.class, () -> detailBuilder.parseStep("This is not a keyword"));
    assertTrue(exp.getMessage().contains("Unknown step keyword"));
  }

  @Test
  void canCreateJScenarioDetail() {
    var detailBuilder = new DetailBuilder();
    var title = "This is a title";
    var steps = List.of(
        "Given I have an apple",
        "And I have a banana",
        "When I eat them",
        "Then I should not be hungry",
        "But I should feel too full");
    var tags = List.of("@tag1 @tag2", "@tag3");
    var scenarioDetail = detailBuilder.buildJScenarioDetail(title, steps, tags);
    assertEquals(title, scenarioDetail.getTitle());
    assertEquals(steps.size(), scenarioDetail.getSteps().size());
    var expectedClasses = List.of(
        GivenStep.class,
        AndStep.class,
        WhenStep.class,
        ThenStep.class,
        ButStep.class);
    for (int i = 0; i < steps.size(); i++) {
      var expectedString = steps.get(i);
      expectedString = expectedString.substring(expectedString.indexOf(" ") + 1);
      assertEquals(expectedString, scenarioDetail.getSteps().get(i).getStepString());
      assertEquals(expectedClasses.get(i), scenarioDetail.getSteps().get(i).getClass());
    }
    assertIterableEquals(List.of("tag1", "tag2", "tag3"), scenarioDetail.getTags());
  }

  @Test
  void canCreateJScenarioOutlineDetail() {
    var detailBuilder = new DetailBuilder();
    var title = "This is a title";
    var steps = List.of(
        "Given I have a <fruit1>",
        "And I have a <fruit2>",
        "When I eat them",
        "Then I should not be hungry",
        "But I should feel too full");
    var examples = List.of(
        "| fruit1 | fruit2 |",
        "| apple | orange |",
        "| banana | peer |");
    var tags = List.of("@tag1 @tag2", "@tag3");
    var scenarioOutlineDetail = detailBuilder.buildJScenarioOutlineDetail(title, steps, examples, tags);
    assertEquals(title, scenarioOutlineDetail.getTitle());
    assertEquals(steps.size(), scenarioOutlineDetail.getScenarios().get(0).getSteps().size());
    assertEquals(examples.size() - 1, scenarioOutlineDetail.getScenarios().size());
    // have to hard code, otherwise we are reimplementing the business logic
    assertEquals("I have a apple", scenarioOutlineDetail.getScenarios().get(0).getSteps().get(0).getStepString());
    assertEquals("I have a orange", scenarioOutlineDetail.getScenarios().get(0).getSteps().get(1).getStepString());
    assertEquals("I eat them", scenarioOutlineDetail.getScenarios().get(0).getSteps().get(2).getStepString());
    assertEquals("I should not be hungry", scenarioOutlineDetail.getScenarios().get(0).getSteps().get(3).getStepString());
    assertEquals("I should feel too full", scenarioOutlineDetail.getScenarios().get(0).getSteps().get(4).getStepString());
    assertEquals("I have a banana", scenarioOutlineDetail.getScenarios().get(1).getSteps().get(0).getStepString());
    assertEquals("I have a peer", scenarioOutlineDetail.getScenarios().get(1).getSteps().get(1).getStepString());
    assertEquals("I eat them", scenarioOutlineDetail.getScenarios().get(1).getSteps().get(2).getStepString());
    assertEquals("I should not be hungry", scenarioOutlineDetail.getScenarios().get(1).getSteps().get(3).getStepString());
    assertEquals("I should feel too full", scenarioOutlineDetail.getScenarios().get(1).getSteps().get(4).getStepString());
    //TODO: as well add test for tags when tags are implemented
    assertIterableEquals(List.of("tag1", "tag2", "tag3"), scenarioOutlineDetail.getTags());
  }

}