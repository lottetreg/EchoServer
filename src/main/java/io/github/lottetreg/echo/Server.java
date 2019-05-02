package io.github.lottetreg.echo;

public class Server {
  private Output out;
  public Socket socket;

  Server(Output out) {
    this.out = out;
    this.socket = new Socket();
  }

  public void start(int portNumber) {
    this.socket.setPort(portNumber);
    this.out.println("Waiting for connection");
    this.socket.acceptConnection();
    this.out.println("Connection accepted");
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }
}
