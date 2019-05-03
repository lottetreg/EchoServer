package io.github.lottetreg.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {
  public Connection connection;

  Reader() {
    this.connection = new Connection();
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
    InputStream inputStream = connection.getInputStream();
    InputStreamReader streamReader = new InputStreamReader(inputStream);
    return new BufferedReader(streamReader);
  }
}
