package io.github.lottetreg.echo;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionTest {
  @Test
  public void returnsTheAcceptedSocket() throws IOException {
    ServerSocket serverSocket = new MockServerSocket(8080);
    Connection connection = new Connection(serverSocket);

    Assert.assertEquals(true, connection.accept() instanceof Socket);
  }

  private class MockServerSocket extends ServerSocket {
    MockServerSocket(int portNumber) throws IOException {
      super(portNumber);
    }

    public Socket accept() {
      return new ConnectionTest.MockSocket();
    }
  }

  private class MockSocket extends Socket {
  }
}
