package scs.comp5903.cucumber.model.jstepdef.matcher;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class WhenJStepMatcher extends AbstractJStepMatcher {
  public WhenJStepMatcher(String matchingString) {
    super(matchingString);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof WhenJStepMatcher)) {
      return false;
    }
    final WhenJStepMatcher other = (WhenJStepMatcher) o;
    if (!other.canEqual(this)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof WhenJStepMatcher;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return "WhenJStepMatcher(matchingString=" + this.getMatchingString() + ")";
  }
}
