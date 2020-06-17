package org.abubusoft.reversi.server.web.controllers;

import org.abubusoft.reversi.server.model.messages.Greeting;
import org.abubusoft.reversi.server.model.messages.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import javax.websocket.server.PathParam;

@Controller
public class GameController {

  @MessageMapping("/game")
  @SendTo("/games/${gameUID}")
  public Greeting nextMove(@PathParam("gameUID") String gameUID, HelloMessage message) throws Exception {
    return null;
  }

  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public Greeting greeting(HelloMessage message) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
  }

}