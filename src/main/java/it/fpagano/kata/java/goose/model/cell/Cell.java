package it.fpagano.kata.java.goose.model.cell;

import java.util.function.IntFunction;

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

  /**
   * A function that represents the rule to apply where a player reach this cell. By default it
   * return the same cell instance. Identity.
   *
   * @param scenario
   * @return
   */
  default IntFunction<Cell> rule(IntFunction<Cell> scenario) {
    return integer -> this;
  }

  /**
   * Apply the {@link Cell#rule(IntFunction)} using the launched dice value as parameter. The
   * scenario is used to know witch cell the user switch.
   *
   * @param diceSum
   * @param scenario
   * @return
   */
  default Cell followRule(int diceSum, IntFunction<Cell> scenario) {
    return this.rule(scenario).apply((diceSum));
  }

}
