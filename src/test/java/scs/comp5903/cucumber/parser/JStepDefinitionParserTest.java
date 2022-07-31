package scs.comp5903.cucumber.parser;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.JStep;
import scs.comp5903.cucumber.model.JStepKeyword;
import scs.comp5903.cucumber.model.matcher.GivenJStepMatcher;
import scs.comp5903.cucumber.model.matcher.ThenJStepMatcher;
import scs.comp5903.cucumber.model.matcher.WhenJStepMatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}

class SampleStepDefinition {

  private static void notAStepDefinitionMethod() {

  }

  public void notAStepDefinition() {

  }

  @JStep(keyword = JStepKeyword.GIVEN, value = "I have {int} apples")
  public void givenIHaveApples(int count) {

  }

  @JStep(keyword = JStepKeyword.WHEN, value = "I eat {int} apples")
  public void whenIEatApples(int count) {

  }

  @JStep(keyword = JStepKeyword.THEN, value = "I should have {int} apples")
  public void thenIShouldHaveApples(int count) {

  }

}

class SampleSubClass extends SampleStepDefinition {
}

class AnotherSampleStepDefinition {

  @JStep(keyword = JStepKeyword.GIVEN, value = "I have {int} oranges")
  public void givenIHaveOranges(int count) {
  }

  @JStep(keyword = JStepKeyword.WHEN, value = "I eat {int} oranges")
  public void whenIEatOranges(int count) {
  }

  @JStep(keyword = JStepKeyword.THEN, value = "I should have {int} oranges")
  public void thenIShouldHaveOranges(int count) {
  }
}