package scs.comp5903.cucumber.model.stepdef.matcher;


import java.util.Objects;

/**
 * The base data class to store the user's matcher string on the step definition method
 *
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public abstract class AbstractJStepMatcher {
  protected String matchingString;

  public AbstractJStepMatcher(String matchingString) {
    this.matchingString = matchingString;
  }

  public String getMatchingString() {
    return this.matchingString;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AbstractJStepMatcher)) {
      return false;
    }
    final AbstractJStepMatcher other = (AbstractJStepMatcher) o;
    if (!other.canEqual(this)) {
      return false;
    }
    final Object thisMatchingString = this.getMatchingString();
    final Object otherMatchingString = other.getMatchingString();
    return Objects.equals(thisMatchingString, otherMatchingString);
  }

  protected boolean canEqual(final Object other) {
    return other instanceof AbstractJStepMatcher;
  }

  @Override
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object thisMatchingString = this.getMatchingString();
    result = result * PRIME + (thisMatchingString == null ? 43 : thisMatchingString.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "AbstractJStepMatcher(matchingString=" + this.getMatchingString() + ")";
  }
}
