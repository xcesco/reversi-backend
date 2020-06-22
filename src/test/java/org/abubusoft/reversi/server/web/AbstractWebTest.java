package org.abubusoft.reversi.server.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abubusoft.reversi.server.WebSocketConfig;
import org.abubusoft.reversi.server.messages.Greeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.abubusoft.reversi.server.ReversiServerApplication.GAME_EXECUTOR;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractWebTest {
  protected WebSocketStompClient stompClient;
  protected StompSession stompSession;
  protected String baseUrl;
  protected String websocketBaseUrl;

  protected CompletableFuture<Greeting> completableFuture;

  protected List<Transport> createTransportClient() {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    return transports;
  }

  @BeforeEach
  public void setup() throws InterruptedException, ExecutionException, TimeoutException {
    completableFuture = new CompletableFuture<>();

    stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    stompSession = stompClient.connect(websocketBaseUrl, new StompSessionHandlerAdapter() {
    }).get(1, SECONDS);
  }

  @BeforeEach
  public void beforeAll() {
    baseUrl = "http://localhost:" + port + "/";
    websocketBaseUrl = "ws://localhost:" + port + WebSocketConfig.WE_ENDPOINT;
  }

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }


  Logger logger = LoggerFactory.getLogger(OnePlayerGameTest.class);
  @LocalServerPort
  protected int port;

  protected ObjectMapper objectMapper;

  @Autowired
  protected TestRestTemplate restTemplate;

  @Qualifier(GAME_EXECUTOR)
  @Autowired
  protected ThreadPoolTaskExecutor executor;
}
