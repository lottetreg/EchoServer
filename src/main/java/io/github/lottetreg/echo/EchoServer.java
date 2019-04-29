package io.github.lottetreg.echo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
  public static void main(String[] args) {
    int portNumber = Integer.parseInt(args[0]);
    try {
      ServerSocket serverSocket = new ServerSocket(portNumber);
      Socket connection = new Connection(serverSocket).accept();
      new EchoServer().execute(connection);
    } catch(IOException e) {
      System.out.println("Something went wrong");
      System.exit(1);
    }
  }

  public void execute(Socket connection) throws IOException {
//    connection.accept();
  }
}
