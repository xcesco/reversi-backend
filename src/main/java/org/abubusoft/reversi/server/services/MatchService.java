package org.abubusoft.reversi.server.services;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Player;

import java.util.UUID;

public interface MatchService {
  UUID getId();

  void updateStatus(Player player, Coordinates coordinates);

  void play();
}
