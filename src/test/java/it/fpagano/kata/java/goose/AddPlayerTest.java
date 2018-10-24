package it.fpagano.kata.java.goose;

import static org.junit.Assert.assertEquals;

import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.junit.Test;

public class AddPlayerTest {

  @Test
  public void whenAddFirstPlayer_AMessageMustBeGenerated() {
    // given:
    Game g = new Game();
    // when:
    final Task<Set<String>> setTask = g.setPlayer(List.of("Pippo"));

    // then:
    assertEquals("players: Pippo", setTask.getLogHistory().last());
  }

  @Test
  public void whenAddASecondPlayer_AMessageMustBeGenerated() {
    Game g = new Game();
    final Task<Set<String>> setTask = g.setPlayer(List.of("Pippo", "Pluto"));
    assertEquals("players: Pippo, Pluto", setTask.getLogHistory().last());
  }

  @Test
  public void whenAPlayerAlreadyExists_FireMessage() {
    Game g = new Game();
    final Task<Set<String>> setTask = g.setPlayer(List.of("Pippo", "Pluto", "Pippo"));
    assertEquals("Pippo: already existing player", setTask.getLogHistory().last());
  }

}
