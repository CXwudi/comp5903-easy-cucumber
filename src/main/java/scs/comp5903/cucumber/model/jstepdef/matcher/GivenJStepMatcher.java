package scs.comp5903.cucumber.model.jstepdef.matcher;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class GivenJStepMatcher extends AbstractJStepMatcher {
  public GivenJStepMatcher(String matchingString) {
    super(matchingString);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GivenJStepMatcher)) {
      return false;
    }
    final GivenJStepMatcher other = (GivenJStepMatcher) o;
    if (!other.canEqual(this)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof GivenJStepMatcher;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return "GivenJStepMatcher(matchingString=" + this.getMatchingString() + ")";
  }
}
