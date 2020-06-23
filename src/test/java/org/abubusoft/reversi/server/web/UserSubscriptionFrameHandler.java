package org.abubusoft.reversi.server.web;

import it.fmt.games.reversi.model.GameSnapshot;
import it.fmt.games.reversi.model.GameStatus;
import it.fmt.games.reversi.model.Piece;
import org.abubusoft.reversi.messages.MatchEndMessage;
import org.abubusoft.reversi.messages.MatchStartMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.UUID;

import static org.abubusoft.reversi.server.services.GameServiceImpl.HEADER_TYPE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserSubscriptionFrameHandler implements StompFrameHandler {
  private final UUID userId;
  private final String userTopicUrl;
  private final StompSender stompSender;
  private final Piece userPiece;
  private UUID matchUUID;
  private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionFrameHandler.class);

  public UserSubscriptionFrameHandler(UUID userId, Piece userPiece, StompSender stompSender) {
    this.userTopicUrl = StompSender.buildUserTopic(userId);
    this.userId = userId;
    this.userPiece=userPiece;
    this.stompSender=stompSender;
  }

  @Override
  public Type getPayloadType(StompHeaders stompHeaders) {
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
        if (userTopicUrl.equals(destination)) {
          matchUUID = matchStartMessage.getMatchUUID();
          assertNotNull(matchStartMessage);
        }
      }
      break;
      case "GameSnapshot": {
        GameSnapshot gameSnapshot = (GameSnapshot) o;
        if (userTopicUrl.equals(destination) && gameSnapshot.getActivePiece() == userPiece && gameSnapshot.getAvailableMoves().getMovesActivePlayer().size() > 0) {
          logger.info("user {} can moves: {}", gameSnapshot.getActivePiece(), gameSnapshot.getAvailableMoves().getMovesActivePlayer());
          assertNotNull(gameSnapshot);
          stompSender.sendMatchMove(userId, userPiece, matchUUID, gameSnapshot.getAvailableMoves().getMovesActivePlayer().get(0));
        } else if (gameSnapshot.getStatus()!= GameStatus.RUNNING) {
          logger.info("player {} detect match has status {}, {} - {}", userPiece, gameSnapshot.getStatus(),
                  gameSnapshot.getScore().getPlayer1Score(), gameSnapshot.getScore().getPlayer2Score()
          );
        }
      }
      break;
      case "MatchEndMessage": {
        MatchEndMessage matchEndMessage = (MatchEndMessage) o;
        assertNotNull(matchEndMessage);
        matchUUID=null;
      }
      break;
    }


    // completableFuture.complete((Greeting) o);
  }
}