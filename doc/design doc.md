# Design Doc of Enhanced "Cucumberized JUnit" 

For developers who want to hack and modify this tool,
this is the document that helps you to understand the design and the internal structure of this tool.

## Proposal of this tool

The goal of designing this tool is simplification. We want a Cucumber implementation that requires zero setup, easy-to-use APIs so that any levels of users can just pick it up and starts using it, without the need to search how to setup, how to integrate with other tools, and etc.

## General Code Flow

TODO:

1. introduce the build/run separation + the pseudocode for flowchart
2. paste the flowchart into the document + explanation

When a Cucumber test is executed, the test needed to be phrased and then get ran. In general, the following high level pseudocode can summarize the procedure of Cucumber test execution:

```
Given inputs: feature file, classes 
1. JfeatureDetail <- parseJfeatureFile(feature file) 
2. JStepDefDetail <- parseStepDefinition(classes) 
3. executableJFeature <- createExecutable(JfeatureDetail, JStepDefDetail)
4. User calls executableJFeature.execute() to run the cucumber test
```

Here is also the associated UML flow chart to visualize the high level pseudocode:

![Flow Chart](images/5903%20diagram-Overall%20Flow.drawio.png)

In the pseudocode, the first 3 lines of codes are building the executable JFeature instance, where the last line, the 4th line, is executing the test. Hence the first 3 lines of codes are also called the buildtime logic, and the last line is also called the runtime logic.

Therefore, the tool only contains two groups of APIs, which are the [`EasyCucumber.build()`](../src/main/java/scs/comp5903/cucumber/EasyCucumber.java) APIs that implements the buildtime logic and the [`JFeature.executeXXX()`](../src/main/java/scs/comp5903/cucumber/execution/JFeature.java) APIs that implements the runtime logic.

## Internal Structure

paste the UML class diagram into the document

Then detail explain each package and the structure

## How does this tool parse the feature file?

Introduce the general idea from COMP2801, can just use https://github.com/CXwudi/comp5903-project/issues/4

then paste the UML state-machine diagram

and explain how UML state-machine diagram is implemented, using the idea from COMP2801.

e.g. to swtich to scenario state, sence, think, react, what did we do.
