package scs.comp5903.cucumber.util;

import java.util.function.Consumer;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-05
 */
public interface ThrowingConsumer<T> extends Consumer<T> {
  @Override
  default void accept(T t) {
    try {
      acceptThrows(t);
    } catch (Exception e) {
      SneakyThrow.theException(e);
    }
  }

  void acceptThrows(T t) throws Exception;

}
