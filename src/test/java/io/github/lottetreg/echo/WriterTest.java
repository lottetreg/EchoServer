package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;



public class WriterTest {
  @Test
  public void testSetConnection() {
    Connection connection = new Connection.Builder().build();
    Writer writer  = new Writer.Builder()
            .setConnection(connection)
            .build();

    assertEquals(writer.connection, connection);
  }

  @Test
  public void itPrintsOutFromTheConnection() {
    MockConnection connection = new MockConnection.Builder().build();
    Writer writer = new Writer.Builder()
            .setConnection(connection)
            .build();

    writer.println("Some other string");

    assertEquals("Some other string\n", connection.getOutputStream().toString());
  }
}
