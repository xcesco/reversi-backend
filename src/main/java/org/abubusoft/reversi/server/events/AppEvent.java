package org.abubusoft.reversi.server.events;

import org.springframework.context.ApplicationEvent;

public abstract class AppEvent extends ApplicationEvent {
  public AppEvent(Object source) {
    super(source);
  }
}
