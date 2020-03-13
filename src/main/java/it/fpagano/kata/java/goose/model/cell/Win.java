package it.fpagano.kata.java.goose.model.cell;

import static it.fpagano.kata.java.goose.Game.NUMBER_OF_CELLS;

import java.util.function.IntFunction;

public class Win implements Cell {

  private static final Win WIN_INSTANCE = new Win();

  public static Win getInstance() {
    return WIN_INSTANCE;
  }

  private Win() {
  }

  @Override
  public int position() {
    return NUMBER_OF_CELLS;
  }

  @Override
  public IntFunction<Cell> rule(IntFunction<Cell> scenario) {
    return integer -> this;
  }

  @Override
  public String toString() {
    return description();
  }
}
