package scs.comp5903.cucumber.parser.jstep;


import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.annotation.step.JStep;
import scs.comp5903.cucumber.model.annotation.step.JStepKeyword;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.jstepdef.matcher.GivenJStepMatcher;
import scs.comp5903.cucumber.model.jstepdef.matcher.ThenJStepMatcher;
import scs.comp5903.cucumber.model.jstepdef.matcher.WhenJStepMatcher;
import scs.comp5903.cucumber.parser.samplestepdef.SampleStepDefWithNewAnnotation;
import scs.comp5903.cucumber.parser.samplestepdef.SampleStepDefinition;
import scs.comp5903.cucumber.parser.samplestepdef.SampleSubClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-12
 */
class JStepDefinitionMethodParserTest {

  private final JStepDefinitionMethodParser methodParser = new JStepDefinitionMethodParser();

  @Test
  void extractOneClass() {
    var steps = methodParser.extractOneClass(SampleStepDefinition.class);
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
    var steps = methodParser.extractOneClass(SampleSubClass.class);
    assertEquals(3, steps.size());
  }

  @Test
  void canExtractFromNewAnnotation() {
    var steps = methodParser.extractOneClass(SampleStepDefWithNewAnnotation.class);
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
    var exp = assertThrows(EasyCucumberException.class, () -> methodParser.extractOneClass(NonPublicStepDefClass.class));
    assertTrue(exp.getMessage().contains("Step definition class must be public"));
  }

  @Test
  void doesNotThrowIfTheClassIsNotStepDef() {
    assertDoesNotThrow(() -> methodParser.extractOneClass(NonStepDefClass.class));
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