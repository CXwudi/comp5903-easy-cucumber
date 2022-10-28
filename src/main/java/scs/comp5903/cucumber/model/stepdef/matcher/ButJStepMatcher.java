package scs.comp5903.cucumber.model.stepdef.matcher;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class ButJStepMatcher extends AbstractJStepMatcher {
  public ButJStepMatcher(String matchingString) {
    super(matchingString);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ButJStepMatcher)) {
      return false;
    }
    final ButJStepMatcher other = (ButJStepMatcher) o;
    if (!other.canEqual(this)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof ButJStepMatcher;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return "ButJStepMatcher(matchingString=" + this.getMatchingString() + ")";
  }
}
