package cn.com.chinatelecom.telnet;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.text.MessageFormat;

import cn.com.chinatelecom.exception.IllegalTelnetConnectionException;
import cn.com.chinatelecom.exception.NeConnectionException;
import cn.com.chinatelecom.exception.NeReceiverInvalidException;
import cn.com.chinatelecom.telnet.util.InputStreamUtils;
import cn.com.chinatelecom.telnet.util.OperatingSystem;
import cn.com.chinatelecom.telnet.util.OperatingSystem.OsType;

public class NeConnection {
  private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
  private static final int DEFAULT_TIMEOUT = 10000;
  private static final OsType DEFAULT_OSTYPE = OsType.Windows;
  private static final String DEFAULT_TERMTYPE = "vt200";
  private OsType osType;
  private String termType;
  private Charset charset;
  private int timeout = -1;
  private NeReceiver receiver;
  private TelnetClient client;
  private OutputStream write;
  private InputStream read;


  public NeConnection(NeReceiver receiver) {
    this.receiver = receiver;
    boolean flag = this.connectTest();
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

  private boolean connectTest() {
    this.client = new TelnetClient(this.getTermType());
    this.client.setCharset(this.getCharset());
    // this.client.setConnectTimeout(DEFAULT_TIMEOUT);
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
        this.write = this.client.getOutputStream();
        this.read = this.client.getInputStream();
        String response = this.read("login:");
        System.out.println(response);
        this.write(this.receiver.getLogin());
        response = this.read(":");
        System.out.println(response);
        this.write(this.receiver.getPassword());
        response = this.read("C:\\Users\\Administrator>");
        System.out.println(response);
        if (response.indexOf("C:\\Users\\Administrator>") >= 0) {
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

  private boolean connect() {
    this.client = new TelnetClient(this.getTermType());
    this.client.setCharset(this.getCharset());
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
        this.write = this.client.getOutputStream();
        this.read = this.client.getInputStream();
        this.read("\r\n");
        String command = MessageFormat.format("LGI:OP=\"{0}\", PWD=\"{1}\";",
            this.receiver.getLogin(), this.receiver.getPassword());
        this.write(command);
        String response = this.read("\r\n");
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
      str = str + "\r\n";
      this.write.write(str.getBytes(this.getCharset()));
      this.write.flush();
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public String read(String pattern) {
    try {
      String result = InputStreamUtils.read(this.read, Charset.forName("GBK"), pattern);
      return result;
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    return null;
    // InputStreamReader isr = new InputStreamReader(this.read, Charset.forName("GBK"));
    // char[] charBytes = new char[1024];
    // int n = 0;
    // boolean flag = false;
    // String str = "";
    // try {
    // while ((n = isr.read(charBytes)) != -1) {
    // System.out.println(n);
    // for (int i = 0; i < n; i++) {
    // char c = (char) charBytes[i];
    // str += c;
    // if (str.endsWith(pattern)) {
    // flag = true;
    // break;
    // }
    // }
    // if (flag) {
    // break;
    // }
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return str;
  }

  public String sendCommand(String command, String terminator) {
    this.write(command);
    String response = this.read(terminator);
    this.disconnect();
    return response;
  }

  public String sendCommand(String command) {
    return this.sendCommand(command, "\r\n");
  }
}
