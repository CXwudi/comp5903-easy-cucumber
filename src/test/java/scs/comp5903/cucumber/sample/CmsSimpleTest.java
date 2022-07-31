package scs.comp5903.cucumber.sample;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.util.ResourceUtil;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-20
 */
@Disabled("Disabled for CI, uncomment this line to run this test locally")
class CmsSimpleTest {

  @Test
  void canRunASingleSimpleTest() {
    assertDoesNotThrow(() -> {
      var jFeatureFile = getSingleStudentRegisterCourseJFeaturePath();
      var registerCourseStepDefInstance = new CmsSimpleRegisterCourseStepDefs(1, "COMP3004");
      EasyCucumber.build(jFeatureFile, registerCourseStepDefInstance).execute();
    });
  }

  @Test
  void canRunForADifferentStudentAndCourse() {
    assertDoesNotThrow(() -> {
      var jFeatureFile = getSingleStudentRegisterCourseJFeaturePath();
      var registerCourseStepDefInstance = new CmsSimpleRegisterCourseStepDefs(2, "JAPA1001");
      EasyCucumber.build(jFeatureFile, registerCourseStepDefInstance).execute();
    });
  }

  private Path getSingleStudentRegisterCourseJFeaturePath() throws URISyntaxException {
    return ResourceUtil.getResourcePath("sample/jfeature/cms/a_student_register_course.jfeature");
  }
}
