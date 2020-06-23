package org.abubusoft.reversi.server.web;

import it.fmt.games.reversi.Player1;
import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.Piece;
import org.abubusoft.reversi.server.messages.MatchEndMessage;
import org.abubusoft.reversi.server.messages.MatchStartMessage;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.model.UserRegistration;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.abubusoft.reversi.server.WebSocketConfig.TOPIC_PREFIX;
import static org.abubusoft.reversi.server.services.GameServiceImpl.HEADER_TYPE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-client.properties")
public class OnePlayerGameTest extends AbstractWebTest {

  private User user1;
  private User user2;
  private String user1TopicUrl;
  private String user2TopicUrl;
  private UUID matchUUID;

  @Test
  public void playersAreReadyToPlay() throws Exception {
    UserRegistration userRegistration = new UserRegistration();
    userRegistration.setName("ciao");
    user1 = restTemplate.postForEntity(baseUrl + "api/v1/public/users", userRegistration, User.class).getBody();
    user2 = restTemplate.postForEntity(baseUrl + "api/v1/public/users", userRegistration, User.class).getBody();

    user1TopicUrl = TOPIC_PREFIX + "/user/" + user1.getId();
    user2TopicUrl = TOPIC_PREFIX + "/user/" + user2.getId();
    connectOnWebsocket();

    assert user1 != null;
    user1 = restTemplate.patchForObject(baseUrl + String.format("api/v1/public/users/%s/ready", user1.getId()), HttpMethod.PUT, User.class);
    assert user2 != null;
    user2 = restTemplate.patchForObject(baseUrl + String.format("api/v1/public/users/%s/ready", user2.getId()), HttpMethod.PUT, User.class);

    //logger.info(objectMapper.writeValueAsString(user1));
    //logger.info(objectMapper.writeValueAsString(user2));
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
    //logger.info("user 1 start to wait on {}", user1TopicUrl);
    session.subscribe(user1TopicUrl, new CreateGameStompFrameHandler());
    //logger.info("user 2 start to wait on {}", user2TopicUrl);
    session.subscribe(user2TopicUrl, new CreateGameStompFrameHandler());
  }

  private class CreateGameStompFrameHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
      //logger.info("headers " + stompHeaders.toString());

      switch (Objects.requireNonNull(stompHeaders.getFirst(HEADER_TYPE))) {
        case "GameSnapshot":
          return GameSnapshot.class;
        case "MatchStartMessage":
          return MatchStartMessage.class;
        case "MatchEndMessage":
          return MatchEndMessage.class;
      }

      return GameSnapshot.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
      String destination = stompHeaders.getDestination();

      switch (Objects.requireNonNull(stompHeaders.getFirst(HEADER_TYPE))) {
        case "MatchStartMessage": {
          MatchStartMessage matchStartMessage = (MatchStartMessage) o;
          if (user1TopicUrl.equals(destination)) {
            // logger.info("user 1 received message: {}", o);
            matchUUID = matchStartMessage.getMatchUUID();
            assertNotNull(matchStartMessage);
          } else {
            //logger.info("user 2 received message: {}", o);
            matchUUID = matchStartMessage.getMatchUUID();
            assertNotNull(matchStartMessage);
          }

        }
        break;
        case "GameSnapshot": {
          GameSnapshot gameSnapshot = (GameSnapshot) o;
          if (user1TopicUrl.equals(destination) && gameSnapshot.getActivePiece()== Piece.PLAYER_1 && gameSnapshot.getAvailableMoves().getMovesActivePlayer().size()>0) {
            logger.info("user {} can moves: {}", gameSnapshot.getActivePiece(), gameSnapshot.getAvailableMoves().getMovesActivePlayer());
            assertNotNull(gameSnapshot);
            sendMatchMove(user1.getId(), Piece.PLAYER_1, matchUUID, gameSnapshot.getAvailableMoves().getMovesActivePlayer().get(0));
          } else if (user2TopicUrl.equals(destination) && gameSnapshot.getActivePiece()== Piece.PLAYER_2 && gameSnapshot.getAvailableMoves().getMovesActivePlayer().size()>0) {
            //logger.info("user 2 received message: {}", o);
            logger.info("user {} can moves: {}", gameSnapshot.getActivePiece(), gameSnapshot.getAvailableMoves().getMovesActivePlayer());
            assertNotNull(gameSnapshot);
            sendMatchMove(user2.getId(), Piece.PLAYER_2, matchUUID, gameSnapshot.getAvailableMoves().getMovesActivePlayer().get(0));

          } else {
            logger.info("match status {}", gameSnapshot.getStatus());
          }
        }
        break;
        case "MatchEndMessage": {
          MatchEndMessage matchStartMessage = (MatchEndMessage) o;
          if (user1TopicUrl.equals(destination)) {
            //logger.info("user 1 received message: {}", o);
            assertNotNull(matchStartMessage);
          } else {
            //logger.info("user 2 received message: {}", o);
            assertNotNull(matchStartMessage);
          }

        }
        break;
      }


      // completableFuture.complete((Greeting) o);
    }
  }
}
