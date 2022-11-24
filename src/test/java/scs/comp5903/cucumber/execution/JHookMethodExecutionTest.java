package scs.comp5903.cucumber.execution;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-20
 */
class JHookMethodExecutionTest {

  public void testMethod(String a, int b) {
    throw new RuntimeException("test");
  }

  @Test
  void canExecuteOnMatching() throws NoSuchMethodException {
    JHookMethodExecution execution = new JHookMethodExecution(this.getClass().getMethod("testMethod", String.class, int.class), this);
    var exp = assertThrows(InvocationTargetException.class, () -> execution.executeOnParametersMatch("a", 1));
    assertEquals(RuntimeException.class, exp.getCause().getClass());
  }

  @Test
  void canExecuteOnProvidingMoreParameters() throws NoSuchMethodException {
    JHookMethodExecution execution = new JHookMethodExecution(this.getClass().getMethod("testMethod", String.class, int.class), this);
    var exp = assertThrows(InvocationTargetException.class, () -> execution.executeOnParametersMatch("a", 1, new Object()));
    assertEquals(RuntimeException.class, exp.getCause().getClass());
  }
}