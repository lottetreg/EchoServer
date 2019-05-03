package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketTest {
  private boolean calledAccept;
  private Socket socket;

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

  @Before
  public void setUp() {
    socket = new Socket.Builder().build();
  }

  @After
  public void tearDown() {
    socket.close();
  }

  @Test
  public void itIsCreatedWithABuilder() {
    Socket socket = new Socket.Builder().build();

    assertThat(socket, CoreMatchers.instanceOf(Socket.class));
  }

  @Test
  public void itHasADefaultServerSocket() {
    Socket socket = new Socket.Builder().build();

    assertThat(socket.serverSocket, CoreMatchers.instanceOf(ServerSocket.class));
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
  public void testSetServerSocket() throws IOException {
    ServerSocket serverSocket = new ServerSocket();

    Socket socket = new Socket.Builder()
            .setServerSocket(serverSocket)
            .build();

    assertEquals(serverSocket, socket.serverSocket);
  }

  @Test
  public void testSetPort() {
    socket.setPort(9000);

    assertEquals(9000, socket.serverSocket.getLocalPort());

    socket.close();
  }

  @Test
  public void testAcceptConnectionCallsAcceptOnTheServerSocket() throws IOException {
    MockServerSocket mockServerSocket = new MockServerSocket();
    Socket socket = new Socket.Builder()
            .setServerSocket(mockServerSocket)
            .build();

    socket.setPort(9000);

    socket.acceptConnection();

    assertEquals(true, calledAccept);

    socket.close();
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
    socket.close();

    assertEquals(true, socket.serverSocket.isClosed());
  }
}
