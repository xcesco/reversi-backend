package org.abubusoft.reversi.server.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameWebSocketHandler extends TextWebSocketHandler implements ApplicationListener<ApplicationEvent> {

  private static final Logger logger = LoggerFactory.getLogger(GameWebSocketHandler.class);

  private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<String, WebSocketSession>();

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  //---------------------------------------------------------------------------------------
  // Methods managing WebSocket lifecycle
  //---------------------------------------------------------------------------------------

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    if (message.getPayload().equalsIgnoreCase("bingo")) {
      // log.info("Bingo from player id " + session.getId());
      logger.info("Message {} from player {} ", message, session.getId());
      //applicationEventPublisher.publishEvent(new BingoEvent(this));

      //sendToAll("bingo");
    } else {
      logger.info("Message no sense ");
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    logger.info("New player id " + session.getId());
    sessions.put(session.getId(), session);
    try {
      session.sendMessage(new TextMessage("Welcome to reversi game"));
    } catch (IOException e) {
      logger.error("Error sending greetings", e);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    logger.info("Abandoned player id " + session.getId());
    sessions.remove(session.getId());
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    logger.error(exception.getMessage());
  }

  //---------------------------------------------------------------------------------------
  // Methods managing bingo events
  //---------------------------------------------------------------------------------------

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
//    if (event instanceof DrawEvent) {
//      sendToAll(((DrawEvent) event).getNumber().toString());
//
//    } else if (event instanceof StartEvent) {
//      sendToAll("start");
//    }
    sendToAll("start");
  }

  //---------------------------------------------------------------------------------------
  // Methods managing communication to the clients
  //---------------------------------------------------------------------------------------

  private void sendToAll(String message) {
    try {
      if (!sessions.isEmpty()) {
        for (WebSocketSession session : sessions.values()) {
          if (session.isOpen()) {
            session.sendMessage(new TextMessage(message));
          }
        }
      }
    } catch (IOException e) {
      logger.error("Error sending message", e);
    }
  }
}
