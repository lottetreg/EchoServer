package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

public class EchoServerTest {
  int portNumber = 8080;
  Socket socket;

  @Test
  public void itEchoesTheClientsMessageBackToThem() throws IOException {
    startServer();
    socket = new Socket("localhost", portNumber);
    sendMessageToServer("Hello, World!");

    assertEquals("Hello, World!", readFromSocketInputStream());

    socket.close();
  }

  public void startServer() {
    Thread serverHandlerThread = new ServerHandlerThread();
    serverHandlerThread.start();
  }

  class ServerHandlerThread extends Thread {
    public void run() {
      String[] args = {Integer.toString(portNumber)};
      EchoServer.main(args);
    }
  }

  public void sendMessageToServer(String message) throws IOException {
    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
    printWriter.println(message);
  }

  public String readFromSocketInputStream() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    return bufferedReader.readLine();
  }
}
