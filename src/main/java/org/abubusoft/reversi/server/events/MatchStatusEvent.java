package org.abubusoft.reversi.server.events;

import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.NetworkPlayer1;
import org.abubusoft.reversi.server.model.NetworkPlayer2;

public class MatchStatusEvent extends GameEvent {

  private final NetworkPlayer1 player1;
  private final NetworkPlayer2 player2;
  private final MatchStatus status;

  public MatchStatusEvent(NetworkPlayer1 player1, NetworkPlayer2 player2, MatchStatus status) {
    super(status.getId());
    this.player1 = player1;
    this.player2 = player2;
    this.status = status;
  }

  public NetworkPlayer1 getPlayer1() {
    return player1;
  }

  public NetworkPlayer2 getPlayer2() {
    return player2;
  }

  public MatchStatus getMatchStatus() {
    return status;
  }
}
