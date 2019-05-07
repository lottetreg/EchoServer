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
  Socket socket;

  @Test
  public void itWorks() throws IOException {
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
    PrintStream printStream = new PrintStream(socket.getOutputStream(), true);
    printStream.println(message);
  }

  public String readFromSocketInputStream() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    return bufferedReader.readLine();
  }
}
