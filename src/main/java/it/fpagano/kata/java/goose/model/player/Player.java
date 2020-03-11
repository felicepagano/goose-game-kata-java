package it.fpagano.kata.java.goose.model.player;

import io.vavr.collection.List;
import it.fpagano.kata.java.goose.model.cell.Cell;
import it.fpagano.kata.java.goose.model.cell.Start;
import it.fpagano.kata.java.goose.model.dice.Dice;
import it.fpagano.kata.java.goose.model.dice.PseudoRandomDice;
import java.util.Objects;
import java.util.function.IntFunction;

public class Player {

  public final String name;
  private final List<Dice> dice;
  public final Cell position;

  public Player(String name) {
    this.name = name;
    this.dice = List.of(PseudoRandomDice.getInstance(), PseudoRandomDice.getInstance());
    this.position = Start.getInstance();
  }

  public Player(String name, Cell cell) {
    this.name = name;
    this.dice = List.of(PseudoRandomDice.getInstance(), PseudoRandomDice.getInstance());
    this.position = cell;
  }

  public Player(String name, List<Dice> dice, Cell position) {
    this.name = name;
    this.dice = dice;
    this.position = position;
  }

  public List<Integer> launchDice() {
    return dice.map(Dice::generate);
  }

  public Cell move(int diceSum, IntFunction<Cell> scenario) {
    return scenario.apply(diceSum + position());
  }

  private int position() {
    return this.position.position();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return name;
  }
}
