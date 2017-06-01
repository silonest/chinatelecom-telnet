package cn.com.chinatelecom.telnet;

public class NeTelnetClient {
  public static NeConnection connection(NeReceiver receiver) {
    return new NeConnection(receiver);
  }
}
