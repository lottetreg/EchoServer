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
  public void itCreatesANewSocket() {
    Connection connection = new Connection();

    assertThat(connection.socket, instanceOf(Socket.class));
  }

  @Test
  public void testSetSocket() {
    Socket socket = new Socket();
    Connection connection = new Connection();

    connection.setSocket(socket);

    assertEquals(socket, connection.socket);
  }

  @Test
  public void testGetInputStream() throws IOException {
    Socket socket = new MockSocket();
    Connection connection = new Connection();
    connection.setSocket(socket);

    InputStream inputStream = connection.getInputStream();

    assertEquals(socket.getInputStream(), inputStream);
  }
}
