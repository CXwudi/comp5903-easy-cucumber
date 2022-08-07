package scs.comp5903.cucumber.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.JFeatureDetail;
import scs.comp5903.cucumber.model.jstep.GivenStep;
import scs.comp5903.cucumber.model.jstep.ThenStep;
import scs.comp5903.cucumber.model.jstep.WhenStep;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static scs.comp5903.cucumber.parser.JFeatureFileLineByLineParser.ParseState.*;

/**
 * @author CX无敌
 * @date 2022-08-07
 */
class LineByLineParserBuildingTest {

  private final DetailBuilder detailBuilder = new DetailBuilder();
  private JFeatureFileLineByLineParser lineByLineParser;
  private JFeatureDetail.JFeatureDetailBuilder jFeatureDetailBuilder;

  @BeforeEach
  void setUp() {
    jFeatureDetailBuilder = JFeatureDetail.builder();
    lineByLineParser = new JFeatureFileLineByLineParser(jFeatureDetailBuilder, detailBuilder);
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
    assertEquals(0, lineByLineParser.getTempScenarioOutlineExamples().size());

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
    assertEquals(0, lineByLineParser.getTempScenarioOutlineExamples().size());

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
  void buildScenarioBeforeATag() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(STEP);
    lineByLineParser.setParentState(SCENARIO);
    lineByLineParser.setTempScenarioTitle("This is a scenario title");
    lineByLineParser.setTempScenarioSteps(new ArrayList<>(List.of("Given a step 1", "When a step 2", "Then a step 3")));
    // when
    lineByLineParser.accept("@tag1 @tag2");
    // then
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals("", lineByLineParser.getTempScenarioTitle());
    assertEquals("@tag1 @tag2", lineByLineParser.getTempTagsLiteral().get(0));
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
  void buildScenarioOutlineBeforeATag() {
    // given
    jFeatureDetailBuilder.title("This is a feature title");
    lineByLineParser.setState(EXAMPLE_CONTENT);
    lineByLineParser.setParentState(EXAMPLE_KEYWORD);
    lineByLineParser.setTempScenarioTitle("This is a scenario outline title");
    lineByLineParser.setTempScenarioSteps(new ArrayList<>(List.of("Given a step <counter1>", "When a step <counter2>", "Then a step <counter3>")));
    lineByLineParser.setTempScenarioOutlineExamples(new ArrayList<>(List.of("| counter1 | counter2 | counter3 |", "| 1       | 2       | 3       |", "| 4       | 5       | 6       |")));
    // when
    lineByLineParser.accept("@tag1 @tag2");
    // then
    assertEquals(TAG, lineByLineParser.getState());
    assertEquals(FEATURE, lineByLineParser.getParentState());
    assertEquals("", lineByLineParser.getTempScenarioTitle());
    assertEquals("@tag1 @tag2", lineByLineParser.getTempTagsLiteral().get(0));
    assertEquals(0, lineByLineParser.getTempScenarioSteps().size());
    assertEquals(0, lineByLineParser.getTempScenarioOutlineExamples().size());

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