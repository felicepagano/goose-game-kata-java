package it.fpagano.kata.java.goose.model.cell;

public record NoActionCell(int position) implements Cell {

  @Override
  public String toString() {
    return description();
  }
}
