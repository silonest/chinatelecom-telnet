package cn.com.chinatelecom.telnet;

public class ReceiverBuilder {
  private static final int DEFAULT_PORT = 23;


  public static NeReceiver receiver(String ip, String username, String password) {
    NeReceiver receiver = new NeReceiver(ip, DEFAULT_PORT, username, password);
    return receiver;
  }

  public static NeReceiver receiver(String ip, int port, String username, String password) {
    NeReceiver receiver = new NeReceiver(ip, port, username, password);
    return receiver;
  }

}
