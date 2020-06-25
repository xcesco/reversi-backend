package org.abubusoft.reversi.server;

import org.abubusoft.reversi.server.events.MatchStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class StompEventListener {
  private static final Logger logger = LoggerFactory.getLogger(StompEventListener.class);

  public StompEventListener(SimpMessageSendingOperations messagingTemplate) {

    this.messagingTemplate = messagingTemplate;
  }

  @EventListener
  public void onSessionSubscribeEvent(SessionSubscribeEvent event) {
    // StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    logger.info("[SessionSubscribeEvent] ");
  }

  @EventListener
  public void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
    // StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    logger.info("[SessionUnsubscribeEvent] ");
  }


//  @Override
//  public void onApplicationEvent(SessionConnectEvent event) {
//    logger.info("[Connected] "+event.getMessage());
//    String userId = event.getUser().getName();
//    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
//    boolean isConnect = sha.getCommand() == StompCommand.CONNECT;
//    boolean isDisconnect = sha.getCommand() == StompCommand.DISCONNECT;
//    logger.debug("Connect: " + isConnect + ",disconnect:" + isDisconnect +
//            ", event[sessionId: " + sha.getSessionId() + ";" + userId + " ,command =" + sha.getCommand());
//  }


  @EventListener
  public void onSessionConnected(SessionConnectEvent event) {
    // StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    logger.info("[Connected] ");
  }

//  @EventListener
//  public void onSocketConnected(SessionConnectedEvent event) {
//   // StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
//    logger.info("[Connected] ");
//  }

  private final SimpMessageSendingOperations messagingTemplate;

//  @EventListener
//  public void onGameStatusChanges(MatchStatusEvent event) {
//    //StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
//    logger.info("onGameStatusChanges {}", event.getMatchStatus().getId());
//
//    //transmitting current user's latest location feed
//   // messagingTemplate.convertAndSend(PREFIX , new Greeting("ciao"));
//  }

  @EventListener
  public void onSessionDisconnected(SessionDisconnectEvent event) {
    //StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    logger.info("[Disconnected] ss " + event.getCloseStatus());

    //transmitting current user's latest location feed
    // messagingTemplate.convertAndSend("/app/getData", new Greeting("ciao"));
  }
}