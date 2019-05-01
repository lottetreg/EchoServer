package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;

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
  public void itIsInitializedWithASocket() {
    Socket socket = new Socket();
    Connection connection = new Connection(socket);

    assertEquals(socket, connection.socket);
  }

  @Test
  public void testGetInputStream() throws IOException {
    Socket socket = new MockSocket();
    Connection connection = new Connection(socket);

    InputStream inputStream = connection.getInputStream();

    assertEquals(socket.getInputStream(), inputStream);
  }
}
