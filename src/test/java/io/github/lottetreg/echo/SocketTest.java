package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketTest {
  private boolean calledAccept;
  private Socket socket;

  private class MockServerSocket extends ServerSocket {
    public MockServerSocket() throws IOException {}

    public java.net.Socket accept() {
      SocketTest.this.calledAccept = true;
      return new java.net.Socket();
    }
  }

  @Before
  public void setUp() {
    socket = new Socket();
  }

  @After
  public void tearDown() {
    socket.close();
  }

  @Test
  public void testCreatesAServerSocket() {
    assertThat(socket.serverSocket, instanceOf(ServerSocket.class));
  }

  @Test
  public void testSetServerSocket() throws IOException {
    ServerSocket serverSocket = new ServerSocket();

    socket.setServerSocket(serverSocket);

    assertEquals(serverSocket, socket.serverSocket);
  }

  @Test
  public void testSetPort() {
    socket.setPort(9000);

    assertEquals(9000, socket.serverSocket.getLocalPort());
  }

  @Test
  public void testAcceptConnection() throws IOException {
    ServerSocket mockServerSocket = new MockServerSocket();
    socket.setServerSocket(mockServerSocket);

    socket.setPort(9000);
    socket.acceptConnection();

    assertEquals(true, calledAccept);
  }

  @Test
  public void testClose() {
    socket.close();

    assertEquals(true, socket.serverSocket.isClosed());
  }
}
