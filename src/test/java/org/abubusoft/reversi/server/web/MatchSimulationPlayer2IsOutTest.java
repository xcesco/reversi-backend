package org.abubusoft.reversi.server.web;

import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;
import org.abubusoft.reversi.messages.ConnectedUserMessage;
import org.abubusoft.reversi.server.WebSocketConfig;
import org.abubusoft.reversi.server.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-client.properties")
public class MatchSimulationPlayer2IsOutTest extends AbstractWebTest {

  private User user1;
  private User user2;
  private UserSubscriptionFrameHandler frameHandlerPlayer1;

  @Test
  public void testMatch() throws Exception {
    ConnectedUserMessage user1Registration = new ConnectedUserMessage("user1");
    ConnectedUserMessage user2Registration = new ConnectedUserMessage("user1");

    logger.debug("base url is {}",baseUrl);
    user1 = restTemplate.postForEntity(baseUrl + "api/v1/public/users", user1Registration, User.class).getBody();
    user2 = restTemplate.postForEntity(baseUrl + "api/v1/public/users", user2Registration, User.class).getBody();

    frameHandlerPlayer1=new UserSubscriptionFrameHandler(user1.getId(), Piece.PLAYER_1, this);

    connectOnWebsocket();

    assert user1 != null;
    assert user2 != null;

    user1 = restTemplate.patchForObject(baseUrl + String.format("api/v1/public/users/%s/ready", user1.getId()), HttpMethod.PUT, User.class);
    user2 = restTemplate.patchForObject(baseUrl + String.format("api/v1/public/users/%s/ready", user2.getId()), HttpMethod.PUT, User.class);

    assertNotNull(user1.getId());
    assertNotNull(user2.getId());

    executor.getThreadPoolExecutor().awaitTermination(timeOut, TimeUnit.SECONDS);

    assertTrue(frameHandlerPlayer1.getResult()== GameStatus.PLAYER1_WIN);
  }

  @Override
  protected Type onSessionPayLoadType(StompHeaders headers) {
    return null;
  }

  @Override
  protected void onSessionHandleFrame(StompHeaders headers, Object payload) {

  }

  @Override
  protected void onSessionAfterConnected(StompSession session, StompHeaders connectedHeaders) {
    session.subscribe(StompSender.buildUserTopic(user1.getId()), frameHandlerPlayer1);
    //session.subscribe(StompSender.buildUserTopic(user2.getId()), new UserSubscriptionFrameHandler(user2.getId(), Piece.PLAYER_2, this));
  }

}
