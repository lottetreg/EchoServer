package io.github.lottetreg.echo;

public class EchoServer {
  public static void main(String[] args) {
    int portNumber = new ArgumentParser().parse(args);
    Socket socket = new Socket();
    socket.setPort(portNumber);

    System.out.println("Waiting for connection");
    socket.acceptConnection();
  }
}
