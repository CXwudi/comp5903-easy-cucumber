package scs.comp5903.cucumber.model.jfeature.jstep;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
public class AndStep extends AbstractJStep {
  public AndStep(String stepString) {
    super(stepString);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AndStep)) {
      return false;
    }
    final AndStep other = (AndStep) o;
    if (!other.canEqual(this)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof AndStep;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return "AndStep(stepString=" + this.getStepString() + ")";
  }
}
