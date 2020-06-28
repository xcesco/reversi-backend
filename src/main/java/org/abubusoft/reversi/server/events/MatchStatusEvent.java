package org.abubusoft.reversi.server.events;

import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.NetworkPlayer1;
import org.abubusoft.reversi.server.model.NetworkPlayer2;

import java.util.UUID;

public class MatchStatusEvent extends AppEvent {

  private final UUID player1Id;
  private final UUID player2Id;
  private final MatchStatus status;

  public MatchStatusEvent(UUID player1Id, UUID player2Id, MatchStatus status) {
    super(status.getId());
    this.player1Id = player1Id;
    this.player2Id = player2Id;
    this.status = status;
  }

  public UUID getPlayer1Id() {
    return player1Id;
  }

  public UUID getPlayer2Id() {
    return player2Id;
  }

  public MatchStatus getMatchStatus() {
    return status;
  }
}
