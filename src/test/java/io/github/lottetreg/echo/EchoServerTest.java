package io.github.lottetreg.echo;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class EchoServerTest {
  int portNumber = 8080;

  @Test
  public void itWorks() throws IOException {
    Thread serverHandlerThread = new ServerHandlerThread();
    serverHandlerThread.start();

    Socket socket = new Socket("localhost", portNumber);

    socket.close();
  }

  class ServerHandlerThread extends Thread {
    public void run() {
      String[] args = {Integer.toString(portNumber)};
      EchoServer.main(args);
    }
  }
}
