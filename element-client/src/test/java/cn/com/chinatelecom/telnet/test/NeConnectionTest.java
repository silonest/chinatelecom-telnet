package cn.com.chinatelecom.telnet.test;

import org.junit.Test;

import static cn.com.chinatelecom.telnet.NeTelnetClient.connection;
import static cn.com.chinatelecom.telnet.ReceiverBuilder.receiver;

public class NeConnectionTest {
  @Test
  public void testSendCommand() {
    String result = connection(receiver("123.56.91.88", "administrator", "HUIzhong3206207"))
        .sendCommand("ipconfig", "C:\\Users\\Administrator>");
    System.out.println(result);
  }
}
