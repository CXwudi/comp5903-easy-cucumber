# Easy Cucumber

[![](https://jitpack.io/v/CXwudi/comp5903-easy-cucumber.svg)](https://jitpack.io/#CXwudi/comp5903-easy-cucumber)

COMP5903 project at Carleton University. It is the enhancement of Alexei's
original ["Cucumberized" JUnit](https://github.com/alexeikrumshyn/cucumberized-junit)

List of improvement, UML diagram for improved Cucumberized JUnit can be found in `doc` directory

TOC:

<!-- TOC -->

* [Easy Cucumber](#easy-cucumber)
  * [Prerequisite](#prerequisite)
  * [How to import](#how-to-import)
    * [Maven](#maven)
    * [Gradle](#gradle)
  * [How to use](#how-to-use)
    * [Build the test](#build-the-test)
    * [Run the test](#run-the-test)
  * [Supported Features](#supported-features)
    * [Supported Cucumber Keywords](#supported-cucumber-keywords)
    * [Supported Cucumber Features](#supported-cucumber-features)
    * [Extra Features that Official Cucumber does not support](#extra-features-that-official-cucumber-does-not-support)
  * [For Developers](#for-developers)

<!-- TOC -->

## Prerequisite

To use or develop this tool, you only need:

- Java 11 or above

## How to import

The Maven Package is released in [JitPack](https://jitpack.io/#CXwudi/comp5903-easy-cucumber)

### Maven

Step 1. Add the JitPack repository to your build file:

``` xml
 <repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
 </repositories>
```

Step 2. Add the dependency

``` xml
 <dependency>
     <groupId>com.github.CXwudi</groupId>
     <artifactId>comp5903-easy-cucumber</artifactId>
     <version>the latest version</version>
 </dependency>
```

### Gradle

Add it in your root `build.gradle` at the end of repositories:

``` gradle
allprojects {
  repositories {
    // your other maven repositories
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency

``` gradle
dependencies {
  implementation 'com.github.CXwudi:comp5903-easy-cucumber:<the latest version>'
}
```

## How to use

TL;DR: You just call `EasyCucumber.build()` to build the test, and call `JFeature.executeAll()` to run the test.

You can call these two APIs in anywhere that you can run a piece of Java code. For example, in JUnit:

```java
@Test
void myTest(){
    Path myFeatureFile=Paths.get("path/to/my/feature-file.feature");
    JFeature jFeature=EasyCucumber.build(myFeatureFile,MyStepDefinition.class);
    jFeature.executeAll();
    // or JFeature.executeByTag(myTag);
    }
```

As you can see, `EasyCucumber.build()`represents the build phase
and `JFeature.executeAll()` represents the runtime phase.

### Build the test

1. First, write the feature file like following. The feature file can be stored in any directory that you can access
   using Java's `Path`.

    ```gherkin
    # you can add comments
    Feature: Fruits
      You can also add multi-line
      description
    
      @tag1 @tag2
      Scenario: I eat apple
        Given I have 5 apple
        When I eat 4 apple
        Then I should left 1 apple
    
      @tag2 @tag3
      Scenario Outline: I eat other fruits
        Given I have <total> apple
        When I eat <eatCount> apple
        Then I should left <leftCount> apple
        Examples:
          | total | eatCount | leftCount |
          | 5     | 4        | 1         |
          | 10    | 5        | 5         |
    ```

2. Create the step definition class(es). These classes must be public and all step definition must be public as well,
   like following:
    ```java
    // MyStepDefinition.java
    import scs.comp5903.cucumber.model.annotation.step.JGivenStep;
    import scs.comp5903.cucumber.model.annotation.step.JThenStep;
    import scs.comp5903.cucumber.model.annotation.step.JWhenStep;
    
    public class MyStepDefinition {
    
      private int count;
    
      @JGivenStep("I have {int} apple")
      // @JStep(keyword = JStepKeyword.GIVEN, step = "I have {int} apple") // alternatively, u can use @JStep
      public void iHave(int count) {
        this.count = count;
      }
    
      @JWhenStep("I eat {int} apple")
      public void iEat(int eatCount) {
        this.count -= eatCount;
      }
    
      @JThenStep("I should left {int} apple")
      public void iShouldLeft(int leftCount) {
        assertEquals(leftCount, this.count);
      }
    }
    ```
   Optionally, you can create some hooks, either in the same step definition class(es) or in a separated class:
   ```java
   // MyHooks.java
   import scs.comp5903.cucumber.model.annotation.hook.*;
   public class MyHooks {
     @BeforeAllJScenarios
     public void beforeAllScenarios() {
       // do something before all scenarios
     }
     @AfterAllJScenarios
     public void afterAllScenarios() {
       // do something after all scenarios
     }
     @BeforeEachJScenario
     public void beforeEachScenario(JScenarioStatus status) { // this parameter is optional
       // do something before each scenario
     }
     @AfterEachJScenario
     public void afterEachScenario(JScenarioStatus status) { // this parameter is optional
       // do something after each scenario
     }
     @BeforeEachJStep
     public void beforeEachStep(JStepStatus status) { // this parameter is optional
       // do something before each step
     }
     @AfterEachJStep
     public void afterEachStep(JStepStatus status) { // this parameter is optional
       // do something after each step
     }
   }
   ```
3. Call [`EasyCucumber.build()`](src/main/java/scs/comp5903/cucumber/EasyCucumber.java) method to parse and create a
   cucumber test like following:
    ```java
    Path myFeatureFile = Paths.get("path/to/my/feature-file.feature");
    JFeature jFeature = EasyCucumber.build(myFeatureFile, MyStepDefinition.class, MyHooks.class);
    // or you can use any one of following alternative
    // JFeature jFeature = EasyCucumber.build(myFeatureFile, MyStepDefinition1.class, MyStepDefinition2.class, MyHooks.class);
    // JFeature jFeature = EasyCucumber.build(myFeatureFile, new MyStepDefinition1(), new MyStepDefinition2(), new MyHooks());
    // JFeature jFeature = EasyCucumber.build(myFeatureFile, "package.to.stepdefinition");
    // JFeature jFeature = EasyCucumber.build(myFeatureFile, List.of(MyStepDefinition1.class, MyStepDefinition2.class), new EasyCachingObjectProvider());
    // How BaseObjectProvider works can be found in design document mentioned at the end of this readme file
    ```
   You can call `EasyCucumber.build()` at anywhere that Java code can execute. 
   For example, in JUnit, you would likely do:
   ```java
   @Test
   void myTest() {
     Path myFeatureFile = Paths.get("path/to/my/feature-file.feature");
     JFeature jFeature = EasyCucumber.build(myFeatureFile, MyStepDefinition.class);
     // do something with jFeature
   }
   ```

### Run the test

Once you get the executable instance of [`JFeature`](src/main/java/scs/comp5903/cucumber/execution/JFeature.java),
you can run the cucumber test through calling `JFeature.executeAll()` or `JFeature.executeByTag(BaseFilteringTag tag)`.

`JFeature.executeAll()` will run all scenarios and scenario outlines in the feature file.

`JFeature.executeByTag(BaseFilteringTag tag)` take an instance
of [`BaseFilteringTag`](src/main/java/scs/comp5903/cucumber/execution/tag/BaseFilteringTag.java). Then, each scenario or
scenario outline will be examined by the input tag to check should it be run. The input tag can be created by any public
static method in that class including:

1. `static BaseFilteringTag tag(String tag)`: which create a simple tag instance of the
   given string
2. `static BaseFilteringTag or(BaseFilteringTag... tags)`: which create an or-tag so that
   any one of the input tags matches will result in the or-tag is matched
3. `static BaseFilteringTag and(BaseFilteringTag... tags)`: which create an and-tag so
   that all input tags need to be matched to result in the and-tag is matched
4. `static BaseFilteringTag not(BaseFilteringTag tag)`: which create a not-tag so that
   negating the match result of the input tag

These 4 methods can be combined to create complex tag expression. For example, you can create an xor-tag so that
scenario or
scenario outline with either `@tag1` or `@tag2` will be run, but not both or neither, like following:

```java
// import these static methods for convenience

import static scs.comp5903.cucumber.execution.tag.BaseFilteringTag.*;
// then you can create a convenient helper method:
private BaseFilteringTag creatXor(String tag1Str,String tag2Str){
    return or(and(tag(tag1Str),not(tag(tag2Str))),and(not(tag(tag1Str)),tag(tag2Str)));
    }
```

Then, then somewhere in your test code:

```java
myJFeature.executeByTag(createXor("tag1","tag2")); // will only run scenarios with either `@tag1` or `@tag2`, but not both or neither
```

## Supported Features

Only a subset of keywords and features that official cucumber have are supported.

### Supported Cucumber Keywords

- `Feature`: the title of the feature file
- `Scenario`: the scenario
- `Scenario Outline`: the scenario outline, also an alias of `Scenario Template`
- `Given`, `When`, `Then`, `And`, `But`: the step definition
- `Examples`: the examples of the scenario outline, also an alias of `Example`
- `@`: the tag, only above `Feature`, `Scenario` or `Scenario Outline` keywords

### Supported Cucumber Features

- Able to parse `{}` (parsed as string), `{int}`, `{string}`, `{double}`, `{biginteger}` and `{bigdecimal}` in step
  definition
  - see <https://github.com/cucumber/cucumber-expressions#parameter-types> for more details
- Can ignore comments began with `#`
- Can ignore multi-line description placed under `Feature`, `Scenario` or `Scenario Outline`

### Extra Features that Official Cucumber does not support

- Accept passing instances of step definition class:
  Several `EasyCucumber.build()` methods can take the instance of your step definition class as parameter.
  In this case, the cucumber will use your instance to run the step,
  instead of creating a fresh new instance of the step definition class itself using Java Reflection API
  - For example, you can pass in `new MyStepDefinition()` instead of `MyStepDefinition.class` as the parameter
    to `EasyCucumber.build()`
  - This is the **key** feature to **support controlled concurrent execution** of multiple Cucumber tests, 
    achieved by sharing states between different cucumber tests,
    See [this document](./doc/complex%20scenario%20guide.md) for more detail.
- Enforced Keyword Check:
  The step definition keyword in feature file must match the keyword in the annotation of the
  step definition method.
  - For examples: to match `Given a step`, you have to use `@JGivenStep("a step")` instead of `@JWhenStep("a step")`
    or `@JThenStep("a step")`.
    This means if you have both `Given a step` and `When a step` in your feature file,
    you have to write two step definition methods with `@JGivenStep("a step")` and `@JWhenStep("a step")` respectively.
  - This is explicitly designed to allow better flexibility

## For Developers

Please check the [design document](./doc/design%20doc.md) for more details.
