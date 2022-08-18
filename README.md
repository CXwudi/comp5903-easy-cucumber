# Easy Cucumber

[![](https://jitpack.io/v/CXwudi/comp5903-easy-cucumber.svg)](https://jitpack.io/#CXwudi/comp5903-easy-cucumber)

COMP5903 project at Carleton University. It is the enhancement of Alexei's
original ["Cucumberized" JUnit](https://github.com/alexeikrumshyn/cucumberized-junit)

List of improvement, UML diagram for improved Cucumberized JUnit can be found in `doc` directory

TOC:

<!-- TOC -->

* [Prerequisite](#prerequisite)
* [How to import](#how-to-import)
  * [Maven](#maven)
  * [Gradle](#gradle)
* [How to use](#how-to-use)
* [Supported Features](#supported-features)
  * [Supported Cucumber Keywords](#supported-cucumber-keywords)
  * [Supported Cucumber Features](#supported-cucumber-features)
  * [Extra Features that Official Cucumber does not support](#extra-features-that-official-cucumber-does-not-support)

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

The API is divided into build phase and runtime phase.

### Build the test

1. First, write your feature file. The feature file can be stored in any directory that you can access using
   Java's `Path`.

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

2. Create your step definition class(es), these classes must be public and all step definition must be public as well.
    ```java
    // MyStepDefinition.java
    import scs.comp5903.cucumber.model.JStep;
    import scs.comp5903.cucumber.model.JStepKeyword;
    
    public class MyStepDefinition {
    
      private int count;
    
      @JStep(keyword = JStepKeyword.GIVEN, step = "I have {int} apple")
      public void iHave(int count) {
        this.count = count;
      }
    
      @JStep(keyword = JStepKeyword.WHEN, step = "I eat {int} apple")
      public void iEat(int eatCount) {
        this.count -= eatCount;
      }
    
      @JStep(keyword = JStepKeyword.THEN, step = "I should left {int} apple")
      public void iShouldLeft(int leftCount) {
        assertEquals(leftCount, this.count);
      }
    }
    ```

3. Call [`EasyCucumber.build()`](src/main/java/scs/comp5903/cucumber/EasyCucumber.java) method to parse and create a
   cucumber test like following:
    ```java
    Path myFeatureFile=Paths.get("path/to/my/feature-file.feature");
    JFeature jFeature=EasyCucumber.build(myFeatureFile,MyStepDefinition.class);
    // or you can use any one of following alternative
    // JFeature jFeature = EasyCucumber.build(myFeatureFile, MyStepDefinition1.class, MyStepDefinition2.class);
    // JFeature jFeature = EasyCucumber.build(myFeatureFile, new MyStepDefinition1(), new MyStepDefinition2());
    // JFeature jFeature = EasyCucumber.build(myFeatureFile, "package.to.stepdefinition");
    // JFeature jFeature = EasyCucumber.build(myFeatureFile, List.of(MyStepDefinition1.class, MyStepDefinition2.class), new EasyCachingObjectProvider());
    // TODO: more explanation of using user-defined BaseObjectProvider 
    ```

### Run the test

//TODO: more detailed explanation

Once you get the executable instance of [`JFeature`](src/main/java/scs/comp5903/cucumber/execution/JFeature.java),
you can run the cucumber test through calling `JFeature.executeAll()`or `JFeature.executeByTag(BaseFilteringTag tag)`
method to execute selected scenarios and scenario outlines that matching the input tag

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

- Able to parse `{int}`, `{string}`, `{double}`, `{biginteger}` and `{bigdecimal}` in step definition
  - see <https://github.com/cucumber/cucumber-expressions#parameter-types> for more details
- Can ignore comments began with `#`
- Can ignore multi-line description placed under `Feature`, `Scenario` or `Scenario Outline`

### Extra Features that Official Cucumber does not support

- Several `EasyCucumber.build()` methods can take the instance of your step definition class as parameter. In this case,
  the cucumber will use your instance to run the step, instead of create a fresh new instance of the step definition
  class using Java Reflection API
  - For example, you can pass in `new MyStepDefinition()` instead of `MyStepDefinition.class` as the parameter
    to `EasyCucumber.build()`
  - This can be useful for sharing states between different cucumber tests,
    or sharing the same step definition class instance across multiple cucumber tests.
    TODO: more explain on this in another doc

