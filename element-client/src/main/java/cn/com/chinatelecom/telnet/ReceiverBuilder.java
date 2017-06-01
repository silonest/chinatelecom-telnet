package cn.com.chinatelecom.telnet;

import java.net.URI;
import java.net.URISyntaxException;

import cn.com.chinatelecom.exception.NeReceiverInvalidException;

public class ReceiverBuilder {
  private static final int DEFAULT_PORT = 23;


  public static NeReceiver receiver(String uri, String username, String password) {
    try {
      NeReceiver receiver = new NeReceiver(new URI(uri), DEFAULT_PORT, username, password);
      return receiver;
    } catch (URISyntaxException ex) {
      throw new NeReceiverInvalidException("Receiver address is wrong.");
    }
  }

  public static NeReceiver receiver(URI uri, String username, String password) {
    NeReceiver receiver = new NeReceiver(uri, DEFAULT_PORT, username, password);
    return receiver;
  }

  public static NeReceiver receiver(String uri, int port, String username, String password) {
    try {
      NeReceiver receiver = new NeReceiver(new URI(uri), port, username, password);
      return receiver;
    } catch (URISyntaxException ex) {
      throw new NeReceiverInvalidException("Receiver address is wrong.");
    }
  }

  public static NeReceiver receiver(URI uri, int port, String username, String password) {
    NeReceiver receiver = new NeReceiver(uri, port, username, password);
    return receiver;
  }
}
