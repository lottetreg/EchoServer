package io.github.lottetreg.echo;

public class Echo {
  public Reader reader;
  public Writer writer;

  Echo(Builder builder) {
    this.reader = builder.reader;
    this.writer = builder.writer;
  }

  public void setConnection(Connection connection) {
    this.reader.setConnection(connection);
    this.writer.setConnection(connection);
  }

  public void echo() {
    String input;
    while((input = this.reader.readLine()) != null) {
      this.writer.println(input);
    }
  }

  public static class Builder {
    private Reader reader = new Reader();
    private Writer writer = new Writer();

    public Builder setReader(Reader reader) {
      this.reader = reader;
      return this;
    }

    public Builder setWriter(Writer writer) {
      this.writer = writer;
      return this;
    }

    public Echo build() {
      return new Echo(this);
    }
  }
}
