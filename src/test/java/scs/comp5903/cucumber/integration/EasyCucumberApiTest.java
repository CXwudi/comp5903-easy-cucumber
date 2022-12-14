package scs.comp5903.cucumber.integration;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.integration.samplestepdef.RummikubDummySeparatedJStepDefs;

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
        RummikubDummySeparatedJStepDefs.class,
        RummikubDummySeparatedJStepDefs.RummikubDummySeparatedJStepDefs2.class
    ).executeAll());
  }

  @Test
  void canBuildWithObjects() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        new RummikubDummySeparatedJStepDefs(),
        new RummikubDummySeparatedJStepDefs.RummikubDummySeparatedJStepDefs2()
    ).executeAll());
  }

  @Test
  void canBuildWithListOfClasses() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        List.of(
            RummikubDummySeparatedJStepDefs.class,
            RummikubDummySeparatedJStepDefs.RummikubDummySeparatedJStepDefs2.class
        )
    ).executeAll());
  }

  @Test
  void canBuildWithListOfObjects() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        List.of(
            new RummikubDummySeparatedJStepDefs(),
            new RummikubDummySeparatedJStepDefs.RummikubDummySeparatedJStepDefs2()
        )
    ).executeAll());
  }

  @Test
  void canBuildWithListOfObjectsAndClasses() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        List.of(
            new RummikubDummySeparatedJStepDefs(),
            RummikubDummySeparatedJStepDefs.RummikubDummySeparatedJStepDefs2.class
        )
    ).executeAll());
  }


  @Test
  void canBuildWithPackage() {
    assertDoesNotThrow(() -> EasyCucumber.build(
        Path.of("src/test/resources/sample/jfeature/rummikub/initial_points.jfeature"),
        "scs.comp5903.cucumber.integration.samplestepdef"
    ).executeAll());
  }
}