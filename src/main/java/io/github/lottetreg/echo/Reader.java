package io.github.lottetreg.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {
  public Connection connection;

  Reader(Builder builder) {
    this.connection = builder.connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public String readLine() {
    try {
      return newBufferedReader().readLine();
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  private BufferedReader newBufferedReader() {
    InputStream inputStream = this.connection.getInputStream();
    InputStreamReader streamReader = new InputStreamReader(inputStream);
    return new BufferedReader(streamReader);
  }

  public static class Builder {
    private Connection connection = new Connection.Builder().build();

    public Builder setConnection(Connection connection) {
      this.connection = connection;
      return this;
    }

    public Reader build() {
      return new Reader(this);
    }
  }
}
