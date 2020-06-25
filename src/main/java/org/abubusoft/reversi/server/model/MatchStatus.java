package org.abubusoft.reversi.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import it.fmt.games.reversi.model.GameSnapshot;
import org.abubusoft.reversi.server.repositories.support.GameSnaphostJpaConverterJson;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class MatchStatus extends AbstractBaseEntity {
  @Lob
  @Convert(converter = GameSnaphostJpaConverterJson.class)
  private GameSnapshot snapshot;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime finishDateTime;

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

  public LocalDateTime getFinishDateTime() {
    return finishDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MatchStatus that = (MatchStatus) o;
    return Objects.equals(snapshot, that.snapshot) &&
            Objects.equals(finishDateTime, that.finishDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), snapshot, finishDateTime);
  }

  public void setFinishDateTime(LocalDateTime finishDateTime) {
    this.finishDateTime = finishDateTime;
  }

  public GameSnapshot getSnapshot() {
    return snapshot;
  }

  public void setSnapshot(GameSnapshot snapshot) {
    this.snapshot = snapshot;
  }
}
