package scs.comp5903.cucumber.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.JFeatureDetail;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static scs.comp5903.cucumber.parser.JFeatureFileLineByLineParser.ParseState.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-06
 */
class LineByLineParserParsingTest {

  private final DetailBuilder detailBuilder = new DetailBuilder();
  private JFeatureFileLineByLineParser lineByLineParser;
  private JFeatureDetail.JFeatureDetailBuilder jFeatureDetailBuilder;

  @BeforeEach
  void setUp() {
    lineByLineParser = new JFeatureFileLineByLineParser(detailBuilder);
    jFeatureDetailBuilder = lineByLineParser.initNewJFeatureDetailBuilder();
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
    assertEquals(START, lineByLineParser.getParentState());
  }

  @Test
  void featureTitleWithTags() {
    // given
    // when
    lineByLineParser.accept("@tag1 @tag2");
    // then
    assertEquals(1, lineByLineParser.getTempFeatureTagsLiteral().size());
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(START, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("@anotherTag");
    // then
    assertEquals(2, lineByLineParser.getTempFeatureTagsLiteral().size());
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(START, lineByLineParser.getParentState());
    // and when
    lineByLineParser.accept("Feature: This is a feature title");
    // then
    assertEquals("This is a feature title", jFeatureDetailBuilder.build().getTitle());
    assertEquals(FEATURE, lineByLineParser.getState());
    assertEquals(START, lineByLineParser.getParentState());
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
    assertEquals(1, lineByLineParser.getTempScenarioOrScenarioOutlineTagsLiteral().size());
    // and when
    lineByLineParser.accept("@anotherTag");
    // then
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals(2, lineByLineParser.getTempScenarioOrScenarioOutlineTagsLiteral().size());
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
    assertEquals(1, lineByLineParser.getTempScenarioOrScenarioOutlineTagsLiteral().size());
    // and when
    lineByLineParser.accept("@anotherTag");
    // then
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals(2, lineByLineParser.getTempScenarioOrScenarioOutlineTagsLiteral().size());
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
      lineByLineParser.setTempScenarioOrScenarioOutlineTagsLiteral(new ArrayList<>(List.of("@tag1 @tag2", "@anotherTag")));
      // when
      lineByLineParser.accept("I am a description");
      fail("Should not be able to switch from tag to description");
    });
    assertTrue(exp.getMessage().contains("After tags, only feature (title), scenario, scenario outline or more tags are allowed"));
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

}