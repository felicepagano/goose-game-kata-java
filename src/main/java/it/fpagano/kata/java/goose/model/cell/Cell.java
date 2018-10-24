package it.fpagano.kata.java.goose.model.cell;

import java.util.function.Function;

public interface Cell {

  /**
   * The rule to follow when you reach this cell.
   * It return a new cell where the player should move on.
   * @return
   */
  int position();

  default String description() {
    return String.valueOf(position());
  }

  default Function<Integer, Cell> rule(Function<Integer, Cell> scenario) {
    return integer -> this;
  }

  default Cell followRule(int diceSum,
      Function<Integer, Cell> scenario) {
    return this.rule(scenario).apply((diceSum));
  }

}
