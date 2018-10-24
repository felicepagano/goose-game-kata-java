package it.fpagano.kata.java.goose.model.cell;

import java.util.function.Function;

public class Goose implements Cell {

  private final int position;

  public Goose(int position) {
    this.position = position;
  }

  @Override
  public int position() {
    return position;
  }

  @Override
  public String description() {
    return position + ", The Goose";
  }

  @Override
  public String toString() {
    return description();
  }

  @Override
  public Function<Integer, Cell> rule(Function<Integer, Cell> scenario) {
    return diceSum -> scenario.apply(this.position + diceSum);
  }
}