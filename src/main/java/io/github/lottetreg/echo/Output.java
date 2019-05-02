package io.github.lottetreg.echo;

import java.io.PrintStream;

public class Output {
  private PrintStream out;

  Output(PrintStream out) {
    this.out = out;
  }

  public void println(String output) {
    this.out.println(output);
  }
}
