package io.github.lottetreg.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Socket {
  public ServerSocket serverSocket;

  public Socket() {
    try {
      this.serverSocket = new ServerSocket();
    } catch (IOException e) {
      this.serverSocket = null;
    }
  }

  public void setServerSocket(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public void setPort(int portNumber) {
    String hostname = "0.0.0.0";
    InetSocketAddress socketAddress = new InetSocketAddress(hostname, portNumber);
    try {
      this.serverSocket.bind(socketAddress);
    } catch (IOException e) {
      System.out.println(e.getStackTrace());
    }
  }

  public Connection acceptConnection() {
    try {
      java.net.Socket socket = this.serverSocket.accept();
      System.out.println("Connection accepted on port " + this.serverSocket.getLocalPort());
      return new Connection();
    } catch (IOException e) {
      System.out.println(e.getStackTrace());
      return null;
    }
  }

  public void close() {
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      System.out.println(e.getStackTrace());
    }
  }
}


