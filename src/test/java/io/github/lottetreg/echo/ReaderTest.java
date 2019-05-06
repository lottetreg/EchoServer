package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

public class ReaderTest {
  @Test
  public void itHasADefaultConnection() {
    Reader reader = new Reader.Builder().build();

    assertThat(reader.connection, instanceOf(Connection.class));
  }

  @Test
  public void theConnectionCanBeSetThroughTheBuilder() {
    Connection connection = new Connection.Builder().build();

    Reader reader = new Reader.Builder()
            .setConnection(connection)
            .build();

    assertEquals(reader.connection, connection);
  }

  @Test
  public void testSetConnection() {
    Connection connection = new Connection.Builder().build();
    Reader reader = new Reader.Builder()
            .setConnection(connection)
            .build();

    assertEquals(reader.connection, connection);
  }

  @Test
  public void testReadLine() {
    Connection connection = new MockConnection.Builder().build();
    Reader reader = new Reader.Builder()
            .setConnection(connection)
            .build();

    assertEquals(reader.readLine(), "Some string");
  }
}
