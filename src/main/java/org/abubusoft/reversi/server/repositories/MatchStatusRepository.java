package org.abubusoft.reversi.server.repositories;

import org.abubusoft.reversi.server.model.MatchStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchStatusRepository extends CrudRepository<MatchStatus, UUID> {

}