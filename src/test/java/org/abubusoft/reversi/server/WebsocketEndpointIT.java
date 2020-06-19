package org.abubusoft.reversi.server;


import org.abubusoft.reversi.server.model.messages.Greeting;
import org.abubusoft.reversi.server.web.controllers.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.abubusoft.reversi.server.WebSocketConfig.TOPIC_PREFIX;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
//@EnableAutoConfiguration(includes = {SecurityAutoConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebsocketEndpointIT extends WebSecurityConfigurerAdapter {
  Logger logger = LoggerFactory.getLogger(WebsocketEndpointIT.class);
  @LocalServerPort
  private int port;

  private String URL;

  private static final String SEND_CREATE_BOARD_ENDPOINT = "/app/create/";
  private static final String SEND_MOVE_ENDPOINT = "/app/move/";
  private static final String SUBSCRIBE_GREETING_PREFIX = "/users/";
  private static final String SUBSCRIBE_MOVE_ENDPOINT = "/topic/move/";

  private CompletableFuture<Greeting> completableFuture;

  @BeforeEach
  public void setup() {
    completableFuture = new CompletableFuture<>();
    URL = "ws://localhost:" + port + WebSocketConfig.WE_ENDPOINT;
  }


  @Test
  public void testCreateGameEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
    String uuid = UUID.randomUUID().toString();

    WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
    }).get(1, SECONDS);


    stompSession.subscribe(TOPIC_PREFIX + SUBSCRIBE_GREETING_PREFIX + uuid, new CreateGameStompFrameHandler());
    stompSession.send(WebSocketConfig.APPLICATION_ENDPOINT + GameController.GREETINGS + uuid, Greeting.of("Benvenuto " + uuid));

    Greeting greeting = completableFuture.get(10, SECONDS);

    logger.info("Recived " + greeting.getMessage());
    assertNotNull(greeting);
  }
/*
    @Test
    public void testMakeMoveEndpoint() throws InterruptedException, ExecutionException, TimeoutException {
        String uuid = UUID.randomUUID().toString();

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_MOVE_ENDPOINT + uuid, new CreateGameStompFrameHandler());
        stompSession.send(SEND_MOVE_ENDPOINT + uuid, new Move(1, 0));
        GameState gameStateAfterMove = completableFuture.get(5, SECONDS);

        assertNotNull(gameStateAfterMove);
    }*/

  private List<Transport> createTransportClient() {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    return transports;
  }


  private class CreateGameStompFrameHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
      return Greeting.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
      completableFuture.complete((Greeting) o);
    }
  }
}