package org.abubusoft.reversi.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.fmt.games.reversi.model.GameSnapshot;
import org.abubusoft.reversi.server.repositories.support.GameSnaphostJpaConverterJson;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class MatchStatus extends AbstractBaseEntity {
  @Lob
  @Convert(converter = GameSnaphostJpaConverterJson.class)
  private GameSnapshot snapshot;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MatchStatus that = (MatchStatus) o;
    return Objects.equals(snapshot, that.snapshot) &&
            Objects.equals(users, that.users);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), snapshot, users);
  }

  @JsonIgnore
  @JoinColumn(name = "match_id")
  @OneToMany
  private List<User> users = new ArrayList<>();

  public MatchStatus(UUID id) {
    super(id);
  }

  public MatchStatus() {
    super(null);
  }

  public static MatchStatus of(UUID matchUUID, GameSnapshot snapshot) {
    MatchStatus matchStatus = new MatchStatus(matchUUID);
    matchStatus.snapshot = snapshot;

    return matchStatus;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public GameSnapshot getSnapshot() {
    return snapshot;
  }

  public void setSnapshot(GameSnapshot snapshot) {
    this.snapshot = snapshot;
  }
}
