package org.abubusoft.reversi.server.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.Piece;
import org.abubusoft.reversi.server.JSONMapperUtils;
import org.abubusoft.reversi.server.WebSocketConfig;
import org.abubusoft.reversi.server.exceptions.AppRuntimeException;
import org.abubusoft.reversi.messages.MatchMoveMessage;
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
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.abubusoft.reversi.server.ReversiServerApplication.MATCH_EXECUTOR;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractWebTest implements StompSender {
  protected WebSocketStompClient stompClient;
  protected StompSession stompSession;
  protected String baseUrl;
  protected String websocketBaseUrl;

  protected List<Transport> createTransportClient() {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    return transports;
  }

  protected abstract Type onSessionPayLoadType(StompHeaders headers);
  protected abstract void onSessionHandleFrame(StompHeaders headers, Object payload);
  protected abstract void onSessionAfterConnected(StompSession session, StompHeaders connectedHeaders);

  @BeforeEach
  public void setup()  {
  }

  protected void connectOnWebsocket() throws InterruptedException, ExecutionException, TimeoutException {
    stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
    MappingJackson2MessageConverter mappingJackson2MessageConverter=new MappingJackson2MessageConverter();
    mappingJackson2MessageConverter.setObjectMapper(JSONMapperUtils.createMapper());
    stompClient.setMessageConverter(mappingJackson2MessageConverter);
    stompSession = stompClient.connect(websocketBaseUrl, new StompSessionHandlerAdapter() {
      @Override
      public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        throw new AppRuntimeException(exception.getMessage());
      }

      @Override
      public void handleTransportError(StompSession session, Throwable exception) {
        throw new AppRuntimeException(exception.getMessage());
      }

      @Override
      public Type getPayloadType(StompHeaders headers) {
        return onSessionPayLoadType(headers);
      }

      @Override
      public void handleFrame(StompHeaders headers, Object payload) {
        onSessionHandleFrame(headers, payload);
      }

      @Override
      public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        onSessionAfterConnected(session, connectedHeaders);
      }
    }).get(1, SECONDS);
  }


  @BeforeEach
  public void beforeAll() {
    baseUrl = "http://localhost:" + port + "/";
    websocketBaseUrl = "ws://localhost:" + port + WebSocketConfig.WE_ENDPOINT;
  }

  public void sendMatchMove(UUID playerId, Piece piece, UUID matchId, Coordinates move) {
    String url="/app/users/"+playerId+"/moves";
    logger.info("send info to {}", url);
    stompSession.send(url, MatchMoveMessage.of(matchId, playerId, piece, move));
  }

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }


  Logger logger = LoggerFactory.getLogger(MatchSimulatinoTest.class);
  @LocalServerPort
  protected int port;

  protected ObjectMapper objectMapper;

  @Autowired
  protected TestRestTemplate restTemplate;

  @Qualifier(MATCH_EXECUTOR)
  @Autowired
  protected ThreadPoolTaskExecutor executor;
}
