package org.abubusoft.reversi.server.events;

import org.springframework.context.ApplicationEvent;

public abstract class GameEvent extends ApplicationEvent {
  public GameEvent(Object source) {
    super(source);
  }
}
