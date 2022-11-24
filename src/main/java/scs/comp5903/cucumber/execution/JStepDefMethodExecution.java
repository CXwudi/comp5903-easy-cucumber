package scs.comp5903.cucumber.execution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public class JStepDefMethodExecution {
  private final Method method;
  private final Object instance;
  private final Object[] args;

  public JStepDefMethodExecution(Method method, Object instance, Object... args) {
    this.method = method;
    this.instance = instance;
    this.args = args;
  }

  public Object execute() throws InvocationTargetException, IllegalAccessException {
    return method.invoke(instance, args);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JStepDefMethodExecution)) {
      return false;
    }
    JStepDefMethodExecution that = (JStepDefMethodExecution) o;
    return Objects.equals(method, that.method) && Objects.equals(instance, that.instance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, instance);
  }
}
