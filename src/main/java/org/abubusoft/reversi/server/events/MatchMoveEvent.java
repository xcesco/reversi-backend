package org.abubusoft.reversi.server.events;

import org.abubusoft.reversi.messages.MatchMove;

public class MatchMoveEvent extends AppEvent {

  public MatchMoveEvent(MatchMove move) {
    super(move);
  }

  public MatchMove getMove() {
    return (MatchMove) source;
  }

}
