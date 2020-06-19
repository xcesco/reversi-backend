package org.abubusoft.reversi.server.model.events;

import org.springframework.context.ApplicationEvent;

public abstract class GameEvent extends ApplicationEvent {
  public GameEvent(Object source) {
    super(source);
  }
}
