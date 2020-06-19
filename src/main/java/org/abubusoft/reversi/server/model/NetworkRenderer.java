package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.model.GameSnapshot;

public class NetworkRenderer implements GameRenderer {
  @Override
  public void render(GameSnapshot gameSnapshot) {
      // invia al network lo stato (ad uno o a tutti e due, se gioco terminato
  }
}
