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
import static org.junit.Assert.*;

class MockEcho extends Echo {
  public int echoCallCount;

  MockEcho(Builder builder) {
    super(builder);
    this.echoCallCount = 0;
  }

  public void echo() {
    this.echoCallCount++;
  }

  public static class Builder extends Echo.Builder {
    public MockEcho build() {
      return new MockEcho(this);
    }
  }
}

class MockSocket extends Socket {
  public Connection connection;

  MockSocket(Builder builder) {
    super(builder);
    this.connection = new Connection.Builder().build();
  }

  public Connection acceptConnection() {
    return this.connection;
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
    public void itHasADefaultSocket() {
      Server server = new Server.Builder(out).build();

      assertThat(server.socket, instanceOf(Socket.class));
    }

    @Test
    public void itHasADefaultEcho() {
      Server server = new Server.Builder(out).build();

      assertThat(server.echo, instanceOf(Echo.class));
    }

    @Test
    public void theSocketCanBeSetThroughTheBuilder() {
      Socket socket = new Socket.Builder().build();

      Server server = new Server.Builder(out)
              .setSocket(socket)
              .build();

      assertEquals(socket, server.socket);
    }

    @Test
    public void theEchoCanBeSetThroughTheBuilder() {
      Echo echo = new Echo.Builder().build();

      Server server = new Server.Builder(out)
              .setEcho(echo)
              .build();

      assertEquals(echo, server.echo);
    }

    @Test
    public void theOutIsSetThroughTheBuilder() {
      Server server = new Server.Builder(out).build();

      assertEquals(out, server.out);
    }
  }

  public static class StartMethodTests {
    private Server server;
    private Socket socket;
    private Echo echo;

    @Before
    public void setUp() {
      socket = new MockSocket.Builder().build();
      echo = new MockEcho.Builder().build();

      server = new Server.Builder(out)
              .setSocket(socket)
              .setEcho(echo)
              .build();
    }

    @After
    public void tearDown() {
      server.socket.close();
    }

    @Test
    public void testWritesWaitingForConnectionMessage() {
      server.start(0);

      assertThat(bytes.toString(), containsString("Waiting for connection"));
    }

    @Test
    public void testWritesConnectionAcceptedMessage() {
      server.start(0);

      assertThat(bytes.toString(), containsString("Connection accepted"));
    }

    @Test
    public void itSetsTheEchosReaderAndWriterConnectionsToTheSocketsAcceptedConnection() {
      server.start(0);

      assertEquals(((MockSocket) socket).connection, echo.reader.connection);
      assertEquals(((MockSocket) socket).connection, echo.writer.connection);
    }

    @Test
    public void itCallsEchoOnce() {
      server.start(0);

      expectEchoToBeCalledOnce();
    }

    public void expectEchoToBeCalledOnce() {
      assertEquals(1, ((MockEcho) echo).echoCallCount);
    }
  }
}
