package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.DecisionHandler;
import it.fmt.games.reversi.Player1;

import java.util.UUID;

public class NetworkPlayer1 extends Player1 implements PlayerWithId {
  private final UUID userId;

  public String getName() {
    return name;
  }

  private final String name;

  public NetworkPlayer1(UUID userId, String name) {
    this.userId = userId;
    this.name = name;
  }

  public NetworkPlayer1(DecisionHandler decisionHandler, String name) {
    super(decisionHandler);
    this.name = name;
    userId = null;
  }

  @Override
  public UUID getUserId() {
    return userId;
  }
}
