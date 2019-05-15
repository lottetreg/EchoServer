package io.github.lottetreg.echo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

class MockThread extends Thread {
  public int startCallCount;

  MockThread() {
    this.startCallCount = 0;
  }

  public void start() {
    this.startCallCount++;
  }
}

public class ThreadRunnerTest {
  Thread thread;

  @Test
  public void itCallsStartOnTheThread() {
    thread = new MockThread();

    new ThreadRunner().run(thread);

    expectStartToBeCalledOnce();
  }

  public void expectStartToBeCalledOnce() {
    assertEquals(1, ((MockThread) thread).startCallCount);
  }
}
