package it.fpagano.kata.java.goose.model.cell;

import java.util.function.Function;
import java.util.function.IntFunction;

public class Bridge implements Cell {

  private final int position;

  public Bridge(int position) {
    this.position = position;
  }

  @Override
  public Function<Integer, Cell> rule(IntFunction<Cell> scenario) {
    return i -> scenario.apply(12);
  }

  @Override
  public int position() {
    return position;
  }

  @Override
  public String description() {
    return "The Bridge";
  }

  @Override
  public String toString() {
    return description();
  }
}
