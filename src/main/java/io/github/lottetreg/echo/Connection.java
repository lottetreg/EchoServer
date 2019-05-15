package io.github.lottetreg.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {
  public Socket socket;

  Connection(Builder builder) {
    this.socket = builder.socket;
  }

  public InputStream getInputStream() {
    try {
      return this.socket.getInputStream();
    } catch(Exception e) {
      throw new FailedToGetInputStreamException(e);
    }
  }

  public OutputStream getOutputStream() {
    try {
      return this.socket.getOutputStream();
    } catch(IOException e) {
      throw new FailedToGetOutputStreamException(e);
    }
  }

  public void close() {
    try {
      this.socket.close();
    } catch (Exception e) {
      throw new FailedToCloseConnectionException(e);
    }
  }

  public static class Builder {
    private Socket socket = new Socket();

    public Builder setSocket(Socket socket) {
      this.socket = socket;
      return this;
    }

    public Connection build() {
      return new Connection(this);
    }
  }

  class FailedToGetInputStreamException extends RuntimeException {
    FailedToGetInputStreamException(Throwable cause) {
      super("Failed to get the socket's input stream", cause);
    }
  }

  class FailedToGetOutputStreamException extends RuntimeException {
    FailedToGetOutputStreamException(Throwable cause) {
      super("Failed to get the socket's output stream", cause);
    }
  }

  class FailedToCloseConnectionException extends RuntimeException {
    FailedToCloseConnectionException(Throwable cause) {
      super("Failed to close the connection", cause);
    }
  }
}
