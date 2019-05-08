package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

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
    PrintStream printStream = new PrintStream(socket.getOutputStream(), true);
    printStream.println(message);
  }

  public String readFromSocketInputStream(Socket socket) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    return bufferedReader.readLine();
  }
}
