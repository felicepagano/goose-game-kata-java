package it.fpagano.kata.java.goose;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents an execution that return a getExecutionLastValue T and can have a appendLog.
 * To carry the appendLog you must call the appendLog
 * @param <T>
 */
public class Task<T> {

  /**
   * Task use an instance of tuple2 to store the value and the collected logs.
   */
  private final Tuple2<List<String>, T> logsHistoryLastExecution;

  private Task(Tuple2<List<String>, T> logsHistoryLastExecution) {
    this.logsHistoryLastExecution = logsHistoryLastExecution;
  }

  public static <T> Task<T> of (T value) {
    return new Task<>(Tuple.of(List.empty(), value));
  }

  public static <T> Task<T> of (T s, String log) {
    return new Task<>(Tuple.of(List.of(log), s));
  }

  public T getExecutionLastValue() {
    return logsHistoryLastExecution._2;
  }

  public List<String> getLogHistory() {
    return logsHistoryLastExecution._1;
  }

  /**
   * Append all parameter logs to the instance logs and preserve only the parameter execution.
   * Like flatMap function.
   * @param t
   * @return
   */
  public <P> Task<P> merge(Function<T, Task<P>> t) {
    final Task<P> apply = t.apply(this.getExecutionLastValue());
    return new Task<>(Tuple.of(this.getLogHistory().appendAll(apply.getLogHistory()), apply.getExecutionLastValue()));
  }

  /**
   * Transform a task to another one.
   * Like map function.
   * @param f
   * @param <P>
   * @return
   */
  public <P> Task<P> to(Function<T, P> f) {
    return Task.of(f.apply(this.getExecutionLastValue()), getLogHistory()
        //.append("\n")
        .collect(Collectors.joining(". "))
    );
  }

  @Override
  public String toString() {
    return "Task{" +
        "logsHistoryLastExecution=" + logsHistoryLastExecution +
        '}';
  }
}
