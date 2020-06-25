package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.events.MatchMoveEvent;
import org.abubusoft.reversi.messages.MatchMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MessagesController {
  private ApplicationEventPublisher applicationEventPublisher;
  private static Logger logger = LoggerFactory.getLogger(MessagesController.class);

  public MessagesController(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @MessageMapping(WebPathConstants.USER_MOVES_URL_SEGMENT)
  public void matchMove(@DestinationVariable("uuid") String userUUID, MatchMove move) {
    logger.debug("receive message from "+userUUID);
    if (userUUID.equals(move.getPlayerId().toString())) {
      applicationEventPublisher.publishEvent(new MatchMoveEvent(move));
    } else {
      logger.warn("something is strange");
    }
  }

}