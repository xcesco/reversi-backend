package org.abubusoft.reversi.server.messages;

import java.io.Serializable;

public class Greeting implements Serializable {
  public static Greeting of(String ciao) {
    Greeting greeting = new Greeting();
    greeting.setMessage(ciao);
    return greeting;
  }

  public Greeting() {
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  private String message;

}
