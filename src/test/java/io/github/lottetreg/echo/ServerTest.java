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
import static org.junit.Assert.assertEquals;
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
    public void itHasADefaultSocket() {
      Server server = new Server.Builder(out).build();

      assertThat(server.socket, instanceOf(Socket.class));
    }

    @Test
    public void theSocketCanBeSetThroughTheBuilder() {
      Socket socket = new Socket.Builder().build();

      Server server = new Server.Builder(out)
              .setSocket(socket)
              .build();

      assertEquals(server.socket, socket);
    }

    @Test
    public void itHasADefaultReader() {
      Server server = new Server.Builder(out).build();

      assertThat(server.reader, instanceOf(Reader.class));
    }

    @Test
    public void theReaderCanBeSetThroughTheBuilder() {
      Reader reader = new Reader.Builder().build();

      Server server = new Server.Builder(out)
              .setReader(reader)
              .build();

      assertEquals(server.reader, reader);
    }

    @Test
    public void theOutIsSetThroughTheBuilder() {
      Server server = new Server.Builder(out).build();

      assertEquals(server.out, out);
    }
  }

  public static class OutputTests {
    private Server server;

    @Before
    public void setUp() {
      Socket socket = new MockSocket.Builder().build();
      Reader reader = new MockReader.Builder().build();

      server = new Server.Builder(out)
              .setSocket(socket)
              .setReader(reader)
              .build();
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
