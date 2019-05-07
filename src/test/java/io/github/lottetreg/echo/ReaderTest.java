package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class MockConnection extends Connection {
  MockConnection(Builder builder) {
    super(builder);
  }

  public InputStream getInputStream() {
    byte[] byteArray = "Some string".getBytes();
    return new ByteArrayInputStream(byteArray);
  }

  public static class Builder extends Connection.Builder {
    public MockConnection build() {
      return new MockConnection(this);
    }
  }
}

public class ReaderTest {
  @Test
  public void itIsCreatedWithABuilder() {
    Reader reader = new Reader.Builder().build();

    assertThat(reader, instanceOf(Reader.class));
  }

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
  public void testItHasAConnection() {
    Reader reader = new Reader.Builder().build();

    assertThat(reader.connection, instanceOf(Connection.class));
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
