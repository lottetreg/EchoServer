package io.github.lottetreg.echo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
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
      public Connection acceptConnection() {
        return new Connection.Builder().build();
      }
    }

    private class MockReader extends Reader {
      public String readLine() {
        return "Some string";
      }
    }

    @Before
    public void setUp() {
      Socket socket = new MockSocket();
      Reader reader = new MockReader();

      server = new Server(out);
      server.setSocket(socket);
      server.setReader(reader);
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
