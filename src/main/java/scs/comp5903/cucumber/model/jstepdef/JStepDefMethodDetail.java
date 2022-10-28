package scs.comp5903.cucumber.model.jstepdef;

import scs.comp5903.cucumber.model.jstepdef.matcher.AbstractJStepMatcher;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * This class store the information of a step definition method.
 *
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class JStepDefMethodDetail {
  private final Method method;
  private final AbstractJStepMatcher matcher;

  public JStepDefMethodDetail(Method method, AbstractJStepMatcher matcher) {
    this.method = method;
    this.matcher = matcher;
  }

  public Method getMethod() {
    return this.method;
  }

  public AbstractJStepMatcher getMatcher() {
    return this.matcher;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof JStepDefMethodDetail)) {
      return false;
    }
    final JStepDefMethodDetail other = (JStepDefMethodDetail) o;
    final Object thisMethod = this.getMethod();
    final Object otherMethod = other.getMethod();
    if (!Objects.equals(thisMethod, otherMethod)) {
      return false;
    }
    final Object thisMatcher = this.getMatcher();
    final Object otherMatcher = other.getMatcher();
    return Objects.equals(thisMatcher, otherMatcher);
  }

  @Override
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object thisMethod = this.getMethod();
    result = result * PRIME + (thisMethod == null ? 43 : thisMethod.hashCode());
    final Object thisMatcher = this.getMatcher();
    result = result * PRIME + (thisMatcher == null ? 43 : thisMatcher.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "JStepDefMethodDetail(method=" + this.getMethod() + ", matcher=" + this.getMatcher() + ")";
  }
}
