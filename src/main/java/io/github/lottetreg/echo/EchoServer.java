package io.github.lottetreg.echo;

public class EchoServer {
  public static void main(String[] args) {
    int portNumber = new ArgumentParser().parse(args);
    Output out = new Output(System.out);
    new Server.Builder(out).build().start(portNumber);
  }
}
