package io.github.lottetreg.echo;

public class EchoServer {
  public static void main(String[] args) {
    try {
      Output out = new Output(System.out);
      int portNumber = new ArgumentParser().parse(args);
      new Server.Builder(out).build().start(portNumber);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }
}
