package scs.comp5903.cucumber.builder;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.model.JStepDefMethodDetail;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.jstep.GivenStep;
import scs.comp5903.cucumber.model.matcher.GivenJStepMatcher;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-24
 */
class JStepParameterExtractorTest {

  private final JStepParameterExtractor jStepParameterExtractor = new JStepParameterExtractor();

  @Test
  void testExtractParameters() throws NoSuchMethodException {
    var parameters = jStepParameterExtractor.tryExtractParameters(new GivenStep("I have a \"string\" and an int 5"),
            new JStepDefMethodDetail(this.getClass().getDeclaredMethod("aMethod", String.class, int.class), new GivenJStepMatcher("I have a {string} and an int {int}")))
        .orElseThrow();
    assertEquals(2, parameters.size());
    assertEquals(String.class, parameters.get(0).getClass());
    assertEquals("string", parameters.get(0));
    assertEquals(5, parameters.get(1));
  }

  @Test
  void testExtractParameters2() throws NoSuchMethodException {
    var parameters = jStepParameterExtractor.tryExtractParameters(new GivenStep("I have a double 5.2"),
            new JStepDefMethodDetail(this.getClass().getDeclaredMethod("aMethodWithDouble", double.class), new GivenJStepMatcher("I have a double {double}")))
        .orElseThrow();
    assertEquals(1, parameters.size());
    assertEquals(Double.class, parameters.get(0).getClass());
    assertEquals(5.2, (double) parameters.get(0), 0.001);
  }

  @Test
  void testNoParameters() throws NoSuchMethodException {
    var parameters = jStepParameterExtractor.tryExtractParameters(new GivenStep("I have a method without parameters"),
            new JStepDefMethodDetail(this.getClass().getDeclaredMethod("noParameterMethod"), new GivenJStepMatcher("I have a method without parameters")))
        .orElseThrow();
    assertTrue(parameters.isEmpty());
  }

  @Test
  void canIgnoreIfNotDeclared() throws NoSuchMethodException {
    var parameters = jStepParameterExtractor.tryExtractParameters(new GivenStep("I have an int 5, a 'string', and a double 5.2"),
            new JStepDefMethodDetail(this.getClass().getDeclaredMethod("noParameterMethod"), new GivenJStepMatcher("I have an int 5, a 'string', and a double 5.2")))
        .orElseThrow();
    assertTrue(parameters.isEmpty());
  }

  @Test
  void shouldThrowIfMethodHasNotEnoughParameters() {
    var exp = assertThrows(EasyCucumberException.class, () -> jStepParameterExtractor.tryExtractParameters(new GivenStep("I have an int 5"),
        new JStepDefMethodDetail(
            this.getClass().getDeclaredMethod("noParameterMethod"),
            new GivenJStepMatcher("I have an int {int}"))
    ));
    assertTrue(exp.getMessage().contains("The amount of extracted parameters doesn't match the amount of parameters of the step definition"));
  }


  void aMethod(String a, int b) {
    // do nothing
  }

  void aMethodWithDouble(double c) {
    // do nothing
  }

  void noParameterMethod() {
    // do nothing
  }
}