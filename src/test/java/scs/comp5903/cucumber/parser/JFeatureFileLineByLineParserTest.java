package scs.comp5903.cucumber.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.JFeatureDetail;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.jstep.GivenStep;
import scs.comp5903.cucumber.model.jstep.ThenStep;
import scs.comp5903.cucumber.model.jstep.WhenStep;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static scs.comp5903.cucumber.parser.JFeatureFileLineByLineParser.ParseState.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-06
 */
class JFeatureFileLineByLineParserTest {

  private final DetailBuilder detailBuilder = new DetailBuilder();
  private JFeatureFileLineByLineParser lineByLineParser;
  private JFeatureDetail.JFeatureDetailBuilder jFeatureDetailBuilder;

  @BeforeEach
  void setUp() {
    jFeatureDetailBuilder = JFeatureDetail.builder();
    lineByLineParser = new JFeatureFileLineByLineParser(jFeatureDetailBuilder, detailBuilder);
  }

  @Test
  void featureTitle() {
    // given
    // when
    lineByLineParser.accept("Feature: This is a feature title");
    var jFeatureDetail = jFeatureDetailBuilder.build();
    // then
    assertEquals("This is a feature title", jFeatureDetail.getTitle());
    assertEquals(FEATURE, lineByLineParser.getState());
    assertNull(lineByLineParser.getParentState());
  }

  @Test
  void scenarioTitle() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(FEATURE);
    // when
    lineByLineParser.accept("Scenario: This is a scenario title");
    // then
    assertEquals("This is a scenario title", lineByLineParser.getTempScenarioTitle());
    assertEquals(SCENARIO, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
  }

  @Test
  void scenarioOutlineTitle() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(FEATURE);
    // when
    lineByLineParser.accept("Scenario Outline: This is a scenario outline title");
    // then
    assertEquals("This is a scenario outline title", lineByLineParser.getTempScenarioTitle());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
  }

  @Test
  void descriptionBetweenFeatureAndScenarioTitles() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(FEATURE);
    // when
    lineByLineParser.accept("I am a description");
    // then
    assertEquals(DESCRIPTION, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("another description");
    lineByLineParser.accept("Given a tricky description");
    // then
    assertEquals(DESCRIPTION, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());
    // and when
    lineByLineParser.accept("Scenario: This is a scenario title");
    // then
    assertEquals("This is a scenario title", lineByLineParser.getTempScenarioTitle());
    assertEquals(SCENARIO, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
  }

  @Test
  void descriptionBetweenFeatureAndScenarioOutlineTitles() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(FEATURE);
    // when
    lineByLineParser.accept("I am a description");
    // then
    assertEquals(DESCRIPTION, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("another description");
    lineByLineParser.accept("Given a tricky description");
    // then
    assertEquals(DESCRIPTION, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());
    // and when
    lineByLineParser.accept("Scenario Outline: This is a scenario outline title");
    // then
    assertEquals("This is a scenario outline title", lineByLineParser.getTempScenarioTitle());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
  }

  @Test
  void tagBetweenFeatureAndScenarioTitles() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(FEATURE);
    // when
    lineByLineParser.accept("@tag1 @tag2");
    // then
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals(1, lineByLineParser.getTempTagsLiteral().size());
    // and when
    lineByLineParser.accept("@anotherTag");
    // then
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals(2, lineByLineParser.getTempTagsLiteral().size());
    // and when
    lineByLineParser.accept("Scenario: This is a scenario title");
    // then
    assertEquals("This is a scenario title", lineByLineParser.getTempScenarioTitle());
    assertEquals(SCENARIO, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
  }

  @Test
  void tagBetweenFeatureAndScenarioOutlineTitles() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(FEATURE);
    // when
    lineByLineParser.accept("@tag1 @tag2");
    // then
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals(1, lineByLineParser.getTempTagsLiteral().size());
    // and when
    lineByLineParser.accept("@anotherTag");
    // then
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals(2, lineByLineParser.getTempTagsLiteral().size());
    // and when
    lineByLineParser.accept("Scenario Outline: This is a scenario outline title");
    // then
    assertEquals("This is a scenario outline title", lineByLineParser.getTempScenarioTitle());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
  }

  @Test
  void canNotSwitchFromTagToDescription() {
    var exp = assertThrows(EasyCucumberException.class, () -> {
      // given
      jFeatureDetailBuilder.title("This is a feature title");
      lineByLineParser.setState(TAG);
      lineByLineParser.setParentState(FEATURE);
      lineByLineParser.setTempTagsLiteral(new ArrayList<>(List.of("@tag1 @tag2", "@anotherTag")));
      // when
      lineByLineParser.accept("I am a description");
      fail("Should not be able to switch from tag to description");
    });
    assertTrue(exp.getMessage().contains("After tags, only scenario, scenario outline or more tags are allowed"));
  }

  @Test
  void stepsAfterScenarioTitle() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(SCENARIO);
    lineByLineParser.setParentState(FEATURE);
    lineByLineParser.setTempScenarioTitle("This is a scenario title");
    // when
    lineByLineParser.accept("Given a step 1");
    // then
    assertEquals(1, lineByLineParser.getTempScenarioSteps().size());
    assertEquals("Given a step 1", lineByLineParser.getTempScenarioSteps().get(0));
    assertEquals(STEP, lineByLineParser.getState());
    assertEquals(SCENARIO, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("When a step 2");
    lineByLineParser.accept("Then a step 3");
    // then
    assertEquals(3, lineByLineParser.getTempScenarioSteps().size());
    assertEquals("Given a step 1", lineByLineParser.getTempScenarioSteps().get(0));
    assertEquals("When a step 2", lineByLineParser.getTempScenarioSteps().get(1));
    assertEquals("Then a step 3", lineByLineParser.getTempScenarioSteps().get(2));
    assertEquals(STEP, lineByLineParser.getState());
    assertEquals(SCENARIO, lineByLineParser.getParentState());
  }

  @Test
  void stepsAfterScenarioOutlineTitle() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(SCENARIO_OUTLINE);
    lineByLineParser.setParentState(FEATURE);
    lineByLineParser.setTempScenarioTitle("This is a scenario outline title");
    // when
    lineByLineParser.accept("Given a step <counter1>");
    // then
    assertEquals(1, lineByLineParser.getTempScenarioSteps().size());
    assertEquals("Given a step <counter1>", lineByLineParser.getTempScenarioSteps().get(0));
    assertEquals(STEP, lineByLineParser.getState());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("When a step <counter2>");
    lineByLineParser.accept("Then a step <counter3>");
    // then
    assertEquals(3, lineByLineParser.getTempScenarioSteps().size());
    assertEquals("Given a step <counter1>", lineByLineParser.getTempScenarioSteps().get(0));
    assertEquals("When a step <counter2>", lineByLineParser.getTempScenarioSteps().get(1));
    assertEquals("Then a step <counter3>", lineByLineParser.getTempScenarioSteps().get(2));
    assertEquals(STEP, lineByLineParser.getState());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getParentState());
  }

  @Test
  void descriptionBetweenScenarioTitleAndSteps() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(SCENARIO);
    lineByLineParser.setParentState(FEATURE);
    lineByLineParser.setTempScenarioTitle("This is a scenario title");
    // when
    lineByLineParser.accept("I am a description");
    // then
    assertEquals(DESCRIPTION, lineByLineParser.getState());
    assertEquals(SCENARIO, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("another description");
    lineByLineParser.accept("Scenario: a tricky description");
    // then
    assertEquals(DESCRIPTION, lineByLineParser.getState());
    assertEquals(SCENARIO, lineByLineParser.getParentState());
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());
    // and when
    lineByLineParser.accept("Given a step 1");
    // then
    assertEquals(1, lineByLineParser.getTempScenarioSteps().size());
    assertEquals("Given a step 1", lineByLineParser.getTempScenarioSteps().get(0));
    assertEquals(STEP, lineByLineParser.getState());
    assertEquals(SCENARIO, lineByLineParser.getParentState());
  }

  @Test
  void descriptionBetweenScenarioOutlineTitleAndSteps() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(SCENARIO_OUTLINE);
    lineByLineParser.setParentState(FEATURE);
    lineByLineParser.setTempScenarioTitle("This is a scenario outline title");
    // when
    lineByLineParser.accept("I am a description");
    // then
    assertEquals(DESCRIPTION, lineByLineParser.getState());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("another description");
    lineByLineParser.accept("Scenario: a tricky description");
    // then
    assertEquals(DESCRIPTION, lineByLineParser.getState());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getParentState());
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());
    // and when
    lineByLineParser.accept("Given a step <counter1>");
    // then
    assertEquals(1, lineByLineParser.getTempScenarioSteps().size());
    assertEquals("Given a step <counter1>", lineByLineParser.getTempScenarioSteps().get(0));
    assertEquals(STEP, lineByLineParser.getState());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getParentState());
  }

  @Test
  void scenarioOutlineExamples() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(STEP);
    lineByLineParser.setParentState(SCENARIO_OUTLINE);
    lineByLineParser.setTempScenarioTitle("This is a scenario outline title");
    lineByLineParser.setTempScenarioSteps(new ArrayList<>(List.of("Given a step <counter1>", "When a step <counter2>", "Then a step <counter3>")));
    // when
    lineByLineParser.accept("Example:");
    // then
    assertEquals(EXAMPLE_KEYWORD, lineByLineParser.getState());
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("| counter1 | counter2 | counter3 |");
    lineByLineParser.accept("| 1       | 2       | 3       |");
    lineByLineParser.accept("| 4       | 5       | 6       |");
    // then
    assertEquals(3, lineByLineParser.getTempScenarioOutlineExamples().size());
    assertEquals("| counter1 | counter2 | counter3 |", lineByLineParser.getTempScenarioOutlineExamples().get(0));
    assertEquals("| 1       | 2       | 3       |", lineByLineParser.getTempScenarioOutlineExamples().get(1));
    assertEquals("| 4       | 5       | 6       |", lineByLineParser.getTempScenarioOutlineExamples().get(2));
    assertEquals(EXAMPLE_CONTENT, lineByLineParser.getState());
    assertEquals(EXAMPLE_KEYWORD, lineByLineParser.getParentState());
  }

  @Test
  void buildScenarioBeforeStartingANewScenario() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(STEP);
    lineByLineParser.setParentState(SCENARIO);
    lineByLineParser.setTempScenarioTitle("This is a scenario title");
    lineByLineParser.setTempScenarioSteps(new ArrayList<>(List.of("Given a step 1", "When a step 2", "Then a step 3")));
    // when
    lineByLineParser.accept("Scenario: This is another scenario title");
    // then
    assertEquals(SCENARIO, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals("This is another scenario title", lineByLineParser.getTempScenarioTitle());
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());

    var jFeatureDetail = jFeatureDetailBuilder.build();
    assertEquals(1, jFeatureDetail.getScenarios().size());
    assertEquals(1, jFeatureDetail.getScenarioOrders().size());
    assertEquals(0, jFeatureDetail.getScenarioOrders().get(0));
    var jScenarioDetail = jFeatureDetail.getScenarios().get(0);
    assertEquals("This is a scenario title", jScenarioDetail.getTitle());
    assertEquals(3, jScenarioDetail.getSteps().size());
    assertEquals("a step 1", jScenarioDetail.getSteps().get(0).getStepString());
    assertEquals(GivenStep.class, jScenarioDetail.getSteps().get(0).getClass());
    assertEquals("a step 2", jScenarioDetail.getSteps().get(1).getStepString());
    assertEquals(WhenStep.class, jScenarioDetail.getSteps().get(1).getClass());
    assertEquals("a step 3", jScenarioDetail.getSteps().get(2).getStepString());
    assertEquals(ThenStep.class, jScenarioDetail.getSteps().get(2).getClass());
  }

  /**
   * this test is same as {@link #buildScenarioBeforeStartingANewScenario()} but with the scenario outline
   */
  @Test
  void buildScenarioBeforeStartingANewScenarioOutline() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(STEP);
    lineByLineParser.setParentState(SCENARIO);
    lineByLineParser.setTempScenarioTitle("This is a scenario title");
    lineByLineParser.setTempScenarioSteps(new ArrayList<>(List.of("Given a step 1", "When a step 2", "Then a step 3")));
    // when
    lineByLineParser.accept("Scenario Outline: This is another scenario outline title");
    // then
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals("This is another scenario outline title", lineByLineParser.getTempScenarioTitle());
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());

    var jFeatureDetail = jFeatureDetailBuilder.build();
    assertEquals(1, jFeatureDetail.getScenarios().size());
    assertEquals(1, jFeatureDetail.getScenarioOrders().size());
    assertEquals(0, jFeatureDetail.getScenarioOrders().get(0));
    var jScenarioDetail = jFeatureDetail.getScenarios().get(0);
    assertEquals("This is a scenario title", jScenarioDetail.getTitle());
    assertEquals(3, jScenarioDetail.getSteps().size());
    assertEquals("a step 1", jScenarioDetail.getSteps().get(0).getStepString());
    assertEquals(GivenStep.class, jScenarioDetail.getSteps().get(0).getClass());
    assertEquals("a step 2", jScenarioDetail.getSteps().get(1).getStepString());
    assertEquals(WhenStep.class, jScenarioDetail.getSteps().get(1).getClass());
    assertEquals("a step 3", jScenarioDetail.getSteps().get(2).getStepString());
    assertEquals(ThenStep.class, jScenarioDetail.getSteps().get(2).getClass());
  }

  @Test
  void buildScenarioOutlineBeforeStartingANewScenario() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(EXAMPLE_CONTENT);
    lineByLineParser.setParentState(EXAMPLE_KEYWORD);
    lineByLineParser.setTempScenarioTitle("This is a scenario outline title");
    lineByLineParser.setTempScenarioSteps(new ArrayList<>(List.of("Given a step <counter1>", "When a step <counter2>", "Then a step <counter3>")));
    lineByLineParser.setTempScenarioOutlineExamples(new ArrayList<>(List.of("| counter1 | counter2 | counter3 |", "| 1       | 2       | 3       |", "| 4       | 5       | 6       |")));
    // when
    lineByLineParser.accept("Scenario: This is another scenario title");
    // then
    assertEquals(SCENARIO, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals("This is another scenario title", lineByLineParser.getTempScenarioTitle());
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());

    var jFeatureDetail = jFeatureDetailBuilder.build();
    assertEquals(1, jFeatureDetail.getScenarioOutlines().size());
    assertEquals(1, jFeatureDetail.getScenarioOutlineOrders().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlineOrders().get(0));
    var jScenarioOutlineDetail = jFeatureDetail.getScenarioOutlines().get(0);
    assertEquals("This is a scenario outline title", jScenarioOutlineDetail.getTitle());
    assertEquals(2, jScenarioOutlineDetail.getScenarios().size());

    var jScenarioDetail1 = jScenarioOutlineDetail.getScenarios().get(0);
    assertEquals("a step 1", jScenarioDetail1.getSteps().get(0).getStepString());
    assertEquals(GivenStep.class, jScenarioDetail1.getSteps().get(0).getClass());
    assertEquals("a step 2", jScenarioDetail1.getSteps().get(1).getStepString());
    assertEquals(WhenStep.class, jScenarioDetail1.getSteps().get(1).getClass());
    assertEquals("a step 3", jScenarioDetail1.getSteps().get(2).getStepString());
    assertEquals(ThenStep.class, jScenarioDetail1.getSteps().get(2).getClass());

    var jScenarioDetail2 = jScenarioOutlineDetail.getScenarios().get(1);
    assertEquals("a step 4", jScenarioDetail2.getSteps().get(0).getStepString());
    assertEquals(GivenStep.class, jScenarioDetail2.getSteps().get(0).getClass());
    assertEquals("a step 5", jScenarioDetail2.getSteps().get(1).getStepString());
    assertEquals(WhenStep.class, jScenarioDetail2.getSteps().get(1).getClass());
    assertEquals("a step 6", jScenarioDetail2.getSteps().get(2).getStepString());
    assertEquals(ThenStep.class, jScenarioDetail2.getSteps().get(2).getClass());
  }

  @Test
  void buildScenarioOutlineBeforeStartingANewScenarioOutline() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(EXAMPLE_CONTENT);
    lineByLineParser.setParentState(EXAMPLE_KEYWORD);
    lineByLineParser.setTempScenarioTitle("This is a scenario outline title");
    lineByLineParser.setTempScenarioSteps(new ArrayList<>(List.of("Given a step <counter1>", "When a step <counter2>", "Then a step <counter3>")));
    lineByLineParser.setTempScenarioOutlineExamples(new ArrayList<>(List.of("| counter1 | counter2 | counter3 |", "| 1       | 2       | 3       |", "| 4       | 5       | 6       |")));
    // when
    lineByLineParser.accept("Scenario Outline: This is another scenario outline title");
    // then
    assertEquals(SCENARIO_OUTLINE, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals("This is another scenario outline title", lineByLineParser.getTempScenarioTitle());
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());

    var jFeatureDetail = jFeatureDetailBuilder.build();
    assertEquals(1, jFeatureDetail.getScenarioOutlines().size());
    assertEquals(1, jFeatureDetail.getScenarioOutlineOrders().size());
    assertEquals(0, jFeatureDetail.getScenarioOutlineOrders().get(0));
    var jScenarioOutlineDetail = jFeatureDetail.getScenarioOutlines().get(0);
    assertEquals("This is a scenario outline title", jScenarioOutlineDetail.getTitle());
    assertEquals(2, jScenarioOutlineDetail.getScenarios().size());

    var jScenarioDetail1 = jScenarioOutlineDetail.getScenarios().get(0);
    assertEquals("a step 1", jScenarioDetail1.getSteps().get(0).getStepString());
    assertEquals(GivenStep.class, jScenarioDetail1.getSteps().get(0).getClass());
    assertEquals("a step 2", jScenarioDetail1.getSteps().get(1).getStepString());
    assertEquals(WhenStep.class, jScenarioDetail1.getSteps().get(1).getClass());
    assertEquals("a step 3", jScenarioDetail1.getSteps().get(2).getStepString());
    assertEquals(ThenStep.class, jScenarioDetail1.getSteps().get(2).getClass());

    var jScenarioDetail2 = jScenarioOutlineDetail.getScenarios().get(1);
    assertEquals("a step 4", jScenarioDetail2.getSteps().get(0).getStepString());
    assertEquals(GivenStep.class, jScenarioDetail2.getSteps().get(0).getClass());
    assertEquals("a step 5", jScenarioDetail2.getSteps().get(1).getStepString());
    assertEquals(WhenStep.class, jScenarioDetail2.getSteps().get(1).getClass());
    assertEquals("a step 6", jScenarioDetail2.getSteps().get(2).getStepString());
    assertEquals(ThenStep.class, jScenarioDetail2.getSteps().get(2).getClass());
  }

}