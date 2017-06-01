package cn.com.chinatelecom.telnet.reply;

public class NeReply {
  private ReplyStatus status;
  private String message;

  public ReplyStatus getStatus() {
    return status;
  }

  public void setStatus(ReplyStatus status) {
    this.status = status;
  }

  public boolean isSuccess() {
    if (this.status == ReplyStatus.SUCCESS) {
      return true;
    } else {
      return false;
    }
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static enum ReplyStatus {
    SUCCESS, ERROR, INVALID
  }
}
