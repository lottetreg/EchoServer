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

class MockWriter extends Writer {
  public boolean calledPrintln;
  public String outputArg;

  MockWriter(Builder builder) { super(builder); }

  public void println(String output) {
    this.calledPrintln = true;
    this.outputArg = output;
  }

  public static class Builder extends Writer.Builder {
    public MockWriter build() {
      return new MockWriter(this);
    }
  }
}

class MockReader extends Reader {
  private String inputString;

  MockReader(Builder builder) {
    super(builder);
    this.inputString = builder.inputString;
  }

  public String readLine() {
    return this.inputString;
  }

  public static class Builder extends Reader.Builder {
    private String inputString;

    public Builder setInputString(String inputString) {
      this.inputString = inputString;
      return this;
    }

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
    public void itHasADefaultWriter() {
      Server server = new Server.Builder(out).build();

      assertThat(server.writer, instanceOf(Writer.class));
    }

    @Test
    public void theWriterCanBeSetThroughTheBuilder() {
      Writer writer = new Writer.Builder().build();

      Server server = new Server.Builder(out)
              .setWriter(writer)
              .build();

      assertEquals(server.writer, writer);
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
      Writer writer = new MockWriter.Builder().build();

      server = new Server.Builder(out)
              .setSocket(socket)
              .setReader(reader)
              .setWriter(writer)
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
  }

  public static class WritesToTheConnectionTests {
    @Test
    public void itWritesTheInputBackToTheConnectionsOutputStream() {
      Socket socket = new MockSocket.Builder().build();
      Reader reader = new MockReader.Builder()
              .setInputString("Some string")
              .build();
      Writer writer = new MockWriter.Builder().build();

      Server server = new Server.Builder(out)
              .setSocket(socket)
              .setReader(reader)
              .setWriter(writer)
              .build();

      server.start(0);

      assertEquals(true, ((MockWriter) writer).calledPrintln);
      assertEquals("Some string", ((MockWriter) writer).outputArg);
    }
  }
}
