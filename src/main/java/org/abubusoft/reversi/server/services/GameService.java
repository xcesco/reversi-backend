package org.abubusoft.reversi.server.services;

import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.model.UserRegistration;

import java.util.UUID;

public interface GameService {
  Iterable<MatchStatus> findAllUMatchStatus();

  User saveUser(UserRegistration userRegistration);

  Iterable<User> findAllUsers();

  MatchStatus findMatchByUserId(UUID userId);

  User readyToPlay(UUID userUUID);

  User stopPlaying(UUID userUUID);
}
