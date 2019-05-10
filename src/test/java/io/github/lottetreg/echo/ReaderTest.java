package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ReaderTest {
  @Test
  public void testSetConnection() {
    Connection connection = new Connection.Builder().build();
    Reader reader = new Reader();
    reader.setConnection(connection);

    assertEquals(reader.connection, connection);
  }

  @Test
  public void testReadLine() {
    byte[] byteArray = "Some string".getBytes();
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Connection connection = new MockConnection.Builder()
            .setInputStream(inputStream)
            .build();
    Reader reader = new Reader();
    reader.setConnection(connection);

    assertEquals(reader.readLine(), "Some string");
  }
}
