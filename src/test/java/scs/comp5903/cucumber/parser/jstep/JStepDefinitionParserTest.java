package scs.comp5903.cucumber.parser.jstep;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.jstepdef.JStepDefHookDetail;
import scs.comp5903.cucumber.model.jstepdef.matcher.GivenJStepMatcher;
import scs.comp5903.cucumber.model.jstepdef.matcher.ThenJStepMatcher;
import scs.comp5903.cucumber.model.jstepdef.matcher.WhenJStepMatcher;
import scs.comp5903.cucumber.parser.samplestepdef.AnotherSampleHookDefinition;
import scs.comp5903.cucumber.parser.samplestepdef.AnotherSampleStepDefinition;
import scs.comp5903.cucumber.parser.samplestepdef.SampleHookDefinition;
import scs.comp5903.cucumber.parser.samplestepdef.SampleStepDefinition;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
class JStepDefinitionParserTest {
  
  private final JStepDefinitionParser parser = new JStepDefinitionParser(
      new JStepDefinitionMethodParser(),
      new JStepDefinitionHookParser());

  @Test
  void canExtractMultipleClasses() {
    var stepDefDetail = parser.parse(
        SampleStepDefinition.class,
        AnotherSampleStepDefinition.class,
        SampleHookDefinition.class,
        AnotherSampleHookDefinition.class);
    assertEquals(6, stepDefDetail.getSteps().size());
    assertEquals(2, stepDefDetail.getSteps().stream().filter(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof GivenJStepMatcher).count());
    assertEquals(2, stepDefDetail.getSteps().stream().filter(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof WhenJStepMatcher).count());
    assertEquals(2, stepDefDetail.getSteps().stream().filter(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof ThenJStepMatcher).count());
    assertTrue(stepDefDetail.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I have {int} apples")));
    assertTrue(stepDefDetail.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I eat {int} apples")));
    assertTrue(stepDefDetail.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I should have {int} apples")));
    assertTrue(stepDefDetail.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I have {int} oranges")));
    assertTrue(stepDefDetail.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I eat {int} oranges")));
    assertTrue(stepDefDetail.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I should have {int} oranges")));

    assertEquals(12, stepDefDetail.getHooks().size());
    var orderList = stepDefDetail.getHooks().stream().map(JStepDefHookDetail::getOrder).sorted().collect(Collectors.toList());
    var expected = List.of(1,2,3,4,5,6,7,8,9,10,11,12);
    assertEquals(expected, orderList);
  }
}