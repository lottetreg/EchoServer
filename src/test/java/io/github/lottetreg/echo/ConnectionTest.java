package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ConnectionTest {

  private class MockSocket extends Socket {
    private ByteArrayInputStream inputStream;

    MockSocket() {
      inputStream = new ByteArrayInputStream(new byte[] {});
    }

    public InputStream getInputStream() {
      return inputStream;
    }
  }

  @Test
  public void itIsCreatedWithABuilder() {
    Connection connection = new Connection.Builder().build();

    assertThat(connection, instanceOf(Connection.class));
  }

  @Test
  public void itHasADefaultSocket() {
    Connection connection = new Connection.Builder().build();

    assertThat(connection.socket, instanceOf(Socket.class));
  }

  @Test
  public void theSocketCanBeSetThroughTheBuilder() {
    Socket socket = new Socket();

    Connection connection = new Connection.Builder()
            .setSocket(socket)
            .build();

    assertEquals(connection.socket, socket);
  }

  @Test
  public void testGetInputStreamReturnsTheSocketsInputStream() throws IOException {
    Socket socket = new MockSocket();

    Connection connection = new Connection.Builder()
            .setSocket(socket)
            .build();

    InputStream inputStream = connection.getInputStream();

    assertEquals(socket.getInputStream(), inputStream);
  }
}
