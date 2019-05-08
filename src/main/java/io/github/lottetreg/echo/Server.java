package io.github.lottetreg.echo;

public class Server {
  public Output out;
  public Socket socket;
  public Connection connection;
  public Reader reader;

  Server(Builder builder) {
    this.out = builder.out;
    this.socket = builder.socket;
    this.reader = builder.reader;
  }

  public void start(int portNumber) {
    this.socket.setPort(portNumber);
    this.out.println("Waiting for connection");

    this.connection = this.socket.acceptConnection();
    this.out.println("Connection accepted");

    this.reader.setConnection(this.connection);
    out.println(this.reader.readLine());
  }

  public static class Builder {
    public Output out;
    private Socket socket = new Socket.Builder().build();
    private Reader reader = new Reader.Builder().build();

    Builder(Output out) {
      this.out = out;
    }

    public Builder setSocket(Socket socket) {
      this.socket = socket;
      return this;
    }

    public Builder setReader(Reader reader) {
      this.reader = reader;
      return this;
    }

    public Server build() {
      return new Server(this);
    }
  }
}
