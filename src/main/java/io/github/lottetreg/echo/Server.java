package io.github.lottetreg.echo;

public class Server {
  public Output out;
  public Socket socket;
  public Connection connection;
  public Reader reader;
  public Writer writer;

  Server(Builder builder) {
    this.out = builder.out;
    this.socket = builder.socket;
    this.reader = builder.reader;
    this.writer = builder.writer;
  }

  public void start(int portNumber) {
    this.socket.setPort(portNumber);
    this.out.println("Waiting for connection");

    this.connection = this.socket.acceptConnection();
    this.out.println("Connection accepted");

    this.reader.setConnection(this.connection);
    this.writer.setConnection(this.connection);

    this.writer.println(this.reader.readLine());
  }

  public static class Builder {
    public Output out;
    private Socket socket = new Socket.Builder().build();
    private Reader reader = new Reader.Builder().build();
    private Writer writer = new Writer();

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

    public Builder setWriter(Writer writer) {
      this.writer = writer;
      return this;
    }

    public Server build() {
      return new Server(this);
    }
  }
}
