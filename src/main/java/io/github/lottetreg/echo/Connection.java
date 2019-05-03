package io.github.lottetreg.echo;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Connection {
  public Socket socket;

  Connection(Builder builder) {
    this.socket = builder.socket;
  }

  public InputStream getInputStream() {
    try {
      return socket.getInputStream();
    } catch(IOException e) {
      System.out.println(e);
      return null;
    }
  }

  public static class Builder {
    private Socket socket = new Socket();

    public Builder() {}

    public Builder setSocket(Socket socket) {
      this.socket = socket;
      return this;
    }

    public Connection build() {
      return new Connection(this);
    }
  }
}
