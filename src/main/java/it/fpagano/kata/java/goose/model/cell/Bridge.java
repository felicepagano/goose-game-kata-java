package it.fpagano.kata.java.goose.model.cell;

import java.util.function.IntFunction;

public record Bridge(int position) implements Cell {

  @Override
  public IntFunction<Cell> rule(IntFunction<Cell> scenario) {
    return i -> scenario.apply(12);
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
