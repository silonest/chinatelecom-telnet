package cn.com.chinatelecom.telnet.test;

import org.junit.Test;

import static cn.com.chinatelecom.telnet.NeTelnetClient.connection;
import static cn.com.chinatelecom.telnet.ReceiverBuilder.receiver;

public class NeConnectionTest {
  @Test
  public void testSendCommand() {
    String result =
        connection(receiver("172.17.11.18", "administrator", "ceshi")).sendCommand("abc");
    System.out.println(result);
  }
}
