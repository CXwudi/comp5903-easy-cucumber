# Design Doc of Enhanced "Cucumberized JUnit"

For developers who want to hack and modify this tool,
this is the document that helps you to understand the design and the internal structure of this tool.

## Proposal of this tool

The goal of designing this tool is simplification. We want a Cucumber implementation that requires zero setup,
easy-to-use APIs so that any levels of users can just pick it up and starts using it, without the need to search how to
setup, how to integrate with other tools, and etc.

## General Code Flow

When a Cucumber test is executed, the test needed to be phrased and then get ran. In general, the following high level
pseudocode can summarize the procedure of Cucumber test execution:

```
Given inputs: feature file, classes 
1. JfeatureDetail <- parseJfeatureFile(feature file) 
2. JStepDefDetail <- parseStepDefinition(classes) 
3. executableJFeature <- createExecutable(JfeatureDetail, JStepDefDetail)
4. User calls executableJFeature.execute() to run the cucumber test
```

Here is also the associated UML flow chart to visualize the high level pseudocode:

![Flow Chart](images/5903%20diagram-Overall%20Flow.drawio.png)

In the pseudocode, the first 3 lines of codes are building the executable `JFeature` instance, where the last line, the
4th line, is executing the test. Hence the first 3 lines of codes are also called the buildtime logic, and the last line
is also called the runtime logic.

Therefore, the tool only contains two groups of APIs, which are
the [`EasyCucumber.build()`](../src/main/java/scs/comp5903/cucumber/EasyCucumber.java) APIs that implements the
buildtime logic and the [`JFeature.executeXXX()`](../src/main/java/scs/comp5903/cucumber/execution/JFeature.java) APIs
that implements the runtime logic.

## Internal Structure

// TODO:paste the UML class diagram into the document

// Then detail explain each package and the structure

Design of the internal structure is more or less following the pseudocode.

### The `models` and `parser` packages

In the first line `JfeatureDetail <- parseJfeatureFile(feature file) `, there is an entity class `JFeatureDetail` and a
class for executing the `parseJfeatureFile()` method. For the later one, the class is designed as `JFeatureFileParser`.
These two classes have two different roles. The `JFeatureDetail`, coded as POJO (Plain Old Java Object), is
corresponding of recording the parsed information from `JFeatureFileParser.parseJfeatureFile()` method.
Whereas `JFeatureFileParser` simply just executes the `parseJfeatureFile()` method. Therefore, these two classes belongs
to two different packages.

Similarly, in the second line `JStepDefDetail <- parseStepDefinition(classes) `, the entity class is `JStepDefDetail`
and the class executing the `parseStepDefinition()` method is designed as `JStepDefinitionParser`.

Both `JFeatureDetail` and `JStepDefDetail` are entity classes. So they are placed in the same package called `models`.
Likewise, both `JStepDefinitionParser` and `JFeatureFileParser` are parsers. So these two classes are placed in the same
package called `parser`. Hence, the first two line of the pseudocode ends up with a partial UML class diagram as
following:

![models](./images/5903%20diagram-UML%20Class%20Diagram.drawio-models.png)
![parser](./images/5903%20diagram-UML%20Class%20Diagram.drawio-parser.png)

In the `models` package, `JFeatureDetail`
contains a list of `JScenarioDetail` and a list of `JScenarioOutlineDetail`.
The `JScenarioOutlineDetail` also contains a list of `JScenarioDetail`,
because the example table in a scenario outline is
preprocessed and converted into a list of scenarios for simplicity.
The `JStepDefDetail` simply contains a list of `JStepDefMethodDetail`,
which holds information of a step definition method from a step definition class.

In the `parser` package, there is `JStepDefinitionParser` and `JFeatureFileParser`.
`JFeatureFileParser` contains some dependencies that will be explained in the later sections of the document.

### The `execution` and `builder` package

The third line of the pseudocode, `executableJFeature <- createExecutable(JfeatureDetail, JStepDefDetail)`, is where all
detail objects are combined to create the executable `JFeature` instance.

Hence, a class called `JFeatureBuilder` is designed to execute the `createExecutable()` method.
The class is also placed in the `builder` package.

![builder](./images/5903%20diagram-UML%20Class%20Diagram.drawio-builder.png)

// TODO: explain the optional `BaseObjectProvider` class

`createExecutable()` method produces an `executableJFeature`.
Hence, a new entity class `JFeature` that represents is created.
The structure of the `JFeature` is very similar to the `JFeatureDetail` in the `models` package, except `JFeature` has
several methods to perform the Cucumber test execution,
which are the `executeXXX()` methods that implements the runtime logic mentioned in the pseudocode section of this
document.

![execution](./images/5903%20diagram-UML%20Class%20Diagram.drawio-execution.png)

### The whole UML class diagram

Therefore, putting all packages together, the whole UML class diagram looks like:

![UML Class Diagram](./images/5903%20diagram-UML%20Class%20Diagram.drawio.png)

## How does this tool parse the feature file?

Introduce the general idea from COMP2801, can just use https://github.com/CXwudi/comp5903-project/issues/4

then paste the UML state-machine diagram

and explain how UML state-machine diagram is implemented, using the idea from COMP2801.

e.g. to swtich to scenario state, sence, think, react, what did we do.
