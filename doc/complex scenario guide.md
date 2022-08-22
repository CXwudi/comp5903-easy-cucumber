# How to write complex scenario

A complex scenario is scenario that involve testing of concurrency, which usually involve multi-thread, resource
contention, and etc.

However, Cucumber, as a sequential executed test framework, does not natively supports running multiple tests
concurrently. Although attempts has been made to encounter this inability (TODO: need reference of Mudit;s paper), but
either the solution is very fragile, or not fully completed.

In this guide, I would like to share two ways of writ

## Scenario explanation

Imaging a Course Management System (CMS), where there is only one spot left for a course, and there are two students
trying to register the course. We want to test that when both students click the confirmation button at the same time,
only one of them can be registered.

## Way 1: embed the multi-thread logic inside one step definition class

//TODO: explain the idea: step definition method is ran one-by-one, then we can create threads and lock in those
one-by-one method call.

//TODO: provide an
example https://github.com/CXwudi/comp5903-project/blob/main/cms-cucumber/src/test/java/scs/comp5903/cmscucumber/cucumber/twostudents/TwoStudentsRaceConditionStepDef.java
and https://github.com/CXwudi/comp5903-project/blob/main/cms-cucumber/src/test/resources/scs.comp5903.cmscucumber/cucumber/twostudents/two_students_race_for_one_spot.feature

//TODO: then explain the pro and con

pro: easy understand

con: poor reusability and extensibility, and hence we need the way 2 below

## Way 2: providing own instances of step definition class to achieve share states between different cucumber tests

//TODO: explain the idea: given that we can create our own instance of step definition, and also given that running a
test is basically some function call, then we run multiple cucumber tests inside a cucumber test, and each smaller test
has shared resources

//TODO:
example: https://github.com/CXwudi/comp5903-easy-cucumber/blob/main/src/test/java/scs/comp5903/cucumber/sample/CmsComplexScenarioMultithreadStepDefs.java
and https://github.com/CXwudi/comp5903-easy-cucumber/blob/main/src/test/resources/sample/jfeature/cms/two_students_race_for_one_spot.jfeature

//TODO: pro and con:

pro: reusable, maintainable, extensibillity

con: hacky step definition implementation with extra locking and threads managing.
