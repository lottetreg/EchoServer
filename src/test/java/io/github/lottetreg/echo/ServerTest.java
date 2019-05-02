package io.github.lottetreg.echo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class ServerTest {
  private ByteArrayOutputStream bytes;
  private Server server;

  private class MockSocket extends Socket {
    MockSocket() {
      super();
    }

    public Connection acceptConnection() {
      return new Connection(new java.net.Socket());
    }
  }

  @Before
  public void setUp() {
    bytes = new ByteArrayOutputStream();
    Output out = new Output(new PrintStream(bytes));

    Socket mockSocket = new MockSocket();
    server = new Server(out);
    server.setSocket(mockSocket);
  }

  @After
  public void tearDown() {
    server.socket.close();
  }

  @Test
  public void testWritesWaitingForConnectionMessage() {
    server.start(9000);

    assertThat(bytes.toString(), containsString("Waiting for connection"));
  }

  @Test
  public void testWritesConnectionAcceptedMessage() {
    server.start(9000);

    assertThat(bytes.toString(), containsString("Connection accepted"));
  }
}
