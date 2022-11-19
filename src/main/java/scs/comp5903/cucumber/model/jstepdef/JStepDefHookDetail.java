package scs.comp5903.cucumber.model.jstepdef;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-12
 */
public class JStepDefHookDetail {

  private final Method method;
  private final JHookType type;

  public JStepDefHookDetail(Method method, JHookType type) {
    this.method = method;
    this.type = type;
  }

  public Method getMethod() {
    return this.method;
  }

  public JHookType getType() {
    return this.type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JStepDefHookDetail)) return false;
    JStepDefHookDetail that = (JStepDefHookDetail) o;
    return Objects.equals(method, that.method) && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, type);
  }
}
