package io.github.lottetreg.echo;

import java.net.Socket;

public class Connection {
  public Socket socket;

  Connection(Socket socket) {
    this.socket = socket;
  }
}
