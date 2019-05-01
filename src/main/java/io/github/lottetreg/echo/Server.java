package io.github.lottetreg.echo;

public class Server {
  private Output out;
  public Socket socket;
  public Connection connection;

  Server(Output out) {
    this.out = out;
    this.socket = new Socket();
    this.connection = new Connection();
  }

  public void start(int portNumber) {
    this.socket.setPort(portNumber);
    this.out.println("Waiting for connection");
    this.connection = this.socket.acceptConnection();
    this.out.println("Connection accepted");
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }
}
