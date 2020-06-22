package org.abubusoft.reversi.server.model;

import it.fmt.games.reversi.model.GameSnapshot;
import org.abubusoft.reversi.server.repositories.support.JpaConverterJson;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class MatchStatus extends AbstractBaseEntity {
  @Lob
  @Convert(converter = JpaConverterJson.class)
  private GameSnapshot snapshot;

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

  public GameSnapshot getSnapshot() {
    return snapshot;
  }

  public void setSnapshot(GameSnapshot snapshot) {
    this.snapshot = snapshot;
  }
}
