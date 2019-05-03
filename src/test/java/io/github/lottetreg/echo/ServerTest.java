package io.github.lottetreg.echo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class ServerTest {
  private static ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  private static Output out = new Output(new PrintStream(bytes));

  public static class SetUpTests {
    @Test
    public void testItCreatesANewSocket () {
      assertThat(new Server(out).socket, instanceOf(Socket.class));
    }

    @Test
    public void testItCreatesANewConnection () {
      assertThat(new Server(out).connection, instanceOf(Connection.class));
    }
  }

  public static class OutputTests {
    private Server server;

    private class MockSocket extends Socket {
      MockSocket() {
        super();
      }

      public void acceptConnection(Connection connection) {}
    }

    private class MockConnection extends Connection {
      MockConnection() {
        super();
      }

      public InputStream getInputStream() {
        byte[] byteArray = "Some string".getBytes();
        return new ByteArrayInputStream(byteArray);
      }
    }

    @Before
    public void setUp() {
      MockSocket mockSocket = new MockSocket();
      MockConnection connection = new MockConnection();
      server = new Server(out);
      server.setSocket(mockSocket);
      server.setConnection(connection);
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

    @Test
    public void testWritesDataFromConnection() {
      server.start(9000);

      assertThat(bytes.toString(), containsString("Some string"));
    }
  }
}
