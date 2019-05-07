package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;


public class WriterTest {
  @Test
  public void testSetConnection() {
    Connection connection = new Connection.Builder().build();
    Writer writer = new Writer();
    writer.setConnection(connection);

    assertEquals(writer.connection, connection);
  }

  @Test
  public void itPrintsOutFromTheConnection() {
    OutputStream outputStream = new ByteArrayOutputStream();
    MockConnection connection = new MockConnection.Builder()
            .setOutputStream(outputStream)
            .build();
    Writer writer = new Writer();
    writer.setConnection(connection);

    writer.println("Some other string");

    assertEquals("Some other string\n", outputStream.toString());
  }
}
