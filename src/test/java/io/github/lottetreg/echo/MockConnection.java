package io.github.lottetreg.echo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MockConnection extends Connection {
  private ByteArrayOutputStream outputStream;

  MockConnection(Builder builder) {
    super(builder);
    this.outputStream = new ByteArrayOutputStream();
  }

  public InputStream getInputStream() {
    byte[] byteArray = "Some string".getBytes();
    return new ByteArrayInputStream(byteArray);
  }

  public OutputStream getOutputStream() {
    return this.outputStream;
  }

  public static class Builder extends Connection.Builder {
    public MockConnection build() {
      return new MockConnection(this);
    }
  }
}
