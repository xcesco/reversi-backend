package org.abubusoft.reversi.server.messages;

import java.io.Serializable;

public class Hello implements Serializable {
  public Hello() {
  }

  public static Hello of(String message) {
    Hello result = new Hello();
    result.setMessage(message);
    return result;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  private String message;
}
