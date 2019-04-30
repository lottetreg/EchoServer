package io.github.lottetreg.echo;

import org.junit.Assert;
import org.junit.Test;

public class ArgumentParserTest {

  @Test
  public void testExtractsPortFromArgsArray() {
    ArgumentParser parser = new ArgumentParser();
    String[] args = {"8000"};
    int port = parser.parse(args);
    Assert.assertEquals(port, 8000);
  }

}
