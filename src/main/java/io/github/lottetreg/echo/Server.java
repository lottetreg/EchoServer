package io.github.lottetreg.echo;

public class Server {
  private Output out;
  public Socket socket;
  public Connection connection;
  public Reader reader;

  Server(Output out) {
    this.out = out;
    this.socket = new Socket.Builder().build();
    this.connection = new Connection.Builder().build();
    this.reader = new Reader.Builder().build();
  }

  public void start(int portNumber) {
    this.socket.setPort(portNumber);
    this.out.println("Waiting for connection");

    this.connection = this.socket.acceptConnection();
    this.out.println("Connection accepted");

    this.reader.setConnection(this.connection);
    out.println(this.reader.readLine());
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }
}
