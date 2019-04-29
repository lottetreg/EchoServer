package io.github.lottetreg.echo;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerTest {
  @Test
  public void printsErrorMessageWhenConnectionFails() {
//    Connection connection = new MockConnection(serverSocket);
//    EchoServer.main(connection);

//    Assert.assertEquals(8080, echoServer.serverSocket.getLocalPort());
  }

  private class MockConnection extends Connection {
    MockConnection(ServerSocket socket) {
      super(socket);
    }

    public Socket accept() throws IOException {
      throw new IOException();
    }
  }
}
