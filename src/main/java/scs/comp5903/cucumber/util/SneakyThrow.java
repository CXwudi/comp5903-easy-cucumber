package scs.comp5903.cucumber.util;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-05
 */
public class SneakyThrow {
  private SneakyThrow() {
  }

  public static <E extends Exception> void theException(Exception e) throws E {
    throw (E) e;
  }
}
