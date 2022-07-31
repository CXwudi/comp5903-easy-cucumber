package scs.comp5903.cucumber.model.matcher;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class ThenJStepMatcher extends AbstractJStepMatcher {
  public ThenJStepMatcher(String matchingString) {
    super(matchingString);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ThenJStepMatcher)) {
      return false;
    }
    final ThenJStepMatcher other = (ThenJStepMatcher) o;
    if (!other.canEqual(this)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof ThenJStepMatcher;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return "ThenJStepMatcher(matchingString=" + this.getMatchingString() + ")";
  }
}
