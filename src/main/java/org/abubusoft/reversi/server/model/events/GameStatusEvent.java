package org.abubusoft.reversi.server.model.events;

import it.fmt.games.reversi.model.GameSnapshot;

import java.util.UUID;

public class GameStatusEvent extends GameEvent {
  public UUID getGameUUID() {
    return gameUUID;
  }

  public GameSnapshot getGameSnapshot() {
    return (GameSnapshot) this.getSource();
  }

  private final UUID gameUUID;

  public GameStatusEvent(UUID gameUUID, GameSnapshot source) {
    super(source);
    this.gameUUID = gameUUID;
  }

  public static GameStatusEvent of(UUID gameUUID, GameSnapshot gameSnapshot) {
    return new GameStatusEvent(gameUUID, gameSnapshot);
  }
}
