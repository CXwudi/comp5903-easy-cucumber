# Feature to Test Table

This markdown file traces each features listed in the project proposal to test case(s) wrote in the project

## Table

Notice:

- All tests are under the `src/test/java/` directory
- test case is pointed either by the full reference of the test class or the test method in the test class

|    Feature code    |                                     Unit Test                                      |                          Integration Test                          | Feature Summary                                                                                        | N/A Reason                                       |
|:------------------:|:----------------------------------------------------------------------------------:|:------------------------------------------------------------------:|--------------------------------------------------------------------------------------------------------|--------------------------------------------------|
|         1a         |                                        N/A                                         |                                N/A                                 | Make the project a maven package                                                                       | Untestable                                       |
|         2a         |                                        N/A                                         |                                N/A                                 | Reformat the code to use separate classes for different part of the parsing logic                      | This is done by designing the new structure      |
|         2b         |                                        N/A                                         |                                N/A                                 | Create data classes instead of using nested collection to store information about the cucumber feature | This is also done by designing the new structure |
|         2c         | `scs.comp5903.cucumber.builder.JStepParameterExtractorTest#canIgnoreIfNotDeclared` |                                None                                | Relax the requirement where an int in the feature file is always mapping into a step de                |                                                  |
|         2d         |                                        None                                        | `scs.comp5903.cucumber.sample.RummikubEasyCucumberIntegrationTest` | Relax the requirement where step definition method name must be in a fixed pattern                     |                                                  |
| TODO: more to come |                                                                                    |                                                                    |                                                                                                        |                                                  |

