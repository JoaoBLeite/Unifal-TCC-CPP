package br.com.unifal.tcc.algorithms.exceptions;

public class UnreachableVertexException extends RuntimeException {

  /**
   * Creates a new exception with the specified detail message.
   *
   * @param message the detail message
   */
  public UnreachableVertexException(String message) {
    super(message);
  }
}
