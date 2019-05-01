package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.Socket;

public class ReaderTest {
  private Reader reader;

  private class MockSocket extends Socket {
    public InputStream getInputStream() {
      byte[] byteArray = "Some string".getBytes();
      return new ByteArrayInputStream(byteArray);
    }
  }

  @Before
  public void setUp() {
    reader = new Reader();
  }

  @Test
  public void testItHasAConnection() {
    assertThat(reader.connection, instanceOf(Connection.class));
  }

  @Test
  public void testSetConnection() {
    Connection connection = new Connection(new Socket());

    reader.setConnection(connection);

    assertEquals(reader.connection, connection);
  }

  @Test
  public void testReadLine() {
    // better to mock the socket or the connection instead?
    Socket mockSocket = new MockSocket();
    Connection connection = new Connection(mockSocket);
    reader.setConnection(connection);

    assertEquals(reader.readLine(), "Some string");
  }
}
