package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;

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

    try {
      socket.setPort(9000);
    } catch (RuntimeException e) {
      assertEquals("Failed to bind socket to port 9000", e.getMessage());
    }
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

    try {
      socket.acceptConnection();
    } catch (RuntimeException e) {
      assertEquals("Socket failed to accept connection", e.getMessage());
    }
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

    try {
      socket.close();
    } catch (RuntimeException e) {
      assertEquals("Failed to close socket", e.getMessage());
    }
  }
}
