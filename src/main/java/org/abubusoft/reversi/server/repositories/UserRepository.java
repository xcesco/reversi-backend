package org.abubusoft.reversi.server.repositories;

import org.abubusoft.reversi.server.model.GameUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<GameUser, Long> {}