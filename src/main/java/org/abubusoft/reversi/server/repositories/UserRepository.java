package org.abubusoft.reversi.server.repositories;

import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.model.UserStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
  List<User> findByStatus(UserStatus status);

  Optional<Object> findById(User user);
}