package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketTest {

  private class MockServerSocket extends ServerSocket {

    public boolean called = false;

    public MockServerSocket() throws IOException {
    }

    public java.net.Socket accept() {
      this.called = true;
      return new java.net.Socket();
    }
  }

  @Test
  public void testCreatesAServerSocket() {
    Socket socket = new Socket();
    assertThat(socket.serverSocket, instanceOf(ServerSocket.class));
  }

  @Test
  public void testSetServerSocket() throws IOException {
    ServerSocket serverSocket = new ServerSocket();
    Socket socket = new Socket();

    socket.setServerSocket(serverSocket);

    assertEquals(socket.serverSocket, serverSocket);
  }

  @Test
  public void testSetPort() throws IOException {
    Socket socket = new Socket();

    socket.setPort(9000);

    assertEquals(socket.serverSocket.getLocalPort(), 9000);
  }

  @Test
  public void testAcceptConnection() throws IOException {
    ServerSocket mockServerSocket = new MockServerSocket();
    Socket socket = new Socket();
    socket.setServerSocket(mockServerSocket);

    socket.setPort(9000);
    socket.acceptConnection();

    MockServerSocket mockSocket = (MockServerSocket) socket.serverSocket;
    assertEquals(mockSocket.called, true);
  }


}
