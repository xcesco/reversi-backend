package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.Player2;

import java.util.UUID;

public interface GameService {
  GameInstance createMatch(Player1 player1, Player2 player2);

  GameInstance findMatchById(UUID gameUUID);

  void registryUser(String uuid);
}
