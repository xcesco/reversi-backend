package org.abubusoft.reversi.server.events;

import java.util.UUID;

public class MatchStartEvent extends AbstractMatchEvent {
  private final String player1Name;
  private final String player2Name;

  public MatchStartEvent(UUID matchUUID, UUID player1UUID, String player1Name, UUID player2UUID, String player2Name) {
    super(matchUUID, player1UUID, player2UUID);
    this.player1Name = player1Name;
    this.player2Name = player2Name;
  }

  public String getPlayer1Name() {
    return player1Name;
  }

  public String getPlayer2Name() {
    return player2Name;
  }
}
