package scs.comp5903.cucumber.model.feature.jstep;

import java.util.Objects;

/**
 * The base class to store user's step
 *
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
public abstract class AbstractJStep {
  private final String stepString;

  protected AbstractJStep(String stepString) {
    this.stepString = stepString;
  }

  public String getStepString() {
    return stepString;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AbstractJStep)) {
      return false;
    }
    final AbstractJStep other = (AbstractJStep) o;
    if (!other.canEqual(this)) {
      return false;
    }
    final Object thisStep = this.getStepString();
    final Object otherStep = other.getStepString();
    return Objects.equals(thisStep, otherStep);
  }

  protected boolean canEqual(final Object other) {
    return other instanceof AbstractJStep;
  }

  @Override
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object stepString1 = this.getStepString();
    result = result * PRIME + (stepString1 == null ? 43 : stepString1.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "AbstractJStep(stepString=" + this.getStepString() + ")";
  }
}
