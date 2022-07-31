package scs.comp5903.cucumber.model.exception;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-18
 */
public class EasyCucumberException extends RuntimeException {

  public EasyCucumberException(ErrorCode errorCode) {
    super(buildMessage(errorCode, "Unknown message"));
  }

  /**
   * Constructs a new runtime exception with the specified detail message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for
   *                later retrieval by the {@link #getMessage()} method.
   */
  public EasyCucumberException(ErrorCode errorCode, String message) {
    super(buildMessage(errorCode, message));
  }

  /**
   * Constructs a new runtime exception with the specified detail message and
   * cause.  <p>Note that the detail message associated with
   * {@code cause} is <i>not</i> automatically incorporated in
   * this runtime exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval
   *                by the {@link #getMessage()} method).
   * @param cause   the cause (which is saved for later retrieval by the
   *                {@link #getCause()} method).  (A {@code null} value is
   *                permitted, and indicates that the cause is nonexistent or
   *                unknown.)
   * @since 1.4
   */
  public EasyCucumberException(ErrorCode errorCode, String message, Throwable cause) {
    super(buildMessage(errorCode, message), cause);
  }

  /**
   * Constructs a new runtime exception with the specified cause and a
   * detail message of {@code (cause==null ? null : cause.toString())}
   * (which typically contains the class and detail message of
   * {@code cause}).  This constructor is useful for runtime exceptions
   * that are little more than wrappers for other throwables.
   *
   * @param cause the cause (which is saved for later retrieval by the
   *              {@link #getCause()} method).  (A {@code null} value is
   *              permitted, and indicates that the cause is nonexistent or
   *              unknown.)
   * @since 1.4
   */
  public EasyCucumberException(ErrorCode errorCode, Throwable cause) {
    super(buildMessage(errorCode, "Unknown message"), cause);
  }

  /**
   * Constructs a new runtime exception with the specified detail
   * message, cause, suppression enabled or disabled, and writable
   * stack trace enabled or disabled.
   *
   * @param message            the detail message.
   * @param cause              the cause.  (A {@code null} value is permitted,
   *                           and indicates that the cause is nonexistent or unknown.)
   * @param enableSuppression  whether or not suppression is enabled
   *                           or disabled
   * @param writableStackTrace whether or not the stack trace should
   *                           be writable
   * @since 1.7
   */
  public EasyCucumberException(ErrorCode errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(buildMessage(errorCode, message), cause, enableSuppression, writableStackTrace);
  }

  private static String buildMessage(ErrorCode errorCode, String message) {
    return String.format("%s: %s", errorCode, message);
  }
}
