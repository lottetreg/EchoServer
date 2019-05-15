package io.github.lottetreg.echo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {
  public Connection connection;

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public String readLine() {
    try {
      return newBufferedReader().readLine();
    } catch(Connection.FailedToGetInputStreamException e) {
      throw e;
    } catch (Exception e) {
      throw new FailedToReadLineException(e);
    }
  }

  public BufferedReader newBufferedReader() {
    InputStream inputStream = this.connection.getInputStream();
    InputStreamReader streamReader = new InputStreamReader(inputStream);
    return new BufferedReader(streamReader);
  }

  class FailedToReadLineException extends RuntimeException {
    FailedToReadLineException(Throwable cause) {
      super("Failed to read from the buffered reader", cause);
    }
  }
}
