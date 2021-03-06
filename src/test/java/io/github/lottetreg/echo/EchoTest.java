package io.github.lottetreg.echo;

import junit.framework.AssertionFailedError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;

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

public class EchoTest {
  @Test
  public void itHasAReader() {
    Echo echo = new Echo.Builder().build();
    assertThat(echo.reader, instanceOf(Reader.class));
  }

  @Test
  public void itHasAWriter() {
    Echo echo = new Echo.Builder().build();
    assertThat(echo.writer, instanceOf(Writer.class));
  }

  @Test
  public void theReaderCanBeSetThroughTheBuilder() {
    Reader reader = new Reader();
    Echo echo = new Echo.Builder()
            .setReader(reader)
            .build();

    assertEquals(reader, echo.reader);
  }

  @Test
  public void theWriterCanBeSetThroughTheBuilder() {
    Writer writer = new Writer();
    Echo echo = new Echo.Builder()
            .setWriter(writer)
            .build();

    assertEquals(writer, echo.writer);
  }

  @Test
  public void setConnectionSetsTheConnectionOnTheReaderAndWriter() {
    Connection connection = new Connection.Builder().build();
    Reader reader = new Reader();
    Writer writer = new Writer();

    Echo echo = new Echo.Builder()
            .setReader(reader)
            .setWriter(writer)
            .build();

    echo.setConnection(connection);

    assertEquals(connection, echo.reader.connection);
    assertEquals(connection, echo.writer.connection);
  }

  @Test
  public void itWritesTheInputBackToTheConnectionsOutputStream() {
    Reader reader = new MockReader();
    Writer writer = new MockWriter();

    allowReadLineToReturn(reader, new String[] {"Some string", null});

    Echo echo = new Echo.Builder()
            .setReader(reader)
            .setWriter(writer)
            .build();

    echo.echo();

    expectPrintlnToBeCalledNTimes((MockWriter) writer, 1);
    expectPrintlnToReceive(writer, new String[] {"Some string"});
  }

  @Test
  public void itWritesMultipleTimesToTheConnectionsOutputStream() {
    Reader reader = new MockReader();
    Writer writer = new MockWriter();

    allowReadLineToReturn(reader, new String[] {"Some string", "Some other string", null});

    Echo echo = new Echo.Builder()
            .setReader(reader)
            .setWriter(writer)
            .build();

    echo.echo();

    expectPrintlnToBeCalledNTimes((MockWriter) writer, 2);
    expectPrintlnToReceive(writer, new String[] {"Some string", "Some other string"});
  }

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itNotifiesTheClientAndClosesTheReadersConnectionIfItCannotReadIn() {
    class UnreadableReader extends Reader {
      public String readLine() {
        throw new RuntimeException();
      }
    }

    Reader unreadableReader = new UnreadableReader();
    Writer writer = new MockWriter();

    Echo echo = new Echo.Builder()
            .setReader(unreadableReader)
            .setWriter(writer)
            .build();

    echo.setConnection(new Connection.Builder().build());

    try {
      echo.echo();
    } catch (Exception e) {
    } finally {
      expectPrintlnToReceive(writer, new String[] {"Could not read from this connection."});
      assertEquals(unreadableReader.connection.socket.isClosed(), true);
    }
  }

  @Test
  public void itRethrowsAnExceptionWhenReadInFails() {
    class UnreadableReader extends Reader {
      public String readLine() {
        throw new RuntimeException();
      }
    }

    Echo echo = new Echo.Builder()
            .setReader(new UnreadableReader())
            .build();

    exceptionRule.expect(RuntimeException.class);

    echo.echo();
  }

  @Test
  public void itClosesTheWritersConnectionIfItCannotPrintOut() {
    class UnprintableWriter extends Writer {
      public void println(String x) {
        throw new RuntimeException();
      }
    }

    Writer unprintableWriter = new UnprintableWriter();

    Echo echo = new Echo.Builder()
            .setWriter(unprintableWriter)
            .build();

    echo.setConnection(new Connection.Builder().build());

    try {
      echo.echo();
    } catch (Exception e) {
    } finally {
      assertEquals(unprintableWriter.connection.socket.isClosed(), true);
    }
  }

  @Test
  public void itRethrowsAnExceptionWhenPrintOutFails() {
    class UnprintableWriter extends Writer {
      public void println(String x) {
        throw new RuntimeException();
      }
    }

    Echo echo = new Echo.Builder()
            .setWriter(new UnprintableWriter())
            .build();

    exceptionRule.expect(RuntimeException.class);

    echo.echo();
  }

  public void allowReadLineToReturn(Reader reader, String[] returnValues) {
    ((MockReader) reader).readLineOutputs = returnValues;
  }

  public void expectPrintlnToBeCalledNTimes(Writer writer, int times) {
    assertEquals(times, ((MockWriter) writer).printlnCallCount);
  }

  public void expectPrintlnToReceive(Writer writer, String[] args) {
    Iterator<String> expectedArgs = new ArrayList<String>(Arrays.asList(args)).iterator();
    Iterator<String> actualArgs = ((MockWriter) writer).printlnArgs.iterator();

    compareExpectedWithActualArgs(expectedArgs, actualArgs);
  }

  public void compareExpectedWithActualArgs(Iterator<String> expectedArgs, Iterator<String> actualArgs) {
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
