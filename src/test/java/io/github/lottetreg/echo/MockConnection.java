package io.github.lottetreg.echo;

import java.io.InputStream;
import java.io.OutputStream;

public class MockConnection extends Connection {
  private OutputStream outputStream;
  private InputStream inputStream;

  MockConnection(Builder builder) {
    super(builder);
    this.outputStream = builder.outputStream;
    this.inputStream = builder.inputStream;
  }

  public InputStream getInputStream() {
    return this.inputStream;
  }

  public OutputStream getOutputStream() {
    return this.outputStream;
  }

  public static class Builder extends Connection.Builder {
    private OutputStream outputStream;
    private InputStream inputStream;

    public Builder setOutputStream(OutputStream outputStream) {
      this.outputStream = outputStream;
      return this;
    }

    public Builder setInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
      return this;
    }

    public MockConnection build() {
      return new MockConnection(this);
    }
  }
}
