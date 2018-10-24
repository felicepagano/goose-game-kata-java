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
import java.util.function.Function;
import java.util.stream.Collectors;

public class Game {

  public static final int NUMBER_OF_CELLS = 63;
  public static final Function<Integer, Cell> scenario = idx -> {
    if(idx > NUMBER_OF_CELLS) {
      return new Bounces(idx);
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

  public static void main(String[] args) {
    Game g = new Game();

    final Task<Set<String>> addPlayerTask = g
        .setPlayer(List.of(args));

    final Task<Set<Turn>> to = addPlayerTask.to(strings -> strings.map(Player::new).map(Turn::new));

    final List<String> logHistory = to.getLogHistory();
    final List<Task<Turn>> turnTask = to.getExecutionLastValue().map(Task::of).toList();

    final List<Task<Turn>> tasks = g.generateTurn(turnTask);

    final List<String> map = tasks.map(Task::getLogHistory)
        .flatMap(List::zipWithIndex).sortBy(stringIntegerTuple2 -> stringIntegerTuple2._2)
        .map(stringIntegerTuple2 -> stringIntegerTuple2._1);

    logHistory.forEach(System.out::println);
    map.forEach(System.out::println);

  }

  public Task<Set<String>> setPlayer(List<String> args) {
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

  private List<Task<Turn>> generateTurn(List<Task<Turn>> t) {

    if(t.isEmpty())
      return List.empty();

    final Option<Task<Turn>> tasks = t
        .find(turnTask -> turnTask.getExecutionLastValue().isPlayerWinInThisTurn());
    if(tasks.isDefined()) {
      return t;
    }

    return generateTurn(t.map(turnTask -> turnTask.merge(turn -> turn.combine(scenario))));

  }

}
