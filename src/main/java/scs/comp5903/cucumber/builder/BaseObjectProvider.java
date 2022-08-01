package scs.comp5903.cucumber.builder;

/**
 * The class used by this cucumber framework to provide the instance of the step definition class when requested. <br/>
 * It is conceptually similar to IoC container, but for step definition classes only.
 *
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public interface BaseObjectProvider {
  default void initialize() {
  }

  /**
   * Get the instance of the step definition given it's class, superclass or interface. <br/>
   * Implementation must be thread-safe.
   */
  <T> T get(Class<T> clazz);

  default void shutDown() {
  }
}
