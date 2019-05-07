package io.github.lottetreg.echo;

import java.io.OutputStream;
import java.io.PrintWriter;

public class Writer {
  public Connection connection;

  Writer(Builder builder) {
    this.connection = builder.connection;
  }

  public void println(String output) {
    OutputStream outputStream = this.connection.getOutputStream();
    PrintWriter printWriter = new PrintWriter(outputStream, true);
    printWriter.println(output);
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public static class Builder {
    private Connection connection = new Connection.Builder().build();

    public Builder setConnection(Connection connection) {
      this.connection = connection;
      return this;
    }

    public Writer build() {
      return new Writer(this);
    }
  }
}
