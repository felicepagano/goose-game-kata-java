package it.fpagano.kata.java.goose.model.cell;

import java.util.function.Function;
import java.util.function.IntFunction;

public record Goose(int position) implements Cell {

  @Override
  public String description() {
    return position + ", The Goose";
  }

  @Override
  public String toString() {
    return description();
  }

  @Override
  public Function<Integer, Cell> rule(IntFunction<Cell> scenario) {
    return diceSum -> scenario.apply(this.position + diceSum);
  }
}
