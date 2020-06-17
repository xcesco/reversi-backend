package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.model.GameSnapshot;
import org.abubusoft.reversi.server.repositories.support.JpaConverterJson;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class GameStatus extends AbstractBaseEntity {
  private String player1;
  private String player2;

  @Lob
  @Convert(converter = JpaConverterJson.class)
  private GameSnapshot snapshot;

  public GameStatus() {
    super(null);
  }

  public static GameStatus of(String player1, String player2) {
    GameStatus gameStatus = new GameStatus();
    gameStatus.player1 = player1;
    gameStatus.player2 = player2;

    return gameStatus;
  }

  public String getPlayer1() {
    return player1;
  }

  public void setPlayer1(String player1) {
    this.player1 = player1;
  }

  public String getPlayer2() {
    return player2;
  }

  public void setPlayer2(String player2) {
    this.player2 = player2;
  }

  public GameSnapshot getSnapshot() {
    return snapshot;
  }

  public void setSnapshot(GameSnapshot snapshot) {
    this.snapshot = snapshot;
  }
}
