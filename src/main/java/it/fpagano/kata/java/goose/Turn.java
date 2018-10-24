package it.fpagano.kata.java.goose;

import io.vavr.collection.List;
import it.fpagano.kata.java.goose.model.cell.Bounces;
import it.fpagano.kata.java.goose.model.cell.Cell;
import it.fpagano.kata.java.goose.model.cell.Goose;
import it.fpagano.kata.java.goose.model.cell.Win;
import it.fpagano.kata.java.goose.model.player.Player;
import it.fpagano.kata.java.goose.model.player.WinningPlayer;
import java.time.Instant;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Turn class represents an instant when the player has played in it's turn.action
 */
class Turn {

  private final Player player;
  private final Instant ts;

  Turn(Player player) {
    this.player = player;
    this.ts = Instant.now();
  }

  private Task<List<Integer>> rollDice() {
    final List<Integer> integers = player.launchDice();
    return Task.of(integers, player.name + " rolls " + integers.map(String::valueOf).collect(
        Collectors.joining(", ")));
  }

  private Task<Cell> move(List<Integer> diceSum, Function<Integer, Cell> scenario) {
    final Cell move = player.move(diceSum.sum().intValue(), scenario);
    return Task.of(move, player.name + " moves from " + player.position + " to " + move);
  }

  private Task<Cell> followRule(Cell cell, int sumDice, Function<Integer, Cell> scenario) {
    final Cell followedCellRule = cell.followRule(sumDice, scenario);
    if(followedCellRule == cell) {
      if(cell instanceof Win) {
        return Task.of(followedCellRule, player.name + " Wins!!");
      }
      return Task.of(followedCellRule);
    } else {
      if(cell instanceof Goose) {
        return Task.of(followedCellRule, player.name + " moves again and goes to " + followedCellRule).merge(cell1 -> followRule(cell1, sumDice, scenario));
      } else if(cell instanceof Bounces) {
        return Task.of(followedCellRule, player.name + " bounces! " + player.name + " returns to " + followedCellRule).merge(cell1 -> followRule(cell1, sumDice, scenario));
      }
      return Task.of(followedCellRule, player.name + " jumps to " + followedCellRule).merge(cell1 -> followRule(cell1, sumDice, scenario));
    }
  }

  /**
   * Generate a Turn from the current status.
   * This method combine the player's action in order to:
   * <ul>
   *   <li>launch a list of dice</li>
   *   <li>move the player on a cell</li>
   *   <li>follow the cell rule recursively</li>
   * </ul>
   * @param scenario is used to generate the cells.
   * @return
   */
  Task<Turn> playTurn(Function<Integer, Cell> scenario) {
    int rolledDiceSum = this.rollDice().getExecutionLastValue().sum().intValue();
    return this.rollDice().merge((List<Integer> diceSum) -> move(diceSum, scenario)).merge(cell -> this.followRule(cell, rolledDiceSum, scenario)).to(cell -> {
      if(cell instanceof Win) {
        return new Turn(new WinningPlayer(player.name));
      }
      return new Turn(new Player(player.name, cell));
    });
  }

  boolean isPlayerWinInThisTurn() {
    return player instanceof WinningPlayer;
  }

  Player getPlayer() {
    return player;
  }

  /**
   * If in the same turn more than one player win, ts could be used to identify the first winner.
   * @return
   */
  public Instant getTs() {
    return ts;
  }
}
