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
    String hostname = "127.0.0.1";
    InetSocketAddress socketAddress = new InetSocketAddress(hostname, portNumber);
    try {
      this.serverSocket.bind(socketAddress);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public Connection acceptConnection() {
    try {
      java.net.Socket socket = this.serverSocket.accept();
      return new Connection.Builder().setSocket(socket).build();
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  public void close() {
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public static class Builder {
    private ServerSocket serverSocket;

    Builder() {
      try {
        this.serverSocket = new ServerSocket();
      } catch (IOException e) {
        System.out.println(e);
      }
    }

    public Builder setServerSocket(ServerSocket serverSocket) {
      this.serverSocket = serverSocket;
      return this;
    }

    public Socket build() {
      return new Socket(this);
    }
  }
}


