package org.abubusoft.reversi.server.exceptions;

import it.fmt.games.reversi.model.Player;

public class AppPlayerTimeoutException extends AppRuntimeException {
  public Player getPlayer() {
    return player;
  }

  private final Player player;

  public AppPlayerTimeoutException(Player player, String message) {
    super(message);
    this.player = player;
  }
}
