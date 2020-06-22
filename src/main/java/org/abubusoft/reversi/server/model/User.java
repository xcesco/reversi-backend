package org.abubusoft.reversi.server.model;

import javax.persistence.*;

@Entity
public class User extends AbstractBaseEntity {
  private String name;

  @Enumerated(EnumType.STRING)
  private UserStatus status;
  @ManyToOne(fetch = FetchType.LAZY)
  private MatchStatus matchStatus;

  public User() {
    super(null);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public MatchStatus getMatchStatus() {
    return matchStatus;
  }

  public void setMatchStatus(MatchStatus matchStatus) {
    this.matchStatus = matchStatus;
  }

}