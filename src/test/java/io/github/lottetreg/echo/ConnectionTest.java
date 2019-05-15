package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void GetInputStreamRaisesAnException() {
    class MissingInputStreamSocket extends Socket {
      public InputStream getInputStream() throws IOException {
        throw new IOException();
      }
    }

    MissingInputStreamSocket missingInputStreamSocket = new MissingInputStreamSocket();

    Connection connection = new Connection.Builder()
            .setSocket(missingInputStreamSocket)
            .build();

    exceptionRule.expect(Connection.FailedToGetInputStreamException.class);
    exceptionRule.expectMessage("Failed to get the socket's input stream");

    connection.getInputStream();
  }

  @Test
  public void GetOutputStreamRaisesAnException() {
    class MissingOutputStreamSocket extends Socket {
      public OutputStream getOutputStream() throws IOException {
        throw new IOException();
      }
    }

    MissingOutputStreamSocket missingOutputStreamSocket = new MissingOutputStreamSocket();

    Connection connection = new Connection.Builder()
            .setSocket(missingOutputStreamSocket)
            .build();

    exceptionRule.expect(Connection.FailedToGetOutputStreamException.class);
    exceptionRule.expectMessage("Failed to get the socket's output stream");

    connection.getOutputStream();
  }
}
