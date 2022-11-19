package scs.comp5903.cucumber.execution;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
class JStepDefMethodExecutionTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(JStepDefMethodExecutionTest.class);

  @Test
  void executeAMethodThrowReflection() throws NoSuchMethodException {
    var method = getClass().getMethod("printAStr", String.class);
    var methodExecution = new JStepDefMethodExecution(method, this, "Hello World from MethodExecutionTest");
    assertDoesNotThrow(methodExecution::execute, "failed with exception");
  }

  public void printAStr(String str) {
    log.info(str);
  }

}