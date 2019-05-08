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


class MockThreadRunner extends ThreadRunner {
  public int runCallCount;

  MockThreadRunner() {
    this.runCallCount = 0;
  }

  public void run(Thread thread) {
    this.runCallCount++;
  }
}

class MockSocket extends Socket {
  public Connection connection;
  public int acceptConnectionCallCount;
  public Connection[] acceptConnectionReturnValues;

  MockSocket(Builder builder) {
    super(builder);
    this.connection = new Connection.Builder().build();
    this.acceptConnectionCallCount = 0;
  }

  public Connection acceptConnection() {
    acceptConnectionCallCount++;
    return acceptConnectionReturnValues[acceptConnectionCallCount - 1];
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
    public void itHadADefaultThreadRunner() {
      Server server = new Server.Builder(out).build();

      assertThat(server.threadRunner, instanceOf(ThreadRunner.class));
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
    public void theThreadRunnerCanBeSetThroughTheBuilder() {
      ThreadRunner threadRunner = new ThreadRunner();

      Server server = new Server.Builder(out)
              .setThreadRunner(threadRunner)
              .build();

      assertEquals(threadRunner, server.threadRunner);
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
    private ThreadRunner threadRunner;

    @Before
    public void setUp() {
      socket = new MockSocket.Builder().build();
      threadRunner = new MockThreadRunner();

      server = new Server.Builder(out)
              .setSocket(socket)
              .setThreadRunner(threadRunner)
              .build();

      Connection socketConnection = ((MockSocket) socket).connection;
      allowAcceptConnectionToReturn(socket, new Connection[] {socketConnection, null});
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
  }

  public static class ThreadTests {
    @Test
    public void itStartsTheThread() {
      Socket socket = new MockSocket.Builder().build();
      ThreadRunner threadRunner = new MockThreadRunner();

      Server server = new Server.Builder(out)
              .setSocket(socket)
              .setThreadRunner(threadRunner)
              .build();

      Connection newConnection = new Connection.Builder().build();
      allowAcceptConnectionToReturn(socket, new Connection[] {newConnection, null});

      server.start(0);

      expectRunToBeCalledNTimes(threadRunner, 1);
    }

    @Test
    public void itStartsTheThreadEveryTimeAConnectionIsAccepted() {
      Socket socket = new MockSocket.Builder().build();
      ThreadRunner threadRunner = new MockThreadRunner();

      Server server = new Server.Builder(out)
              .setSocket(socket)
              .setThreadRunner(threadRunner)
              .build();

      Connection firstNewConnection = new Connection.Builder().build();
      Connection secondNewConnection = new Connection.Builder().build();
      allowAcceptConnectionToReturn(socket, new Connection[] {firstNewConnection, secondNewConnection, null});

      server.start(0);

      expectRunToBeCalledNTimes(threadRunner, 2);
    }
  }

  public static void allowAcceptConnectionToReturn(Socket socket, Connection[] returnValues) {
    ((MockSocket) socket).acceptConnectionReturnValues = returnValues;
  }

  public static void expectRunToBeCalledNTimes(ThreadRunner threadRunner, int times) {
    assertEquals(times, ((MockThreadRunner) threadRunner).runCallCount);
  }
}
