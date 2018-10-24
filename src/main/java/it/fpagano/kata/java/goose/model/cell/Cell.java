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

  /**
   * A function that represents the rule to apply where a player reach this cell.
   * By default it return the same cell instance.
   * @param scenario
   * @return
   */
  default Function<Integer, Cell> rule(Function<Integer, Cell> scenario) {
    return integer -> this;
  }

  /**
   * Apply the {@link Cell#rule(Function)} using the launched dice value as parameter.
   * The scenario is used to know witch cell the user switch.
   * @param diceSum
   * @param scenario
   * @return
   */
  default Cell followRule(int diceSum,
      Function<Integer, Cell> scenario) {
    return this.rule(scenario).apply((diceSum));
  }

}
