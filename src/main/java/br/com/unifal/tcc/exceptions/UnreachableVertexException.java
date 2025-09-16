package br.com.unifal.tcc.exceptions;

public class UnreachableVertexException extends RuntimeException {

  /**
   * Creates a new exception with the specified detail message.
   *
   * @param message the detail message
   */
  public UnreachableVertexException(String message) {
    super(message);
  }

  /**
   * Creates a new exception with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception (can be {@code null})
   */
  public UnreachableVertexException(String message, Throwable cause) {
    super(message, cause);
  }
}
