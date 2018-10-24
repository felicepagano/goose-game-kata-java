package it.fpagano.kata.java.goose.model.cell;

public class NoActionCell implements Cell {

  private final int position;

  public NoActionCell(int position) {
    this.position = position;
  }

  @Override
  public int position() {
    return position;
  }

  @Override
  public String toString() {
    return description();
  }
}
