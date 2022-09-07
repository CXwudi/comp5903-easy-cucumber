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

The `JFeatureFileParser` class is responsible for parsing the feature file,
it uses the `JFeatureFileLineByLineParser` class to perform the parsing logic in a state machine manner,
which will be explained below.

### The state machine and the "Sense, Think, React" framework

The implementation of the parsing logic is based on the concept of the state machine.

When a feature file is parsed, the parser will go through the file line by line.
Each line (except comments and empty line) is traded as a step in state machine diagram.
In each step, the parser performs "Sense, Think, React" three actions as following:

1. Sense: Read a line, and extract the information out of the line. (e.g. keyword)
2. Think: Based on the information and the current state, think of what new state to switch to.
3. React: Switch to the new state and react based on the new state.

For example, a parser is currently at the `Feature` state.
When a next line began with `Scenario` is read, the following happens:

1. Sense: Read the line, the parser found out the line begins with `Scenario`.
2. Think: Since the parser is currently on the current state `Feature` and the next line begin with `Scenario`, the
   parser thinks it should switch to the `Scenario` state.
3. React: The parser switches to the `Scenario` state, read the next line, and records the title of this new scenario.

### The state-machine diagram for parsing feature file

With the idea of the state machine and the "Sense, Think, React" framework,
the state-machine diagram for parsing feature file is designed as following:

![state-machine diagram](./images/5903%20diagram-State%20Machine%20Diagram%20for%20feature%20file%20parsing.drawio.png)

Each direction line in the diagram represents a possible state transmissions.
For example, there is a line from the `Feature` state to the `Scenario` states.
So parser can switch from the `Feature` state to the `Scenario` states but not to the `Step` state.

Most of the switching condition can be determined by the current state and the keyword from the next line.
For example, at the `Feature` state, there are many states that parser can choose to switch to,
but the parser has to switch to the `Scenario` state if the next line begins with `Scenario`.

However, some switching conditions need more information than just the current state and the keyword from the next line.
For examples, multi-line description can be put below the `Feature`, the `Scenario`, and the `Scenario Outline` line.
So after multi-line description, the parser can go to any state that the `Feature`,
the `Scenario`, and the `Scenario Outline`state can go to.
Therefore, the parser always kept track of the parent state of the current state that it has visited.
For example, the parent state of the `Scenario` state is the `Feature` state,
and the parent state of the `Step` state is the `Scenario` or the `Scenario Outline` state.
When the parser transfers from the `Scenario` state to the (multi-line) `Description` state, the parent state 
is recorded as the `Scenario` state, because the parser just visited the `Scenario` state.

### Implementation of the "Sense, Think, React" framework

Just like the name of the "Sense, Think, React" framework, 
the implementation of the framework is simply divided into 3 functions that are called one by one in a loop.
Here is an example implementation: (This example is using a robot with sensors, but the idea is the same here)

![example state-machine](./images/example%20state-machine%20implementation.png)

