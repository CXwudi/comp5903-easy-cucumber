package scs.comp5903.cucumber.execution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-19
 */
public class JHookMethodExecution {

  private final Method method;
  private final Object instance;

  public JHookMethodExecution(Method method, Object instance) {
    this.method = method;
    this.instance = instance;
  }

  public void executeWithParameters(Object... args) throws InvocationTargetException, IllegalAccessException {
    method.invoke(instance, args);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JHookMethodExecution)) {
      return false;
    }
    JHookMethodExecution that = (JHookMethodExecution) o;
    return Objects.equals(method, that.method) && Objects.equals(instance, that.instance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, instance);
  }
}
