package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.events.MatchMoveEvent;
import org.abubusoft.reversi.server.messages.MatchMove;
import org.abubusoft.reversi.server.services.GameService;
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

  public static final String GREETINGS = "/greetings/";

  private final GameService gameService;

  public MessagesController(ApplicationEventPublisher applicationEventPublisher, GameService gameService) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.gameService = gameService;
  }

//  @MessageMapping("/create/{uuid}")
//  //@SendTo("/topic/board/{uuid}")
//  public void createGame(@DestinationVariable String uuid) throws IllegalArgumentException {
//    Player1 player1 = PlayerFactory.createCpuPlayer1();
//    Player2 player2 = PlayerFactory.createCpuPlayer2();
//    GameInstance game = gameService.createMatch(player1, player2);
//  }

//  @MessageMapping("/move/{uuid}")
//  @SendTo("/topic/move/{uuid}")
//  public GameState makeMove(@DestinationVariable String uuid, Move move) throws IllegalArgumentException {
//    GameState gameState = gameService.move(UUID.fromString(uuid), move);
//
//    return gameState;
//  }

  //  @MessageMapping("/game")
//  @SendTo("/games/${gameUID}")
//  public Greeting nextMove(WebSocketSession session, @PathParam("gameUID") String gameUID, Hello message) throws Exception {
//    return new Greeting("hello");
//  }
//

  @MessageMapping("/users/{uuid}/moves")
  //@SendTo(TOPIC_PREFIX + "/users/{uuid}")
  public void matchMove(@DestinationVariable("uuid") String userUUID, MatchMove move) throws Exception {
    if (userUUID.equals(move.getPlayerUUID().toString())) {
      applicationEventPublisher.publishEvent(new MatchMoveEvent(move));
    } else {
      logger.warn("something is strange");
    }
   // return Hello.of("Hello, " + HtmlUtils.htmlEscape(message.getMessage()) + " " + uuid + "!");
  }

}