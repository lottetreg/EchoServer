package io.github.lottetreg.echo;

import org.junit.Assert;
import org.junit.Test;

import java.net.Socket;

public class ConnectionTest {
  @Test
  public void itIsInitializedWithASocket() {
    Socket socket = new Socket();
    Connection connection = new Connection(socket);

    Assert.assertEquals(socket, connection.socket);
  }
}
