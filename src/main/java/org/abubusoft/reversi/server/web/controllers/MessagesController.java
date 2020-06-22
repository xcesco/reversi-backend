package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.services.GameService;
import org.abubusoft.reversi.server.messages.Greeting;
import org.abubusoft.reversi.server.messages.Hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import static org.abubusoft.reversi.server.WebSocketConfig.TOPIC_PREFIX;

@Controller
public class MessagesController {

  private static Logger logger = LoggerFactory.getLogger(MessagesController.class);

  public static final String GREETINGS = "/greetings/";

  private final GameService gameService;

  public MessagesController(GameService gameService) {
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

  @MessageMapping(GREETINGS + "{uuid}")
  @SendTo(TOPIC_PREFIX + "/users/{uuid}")
  public Hello greeting(@DestinationVariable String uuid, Greeting message) throws Exception {
    gameService.saveUser(null);
    logger.info("[GREETINGS] {} {}", message.getMessage(), uuid);
    return Hello.of("Hello, " + HtmlUtils.htmlEscape(message.getMessage()) + " " + uuid + "!");
  }

}