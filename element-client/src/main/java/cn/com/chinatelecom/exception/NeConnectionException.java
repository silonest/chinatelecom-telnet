package cn.com.chinatelecom.exception;

/**
 * 链接创建失败则抛出该异常.
 * 
 * @author 陈晨
 * @time 2017年6月7日 下午1:55:13
 * @since v1.0.0
 * @version v0.0.1
 */
public class NeConnectionException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NeConnectionException() {};

  public NeConnectionException(String message) {
    super(message);
  }

  public NeConnectionException(Throwable ex) {
    super(ex);
  }
}
