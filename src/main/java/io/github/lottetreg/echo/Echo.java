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
    while ((input = readIn()) != null) {
      printOut(input);
    }
  }

  private String readIn() {
    try {
      return this.reader.readLine();
    } catch (Exception e) {
      printOut("Could not read from this connection.");
      this.reader.closeConnection();
      throw e;
    }
  }

  private void printOut(String output) {
    try {
      this.writer.println(output);
    } catch (Exception e) {
      this.writer.closeConnection();
      throw e;
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
