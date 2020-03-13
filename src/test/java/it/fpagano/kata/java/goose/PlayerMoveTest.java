package it.fpagano.kata.java.goose;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import java.util.function.IntFunction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PlayerMoveTest {

  public static final String PIPPO = "Pippo";
  public static final String PLUTO = "Pluto";
  private static IntFunction<Cell> scenario;
  private static final int NUMBER_OF_CELLS = 63;

  @BeforeAll
  public static void setUp() {
    generateScenario();
  }

  private static void generateScenario() {
    scenario = idx -> {
      if (idx > NUMBER_OF_CELLS) {
        return new Bounces(NUMBER_OF_CELLS);
      }
      return switch (idx) {
        case 0 -> Start.getInstance();
        case 6 -> new Bridge(idx);
        case NUMBER_OF_CELLS -> Win.getInstance();
        case 5, 9, 14, 18, 23, 27 -> new Goose(idx);
        default -> new NoActionCell(idx);
      };
    };
  }

  @Test
  public void whenPlayerMove_thenPlayerMadeProgress() {
    Dice dice1 = new Dice(() -> 1);
    Dice dice3 = new Dice(() -> 3);

    final Player pippoAtStart = new Player(PIPPO, List.of(dice3, dice1), Start.getInstance());
    final Player plutoAtStart = new Player(PLUTO, List.of(dice1, dice1), Start.getInstance());

    final Task<Turn> pippoPlay = new Turn(pippoAtStart).playTurn(scenario);
    final Task<Turn> plutoPLay = new Turn(plutoAtStart).playTurn(scenario);

    assertEquals("Pippo rolls 3, 1. Pippo moves from Start to 4",
        pippoPlay.getLogHistory().last());
    assertEquals("Pluto rolls 1, 1. Pluto moves from Start to 2",
        plutoPLay.getLogHistory().last());

    final Player pippoAt4 = new Player(PIPPO, List.of(dice1, dice3), new NoActionCell(4));
    final Task<Turn> pippoPlayAgain = new Turn(pippoAt4).playTurn(scenario);
    assertEquals("Pippo rolls 1, 3. Pippo moves from 4 to 8",
        pippoPlayAgain.getLogHistory().last());

  }

  @Test
  public void whenPlayerJumpIntoTheBridge_thenPlayerJumpAgainTo12() {
    Dice dice1 = new Dice(() -> 1);
    Dice dice2 = new Dice(() -> 1);
    final List<Dice> dice11 = List.of(dice1, dice2);
    final Player pippoAt4Position = new Player(PIPPO, dice11, new NoActionCell(4));
    final Task<Turn> combine = new Turn(pippoAt4Position).playTurn(scenario);
    assertEquals("Pippo rolls 1, 1. Pippo moves from 4 to The Bridge. Pippo jumps to 12",
        combine.getLogHistory().last());
  }

  @Test
  public void whenPlayerJumpIntoTheGoose_thenPlayerJumpAgainOfThePreviewsDiceRoll() {
    Dice dice1 = new Dice(() -> 1);
    Dice dice2 = new Dice(() -> 1);
    final List<Dice> dice11 = List.of(dice1, dice2);
    final Player pippo = new Player(PIPPO, dice11, new NoActionCell(3));
    final Task<Turn> combine = new Turn(pippo).playTurn(scenario);
    assertEquals(
        "Pippo rolls 1, 1. Pippo moves from 3 to 5, The Goose. Pippo moves again and goes to 7",
        combine.getLogHistory().last());
  }

  @Test
  public void whenPlayerJumpIntoTheGooseAgainTime_thenPlayerJumpAgain() {
    Dice dice2 = new Dice(() -> 2);
    final List<Dice> dice22 = List.of(dice2, dice2);
    final Player pippo = new Player(PIPPO, dice22, new NoActionCell(10));
    final Task<Turn> combine = new Turn(pippo).playTurn(scenario);
    assertEquals(
        "Pippo rolls 2, 2. Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22",
        combine.getLogHistory().last());
  }
}
