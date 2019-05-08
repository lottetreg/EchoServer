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

class MockReader extends Reader {
  MockReader(Builder builder) {
    super(builder);
  }

  public String readLine() {
    return "Some string";
  }

  public static class Builder extends Reader.Builder {
    public MockReader build() {
      return new MockReader(this);
    }
  }
}

class MockSocket extends Socket {
  MockSocket(Builder builder) {
    super(builder);
  }

  public Connection acceptConnection() {
    return new Connection.Builder().build();
  }

  public static class Builder extends Socket.Builder {
    public MockSocket build() {
      return new MockSocket(this);
    }
  }
}

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

    @Before
    public void setUp() {
      Socket socket = new MockSocket.Builder().build();
      Reader reader = new MockReader.Builder().build();

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
