package org.abubusoft.reversi.server.events;

import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Score;

import java.util.UUID;

public class MatchEndEvent extends AbstractMatchEvent {

  private final GameStatus status;
  private final Score score;

  public MatchEndEvent(UUID matchUUID, UUID player1UUID, UUID player2UUID, GameSnapshot gameSnapshot) {
    super(matchUUID, player1UUID, player2UUID);
    this.status=gameSnapshot.getStatus();
    this.score=gameSnapshot.getScore();
  }

  public GameStatus getStatus() {
    return status;
  }

  public Score getScore() {
    return score;
  }
}