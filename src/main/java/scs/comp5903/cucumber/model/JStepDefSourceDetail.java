package scs.comp5903.cucumber.model;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A class simply storing the input classes/objects that users want the step definition come from.
 * Currently, not being used but who knows if I need it in the future or not.
 *
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public final class JStepDefSourceDetail {

  private final List<Class<?>> classes;
  private final List<Object> instances;
  private final List<Supplier<?>> creations;

  public JStepDefSourceDetail(List<Class<?>> classes, List<Object> instances, List<Supplier<?>> creations) {
    this.classes = classes;
    this.instances = instances;
    this.creations = creations;
  }

  public List<Class<?>> getClasses() {
    return this.classes;
  }

  public List<Object> getInstances() {
    return this.instances;
  }

  public List<Supplier<?>> getCreations() {
    return this.creations;
  }


  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof JStepDefSourceDetail)) {
      return false;
    }
    final JStepDefSourceDetail other = (JStepDefSourceDetail) o;
    final Object thisSourceClasses = this.getClasses();
    final Object otherSourceClasses = other.getClasses();
    if (!Objects.equals(thisSourceClasses, otherSourceClasses)) {
      return false;
    }
    final Object thisSourceInstances = this.getInstances();
    final Object otherSourceInstances = other.getInstances();
    return Objects.equals(thisSourceInstances, otherSourceInstances);
  }

  @Override
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object thisSourceClasses = this.getClasses();
    result = result * PRIME + (thisSourceClasses == null ? 43 : thisSourceClasses.hashCode());
    final Object thisSourceInstances = this.getInstances();
    result = result * PRIME + (thisSourceInstances == null ? 43 : thisSourceInstances.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "JStepDefSourceDetail(classes=" + this.getClasses() + ", instances=" + this.getInstances() + ")";
  }
}
