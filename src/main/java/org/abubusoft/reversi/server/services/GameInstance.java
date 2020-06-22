package org.abubusoft.reversi.server.services;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Player;

public interface GameInstance {
  void updateStatus(Player player, Coordinates coordinates);

  void play();
}
