# Easy Cucumber

The Enhancement of Alexei's ["Cucumberized" JUnit](https://github.com/alexeikrumshyn/cucumberized-junit)

List of improvement, UML diagram for improved Cucumberized JUnit can be found in `easy-cucumber/doc` directory

## To develop or check code

- Java 11 or above

## How to use

First, build the cucumber test using APIs defined in the [`EasyCucumber`](src\main\java\scs\comp5903\cucumber\EasyCucumber.java) class.  
All APIs in `EasyCucumber` return an executable [`JFeature`](src/main/java/scs/comp5903/cucumber/execution/JFeature.java) class that can run the cucumber test.
Simply call it anywhere to run the test.

Currently, this project is not published yet as it is still WIP (work in progress). Once it is ready, it will be published as a maven package through [JitPack](https://jitpack.io/).

## Supported Cucumber Keywords

- `Feature`: the title of the feature file
- `Scenario`: the scenario
- `Scenario Outline`: the scenario outline
- `Given`, `When`, `Then`, `And`, `But`: the step definition

## Supported Cucumber Features

- Able to parse `{int}`, `{string}`, `{double}`, `{biginteger}` and `{bigdecimal}` in step definition
- TODO: more