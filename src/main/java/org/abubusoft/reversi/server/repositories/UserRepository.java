package org.abubusoft.reversi.server.repositories;

import org.abubusoft.reversi.server.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {}