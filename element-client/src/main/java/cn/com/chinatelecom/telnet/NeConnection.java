package cn.com.chinatelecom.telnet;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import cn.com.chinatelecom.exception.IllegalTelnetConnectionException;
import cn.com.chinatelecom.exception.NeReceiverInvalidException;
import cn.com.chinatelecom.telnet.command.NeCommand;
import cn.com.chinatelecom.telnet.reply.NeReply;
import cn.com.chinatelecom.telnet.util.OperatingSystem;
import cn.com.chinatelecom.telnet.util.OperatingSystem.OsType;

public class NeConnection {
  private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
  private static final int DEFAULT_TIMEOUT = 500;
  private static final OsType DEFAULT_OSTYPE = OsType.Windows;
  private static final String DEFAULT_TERMTYPE = "vt200";
  private OsType osType;
  private String termType;
  private Charset charset;
  private int timeout = -1;
  private NeReceiver receiver;
  private TelnetClient client;


  public NeConnection(NeReceiver receiver) {
    this.receiver = receiver;
    this.connect();
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
    try {
      this.client.setKeepAlive(true);
    } catch (SocketException ex) {
      throw new IllegalTelnetConnectionException("Socket is disconnection.");
    }
    this.client.setDefaultPort(receiver.getUri().getPort());
    try {
      this.client.connect(receiver.getUri().getPath());
    } catch (SocketException ex) {
      throw new IllegalTelnetConnectionException("Illegal address.");
    } catch (IOException ex) {
      this.disconnect();
      throw new IllegalTelnetConnectionException("No response or failed response.");
    }
    return true;
  }

  private void disconnect() {
    if (this.client.isAvailable() && this.client.isConnected()) {
      try {
        this.client.disconnect();
      } catch (IOException iex) {
        throw new IllegalTelnetConnectionException("Disconnect telnet faild.");
      }
    }
  }

  public NeReply sendCommand(NeCommand... commands) {
    return null;
  }

  public String sendCommand(String commands) {
    return null;
  }
}
