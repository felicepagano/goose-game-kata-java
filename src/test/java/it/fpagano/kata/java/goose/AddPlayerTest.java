package it.fpagano.kata.java.goose;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.junit.jupiter.api.Test;

public class AddPlayerTest {

  public static final String PIPPO = "Pippo";
  public static final String PLUTO = "Pluto";

  @Test
  public void whenAddFirstPlayer_AMessageMustBeGenerated() {
    // given:
    Game g = new Game();
    // when:
    final Task<Set<String>> setTask = g.setPlayer(List.of(PIPPO));

    // then:
    assertEquals("players: Pippo", setTask.getLogHistory().last());
  }

  @Test
  public void whenAddASecondPlayer_AMessageMustBeGenerated() {
    Game g = new Game();
    final Task<Set<String>> setTask = g.setPlayer(List.of(PIPPO, PLUTO));
    assertEquals("players: Pippo, Pluto", setTask.getLogHistory().last());
  }

  @Test
  public void whenAPlayerAlreadyExists_FireMessage() {
    Game g = new Game();
    final Task<Set<String>> setTask = g.setPlayer(List.of(PIPPO, PLUTO, PIPPO));
    assertEquals("Pippo: already existing player", setTask.getLogHistory().last());
  }

}
