package scs.comp5903.cucumber.parser.jstep;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.jstepdef.matcher.GivenJStepMatcher;
import scs.comp5903.cucumber.model.jstepdef.matcher.ThenJStepMatcher;
import scs.comp5903.cucumber.model.jstepdef.matcher.WhenJStepMatcher;
import scs.comp5903.cucumber.parser.samplestepdef.AnotherSampleStepDefinition;
import scs.comp5903.cucumber.parser.samplestepdef.SampleStepDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
class JStepDefinitionParserTest {
  
  private final JStepDefinitionParser parser = new JStepDefinitionParser(new JStepDefinitionMethodParser());

  @Test
  void canExtractMultipleClasses() {
    var steps = parser.parse(SampleStepDefinition.class, AnotherSampleStepDefinition.class);
    assertEquals(6, steps.getSteps().size());
    assertEquals(2, steps.getSteps().stream().filter(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof GivenJStepMatcher).count());
    assertEquals(2, steps.getSteps().stream().filter(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof WhenJStepMatcher).count());
    assertEquals(2, steps.getSteps().stream().filter(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof ThenJStepMatcher).count());
    assertTrue(steps.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I have {int} apples")));
    assertTrue(steps.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I eat {int} apples")));
    assertTrue(steps.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I should have {int} apples")));
    assertTrue(steps.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I have {int} oranges")));
    assertTrue(steps.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I eat {int} oranges")));
    assertTrue(steps.getSteps().stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I should have {int} oranges")));
  }
}