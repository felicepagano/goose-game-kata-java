package it.fpagano.kata.java.goose.model.dice;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;

public class PseudoRandomDice extends Dice {

  private static final IntSupplier PSEUDO_RANDOM = () -> ThreadLocalRandom
      .current().nextInt(1, 7);

  private static final PseudoRandomDice ourInstance = new PseudoRandomDice();

  public static PseudoRandomDice getInstance() {
    return ourInstance;
  }

  private PseudoRandomDice() {
    super(PSEUDO_RANDOM);
  }
}
