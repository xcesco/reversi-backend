package org.abubusoft.reversi.server.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

public class MatchMoveMessage {

  @JsonCreator
  public MatchMoveMessage(@JsonProperty("matchUUID") UUID matchUUID,
                          @JsonProperty("playerUUID") UUID playerUUID,
                          @JsonProperty("playerPiece") Piece playerPiece,
                          @JsonProperty("move") Coordinates move) {
    this.playerUUID = playerUUID;
    this.playerPiece = playerPiece;
    this.move = move;
    this.matchUUID = matchUUID;
  }

  private final UUID playerUUID;

  public UUID getPlayerUUID() {
    return playerUUID;
  }

  public Piece getPlayerPiece() {
    return playerPiece;
  }

  public Coordinates getMove() {
    return move;
  }

  public UUID getMatchUUID() {
    return matchUUID;
  }

  private final Piece playerPiece;
  private final Coordinates move;
  private final UUID matchUUID;

  public static MatchMoveMessage of(UUID matchUUID, UUID playerUUID, Piece playerPiece, Coordinates move) {
    return new MatchMoveMessage(matchUUID, playerUUID, playerPiece, move);
  }
}
