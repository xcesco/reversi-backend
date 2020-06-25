package org.abubusoft.reversi.messages;

import java.util.UUID;

public abstract class MatchMessage implements MatchMessageVisitable {
  private final UUID matchId;
  private final MatchMessageType messageType;

  public MatchMessage(UUID matchId, MatchMessageType messageType) {
    this.matchId = matchId;
    this.messageType = messageType;
  }

  public MatchMessageType getMessageType() {
    return messageType;
  }

  public UUID getMatchId() {
    return matchId;
  }

}
