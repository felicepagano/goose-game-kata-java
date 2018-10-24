package it.fpagano.kata.java.goose.model.dice;

import java.util.function.IntSupplier;

public class Dice {

  private final IntSupplier numberGeneratorFunction;

  public Dice(IntSupplier numberGeneratorFunction) {
    this.numberGeneratorFunction = numberGeneratorFunction;
  }

  public int generate() {
    return numberGeneratorFunction.getAsInt();
  }
}
