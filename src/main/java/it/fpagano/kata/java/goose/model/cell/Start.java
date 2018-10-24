package it.fpagano.kata.java.goose.model.cell;

public class Start implements Cell {

  private static final Start START = new Start();

  public static Start getInstance() {
    return START;
  }

  private Start() {
  }

  @Override
  public int position() {
    return 0;
  }

  @Override
  public String description() {
    return "Start";
  }

  @Override
  public String toString() {
    return description();
  }
}
