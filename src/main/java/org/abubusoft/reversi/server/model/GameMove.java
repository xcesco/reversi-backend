package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Player;

public class GameMove {
  public Player getPlayer() {
    return player;
  }

  public Coordinates getMove() {
    return move;
  }

  private final Player player;
  private final Coordinates move;

  public GameMove(Player player, Coordinates move) {
    this.player = player;
    this.move = move;
  }

  public static GameMove of(Player player, Coordinates move) {
    return new GameMove(player, move);
  }
}
