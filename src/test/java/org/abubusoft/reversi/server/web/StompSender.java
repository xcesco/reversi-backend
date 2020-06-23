package org.abubusoft.reversi.server.web;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;

import java.util.UUID;

import static org.abubusoft.reversi.server.WebSocketConfig.TOPIC_PREFIX;

public interface StompSender {
  void sendMatchMove(UUID playerId, Piece piece, UUID matchId, Coordinates move);

  static String buildUserTopic(UUID userId) {
    return TOPIC_PREFIX + "/user/" + userId;
  }
}
