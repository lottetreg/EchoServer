package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

public class EchoServerTest {
  int portNumber = 8080;

  @Test
  public void itEchoesBackToMultipleClients() throws IOException {
    startServer();

    Socket firstSocket = new Socket("localhost", portNumber);
    sendMessageToServer(firstSocket,"Hello, World!");

    Socket secondSocket = new Socket("localhost", portNumber);
    sendMessageToServer(secondSocket, "Oh hai!");

    assertEquals("Hello, World!", readFromSocketInputStream(firstSocket));
    assertEquals("Oh hai!", readFromSocketInputStream(secondSocket));

    firstSocket.close();
    secondSocket.close();
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

  public void sendMessageToServer(Socket socket, String message) throws IOException {
    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
    printWriter.println(message);
  }

  public String readFromSocketInputStream(Socket socket) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    return bufferedReader.readLine();
  }
}
