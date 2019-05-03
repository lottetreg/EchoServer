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

    this.socket.acceptConnection(this.connection);
    this.out.println("Connection accepted");

    Reader reader = new Reader();
    reader.setConnection(this.connection);
    out.println(reader.readLine());
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }
}
