package io.github.lottetreg.echo;

public class Connection {
  public java.net.Socket socket;

  Connection(java.net.Socket socket) {
    this.socket = socket;
  }
}
