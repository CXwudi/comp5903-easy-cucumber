# Feature to Test Table

This markdown file traces each features listed in the project proposal to test case(s) wrote in the project

## Table

Notice:

- All tests are under the `src/test/java/` directory
- test case is pointed either by the full reference of the test class or the test method in the test class

|    Feature code    | Feature Summary                                                                                        |                                     Unit Test                                      |                          Integration Test                          | N/A Reason                                       |
|:------------------:|--------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------:|:------------------------------------------------------------------:|--------------------------------------------------|
|         1a         | Make the project a maven package                                                                       |                                        N/A                                         |                                N/A                                 | Untestable                                       |
|         2a         | Reformat the code to use separate classes for different part of the parsing logic                      |                                        N/A                                         |                                N/A                                 | This is done by designing the new structure      |
|         2b         | Create data classes instead of using nested collection to store information about the cucumber feature |                                        N/A                                         |                                N/A                                 | This is also done by designing the new structure |
|         2c         | Relax the requirement where an int in the feature file is always mapping into a step de                | `scs.comp5903.cucumber.builder.JStepParameterExtractorTest#canIgnoreIfNotDeclared` |                                None                                |                                                  |
|         2d         | Relax the requirement where step definition method name must be in a fixed pattern                     |                                        None                                        | `scs.comp5903.cucumber.sample.RummikubEasyCucumberIntegrationTest` |                                                  |
| TODO: more to come |                                                                                                        |                                                                                    |                                                                    |                                                  |

