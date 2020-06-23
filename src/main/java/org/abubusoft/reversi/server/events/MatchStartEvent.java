package org.abubusoft.reversi.server.events;

import java.util.UUID;

public class MatchStartEvent extends AbstractMatchEvent {

  public MatchStartEvent(UUID matchUUID, UUID player1UUID, UUID player2UUID) {
    super(matchUUID, player1UUID, player2UUID);
  }

}
