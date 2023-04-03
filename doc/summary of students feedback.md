All summary is based on version 0.3.x during that time.

## Yuanshi Li

Q: How is Easy Cucumber compared to the official Cucumber?

A: Didn't answer, but yes

pro:

- easy to setup
- provided logical tag filter
  - this is not a pro, official cucumber also has it
  - indicate documentation issue from official cucumber

con:

- worse performance
- ~~no batch execution like CucumberOptions~~
  - my comment: not really, but maybe this can be a documentation issue

## Victor Yang

Q: How is Easy Cucumber compared to the official Cucumber?

A: Yes, it is pretty much similar to official Cucumber

pro:

- better performance

con:

- enforcement of keyword cause more codes
  - my comment: means not all ppl like this enforcement
- ~~(Unreproducible) two methods in two classes with the same name cause an error~~

## Harry Chen

Q: How is Easy Cucumber compared to the official Cucumber?

A: East Cucumber has no fundamental difference than in Official Cucumber

pro:

- easy to setup, easy to use

con:

- lack of table support in step definition (not the scenario template)
- not enough support since it only has one developer

## Riyanson Alfred

Q: How is Easy Cucumber compared to the official Cucumber?

A: the semantics in both cucumber and EASY-Cucumber were very similar

pro:

- better error message for debugging
- ~~tag is useful~~
  - my comment: tag is also supported in official cucumber
- ~~can run selected individual scenario~~
  - my comment: is also supported, https://stackoverflow.com/questions/40945130/cucumber-how-to-run-specific-scenario-from-a-feature-file

con:

- lack of support for indicating which scenario failed
  - my comment: fixed just recently
- lack of support for providing a summary of the test result
  - my comment: fixed just recently

## Shuvankar Saha

Q: How is Easy Cucumber compared to the official Cucumber?

A: Didn't answer

pro:

- better naming in annotations
- enforcement of keyword
- separation of build-time and run-time
- better control in gluing code and annotation (?)
- has a flexible number of parameters in EasyCucumber
- concurrent execution support, sharing state between two or more tests
- easy to setup

con:

- no

## Andrew Hlynka

Q: How is Easy Cucumber compared to the official Cucumber?

A: EASY-CUCUMBER to be about as simple to define the acceptance tests as standard CUCUMBER


pro:

- easy to setup, no extra dependency, easy to use
  - my comment: invalid since 0.5.0
- provided logical tag filter
  - my comment: this is not a pro, official cucumber also has it
  - my comment: indicate documentation issue from official cucumber
- support providing own instance of step definition class
- support complex scenarios with concurrent execution
- enforcement of keyword

con:

- lack better logging to indicate success and failure
  - my comment: this is really based on one's configuration. so another documentation problem
- enforcement of keywords also causes more duplicated codes

## Joshi Saurabh

Q: How is Easy Cucumber compared to the official Cucumber?

A: It is equally easy to express the acceptance tests in both frameworks

pro:

- innovative state-machine-based parser
- easy to setup and easier to use

con:

- the parser is hard to maintain
- lack better logging to indicate success and failure

