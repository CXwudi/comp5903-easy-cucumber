# Design Document of the Enhanced "Cucumberized JUnit"

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
4th line, is executing the test.
Hence, the first 3 lines of codes are also called the buildtime logic,
and the last line is also called the runtime logic.

Therefore, the tool only contains two groups of APIs, which are
the [`EasyCucumber.build()`](../src/main/java/scs/comp5903/cucumber/EasyCucumber.java) APIs that implements the
buildtime logic and the [`JFeature.executeXXX()`](../src/main/java/scs/comp5903/cucumber/execution/JFeature.java) APIs
that implements the runtime logic.

## Internal Structure

Design of the internal structure is more or less following the pseudocode.

### The `models` and `parser` packages

In the first line `JfeatureDetail <- parseJfeatureFile(feature file) `, there is an entity class `JFeatureDetail` and a
control class `JFeatureFileParser` for executing the `parseJfeatureFile()` method.
The entity class `JFeatureDetail` is coded as POJO (Plain Old Java Object).
It stores the parsed information from `JFeatureFileParser.parseJfeatureFile()` method.
Therefore, these two classes belongs to two different packages.

Similarly, in the second line `JStepDefDetail <- parseStepDefinition(classes) `, the entity class is `JStepDefDetail`
and the control class is `JStepDefinitionParser` which contains the `parseStepDefinition()` method.

Both `JFeatureDetail` and `JStepDefDetail` are entity classes. 
So they are placed in the same package called `models`.
Likewise, both `JStepDefinitionParser` and `JFeatureFileParser` are parsers, which are also control classes. 
So these two classes are placed in the same package called `parser`. 
Hence, the first two lines of the pseudocode ends up with a partial UML class diagram as following:

![models](./images/5903%20diagram-UML%20Class%20Diagram.drawio-models.png)
![parser](./images/5903%20diagram-UML%20Class%20Diagram.drawio-parser.png)

In the `models` package, `JFeatureDetail`
contains a list of `JScenarioDetail` and a list of `JScenarioOutlineDetail`.
The `JScenarioOutlineDetail` is designed to contain a list of `JScenarioDetail`,
because the example table in a scenario outline is
preprocessed and converted into a list of scenarios for simplicity.
The `JStepDefDetail` simply contains a list of `JStepDefMethodDetail`,
which holds information of a step definition method from a step definition class.

In the `parser` package, there is `JStepDefinitionParser` and `JFeatureFileParser`.
`JFeatureFileParser` contains some dependencies that will be explained in the later sections of the document.

### The `builder` package

The third line of the pseudocode, `executableJFeature <- createExecutable(JfeatureDetail, JStepDefDetail)`, 
literally just combine the instance of `JFeatureDetail` and `JStepDefDetail` into an executable `JFeature` instance.

Hence, a control class called `JFeatureBuilder` is designed to run the `createExecutable()` method.
The class is placed in the `builder` package. 
It contains the `build()` method which is the implementation of the `createExecutable()` method.

![builder](./images/5903%20diagram-UML%20Class%20Diagram.drawio-builder.png)

Notice that `build()` method takes an extra parameter other than `JFeatureDetail` and `JStepDefDetail`,
called`BaseObjectProvider`. 
This is because the executable `JFeature` instance need to remember
which instance of step definition classes to use to execute step definition methods.
The `BaseObjectProvider` is designed to provide the answer to this question for the executable `JFeature`.
It only has one method `get(clazz: Class<T>): T` which receives an input of the step definition class and 
return the instance of the step definition class.

It is up to the implementation
of `BaseObjectProvider` to decide how to provide the instance of the step definition class.
So far, the tool only contains one implementation, which is `EasyCachingObjectProvider`.
`EasyCachingObjectProvider` always returns the same instance of the same step definition class
unless such instance is not created before.
Then it will create a new instance of the step definition class, store it in a map and return it.

The `EasyCucumber` class contains some APIs that allow users to provide their own `BaseObjectProvider` instance.
For example,
the `EasyCucumber.build(Path featureFile, List<Class<?>> stepDefinitionClasses, BaseObjectProvider objectProvider)` method.
This is intentionally added to allow users to freely use their favourite dependency injection framework
(Spring, Quarkus, Micronaut, Dagger2, etc.) to provide the instance of their step definition classes.
Users only need to create an implementation class of `BaseObjectProvider` that
delegate the method `get(clazz: Class<T>): T` to their own choice of dependency injection frameworks.

### The `execution` package

The structure of the `JFeature` is very similar to the `JFeatureDetail` in the `models` package, except `JFeature` has
several methods to perform the Cucumber test execution,
which are the `executeXXX()` methods that implements the runtime logic mentioned in the pseudocode section of this
document.

![execution](./images/5903%20diagram-UML%20Class%20Diagram.drawio-execution.png)

### The whole UML class diagram

Therefore, putting all packages together, the whole UML class diagram looks like:

![UML Class Diagram](./images/5903%20diagram-UML%20Class%20Diagram.drawio.png)

## How does this tool parse the feature file?

One highlight of this project is the innovative way
of parsing the feature file that utilizes the concept of the state machine.
The implementation is done in the control class `JFeatureFileLineByLineParser`.

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

### How to implement the "Sense, Think, React" framework

The "Sense, Think, React"
framework is implemented in a loop where each iteration is one step in the state machine,
like the following diagram:

![example state-machine](./images/example%20state-machine%20implementation.png)

In this tool, loop is controlled by `JFeatureFileParser`, 
and in each iteration, `JFeatureFileParser` calls the `JFeatureFileLineByLineParser.accept(String)` method.
The `JFeatureFileLineByLineParser.accept(String)` method calls three methods `sense(String)`,
`think(SenceResult)`, and `react(String)` 
which each is self-explanatory of what they do in the "Sense, Think, React" framework.