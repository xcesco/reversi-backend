package org.abubusoft.reversi.server.events;

import org.abubusoft.reversi.messages.MatchMoveMessage;

public class MatchMoveEvent extends AppEvent {

  public MatchMoveEvent(MatchMoveMessage move) {
    super(move);
  }

  public MatchMoveMessage getMove() {
    return (MatchMoveMessage) source;
  }

}
