package org.abubusoft.reversi.server.events;

import java.util.UUID;

public class MatchEndEvent extends AbstractMatchEvent {

  public MatchEndEvent(UUID matchUUID, UUID player1UUID, UUID player2UUID) {
    super(matchUUID, player1UUID, player2UUID);
  }

}