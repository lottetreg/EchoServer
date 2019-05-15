package io.github.lottetreg.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Socket {
  public ServerSocket serverSocket;

  public Socket(Builder builder) {
    this.serverSocket = builder.serverSocket;
  }

  public void setPort(int portNumber) {
    try {
      String hostname = "127.0.0.1";
      InetSocketAddress socketAddress = new InetSocketAddress(hostname, portNumber);
      this.serverSocket.bind(socketAddress);
    } catch (Exception e) {
      throw new FailedToBindSocketException(portNumber, e);
    }
  }

  public Connection acceptConnection() {
    try {
      java.net.Socket socket = this.serverSocket.accept();
      return new Connection.Builder().setSocket(socket).build();
    } catch (Exception e) {
      throw new FailedToAcceptConnectionException(e);
    }
  }

  public void close() {
    try {
      this.serverSocket.close();
    } catch (Exception e) {
      throw new FailedToCloseSocketException(e);
    }
  }

  public static class Builder {
    private ServerSocket serverSocket;

    Builder() {
      try {
        this.serverSocket = newServerSocket();
      } catch (Exception e) {
        throw new NewSocketBuilderFailedException(e);
      }
    }

    public ServerSocket newServerSocket() throws IOException {
      return new ServerSocket();
    }

    public Builder setServerSocket(ServerSocket serverSocket) {
      this.serverSocket = serverSocket;
      return this;
    }

    public Socket build() {
      return new Socket(this);
    }

    class NewSocketBuilderFailedException extends RuntimeException {
      NewSocketBuilderFailedException(Throwable cause) {
        super("Failed to create new Socket.Builder()", cause);
      }
    }
  }

  class FailedToBindSocketException extends RuntimeException {
    FailedToBindSocketException(int portNumber, Throwable cause) {
      super("Failed to bind socket to port " + portNumber, cause);
    }
  }

  class FailedToAcceptConnectionException extends RuntimeException {
    FailedToAcceptConnectionException(Throwable cause) {
      super("Socket failed to accept connection", cause);
    }
  }

  class FailedToCloseSocketException extends RuntimeException {
    FailedToCloseSocketException(Throwable cause) {
      super("Failed to close socket", cause);
    }
  }
}


