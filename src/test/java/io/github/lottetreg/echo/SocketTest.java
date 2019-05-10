package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;

class InvalidSocket extends Socket {
  InvalidSocket(Builder builder) {
    super(builder);
  }

  public static class InvalidBuilder extends Socket.Builder {
    public ServerSocket newServerSocket() throws IOException {
      throw new IOException();
    }
  }
}

public class SocketTest {
  private boolean calledAccept;

  private class MockServerSocket extends ServerSocket {
    public java.net.Socket socket;

    public MockServerSocket() throws IOException {
      this.socket = new java.net.Socket();
    }

    public java.net.Socket accept() {
      SocketTest.this.calledAccept = true;
      return this.socket;
    }
  }

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itThrowsAnExceptionIfItFailsToCreateABuilder() {
    exceptionRule.expect(Socket.Builder.NewSocketBuilderFailedException.class);
    exceptionRule.expectMessage("Failed to create new Socket.Builder()");

    new InvalidSocket.InvalidBuilder();
  }

  @Test
  public void itHasADefaultServerSocket() {
    Socket socket = new Socket.Builder().build();

    assertThat(socket.serverSocket, instanceOf(ServerSocket.class));
  }

  @Test
  public void theServerSocketCanBeSetThroughTheBuilder() throws IOException {
    ServerSocket serverSocket = new ServerSocket();

    Socket socket = new Socket.Builder()
            .setServerSocket(serverSocket)
            .build();

    assertEquals(socket.serverSocket, serverSocket);
  }


  @Test
  public void testSetPort() {
    Socket socket = new Socket.Builder().build();

    socket.setPort(9000);

    assertEquals(9000, socket.serverSocket.getLocalPort());

    socket.close();
  }

  @Test
  public void setPortThrowsAnExceptionIfItCannotBindTheSocket() throws IOException {
    class UnbindableServerSocket extends ServerSocket {
      UnbindableServerSocket() throws IOException {}

      public void bind(SocketAddress endpoint) throws IOException {
        throw new IOException();
      }
    }

    UnbindableServerSocket unbindableServerSocket = new UnbindableServerSocket();
    Socket socket = new Socket.Builder()
            .setServerSocket(unbindableServerSocket)
            .build();

    exceptionRule.expect(Socket.FailedToBindSocketException.class);
    exceptionRule.expectMessage("Failed to bind socket to port 9000");

    socket.setPort(9000);
  }

  @Test
  public void testAcceptConnectionCallsAcceptOnTheServerSocket() throws IOException {
    Socket socket = new Socket.Builder()
            .setServerSocket(new MockServerSocket())
            .build();

    socket.setPort(9000);

    socket.acceptConnection();

    assertEquals(true, calledAccept);

    socket.close();
  }

  @Test
  public void acceptConnectionThrowsExceptionIfTheSocketCannotAccept() throws IOException {
    class UnacceptableServerSocket extends ServerSocket {
      UnacceptableServerSocket() throws IOException {}

      public java.net.Socket accept() throws IOException {
        throw new IOException();
      }
    }

    UnacceptableServerSocket unacceptableServerSocket = new UnacceptableServerSocket();
    Socket socket = new Socket.Builder()
            .setServerSocket(unacceptableServerSocket)
            .build();

    exceptionRule.expect(Socket.FailedToAcceptConnectionException.class);
    exceptionRule.expectMessage("Socket failed to accept connection");

    socket.acceptConnection();
  }

  @Test
  public void testAcceptConnectionSetsTheSocketOnTheConnection() throws IOException {
    MockServerSocket mockServerSocket = new MockServerSocket();
    Socket socket = new Socket.Builder()
            .setServerSocket(mockServerSocket)
            .build();

    socket.setPort(9000);

    Connection connection = socket.acceptConnection();

    assertEquals(mockServerSocket.socket, connection.socket);

    socket.close();
  }

  @Test
  public void testClose() {
    Socket socket = new Socket.Builder().build();

    socket.close();

    assertEquals(true, socket.serverSocket.isClosed());
  }

  @Test
  public void closeThrowsExceptionIfItCannotCloseTheSocket() throws IOException {
    class UnclosableServerSocket extends ServerSocket {
      UnclosableServerSocket() throws IOException {}

      public void close() throws IOException {
        throw new IOException();
      }
    }

    UnclosableServerSocket unclosableServerSocket = new UnclosableServerSocket();
    Socket socket = new Socket.Builder()
            .setServerSocket(unclosableServerSocket)
            .build();

    exceptionRule.expect(Socket.FailedToCloseSocketException.class);
    exceptionRule.expectMessage("Failed to close socket");

    socket.close();
  }
}
