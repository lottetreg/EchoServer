package io.github.lottetreg.echo;

public class Server {
  public Output out;
  public Socket socket;
  public ThreadRunner threadRunner;

  Server(Builder builder) {
    this.out = builder.out;
    this.socket = builder.socket;
    this.threadRunner = builder.threadRunner;
  }

  public void start(int portNumber) {
    this.socket.setPort(portNumber);
    this.out.println("Waiting for connection");

    Connection connection;
    while((connection = socket.acceptConnection()) != null) {
      out.println("Connection accepted");
      this.threadRunner.run(new ConnectionThread(connection));
    }
  }

  public static class Builder {
    public Output out;
    private Socket socket = new Socket.Builder().build();
    private ThreadRunner threadRunner = new ThreadRunner();

    Builder(Output out) {
      this.out = out;
    }

    public Builder setSocket(Socket socket) {
      this.socket = socket;
      return this;
    }

    public Builder setThreadRunner(ThreadRunner threadRunner) {
      this.threadRunner = threadRunner;
      return this;
    }

    public Server build() {
      return new Server(this);
    }
  }
}
