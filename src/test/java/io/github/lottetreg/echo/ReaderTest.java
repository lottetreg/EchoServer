package io.github.lottetreg.echo;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;

public class ReaderTest {
  @Test
  public void testSetConnection() {
    Connection connection = new Connection.Builder().build();
    Reader reader = new Reader();
    reader.setConnection(connection);

    assertEquals(reader.connection, connection);
  }

  @Test
  public void testReadLine() {
    byte[] byteArray = "Some string".getBytes();
    InputStream inputStream = new ByteArrayInputStream(byteArray);
    Connection connection = new MockConnection.Builder()
            .setInputStream(inputStream)
            .build();
    Reader reader = new Reader();
    reader.setConnection(connection);

    assertEquals(reader.readLine(), "Some string");
  }

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itThrowsAnExceptionIfItCannotReadFromBufferedReader() {
    class UnreadableBufferedReader extends BufferedReader {
      UnreadableBufferedReader(java.io.Reader in) { super(in); }

      public String readLine() throws IOException {
        throw new IOException();
      }
    }

    class UnreadableReader extends Reader {
      public BufferedReader newBufferedReader() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        return new UnreadableBufferedReader(new InputStreamReader(inputStream));
      }
    }

    Reader reader = new UnreadableReader();

    exceptionRule.expect(Reader.FailedToReadLineException.class);
    exceptionRule.expectMessage("Failed to read from the buffered reader");

    reader.readLine();
  }

  @Test
  public void itRethrowsFailedToGetInputStreamExceptions() {
    class FailedInputStreamConnection extends Connection {
      FailedInputStreamConnection(Builder builder) { super(builder); }

      public InputStream getInputStream() {
        throw new Connection.FailedToGetInputStreamException(new Throwable());
      }
    }

    Connection failedInputStreamConnection = new FailedInputStreamConnection.Builder().build();
    Reader reader = new Reader();
    reader.setConnection(failedInputStreamConnection);

    exceptionRule.expect(Connection.FailedToGetInputStreamException.class);
    exceptionRule.expectMessage("Failed to get the socket's input stream");

    reader.readLine();
  }
}
