package org.abubusoft.reversi.server.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.Piece;

import java.io.Serializable;
import java.util.UUID;

public class MatchStartMessage implements Serializable {
  private final UUID matchUUID;
  private final Piece piece;

  @JsonCreator
  public MatchStartMessage(@JsonProperty("matchUUID") UUID matchUUID, @JsonProperty("piece") Piece piece) {
    this.matchUUID = matchUUID;
    this.piece = piece;
  }

  public UUID getMatchUUID() {
    return matchUUID;
  }

  public Piece getPiece() {
    return piece;
  }
}
