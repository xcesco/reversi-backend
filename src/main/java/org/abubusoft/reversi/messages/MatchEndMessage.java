package org.abubusoft.reversi.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchEndMessage extends MatchMessage {
  private final Piece piece;

  @JsonCreator
  public MatchEndMessage(@JsonProperty("matchId") UUID matchId, @JsonProperty("piece") Piece piece) {
    super(matchId, MatchMessageType.MATCH_END);
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
