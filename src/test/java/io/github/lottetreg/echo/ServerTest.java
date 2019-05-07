package io.github.lottetreg.echo;

import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

class MockWriter extends Writer {
  public int printlnCallCount;
  public LinkedList printlnArgs;

  MockWriter() {
    this.printlnCallCount = 0;
    this.printlnArgs = new LinkedList();
  }

  public void println(String output) {
    this.printlnCallCount++;
    this.printlnArgs.add(output);
  }
}

class MockReader extends Reader {
  public String[] readLineOutputs;
  private int readLineCallCount;

  MockReader() {
    this.readLineCallCount = 0;
  }

  public String readLine() {
    this.readLineCallCount++;
    return this.readLineOutputs[readLineCallCount - 1];
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
      Reader reader = new Reader();

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
      Writer writer = new Writer();

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
      Reader reader = new MockReader();
      Writer writer = new MockWriter();

      server = new Server.Builder(out)
              .setSocket(socket)
              .setReader(reader)
              .setWriter(writer)
              .build();

      allowReadLineToReturn(reader, new String[] {null});
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
      Reader reader = new MockReader();
      Writer writer = new MockWriter();

      allowReadLineToReturn(reader, new String[] {"Some string", null});

      Server server = new Server.Builder(out)
              .setSocket(socket)
              .setReader(reader)
              .setWriter(writer)
              .build();

      server.start(0);

      expectPrintlnToBeCalledNTimes(writer, 1);
      expectPrintlnToReceive(writer, new String[] {"Some string"});
    }

    @Test
    public void itWritesMultipleTimesToTheConnectionsOutputStream() {
      Socket socket = new MockSocket.Builder().build();
      Reader reader = new MockReader();
      Writer writer = new MockWriter();

      allowReadLineToReturn(reader, new String[] {"Some string", "Some other string", null});

      Server server = new Server.Builder(out)
              .setSocket(socket)
              .setReader(reader)
              .setWriter(writer)
              .build();

      server.start(0);

      expectPrintlnToBeCalledNTimes(writer, 2);
      expectPrintlnToReceive(writer, new String[] {"Some string", "Some other string"});
    }
  }

  public static void allowReadLineToReturn(Reader reader, String[] returnValues) {
    ((MockReader) reader).readLineOutputs = returnValues;
  }

  public static void expectPrintlnToBeCalledNTimes(Writer writer, int times) {
    assertEquals(times, ((MockWriter) writer).printlnCallCount);
  }

  public static void expectPrintlnToReceive(Writer writer, String[] args) {
    Iterator<String> expectedArgs = new ArrayList<String>(Arrays.asList(args)).iterator();
    Iterator<String> actualArgs = ((MockWriter) writer).printlnArgs.iterator();

    compareExpectedWithActualArgs(expectedArgs, actualArgs);
  }

  public static void compareExpectedWithActualArgs(Iterator<String> expectedArgs, Iterator<String> actualArgs) {
    while(expectedArgs.hasNext()) {
      String expectedArg = null;
      try {
        assertEquals(expectedArg = expectedArgs.next(), actualArgs.next());
      } catch (NoSuchElementException e) {
        String message = String.format("Expected println(String s) to receive \"%s\"", expectedArg);
        throw new AssertionFailedError(message);
      }
    }
  }
}
