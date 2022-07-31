package scs.comp5903.cucumber.sample;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.sample.samplestepdef.RummikubDummySaperatedJStepDefs;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Make sure those APIs defined in {@link EasyCucumber} are working as expected.
 *
 * @author Charles Chen 101035684
 * @date 2022-07-28
 */
class EasyCucumberApiTest {

  @Test
  void canBuildWithClasses() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        RummikubDummySaperatedJStepDefs.class,
        RummikubDummySaperatedJStepDefs.RummikubDummySaperatedJStepDefs2.class
    ).execute());
  }

  @Test
  void canBuildWithObjects() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        new RummikubDummySaperatedJStepDefs(),
        new RummikubDummySaperatedJStepDefs.RummikubDummySaperatedJStepDefs2()
    ).execute());
  }

  @Test
  void canBuildWithListOfClasses() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        List.of(
            RummikubDummySaperatedJStepDefs.class,
            RummikubDummySaperatedJStepDefs.RummikubDummySaperatedJStepDefs2.class
        )
    ).execute());
  }

  @Test
  void canBuildWithListOfObjects() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        List.of(
            new RummikubDummySaperatedJStepDefs(),
            new RummikubDummySaperatedJStepDefs.RummikubDummySaperatedJStepDefs2()
        )
    ).execute());
  }

  @Test
  void canBuildWithListOfObjectsAndClasses() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        List.of(
            new RummikubDummySaperatedJStepDefs(),
            RummikubDummySaperatedJStepDefs.RummikubDummySaperatedJStepDefs2.class
        )
    ).execute());
  }


  @Test
  void canBuildWithPackage() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        "scs.comp5903.cucumber.sample.samplestepdef"
    ).execute());
  }
}