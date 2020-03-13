package it.fpagano.kata.java.goose.model.cell;

import static it.fpagano.kata.java.goose.Game.NUMBER_OF_CELLS;

import java.util.function.IntFunction;

/*
Beyond the restrictions above, records behave like normal classes: they can be declared top level or
nested, they can be generic, they can implement interfaces, and they are instantiated via the new
keyword
 */
public record Bounces(int position) implements Cell {

  /*
  https://openjdk.java.net/jeps/359
  Record cannot declare instance fields other than the private final fields which correspond to
  components of the state description. Any other fields which are declared must be static.
  These restrictions ensure that the state description alone defines the representation.
   */
  private static final String LAST_POSITION = String.valueOf(NUMBER_OF_CELLS);

  @Override
  public IntFunction<Cell> rule(IntFunction<Cell> scenario) {
    return diceValue -> scenario.apply(NUMBER_OF_CELLS - (position - NUMBER_OF_CELLS));
  }

  @Override
  public String toString() {
    return LAST_POSITION;
  }

}
