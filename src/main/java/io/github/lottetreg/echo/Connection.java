package io.github.lottetreg.echo;

import java.io.IOException;
import java.io.InputStream;

public class Connection {
  public java.net.Socket socket;

  Connection(java.net.Socket socket) {
    this.socket = socket;
  }

  public InputStream getInputStream() {
    try {
      return socket.getInputStream();
    } catch(IOException e) {
      System.out.println(e);
      return null;
    }
  }
}
