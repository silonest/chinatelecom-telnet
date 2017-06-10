package cn.com.chinatelecom.telnet.test;

import static cn.com.chinatelecom.telnet.NeTelnetClient.connection;
import static cn.com.chinatelecom.telnet.ReceiverBuilder.receiver;

import org.junit.Test;

import cn.com.chinatelecom.telnet.NeConnection;

public class NeConnectionTest {
  @Test
  public void testSendCommand() {
    NeConnection connection =
        connection(receiver("123.56.91.88", "administrator", "HUIzhong3206207"));
    String result = connection.sendCommand("ipconfig", "C:\\Users\\Administrator>");
    System.out.println(result);
  }
}
