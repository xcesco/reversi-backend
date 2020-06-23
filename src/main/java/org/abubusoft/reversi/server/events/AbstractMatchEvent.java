package org.abubusoft.reversi.server.events;

import java.util.UUID;

public abstract class AbstractMatchEvent extends AppEvent {
  private final UUID player1UUID;
  private final UUID player2UUID;

  public AbstractMatchEvent(UUID matchUUID, UUID player1UUID, UUID player2UUID) {
    super(matchUUID);
    this.player1UUID = player1UUID;
    this.player2UUID = player2UUID;
  }

  public UUID getPlayer1UUID() {
    return player1UUID;
  }

  public UUID getPlayer2UUID() {
    return player2UUID;
  }

  public UUID getMatchUUID() {
    return (UUID) getSource();
  }
}
