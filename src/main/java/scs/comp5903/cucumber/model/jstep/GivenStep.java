package scs.comp5903.cucumber.model.jstep;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
public class GivenStep extends AbstractJStep {
  public GivenStep(String stepString) {
    super(stepString);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GivenStep)) {
      return false;
    }
    final GivenStep other = (GivenStep) o;
    if (!other.canEqual(this)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof GivenStep;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return "GivenStep(stepString=" + this.getStepString() + ")";
  }
}
