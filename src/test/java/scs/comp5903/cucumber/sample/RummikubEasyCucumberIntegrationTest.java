package scs.comp5903.cucumber.sample;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import scs.comp5903.cucumber.EasyCucumber;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-26
 */
class RummikubEasyCucumberIntegrationTest {

  @ParameterizedTest
  @MethodSource("jFeatureFiles")
  void canRunRummikubJFeature(Path jFeatureFile) {
    assertDoesNotThrow(() -> {
      var jFeature = EasyCucumber.build(jFeatureFile, RummikubDummyJStepDefs.class);
      jFeature.execute();
    });
  }


  /**
   * This test case is same as {@link RummikubEasyCucumberIntegrationTest#canRunRummikubJFeature(Path)}
   * but using the {@link RummikubDummyJStepDefsWithRandomMethodName} as the step definition class.
   */
  @ParameterizedTest
  @MethodSource("jFeatureFiles")
  void canRunRummikubJFeature2(Path jFeatureFile) {
    assertDoesNotThrow(() -> {
      var jFeature = EasyCucumber.build(jFeatureFile, RummikubDummyJStepDefsWithRandomMethodName.class);
      jFeature.execute();
    });
  }


  static Stream<Arguments> jFeatureFiles() throws IOException {
    return Files.list(Paths.get("src/test/resources/sample/jfeature/rummikub"))
        .map(Arguments::of);
  }
}
