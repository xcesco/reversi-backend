package org.abubusoft.reversi.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.fmt.games.reversi.model.Piece;
import org.abubusoft.reversi.server.model.UserStatus;

import java.util.UUID;

public class ConnectedUser {
  private final String name;
  private final UserStatus status;
  private final Piece piece;
  private final UUID id;

  @JsonCreator
  public ConnectedUser(@JsonProperty("id") UUID id, @JsonProperty("name") String name, @JsonProperty("status") UserStatus status, @JsonProperty("piece") Piece piece) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.piece = piece;
  }

  public UserStatus getStatus() {
    return status;
  }

  public Piece getPiece() {
    return piece;
  }

  public String getName() {
    return name;
  }

  public UUID getId() {
    return id;
  }
}
