package scs.comp5903.cucumber.execution;

import java.lang.invoke.MethodType;
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

  /**
   * @param args the matching parameters of the method. <br/>
   *             be awared that this method allows extra parameters than the method's parameters. <br/>
   *             but the order of parameters must matches the order of method's parameters.
   *
   */
  public void executeOnParametersMatch(Object... args) throws InvocationTargetException, IllegalAccessException {
    var expectedTypes = method.getParameterTypes();
    if (expectedTypes.length <= args.length) {
      var isAssignable = true;
      var extractedCount = 0;
      var extractedParameters = new Object[expectedTypes.length];
      // check each parameter type with consideration of boxed types
      for (int i = 0; i < expectedTypes.length; i++) {
        isAssignable = isAssignable && MethodType.methodType(expectedTypes[i]).wrap().returnType().equals(args[i].getClass());
        if (isAssignable) {
          extractedParameters[i] = args[i];
          extractedCount++;
        } else {
          break;
        }
      }
      if (isAssignable && extractedCount == expectedTypes.length) {
        method.invoke(instance, extractedParameters);
      }
    }
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
