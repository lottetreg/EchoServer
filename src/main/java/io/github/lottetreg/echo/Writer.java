package io.github.lottetreg.echo;

import java.io.OutputStream;
import java.io.PrintWriter;

public class Writer {
  public Connection connection;

  public void println(String output) {
    OutputStream outputStream = this.connection.getOutputStream();
    PrintWriter printWriter = new PrintWriter(outputStream, true);
    printWriter.println(output);
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public void closeConnection() {
    this.connection.close();
  }
}
