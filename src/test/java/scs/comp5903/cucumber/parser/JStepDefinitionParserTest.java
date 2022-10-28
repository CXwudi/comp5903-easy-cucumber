package scs.comp5903.cucumber.parser;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.annotation.step.JStep;
import scs.comp5903.cucumber.model.annotation.step.JStepKeyword;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.stepdef.matcher.GivenJStepMatcher;
import scs.comp5903.cucumber.model.stepdef.matcher.ThenJStepMatcher;
import scs.comp5903.cucumber.model.stepdef.matcher.WhenJStepMatcher;
import scs.comp5903.cucumber.parser.samplestepdef.AnotherSampleStepDefinition;
import scs.comp5903.cucumber.parser.samplestepdef.SampleStepDefWithNewAnnotation;
import scs.comp5903.cucumber.parser.samplestepdef.SampleStepDefinition;
import scs.comp5903.cucumber.parser.samplestepdef.SampleSubClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
class JStepDefinitionParserTest {

  @Test
  void extractOneClass() {
    var steps = new JStepDefinitionParser().extractOneClass(SampleStepDefinition.class);
    assertEquals(3, steps.size());
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof GivenJStepMatcher));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof WhenJStepMatcher));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof ThenJStepMatcher));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I have {int} apples")));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I eat {int} apples")));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I should have {int} apples")));
  }

  @Test
  void canExtractFromSuperclass() {
    var steps = new JStepDefinitionParser().extractOneClass(SampleSubClass.class);
    assertEquals(3, steps.size());
  }

  @Test
  void canExtractMultipleClasses() {
    var steps = new JStepDefinitionParser().parse(SampleStepDefinition.class, AnotherSampleStepDefinition.class);
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

  @Test
  void canExtractFromNewAnnotation() {
    var steps = new JStepDefinitionParser().extractOneClass(SampleStepDefWithNewAnnotation.class);
    assertEquals(3, steps.size());
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof GivenJStepMatcher));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof WhenJStepMatcher));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher() instanceof ThenJStepMatcher));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I have {int} apples")));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I eat {int} apples")));
    assertTrue(steps.stream().anyMatch(jStepDefMethodDetail -> jStepDefMethodDetail.getMatcher().getMatchingString().equals("I should have {int} apples")));
  }

  @Test
  void shouldThrowOnNonPublicStepDefClass() {
    var exp = assertThrows(EasyCucumberException.class, () -> new JStepDefinitionParser().extractOneClass(NonPublicStepDefClass.class));
    assertTrue(exp.getMessage().contains("Step definition class must be public"));
  }

  @Test
  void doesNotThrowIfTheClassIsNotStepDef() {
    assertDoesNotThrow(() -> new JStepDefinitionParser().extractOneClass(NonStepDefClass.class));
  }
}

class NonPublicStepDefClass {
  @JStep(keyword = JStepKeyword.GIVEN, value = "I have {int} apples")
  public void iHaveApples(int apples) {
  }
}

class NonStepDefClass {
  public void aMethod() {
  }
}