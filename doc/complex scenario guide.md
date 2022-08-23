# How to write complex scenario

A complex scenario is scenario that involve testing of concurrency, which usually involve multi-thread, resource
contention, and etc.

However, Cucumber, as a sequential executed test framework, does not natively supports running multiple tests
concurrently. Although attempts has been made to encounter this inability (TODO: need reference of Mudit;s paper), but
either the solution is very fragile, or not fully completed.

In this guide, I would like to share two ways of writing the complex scenario

## Sample scenario we are going to use

Imaging a Course Management System (CMS), where there is only one spot left for a course, and there are two students
trying to register the course. We want to test that when both students click the confirmation button at the same time,
only one of them can be registered, and another one would receive an error message saying the course is already full.

## Way 1: map the any code line-by-line inside one step definition class

This method works for both the official Cucumber and this project implementation.

### The idea

The idea is to translate any piece of code into one step definition class. A piece of code is executed line-by-line from
top to bottom. Similarly, a Cucumber scenario or scenario outline is also executed line-by-line from top to bottom.
Therefore, we can map lines of codes into lines of Cucumber steps, which are also functions declared one after another
in the step definition class.

However, a piece of code can declare and reuse local variables, but in step definition class, a local variable in one
function can not be accessed from another function. Therefore, the restriction of having only one step definition class
comes in. If a scenario or scenario outline is executed by only one step definition class, then all local variables can
be declared as mutable (non-final) fields in the step definition to achieve reusability between multiple step definition
functions.

Therefore, in theory, any code, no matter if the code is multi-threaded or not, can be mapped into a single step
definition class.

### Examples

For example, given 5 lines of codes like following:

```java
code1();
int i=code2();
code3(i);
code4();
code5();
```

We can map these codes into a Cucumber test as following:

- Scenario:

  ```feature
  Scenario: demo
    Given step 1
    When step 2
    Then step 3
  ```

- Step definition class:

  ```java
  public class StepDef {
      
      private int i;
      
      @Given("step 1")
      public void func1(){
          code1();
          i = code2();
      }
      
      @When("step 2")
      public void func2(){
          code3(i);
          code4();
      }
      
      @Then("step 3")
      public void func3(){
          code5();
      }
  }
  ```

And here is another real-world example of testing the CMS scenario described previously.

```feature
Feature: CMS can handle race condition between two students

  Scenario: two students try to register for the last spot
    Given student one and student two
    And both are at the course registration page
    When student one and student two click the register button on COMP3004 at the same time
    Then only one of them should be registered for the course
```

The corresponding step definition class is:

```java
public class TwoStudentsRaceConditionStepDef {

  private WebDriver student1Driver;
  private WebDriver student2Driver;

  private CompletableFuture<Boolean> student1Registration;
  private CompletableFuture<Boolean> student2Registration;

  private final CountDownLatch timingLock = new CountDownLatch(1);

  @Given("student one and student two")
  public void studentOneAndStudentTwo() {
    student1Driver = SeleniumFactory.getDriver();
    student2Driver = SeleniumFactory.getDriver();
  }

  @And("both are at the course registration page")
  public void bothAreAtTheCourseRegistrationPage() {
    CmsPageUtils.getToPageAndLogin(student1Driver, Constants.URL_CMS + "/student/registration", "student1", "pass1234");
    CmsPageUtils.getToPageAndLogin(student2Driver, Constants.URL_CMS + "/student/registration", "student2", "pass1234");
  }

  @When("student one and student two click the register button on COMP3004 at the same time")
  public void studentOneAndStudentTwoClickTheRegisterButtonOnCOMPAtTheSameTime() throws InterruptedException {
    student1Registration = generateRegistrationStep(student1Driver);
    student2Registration = generateRegistrationStep(student2Driver);
    Thread.sleep(1200);
    timingLock.countDown();
    log.info("Race condition starts");
  }

  @Then("only one of them should be registered for the course")
  public void onlyOneOfThemShouldBeRegisteredForTheCourse() throws ExecutionException, InterruptedException {
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
```

### Pros and Cons

pro: easy understand

con: poor reusability and extensibility, and hence we need the way 2 below

The way of writing complex scenario is straight-forward and easy to understand.

However, maintainability and reusability would be a problem when more tests are implemented. Due to the restriction of
only one step definition class per complex scenario, it would be hard to refactor and reuse existing codes. For example,
a small Cucumber test (maybe is just a single scenario where a student registers a course) may already contained the
logic of course registration, but using this way, the complex scenario can not reuse such existing logic.

Hence, the second way below is introduced to solve the maintainability issue.

## Way 2: providing own instances of step definition class to achieve share states between different cucumber tests

This way is only available in this tool. Official Cucumber does not support it.

### Idea

//TODO: explain the idea: given that we can create our own instance of step definition, and also given that running a
test is basically some function call, then we run multiple cucumber tests inside a cucumber test, and each smaller test
has shared resources

//TODO:
example: https://github.com/CXwudi/comp5903-easy-cucumber/blob/main/src/test/java/scs/comp5903/cucumber/sample/CmsComplexScenarioMultithreadStepDefs.java
and https://github.com/CXwudi/comp5903-easy-cucumber/blob/main/src/test/resources/sample/jfeature/cms/two_students_race_for_one_spot.jfeature

//TODO: pro and con:

pro: reusable, maintainable, extensibillity

con: hacky step definition implementation with extra locking and threads managing.
