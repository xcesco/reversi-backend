package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.DecisionHandler;
import it.fmt.games.reversi.Player1;

import java.util.UUID;

public class NetworkPlayer1 extends Player1 implements PlayerWithId {
  private final UUID userId;

  public NetworkPlayer1(UUID userId) {
    this.userId = userId;
  }

  public NetworkPlayer1(DecisionHandler decisionHandler) {
    super(decisionHandler);
    userId = null;
  }

  @Override
  public UUID getUserId() {
    return userId;
  }
}
