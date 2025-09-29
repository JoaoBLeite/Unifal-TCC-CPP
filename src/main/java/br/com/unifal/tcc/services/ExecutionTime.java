package br.com.unifal.tcc.services;

import br.com.unifal.tcc.services.dto.TimedResult;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class ExecutionTime {

  private ExecutionTime() {}

  /**
   * Measures the execution time of a {@link Runnable} method.
   *
   * <p>This method executes the provided {@code Runnable} and calculates the time elapsed between
   * the start and end of execution.
   *
   * @param method the {@link Runnable} to be executed and measured
   * @return a {@link Duration} representing the time taken to execute the method
   * @throws NullPointerException if {@code method} is null
   */
  public static Duration measureTime(Runnable method) {
    Instant startInstant = Instant.now();
    method.run();
    Instant endInstant = Instant.now();
    return Duration.between(startInstant, endInstant);
  }

  /**
   * Measures the execution time of a {@link Supplier} method and returns both the result and the
   * execution duration.
   *
   * <p>This method executes the provided {@code Supplier}, captures its return value, and
   * calculates the time elapsed during execution. Both the result and duration are encapsulated in
   * a {@link TimedResult} object.
   *
   * @param <T> the type of the result returned by the supplier
   * @param method the {@link Supplier} to be executed and measured
   * @return a {@link TimedResult} containing the result of the supplier and the execution duration
   * @throws NullPointerException if {@code method} is null
   */
  public static <T> TimedResult<T> measureTimeWithResult(Supplier<T> method) {
    Instant startInstant = Instant.now();
    T result = method.get();
    Instant endInstant = Instant.now();
    return new TimedResult<>(result, Duration.between(startInstant, endInstant));
  }
}
