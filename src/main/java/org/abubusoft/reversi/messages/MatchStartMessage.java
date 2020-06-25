package org.abubusoft.reversi.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchStartMessage extends MatchMessage {
  private final Piece piece;

  @JsonCreator
  public MatchStartMessage(@JsonProperty("matchId") UUID matchId, @JsonProperty("piece") Piece piece) {
    super(matchId, MatchMessageType.MATCH_START);
    this.piece = piece;
  }

  public Piece getPiece() {
    return piece;
  }

  @Override
  public void accept(MatchMessageVisitor visitor) {
    visitor.visit(this);
  }
}
