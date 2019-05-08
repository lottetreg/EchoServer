package io.github.lottetreg.echo;

public class Server {
  public Output out;
  public Socket socket;
  public Echo echo;

  Server(Builder builder) {
    this.out = builder.out;
    this.socket = builder.socket;
    this.echo = builder.echo;
  }

  public void start(int portNumber) {
    this.socket.setPort(portNumber);
    this.out.println("Waiting for connection");

    Connection connection = this.socket.acceptConnection();
    this.out.println("Connection accepted");

    this.echo.setConnection(connection);
    this.echo.echo();
  }

  public static class Builder {
    public Output out;
    private Socket socket = new Socket.Builder().build();
    private Echo echo = new Echo.Builder().build();

    Builder(Output out) {
      this.out = out;
    }

    public Builder setSocket(Socket socket) {
      this.socket = socket;
      return this;
    }

    public Builder setEcho(Echo echo) {
      this.echo = echo;
      return this;
    }

    public Server build() {
      return new Server(this);
    }
  }
}
