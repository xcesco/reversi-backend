package org.abubusoft.reversi.server.model;

import javax.persistence.Entity;

@Entity
public class GameUser extends AbstractBaseEntity {
  private String name;
  private String email;
  private boolean gameInProgress;

  public GameUser() {
    super(null);
  }

  public boolean isGameInProgress() {
    return gameInProgress;
  }

  public void setGameInProgress(boolean gameInProgress) {
    this.gameInProgress = gameInProgress;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}