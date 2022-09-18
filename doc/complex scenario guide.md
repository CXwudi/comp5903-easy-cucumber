# How to write complex scenario

A complex scenario is the scenario that involves testing of concurrency, which usually involves multi-thread, resource
contention, etc.

However, Cucumber, as a sequential executed test framework, does not natively supports running multiple tests
concurrently.
Although attempts have been made to encounter this inability [1],
but either the solution is very fragile, or not fully completed.

In this guide, I would like to share two ways of writing the complex scenario

## Sample scenario we are going to use

Imaging a Course Management System (CMS), where there is only one spot left for a course,
and there are two students trying to register the course.
We want to test that when both students click the confirmation button at the same time,
only one of them can be registered, and another one would receive an error message saying the course is already full.

## Way 1: map any code line-by-line into a step definition class (only one class)

This method works for both the official Cucumber and this tool.

### The idea

The idea is to translate any piece of code into one step definition class, and only one class.
A piece of code is executed line-by-line from top to bottom.
Similarly, a Cucumber scenario or scenario outline is also executed line-by-line from top to bottom.
Therefore, we can map lines of codes into lines of Cucumber steps, which are also functions declared one after another
in the step definition class.

However, a piece of code can declare and reuse local variables, but in step definition class, a local variable in one
function can not be accessed from another function.
Therefore, the restriction of having only one step definition class comes in.
If a scenario or scenario outline is executed by only one step definition class, 
then all local variables can be declared as mutable (non-final)
fields in the step definition to achieve re-usability between multiple step definition functions.

Therefore, in theory, any code, no matter whether the code is multi-threaded or not, can be mapped into a single step
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

This way of writing a complex scenario is straight-forward and easy to understand.

However, maintainability and re-usability would be a problem when more tests are implemented.
Due to the restriction of only one step definition class per complex scenario,
it would be hard to refactor and reuse existing codes.
For example, a small Cucumber test (maybe is just a single scenario where a student registers a course)
might already contain the logic of course registration,
but using this way, the complex scenario can not reuse such existing logic.

Hence, the second way below is introduced to solve the maintainability issue.

## Way 2: providing own instances of step definition class to achieve share states between different cucumber tests

This way is only available in this tool. Official Cucumber does not support it.

### Idea

To achieve re-usability, two characteristics of this tool are needed.

First, Cucumber tests are ran by function call.
The developer simply calls `EasyCucumber.build()`
and `JFeature.executeXXX()` method to build and run the test at anywhere that a piece of Java code can run.
This includes implementation of `Runnable` interface which is used to create a new thread.
Therefore, the following piece of
code like can achieve concurrent execution of multiple Cucumber tests: [2]

``` java
for(Path featureFile:allFeatureFiles){
  JFeature jFeature= EasyCucumber.build(featureFile,MyStepDef.class);
  new Thread(()->{
    try{
      jFeature.executeAll();
      // if you want, you can also create each thread for each scenario or scenario outline in the jFeature instance.
      Systen.out.println("test success:"+jFeature.getTitle());
    }catch(Exception e){
      Systen.out.println("test fail:"+jFeature.getTitle());
    }
  }).start();
}
```

This piece of code also supports reporting out the result of each test.
However, there is no way to control the execution of all threads.

Therefore, the second characteristic of this tool comes to help, which is the support of using predefined instance of
step definition class, instead of relying on Cucumber itself to create the instance of the step definition class. For
example: `EasyCucumber.build(featureFile, new MyStepDef(myParameters))` instead
of `EasyCucumber.build(featureFile, MyStepDef.class)`.

By creating their predefined instance, developers can create custom constructors on step definition class.
Since [object instances in java are reference variables](https://www.geeksforgeeks.org/reference-variable-in-java/), an
instance of the step definition class can be shared into multiple `EasyCucumber.build()` function calls. Like following:

```java
MyStepDef stepDef=new MyStepDef(myParameter);
EasyCucumber.build(featureFile,stepDef).executeAll();
EasyCucumber.build(featureFile2,stepDef).executeAll();
```

Or multiple instances of the step definition class share a same dependency that a step definition class requires from
the constructor, like following:

```java
SomeSharedDependency sharedDep=new SomeSharedDependency();
EasyCucumber.build(featureFile,new MyStepDef(sharedDep)).executeAll();
EasyCucumber.build(featureFile2,new MyStepDef(sharedDep)).executeAll();
```

In the later case, such `SomeSharedDependency` can be any class from `java.util.concurrent` (
e.g. [`Lock`](https://www.baeldung.com/java-concurrent-locks), `Semaphore`
,  [`CyclicBarrier`](https://www.geeksforgeeks.org/java-util-concurrent-cyclicbarrier-java/?ref=lbp)) package that helps
controlling the concurrency.

Therefore, a complex scenario can now be created that runs multiple smaller and simply scenario concurrently or
asynchronously. The step definition implementation of the complex scenario would includes
several `EasyCucumber.build(fileOfSmallFeature, stepDefinitionInstanceOfSmallFeature)` where
each `stepDefinitionInstanceOfSmallFeature` is created by calling a custom constructor that accept an dependency
from `java.util.concurrent` package. Lately, the step definition implementation of the small scenario (which is the
class`stepDefinitionInstanceOfSmallFeature`) will need to be refactored to accept the dependency.

### Example

Hence, the CMS scenario described above can be rewritten
as [`src/test/java/scs/comp5903/cucumber/sample/CmsComplexScenarioMultiScenarioStepDefs.java`](https://github.com/CXwudi/comp5903-easy-cucumber/blob/main/src/test/java/scs/comp5903/cucumber/sample/CmsComplexScenarioMultiScenarioStepDefs.java) (
using the same feature file in way 1)

```java
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
```

In that example,
you can see that two instances of the simple Cucumber test that check the normal logic of student registering course are
created, with a shared `timingLock`.

```java
timingLock=new CountDownLatch(1);
student1RegisterCourseScenario=EasyCucumber.build(jFeatureFile,new CmsSimpleRegisterCourseStepDefs(1,"COMP3004",timingLock));
student2RegisterCourseScenario=EasyCucumber.build(jFeatureFile,new CmsSimpleRegisterCourseStepDefs(2,"COMP3004",timingLock));
```

A `CountDownLatch` is a lock that takes a counting number in the constructor.
Each call of `await()` will block the calling thread.
Then each call of `countDown()` will decrement the counting number by 1.
When the counting number falls to 0,
all threads that are waiting from calling `await()` will be released at the same time, 
which makes `CountDownLatch` a great tool for creating racing condition.

In the example, both `student1RegisterCourseScenario` and `student2RegisterCourseScenario` share a same `CountDownLatch`
with a counting number `1`.
When `timingLock.countDown()` is called during the step `When student one and student two click the register button on COMP3004 at the same time`,
both student 1 and student 2 will try to press the registration button at the same time,
see the implementation of [`CmsSimpleRegisterCourseStepDefs`](https://github.com/CXwudi/comp5903-easy-cucumber/blob/main/src/test/java/scs/comp5903/cucumber/sample/CmsSimpleRegisterCourseStepDefs.java)
for more details.

### Pro and cons

This way of writing a complex scenario has better maintainability, and code from smaller scenarios can be reused to form
the bigger complex scenario, which increases re-usability.

The only drawback of this way is
that refactoring the step definition implementation in an existing smaller scenario is required.
Test developers need to "hack" their pre-existing step definition class by creating new constructors,
new dependency to achieve code re-usability. 

## Reference

[1]: Scenario-Based Acceptance Testing using Cucumber by Mudit Aggarwal (Could not find proper reference, nor the public link)

[2]: Krumshyn, Alexei. “The ‘Cucumberization’ of JUnit.” Honours Project: 2022 Winter April 25, 2022 - 12:55pm | Www.scs.carleton.ca, https://service.scs.carleton.ca/node/6312. 