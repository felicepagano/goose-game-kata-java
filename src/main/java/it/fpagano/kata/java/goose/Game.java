package it.fpagano.kata.java.goose;

import io.vavr.collection.LinkedHashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import it.fpagano.kata.java.goose.model.cell.Bounces;
import it.fpagano.kata.java.goose.model.cell.Bridge;
import it.fpagano.kata.java.goose.model.cell.Cell;
import it.fpagano.kata.java.goose.model.cell.Goose;
import it.fpagano.kata.java.goose.model.cell.NoActionCell;
import it.fpagano.kata.java.goose.model.cell.Start;
import it.fpagano.kata.java.goose.model.cell.Win;
import it.fpagano.kata.java.goose.model.player.Player;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class Game {

  public static final int NUMBER_OF_CELLS = 63;

  public static final IntFunction<Cell> officialScenario = idx -> {
    if (idx > NUMBER_OF_CELLS) {
      return new Bounces(idx);
    }

    return switch (idx) {
      case 0 -> Start.getInstance();
      case 6 -> new Bridge(idx);
      case NUMBER_OF_CELLS -> Win.getInstance();
      case 5, 9, 14, 18, 23, 27 -> new Goose(idx);
      default -> new NoActionCell(idx);
    };
  };

  public static void main(String[] args) {
    Game g = new Game();

    final Task<Set<String>> addPlayerTask = g.setPlayer(List.of(args));

    final Task<Set<Turn>> task = addPlayerTask.map(playerName ->
        playerName.map(Player::new).map(Turn::new));

    final List<String> logHistory = task.getLogHistory();
    final List<Task<Turn>> turnTask = task.getExecutionLastValue().map(Task::of).toList();

    final List<Task<Turn>> tasks = g.generateTurn(turnTask);

    final List<String> map = tasks.map(Task::getLogHistory)
        .flatMap(List::zipWithIndex).sortBy(stringIntegerTuple2 -> stringIntegerTuple2._2)
        .map(stringIntegerTuple2 -> stringIntegerTuple2._1);

    logHistory.forEach(System.out::println);
    map.forEach(System.out::println);

  }

  Task<Set<String>> setPlayer(List<String> args) {
    return setPlayerRecursive(Task.of(LinkedHashSet.empty(), ""), args);
  }

  private Task<Set<String>> setPlayerRecursive(Task<Set<String>> t, List<String> args) {
    if(args.isEmpty()) {
      return t;
    }
    final String head = args.head();
    return setPlayerRecursive(t.merge(strings -> {
      final Set<String> add = strings.add(head);
      return add != strings ? Task.of(add, "players: " + add.collect(Collectors.joining(", "))) :
          Task.of(add, head + ": already existing player");
    }), args.tail());
  }

  /**
   * Generate recursively a list of turn that players play from a previous state until one player win.
   * @param t
   * @return
   */
  private List<Task<Turn>> generateTurn(List<Task<Turn>> t) {

    if(t.isEmpty())
      return List.empty();

    final Option<Task<Turn>> tasks = t
        .find(turnTask -> turnTask.getExecutionLastValue().isPlayerWinInThisTurn());
    if(tasks.isDefined()) {
      return t;
    }

    return generateTurn(t.map(turnTask -> turnTask.merge(turn -> turn.playTurn(officialScenario))));

  }

}
