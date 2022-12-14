package scs.comp5903.cucumber.util;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.integration.cms.util.SeleniumFactory;
import scs.comp5903.cucumber.integration.samplestepdef.RummikubDummySeparatedJStepDefs;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-28
 */
class ReflectionUtilTest {

  @Test
  void canFindIncludeNestedClasses() {
    var classSet = ReflectionUtil.findAllClassesUsingClassLoader("scs.comp5903.cucumber.parser");
    assertTrue(classSet.stream().anyMatch(clazz -> clazz.getSimpleName().equals("SampleSubClass")));
    // not asserting on the number of classes found, as this can be changed in the future.
  }

  @Test
  void canFindClassesInNestedPackages() {
    var classSet = ReflectionUtil.findAllClassesUsingClassLoader("scs.comp5903.cucumber.integration");
    assertTrue(classSet.contains(SeleniumFactory.class));
  }

  @Test
  void canFindClassesInNestedClasses2() {
    var classSet = ReflectionUtil.findAllClassesUsingClassLoader("scs.comp5903.cucumber.integration.samplestepdef");
    assertTrue(classSet.contains(RummikubDummySeparatedJStepDefs.RummikubDummySeparatedJStepDefs2.class));
  }

}