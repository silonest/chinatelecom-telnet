package cn.com.chinatelecom.telnet.util;

public class OperatingSystem {

  public static OsType format(String osName) {
    osName = osName.trim().toLowerCase();
    if ("windwos".equals(osName)) {
      return OsType.Windows;
    } else if ("linux".equals(osName) || "centos".equals(osName) || "ubuntu".equals(osName)) {
      return OsType.Linux;
    } else {
      throw new IllegalArgumentException("Illegal Operating System name.");
    }
  }

  public enum OsType {
    Windows, Linux
  }
}
