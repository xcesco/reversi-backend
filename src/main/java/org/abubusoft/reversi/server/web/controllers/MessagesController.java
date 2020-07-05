package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.messages.ConnectedUser;
import org.abubusoft.reversi.messages.MatchMove;
import org.abubusoft.reversi.server.events.MatchMoveEvent;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class MessagesController {
  private final GameService gameService;
  private final ApplicationEventPublisher applicationEventPublisher;
  private static final Logger logger = LoggerFactory.getLogger(MessagesController.class);

  public MessagesController(@Autowired ApplicationEventPublisher applicationEventPublisher,
                            @Autowired GameService gameService
  ) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.gameService = gameService;
  }

  @MessageMapping(WebPathConstants.WS_USER_READY_URL_SEGMENT)
  @SendToUser("/status")
  public ConnectedUser playerIsReady(@DestinationVariable("uuid") String userUUID) {
    logger.debug("/status receive message from " + userUUID);
    User user = gameService.readyToPlay(UUID.fromString(userUUID));

    ConnectedUser connectedUser = new ConnectedUser(user.getId(), user.getName(), user.getStatus(), user.getPiece());
    return connectedUser;
  }

  @MessageMapping(WebPathConstants.WS_USER_NOT_READY_URL_SEGMENT)
  @SendToUser("/status")
  public ConnectedUser playerIsNotReady(@DestinationVariable("uuid") String userUUID) {
    logger.debug("/status receive message from " + userUUID);
    User user = gameService.stopPlaying(UUID.fromString(userUUID));

    return new ConnectedUser(user.getId(), user.getName(), user.getStatus(), user.getPiece());
  }

  @MessageMapping(WebPathConstants.WS_USER_MOVES_URL_SEGMENT)
  public void matchMove(@DestinationVariable("uuid") String userUUID, MatchMove move) {
    logger.debug("receive message from " + userUUID);
    if (userUUID.equals(move.getPlayerId().toString())) {
      applicationEventPublisher.publishEvent(new MatchMoveEvent(move));
    } else {
      logger.warn("something is strange");
    }
  }

}