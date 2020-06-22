package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.Player2;
import it.fmt.games.reversi.model.Piece;
import it.fmt.games.reversi.model.Player;

import java.util.UUID;

public class NetworkPlayer2 extends Player2 {
  private final UUID userId;

  public NetworkPlayer2(UUID userId) {
    this.userId=userId;
  }

  public UUID getUserId() {
    return userId;
  }
}
