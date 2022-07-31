package scs.comp5903.cucumber.sample;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-04
 */
class EmptyFeatureTest {

  @Test
  void emptyFeatureFile() {
    assertThrows(EasyCucumberException.class, () -> EasyCucumber.build(Path.of("src/test/resources/sample/jfeature/empty-file.jfeature"), RummikubDummyJStepDefs.class));
  }

  @Test
  void featureFileWithZeroScenarios() {
    assertDoesNotThrow(() -> EasyCucumber.build(Path.of("src/test/resources/sample/jfeature/zero-scenarios.jfeature"), RummikubDummyJStepDefs.class).execute());
  }
}
