package scs.comp5903.cucumber.integration.cms;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jooq.lambda.Unchecked;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.execution.JFeature;
import scs.comp5903.cucumber.model.annotation.step.JStep;
import scs.comp5903.cucumber.util.ResourceUtil;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static scs.comp5903.cucumber.model.annotation.step.JStepKeyword.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-21
 */
public class CmsComplexScenarioMultiScenarioStepDefs {

  private final Path jFeatureFile = ResourceUtil.getResourcePath("sample/jfeature/cms/a_student_register_course.jfeature");

  private JFeature student1RegisterCourseScenario;
  private JFeature student2RegisterCourseScenario;

  private CountDownLatch timingLock;

  private Thread student1RegisterCourseThread;
  private Thread student2RegisterCourseThread;

  private final MutableBoolean student1Registration = new MutableBoolean(true);
  private final MutableBoolean student2Registration = new MutableBoolean(true);

  public CmsComplexScenarioMultiScenarioStepDefs() throws URISyntaxException {
  }

  @JStep(keyword = GIVEN, value = "student one and student two")
  public void student_one_and_student_two() {
    timingLock = new CountDownLatch(1);
    student1RegisterCourseScenario = EasyCucumber.build(jFeatureFile, new CmsSimpleRegisterCourseStepDefs(1, "COMP3004", timingLock));
    student2RegisterCourseScenario = EasyCucumber.build(jFeatureFile, new CmsSimpleRegisterCourseStepDefs(2, "COMP3004", timingLock));
  }

  @JStep(keyword = AND, value = "both are at the course registration page")
  public void both_are_at_the_course_registration_page() {
    student1RegisterCourseThread = new Thread(Unchecked.runnable(() -> student1RegisterCourseScenario.executeAll()));
    student2RegisterCourseThread = new Thread(Unchecked.runnable(() -> student2RegisterCourseScenario.executeAll()));
    student1RegisterCourseThread.setUncaughtExceptionHandler((e, t) -> student1Registration.setFalse());
    student2RegisterCourseThread.setUncaughtExceptionHandler((e, t) -> student2Registration.setFalse());
    student1RegisterCourseThread.start();
    student2RegisterCourseThread.start();
  }

  @JStep(keyword = WHEN, value = "student one and student two click the register button on COMP3004 at the same time")
  public void student_one_and_student_two_click_the_register_button_on_comp3004_at_the_same_time() {
    timingLock.countDown();
  }

  @JStep(keyword = THEN, value = "only one of them should be registered for the course")
  public void only_one_of_them_should_be_registered_for_the_course() throws InterruptedException {
    student1RegisterCourseThread.join();
    student2RegisterCourseThread.join();
    var student1Success = student1Registration.booleanValue();
    var student2Success = student2Registration.booleanValue();
    assertTrue((student1Success && !student2Success) || (!student1Success && student2Success));
  }
}
