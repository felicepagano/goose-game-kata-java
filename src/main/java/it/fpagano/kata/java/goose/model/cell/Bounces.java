package it.fpagano.kata.java.goose.model.cell;

import static it.fpagano.kata.java.goose.Game.NUMBER_OF_CELLS;

import java.util.function.Function;
import java.util.function.IntFunction;

public class Bounces implements Cell {

  private final int position;
  private static final String LAST_POSITION = String.valueOf(NUMBER_OF_CELLS);

  public Bounces(int position) {
    this.position = position;
  }

  @Override
  public int position() {
    return position;
  }

  @Override
  public Function<Integer, Cell> rule(IntFunction<Cell> scenario) {
    return diceValue -> scenario.apply(NUMBER_OF_CELLS - (position - NUMBER_OF_CELLS));
  }

  @Override
  public String toString() {
    return LAST_POSITION;
  }
}
