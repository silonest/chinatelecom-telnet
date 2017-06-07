package cn.com.chinatelecom.telnet;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.text.MessageFormat;

import cn.com.chinatelecom.exception.IllegalTelnetConnectionException;
import cn.com.chinatelecom.exception.NeConnectionException;
import cn.com.chinatelecom.exception.NeReceiverInvalidException;
import cn.com.chinatelecom.telnet.command.NeCommand;
import cn.com.chinatelecom.telnet.reply.NeReply;
import cn.com.chinatelecom.telnet.util.OperatingSystem;
import cn.com.chinatelecom.telnet.util.OperatingSystem.OsType;

public class NeConnection {
  private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
  private static final int DEFAULT_TIMEOUT = 1000;
  private static final OsType DEFAULT_OSTYPE = OsType.Windows;
  private static final String DEFAULT_TERMTYPE = "vt200";
  private OsType osType;
  private String termType;
  private Charset charset;
  private int timeout = -1;
  private NeReceiver receiver;
  private TelnetClient client;
  private PrintStream write;
  private InputStream read;

  public NeConnection(NeReceiver receiver) {
    this.receiver = receiver;
    boolean flag = this.connect();
    if (!flag) {
      throw new NeConnectionException("Failed connect.");
    }
  }

  public NeConnection charset(String charset) {
    try {
      this.charset = Charset.forName(charset.toLowerCase().trim());
    } catch (IllegalCharsetNameException ex) {
      throw new NeReceiverInvalidException("Transmission charset is wrong.");
    } catch (IllegalArgumentException ex) {
      throw new NeReceiverInvalidException("Transmission charset is wrong.");
    }
    if (this.charset == null) {
      throw new NeReceiverInvalidException("Transmission charset is wrong.");
    } else {
      return this;
    }
  }

  public NeConnection charset(Charset charset) {
    this.charset = charset;
    return this;
  }

  public NeConnection os(OsType osType) {
    this.osType = osType;
    return this;
  }

  public NeConnection os(String osName) {
    try {
      this.osType = OperatingSystem.format(osName);
    } catch (IllegalArgumentException ex) {
      throw new NeReceiverInvalidException("Illegal Opreating System name.");
    }
    return this;
  }

  public NeConnection timeout(int timeout) {
    this.timeout = timeout;
    return this;
  }

  public int getTimeout() {
    if (this.timeout == -1) {
      return DEFAULT_TIMEOUT;
    } else {
      return this.timeout;
    }
  }

  public Charset getCharset() {
    if (this.charset == null) {
      return UTF8_CHARSET;
    } else {
      return this.charset;
    }
  }

  public OsType getOsType() {
    if (this.osType == null) {
      return DEFAULT_OSTYPE;
    } else {
      return this.osType;
    }
  }

  public String getTermType() {
    if (this.termType == null) {
      return DEFAULT_TERMTYPE;
    } else {
      return this.termType;
    }
  }

  private boolean connect() {
    this.client = new TelnetClient(this.getTermType());
    this.client.setCharset(this.getCharset());
    this.client.setDefaultTimeout(this.getTimeout());
    this.client.setDefaultPort(this.receiver.getPort());
    try {
      // 初始化链接，并取得输入输出流
      this.client.connect(this.receiver.getIp());
      if (this.client.isConnected()) {
        try {
          this.client.setKeepAlive(true);
        } catch (SocketException ex) {
          throw new IllegalTelnetConnectionException("Socket is disconnection.");
        }
        this.write = new PrintStream(this.client.getOutputStream());
        this.read = this.client.getInputStream();
        // 登录网元
        String command = MessageFormat.format("LGI:OP=\"{0}\", PWD=\"{1}\";", "", "");
        // this.write(command);
        // String response =
        // this.read("--- END" + (this.getOsType() == OsType.Linux ? "/n" : "/n/r"));
        String response = this.read("C:\\Users\\Administrator>");
        if (response.indexOf("success") >= 0) {
          return true;
        } else {
          this.disconnect();
          return false;
        }
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw new NeConnectionException("Connect telnet faild.");
    }
  }

  private void disconnect() {
    if (this.client.isAvailable() && this.client.isConnected()) {
      try {
        this.client.disconnect();
      } catch (IOException ex) {
        throw new IllegalTelnetConnectionException("Disconnect telnet faild.");
      }
    }
  }

  public boolean write(String str) {
    try {
      this.write.print(str.getBytes(this.getCharset()));
      this.write.flush();
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public String read(String pattern) {
    try {
      char lastChar = pattern.charAt(pattern.length() - 1);
      StringBuffer sb = new StringBuffer();
      char ch = (char) this.read.read();
      while (true) {
        // System.out.print(ch);// ---需要注释掉
        sb.append(ch);
        if (ch == lastChar) {
          if (sb.toString().endsWith(pattern)) {
            // 处理编码，界面显示乱码问题
            byte[] temp = sb.toString().getBytes(this.getCharset());
            return new String(temp);
          }
        }
        ch = (char) this.read.read();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public NeReply sendCommand(NeCommand command) {
    this.write(command.content());
    String response = this.read(command.terminator());
    NeReply result = new NeReply();
    result.setMessage(response);
    return result;
  }

  public String sendCommand(String command, String terminator) {
    this.write(command);
    String response = this.read(terminator);
    return response;
  }

  public String sendCommand(String command) {
    // return this.sendCommand(command,
    // "--- END" + (this.getOsType() == OsType.Linux ? "/n" : "/n/r"));
    return this.sendCommand(command, "C:\\Users\\Administrator>");
  }
}
