package scs.comp5903.cucumber.sample;

import org.jooq.lambda.Unchecked;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.step.JStep;
import scs.comp5903.cucumber.sample.cmsutil.CmsPageUtils;
import scs.comp5903.cucumber.sample.cmsutil.Constants;
import scs.comp5903.cucumber.sample.cmsutil.SeleniumFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static scs.comp5903.cucumber.model.JStepKeyword.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-27
 */
public class CmsComplexScenarioMultithreadStepDefs {

  private final static Logger log = org.slf4j.LoggerFactory.getLogger(CmsComplexScenarioMultithreadStepDefs.class);

  private WebDriver student1Driver;
  private WebDriver student2Driver;

  private CompletableFuture<Boolean> student1Registration;
  private CompletableFuture<Boolean> student2Registration;

  private final CountDownLatch timingLock = new CountDownLatch(1);

  @JStep(keyword = GIVEN, value = "student one and student two")
  public void student_one_and_student_two() {
    student1Driver = SeleniumFactory.getDriver();
    student2Driver = SeleniumFactory.getDriver();
  }

  @JStep(keyword = AND, value = "both are at the course registration page")
  public void both_are_at_the_course_registration_page() {
    CmsPageUtils.getToPageAndLogin(student1Driver, Constants.URL_CMS + "/student/registration", "student1", "pass1234");
    CmsPageUtils.getToPageAndLogin(student2Driver, Constants.URL_CMS + "/student/registration", "student2", "pass1234");
  }

  @JStep(keyword = WHEN, value = "student one and student two click the register button on COMP3004 at the same time")
  public void student_one_and_student_two_click_the_register_button_on_comp3004_at_the_same_time() throws InterruptedException {
    student1Registration = generateRegistrationStep(student1Driver);
    student2Registration = generateRegistrationStep(student2Driver);
    Thread.sleep(1200);
    timingLock.countDown();
    log.info("Race condition starts");
  }

  @JStep(keyword = THEN, value = "only one of them should be registered for the course")
  public void only_one_of_them_should_be_registered_for_the_course() throws ExecutionException, InterruptedException {
    var result1 = student1Registration.get();
    var result2 = student2Registration.get();
    assertNotSame(result1, result2);
  }

  private CompletableFuture<Boolean> generateRegistrationStep(WebDriver driver) {
    return CompletableFuture.supplyAsync(Unchecked.supplier(() -> {
      var registrableCourseList = driver.findElement(By.id("registrable_course_list"));
      var comp3004ElementOpt = registrableCourseList.findElements(By.className("list-group-item")).stream().filter(e -> {
        var titleElement = e.findElement(By.cssSelector("div.row.d-flex.justify-content-between.ml-1.mr-1 > div"));
        return titleElement.getText().contains("COMP3004");
      }).findFirst();
      var comp3004Element = comp3004ElementOpt.orElseThrow();
      log.debug("found comp3004");
      var registrationButton = comp3004Element.findElement(By.cssSelector("button"));
      timingLock.await();
      registrationButton.click();
      registrableCourseList = driver.findElement(By.id("registrable_course_list"));
      var comp3004ElementAgainOpt = registrableCourseList.findElements(By.className("list-group-item")).stream().filter(e -> {
        var titleElement = e.findElement(By.cssSelector("div.row.d-flex.justify-content-between.ml-1.mr-1 > div"));
        return titleElement.getText().contains("COMP3004");
      }).findFirst();
      return comp3004ElementAgainOpt.isEmpty();
    }));
  }
}
