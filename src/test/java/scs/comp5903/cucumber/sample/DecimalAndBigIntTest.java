package scs.comp5903.cucumber.sample;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-16
 */
class DecimalAndBigIntTest {

  @Test
  void canParseOtherParameterTypes() {
    assertDoesNotThrow(() -> EasyCucumber.build(Path.of("src/test/resources/sample/jfeature/floating-point-and-big-integer-decimal.jfeature"), DecimalAndBigIntStepDef.class).executeAll());
  }
}

