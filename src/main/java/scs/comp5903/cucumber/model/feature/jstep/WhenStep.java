package scs.comp5903.cucumber.model.feature.jstep;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
public class WhenStep extends AbstractJStep {
  public WhenStep(String stepString) {
    super(stepString);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof WhenStep)) {
      return false;
    }
    final WhenStep other = (WhenStep) o;
    if (!other.canEqual(this)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof WhenStep;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return "WhenStep(stepString=" + this.getStepString() + ")";
  }
}
