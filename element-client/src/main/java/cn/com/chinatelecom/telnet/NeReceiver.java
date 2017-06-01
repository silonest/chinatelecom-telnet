package cn.com.chinatelecom.telnet;

import java.net.URI;

import cn.com.chinatelecom.exception.IllegalTelnetConnectionException;

public class NeReceiver {
  private URI uri;
  private int port;
  private String login;
  private String password;
  

  public NeReceiver(URI uri, int port, String login, String password) {
    this.uri = uri;
    this.port = port;
    this.login = login.trim();
    this.password = password.trim();
    if (this.login == null || "".equals(this.login)) {
      throw new IllegalTelnetConnectionException("Username can not be empty.");
    }
    if (this.password == null || "".equals(this.login)) {
      throw new IllegalTelnetConnectionException("Password can not be empty.");
    }
  }

  public String getLogin() {
    return this.login;
  }

  public String getPassword() {
    return this.password;
  }

  public URI getUri() {
    return this.uri;
  }

  public int getPort() {
    return this.port;
  }
}
