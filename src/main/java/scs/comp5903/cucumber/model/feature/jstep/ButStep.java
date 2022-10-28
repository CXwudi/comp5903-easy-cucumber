package scs.comp5903.cucumber.model.feature.jstep;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
public class ButStep extends AbstractJStep {
  public ButStep(String stepString) {
    super(stepString);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ButStep)) {
      return false;
    }
    final ButStep other = (ButStep) o;
    if (!other.canEqual(this)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof ButStep;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return "ButStep(stepString=" + this.getStepString() + ")";
  }
}
