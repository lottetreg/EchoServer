package io.github.lottetreg.echo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
  ServerSocket socket;

  Connection(ServerSocket socket) {
    this.socket = socket;
  }

  public Socket accept() throws IOException {
    return socket.accept();
  }
}
