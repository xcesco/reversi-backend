package org.abubusoft.reversi.server.exceptions;

public class TimeoutException extends AppRuntimeException {
  public TimeoutException(String message) {
    super(message);
  }
}
