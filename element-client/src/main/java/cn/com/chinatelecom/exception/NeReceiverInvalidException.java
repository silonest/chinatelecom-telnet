package cn.com.chinatelecom.exception;

/**
 * 当无法生成接收者对象时会抛出此异常.
 * 
 * @author 陈晨
 * @time 2017年6月1日 上午11:22:36
 * @since v1.0.0
 * @version v0.0.1
 */
public class NeReceiverInvalidException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NeReceiverInvalidException() {};

  public NeReceiverInvalidException(String message) {
    super(message);
  }

  public NeReceiverInvalidException(Throwable ex) {
    super(ex);
  }
}
