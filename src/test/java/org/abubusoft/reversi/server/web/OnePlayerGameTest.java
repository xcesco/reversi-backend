package org.abubusoft.reversi.server.web;

import org.abubusoft.reversi.server.WebsocketEndpointIT;
import org.abubusoft.reversi.server.messages.Greeting;
import org.abubusoft.reversi.server.model.MatchStatus;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.model.UserRegistration;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;
import java.util.concurrent.*;

import static org.abubusoft.reversi.server.WebSocketConfig.TOPIC_PREFIX;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OnePlayerGameTest extends AbstractWebTest {

  private User user1;
  private User user2;
  private String user1TopicUrl;

  @Test
  public void playersAreReadyToPlay() throws Exception {
    UserRegistration userRegistration = new UserRegistration();
    userRegistration.setName("ciao");
    user1 = restTemplate.postForEntity(baseUrl + "api/v1/public/users", userRegistration, User.class).getBody();
    user2 = restTemplate.postForEntity(baseUrl + "api/v1/public/users", userRegistration, User.class).getBody();

    user1TopicUrl=TOPIC_PREFIX +"/user/"+ user1.getId();
    connectOnWebsocket();

    assert user1 != null;
    user1 = restTemplate.patchForObject(baseUrl + String.format("api/v1/public/users/%s/ready", user1.getId()), HttpMethod.PUT, User.class);
    assert user2 != null;
    user2 = restTemplate.patchForObject(baseUrl + String.format("api/v1/public/users/%s/ready", user2.getId()), HttpMethod.PUT, User.class);

    logger.info(objectMapper.writeValueAsString(user1));
    logger.info(objectMapper.writeValueAsString(user2));
    assertNotNull(user1.getId());
    assertNotNull(user2.getId());

    executor.getThreadPoolExecutor().awaitTermination(30, TimeUnit.SECONDS);
  }

  @Override
  protected Type onPayLoadType(StompHeaders headers) {
    return null;
  }

  @Override
  protected void onHandleFrame(StompHeaders headers, Object payload) {

  }

  @Override
  protected void onAfterConnected(StompSession session, StompHeaders connectedHeaders) {
    logger.info("user 1 start to wait on {}", user1TopicUrl);
    session.subscribe(user1TopicUrl, new CreateGameStompFrameHandler());
  }

  private class CreateGameStompFrameHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
      logger.info("headers " + stompHeaders.toString());
      return MatchStatus.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
      logger.info("user 1 received message: {}",o);
     // completableFuture.complete((Greeting) o);
    }
  }
}
