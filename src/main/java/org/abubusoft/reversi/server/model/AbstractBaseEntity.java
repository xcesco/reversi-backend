package org.abubusoft.reversi.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractBaseEntity implements Persistable<UUID> {

  @Id
  @Column(name = "id", length = 16, unique = true, nullable = false)
  private final UUID id;

  @JsonIgnore
  @Transient
  private boolean persisted;

  public AbstractBaseEntity(UUID id) {
    this.id = id != null ? id : UUID.randomUUID();
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbstractBaseEntity that = (AbstractBaseEntity) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public boolean isNew() {
    return !persisted;
  }

  @PostPersist
  @PostLoad
  private void setPersisted() {
    persisted = true;
  }
}