package io.github.lottetreg.echo;

import java.io.IOException;
import java.net.ServerSocket;

public class EchoServer {
  public static void main(String[] args) {
    int portNumber = new ArgumentParser().parse(args);
    try {
      ServerSocket serverSocket = new ServerSocket(portNumber);
      System.out.println("Waiting for connection");
      serverSocket.accept();
      System.out.println("Connection accepted");
    } catch(IOException e) {
    }
  }
}
