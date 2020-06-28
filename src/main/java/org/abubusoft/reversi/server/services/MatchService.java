package org.abubusoft.reversi.server.services;

import java.util.UUID;

public interface MatchService {
  UUID getId();

  void play();
}
