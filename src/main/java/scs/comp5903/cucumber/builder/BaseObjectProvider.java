package scs.comp5903.cucumber.builder;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public interface BaseObjectProvider {
  default void initialize() {
  }

  /**
   * get the instance of this class or its subclasses
   */
  <T> T get(Class<T> clazz);

  default void shutDown() {
  }
}
