package org.abubusoft.reversi.server.repositories;

import org.abubusoft.reversi.server.model.GameStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<GameStatus, String> {}