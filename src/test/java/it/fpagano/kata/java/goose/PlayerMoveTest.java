package it.fpagano.kata.java.goose;

import io.vavr.collection.List;
import it.fpagano.kata.java.goose.model.cell.Bounces;
import it.fpagano.kata.java.goose.model.cell.Bridge;
import it.fpagano.kata.java.goose.model.cell.Cell;
import it.fpagano.kata.java.goose.model.cell.Goose;
import it.fpagano.kata.java.goose.model.cell.NoActionCell;
import it.fpagano.kata.java.goose.model.cell.Start;
import it.fpagano.kata.java.goose.model.cell.Win;
import it.fpagano.kata.java.goose.model.dice.Dice;
import it.fpagano.kata.java.goose.model.player.Player;
import java.util.function.Function;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerMoveTest {

  private Function<Integer, Cell> scenario;
  private static final int NUMBER_OF_CELLS = 63;

  @Before
  public void setUp() {
    generateScenario();
  }

  private void generateScenario() {
    scenario = idx -> {
      if(idx > NUMBER_OF_CELLS) {
        return new Bounces(NUMBER_OF_CELLS);
      }
      switch (idx) {
        case 0: return Start.getInstance();
        case 6: return new Bridge(idx);
        case NUMBER_OF_CELLS: return Win.getInstance();
        case 5:
        case 9:
        case 14:
        case 18:
        case 23:
        case 27: return new Goose(idx);
        default: return new NoActionCell(idx);
      }
    };
  }

  @Test
  public void whenPlayerMove_thenPlayerMadeProgress() {
    Dice dice1 = new Dice(() -> 1);
    Dice dice3 = new Dice(() -> 3);

    final Player pippoAtStart = new Player("Pippo", List.of(dice3, dice1), Start.getInstance());
    final Player plutoAtStart = new Player("Pluto", List.of(dice1, dice1), Start.getInstance());

    final Task<Turn> pippoPlay = new Turn(pippoAtStart).combine(scenario);
    final Task<Turn> plutoPLay = new Turn(plutoAtStart).combine(scenario);

    Assert.assertEquals("Pippo rolls 3, 1. Pippo moves from Start to 4", pippoPlay.getLogHistory().last());
    Assert.assertEquals("Pluto rolls 1, 1. Pluto moves from Start to 2", plutoPLay.getLogHistory().last());

    final Player pippoAt4 = new Player("Pippo", List.of(dice1, dice3), new NoActionCell(4));
    final Task<Turn> pippoPlayAgain = new Turn(pippoAt4).combine(scenario);
    Assert.assertEquals("Pippo rolls 1, 3. Pippo moves from 4 to 8", pippoPlayAgain.getLogHistory().last());

  }

  @Test
  public void whenPlayerJumpIntoTheBridge_thenPlayerJumpAgainTo12() {
    Dice dice1 = new Dice(() -> 1);
    Dice dice2 = new Dice(() -> 1);
    final List<Dice> dice11 = List.of(dice1, dice2);
    final Player pippoAt4Position = new Player("Pippo", dice11, new NoActionCell(4));
    final Task<Turn> combine = new Turn(pippoAt4Position).combine(scenario);
    Assert.assertEquals("Pippo rolls 1, 1. Pippo moves from 4 to The Bridge. Pippo jumps to 12", combine.getLogHistory().last());
  }

  @Test
  public void whenPlayerJumpIntoTheGoose_thenPlayerJumpAgainOfThePreviewsDiceRoll() {
    Dice dice1 = new Dice(() -> 1);
    Dice dice2 = new Dice(() -> 1);
    final List<Dice> dice11 = List.of(dice1, dice2);
    final Player pippo = new Player("Pippo", dice11, new NoActionCell(3));
    final Task<Turn> combine = new Turn(pippo).combine(scenario);
    Assert.assertEquals("Pippo rolls 1, 1. Pippo moves from 3 to 5, The Goose. Pippo moves again and goes to 7", combine.getLogHistory().last());
  }

  @Test
  public void whenPlayerJumpIntoTheGooseAgainTime_thenPlayerJumpAgain() {
    Dice dice2 = new Dice(() -> 2);
    final List<Dice> dice22 = List.of(dice2, dice2);
    final Player pippo = new Player("Pippo", dice22, new NoActionCell(10));
    final Task<Turn> combine = new Turn(pippo).combine(scenario);
    Assert.assertEquals("Pippo rolls 2, 2. Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22",
        combine.getLogHistory().last());
  }
}
