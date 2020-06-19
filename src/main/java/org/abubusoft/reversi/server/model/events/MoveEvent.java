package org.abubusoft.reversi.server.model.events;

import org.abubusoft.reversi.server.model.GameMove;

public class MoveEvent extends GameEvent {

  public MoveEvent(GameMove move) {
    super(move);
  }


}
