package io.github.lottetreg.echo;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Connection {
  public Socket socket;

  Connection() {
    this.socket = new Socket();
  }

  public void setSocket(Socket socket) {
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
