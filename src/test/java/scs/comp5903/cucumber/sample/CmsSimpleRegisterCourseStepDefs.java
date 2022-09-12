package scs.comp5903.cucumber.sample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.JStep;
import scs.comp5903.cucumber.sample.cmsutil.CmsPageUtils;
import scs.comp5903.cucumber.sample.cmsutil.Constants;
import scs.comp5903.cucumber.sample.cmsutil.SeleniumFactory;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static scs.comp5903.cucumber.model.JStepKeyword.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-20
 */
public class CmsSimpleRegisterCourseStepDefs {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(CmsSimpleRegisterCourseStepDefs.class);

  private WebDriver studentDriver;
  private final int studentId;
  private final String courseCode;

  private final CountDownLatch timingLock;

  public CmsSimpleRegisterCourseStepDefs(int studentId, String courseCode) {
    this(studentId, courseCode, new CountDownLatch(0));
  }

  public CmsSimpleRegisterCourseStepDefs(int studentId, String courseCode, CountDownLatch timingLock) {
    this.studentId = studentId;
    this.courseCode = courseCode;
    this.timingLock = timingLock;
  }

  @JStep(keyword = GIVEN, value = "a student")
  public void student() {
    log.info("Student {}", studentId);
    studentDriver = SeleniumFactory.getDriver();
  }

  @JStep(keyword = AND, value = "the student is at the course registration page")
  public void studentAtCourseRegistrationPage() throws InterruptedException {
    log.info("The student is at the course registration page");
    CmsPageUtils.getToPageAndLogin(studentDriver, Constants.URL_CMS + "/student/registration", "student" + studentId, "pass1234");
    Thread.sleep(1000);
  }

  @JStep(keyword = WHEN, value = "the student clicks the register button on a course")
  public void studentClicksRegisterButtonOnCourse() throws InterruptedException {
    log.info("The student clicks the register button on a course");
    var registrableCourseList = studentDriver.findElement(By.id("registrable_course_list"));
    var courseElementOpt = registrableCourseList.findElements(By.className("list-group-item")).stream().filter(e -> {
      var titleElement = e.findElement(By.cssSelector("div.row.d-flex.justify-content-between.ml-1.mr-1 > div"));
      return titleElement.getText().contains(courseCode);
    }).findFirst();
    var courseElement = courseElementOpt.orElseThrow();
    log.debug("found {}", courseCode);
    var registrationButton = courseElement.findElement(By.cssSelector("button"));
    timingLock.await();
    registrationButton.click();
  }

  @JStep(keyword = THEN, value = "the student is registered for the course")
  public void studentIsRegisteredForCourse() {
    log.info("Student " + studentId + " is registered for course " + courseCode);
    var registrableCourseList = studentDriver.findElement(By.id("registrable_course_list"));
    var courseElementOpt = registrableCourseList.findElements(By.className("list-group-item")).stream().filter(e -> {
      var titleElement = e.findElement(By.cssSelector("div.row.d-flex.justify-content-between.ml-1.mr-1 > div"));
      return titleElement.getText().contains(courseCode);
    }).findFirst();
    assertTrue(courseElementOpt.isEmpty());
  }

}
