package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OutputTest {

  @Test
  public void testPrintsOutput() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    Output output = new Output(out);

    output.println("Hello world!");

    assertEquals("Hello world!\n", bytes.toString());
  }
}
