package cn.com.chinatelecom.telnet;

import cn.com.chinatelecom.exception.IllegalTelnetConnectionException;

public class NeReceiver {
  private String ip;
  private int port;
  private String login;
  private String password;


  public NeReceiver(String ip, int port, String login, String password) {
    this.ip = ip;
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

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

}
