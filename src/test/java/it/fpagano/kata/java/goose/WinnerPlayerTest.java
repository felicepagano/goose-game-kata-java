package it.fpagano.kata.java.goose;

import io.vavr.collection.List;
import it.fpagano.kata.java.goose.model.cell.NoActionCell;
import it.fpagano.kata.java.goose.model.dice.Dice;
import it.fpagano.kata.java.goose.model.player.Player;
import it.fpagano.kata.java.goose.model.player.WinningPlayer;
import org.junit.Assert;
import org.junit.Test;

public class WinnerPlayerTest {

  @Test
  public void whenPlayerReachCell63_thenPlayerWin() {
    List<Dice> diceToWin = List.of(new Dice(() -> 1), new Dice(() -> 2));
    Player p = new Player("Pippo", diceToWin, new NoActionCell(60));
    final Turn turn = new Turn(p);
    final Task<Turn> combine = turn.playTurn(Game.scenario);
    Assert.assertTrue(combine.getExecutionLastValue().getPlayer() instanceof WinningPlayer);
    Assert.assertTrue(combine.getExecutionLastValue().isPlayerWinInThisTurn());
    Assert.assertEquals("Pippo rolls 1, 2. Pippo moves from 60 to 63. Pippo Wins!!",  combine.getLogHistory().last());
  }

  @Test
  public void whenPlayerPassOver63_thenPlayerGoBackOfTheLaunchedDiceValue() {
    List<Dice> diceToBounce = List.of(new Dice(() -> 3), new Dice(() -> 2));
    Player p = new Player("Pippo", diceToBounce, new NoActionCell(60));
    Turn t = new Turn(p);
    final Task<Turn> combine = t.playTurn(Game.scenario);
    Assert.assertEquals("Pippo rolls 3, 2. Pippo moves from 60 to 63. Pippo bounces! Pippo returns to 61", combine.getLogHistory().last());
  }

}
