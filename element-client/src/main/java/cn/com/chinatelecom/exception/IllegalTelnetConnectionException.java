package cn.com.chinatelecom.exception;

public class IllegalTelnetConnectionException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public IllegalTelnetConnectionException() {};

  public IllegalTelnetConnectionException(String message) {
    super(message);
  }

  public IllegalTelnetConnectionException(Throwable ex) {
    super(ex);
  }
}
