package scs.comp5903.cucumber.model;

import java.util.List;
import java.util.Objects;

/**
 * This is the central detail class that stores all information about step definitions/execution for one feature.
 * In the future, this class may contain Hooks, Background, Custom Parameter type registry, etc.
 *
 * @author Charles Chen 101035684
 * @date 2022-06-29
 */
public class JStepDefDetail {

  /**
   * The list of step definitions.
   * Be aware that step definitions for one feature is not tied to a class.
   * It is class-independent.
   * So this list can contain step definitions from many classes.
   */
  private final List<JStepDefMethodDetail> steps;

  public JStepDefDetail(List<JStepDefMethodDetail> steps) {
    this.steps = steps;
  }

  public List<JStepDefMethodDetail> getSteps() {
    return steps;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof JStepDefDetail)) {
      return false;
    }
    final JStepDefDetail other = (JStepDefDetail) o;
    final List<JStepDefMethodDetail> thisStepDefMethodDetails = this.getSteps();
    final List<JStepDefMethodDetail> otherStepDefMethodDetails = other.getSteps();
    return Objects.equals(thisStepDefMethodDetails, otherStepDefMethodDetails);
  }

  @Override
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final List<JStepDefMethodDetail> thisStepDefMethodDetails = this.getSteps();
    result = result * PRIME + (thisStepDefMethodDetails == null ? 43 : thisStepDefMethodDetails.hashCode());
    return result;
  }
}
