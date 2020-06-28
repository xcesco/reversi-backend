package org.abubusoft.reversi.server.services;

import org.abubusoft.reversi.messages.MatchMessage;

import java.util.UUID;

public interface WebSocketSender {

  <E extends MatchMessage> void sendMessage(UUID userUUID, E message);
}
