package io.github.lottetreg.echo;

public class ConnectionThread extends Thread {
  public Connection connection;
  private Echo echo;

  ConnectionThread(Connection connection) {
    this.connection = connection;
    this.echo = new Echo.Builder().build();
  }

  public void run() {
    this.echo.setConnection(this.connection);
    this.echo.echo();
  }
}
